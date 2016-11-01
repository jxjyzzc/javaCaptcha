package hotst.github.javacaptcha.svm;

import hotst.github.javacaptcha.common.PiccConstants;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgprocessor.GenericPreprocessor;
import hotstu.github.javacaptcha.imgseg.GenericSegment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Trainning {
	private static final String PROCESSEDIMGS = "D:/captcha/picc/processDimgs/";
	private static final String RAW_CHAR_DIR = "C:/Users/fahai/Documents/captcha";
	
	public static void batchPreProcess() {
		File dir = new File(RAW_CHAR_DIR);
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".jpg")){   
				      return true;   
				    }else{   
				      return false;   
				    } 
			}
		});
		
		GenericPreprocessor preProcessor = new GenericPreprocessor();
		GenericSegment segProcessor = new GenericSegment();
//		PiccSegment segProcessor = new PiccSegment();
		
		for (File f : files) {
			String fileName = f.getName().split("\\.")[0];
				
			BufferedImage image = null;
			try {
				image = ImageIO.read(f);
				BinaryMatrix bm = preProcessor.preprocess(image);
				segProcessor.seg2file(bm, fileName, PROCESSEDIMGS);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 根据上级文件夹确定训练集归类
	 * @param traChar
	 */
	public static void createTraData()  {
		File dir = new File(PROCESSEDIMGS);
		File[] subDirs = dir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}});
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(PiccConstants.SVM_TRANS_FILE));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		for(File subDirF:subDirs){
			char valueName = subDirF.getName().charAt(0);
			for(File sFile:subDirF.listFiles()){
					BufferedImage image;
					BinaryMatrix bm;
					try {
						image = ImageIO.read(sFile);
						bm = BinaryMatrix.fromImage(image);
						
					} catch (IOException e) {
						System.out.println("IOException,skip:" + sFile.getName());
						continue;
					}
					
					int value;
					if (valueName >= 'a' && valueName <= 'z') {
						value = valueName - 87;
					}
					else if (valueName >= '0' && valueName <= '9') {
						value = valueName - '0';
					}
					else {
						System.out.println("bad filename,skip:" + sFile.getName());
						continue;
					}
					StringBuilder sb = new StringBuilder();
					sb.append(value);
					sb.append(' ');
					int w = bm.getWidth();
					int h = bm.getHeight();
					int count = 1;
					for (int x = 0; x < w; x++) {
						for (int y = 0; y < h; y++) {
							//黑色点标记为1
							sb.append(count);
							sb.append(':');
							if (bm.getValue(x, y)) {
								sb.append('1');
							}else {
								sb.append('0');
							}
							sb.append(' ');
							count++;
						}
					}
					sb.append("\r\n");
					writer.write(sb.toString());
					//System.out.println(sb.toString());
				}
		}
			
			
		
		writer.flush();
		writer.close();
	}

	public static void startTrainning() {
		String[] arg = {"-t","0",PiccConstants.SVM_TRANS_FILE, PiccConstants.SVM_MODEL_FILE};
		
		//predict参数
		//String[] parg = {"svm/svmscale.test","svm/svm.model","svm/result.txt"};
		
		System.out.println("训练开始");
		try {
			svm_train.main(arg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("训练结束");
	}
	public static void main(String[] args) {
//		batchPreProcess();
		createTraData();
		startTrainning();
		
	}

}
