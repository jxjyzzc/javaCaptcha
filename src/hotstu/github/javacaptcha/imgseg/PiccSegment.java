package hotstu.github.javacaptcha.imgseg;

import hotst.github.javacaptcha.common.PiccConstants;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgseg.algorithm.ProjectionSeg;

import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.Nullable;

public class PiccSegment implements ISegment {

	@Override
	public List<BinaryMatrix> seg(BinaryMatrix im) {
		
		List<BinaryMatrix> interList = ProjectionSeg.myProjection(im);
		List<BinaryMatrix>  res = new ArrayList<BinaryMatrix>();
		for (BinaryMatrix i : interList) {
			i=i.trim();
			if(i==null) continue;
			//ImageCommons.matrixPrint(k);
			i = i.scaleTo(PiccConstants.DST_CHAR_WIDTH, PiccConstants.DST_CHAR_HEIGHT);
			//ImageCommons.matrixPrint(m);
			res.add(i);
		}
		
		System.out.println("++分割验证码++: " + res.size() + "块");
		return res;
	}
	
	@Override
	public List<BinaryMatrix> seg2file(BinaryMatrix im, @Nullable String prefix) {
		List<BinaryMatrix> targets = seg(im);
		if(prefix == null || prefix == "") {
			prefix = "temp";
		}
		
		int c = 0;
		for (BinaryMatrix bs : targets) {
			c++;
			bs.dump2bitmap("D:/captcha/tmp/" + prefix + "-" + c + ".png");
		}
		return targets;
	}
	
	public List<BinaryMatrix> seg2file(BinaryMatrix im, String prefix, String dir) {
		List<BinaryMatrix> targets = seg(im);
		if(prefix == null || prefix == "") {
			throw new RuntimeException("prefix is illegal!");
		}
		
		int c = 0;
		for (BinaryMatrix bs : targets) {
			bs.dump2bitmap(dir + prefix + "-" + c + ".png");
			c++;
		}
		return targets;
	}
	

}






