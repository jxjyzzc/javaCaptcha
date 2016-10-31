package hotst.github.javacaptcha.svm;

import hotst.github.javacaptcha.common.PiccConstants;

import java.io.IOException;

import libsvm.svm;
import libsvm.svm_model;

/**
 * ！使用前需确保生成数据svm/svm.model2
 * @author foo
 *
 */
public enum StaticSVM{
    INSTANCE;
    
    private svm_model model;
    

	private StaticSVM() {
		try {
			model = svm.svm_load_model(PiccConstants.SVM_MODEL_FILE);
			System.out.println("++++++svm.model2 is loaded+++++");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public svm_model getModel() {
		return model;
	}
    
}
