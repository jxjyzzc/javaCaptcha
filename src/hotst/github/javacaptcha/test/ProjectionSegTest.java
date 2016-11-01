package hotst.github.javacaptcha.test;

import static org.junit.Assert.fail;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgprocessor.PiccPreprocessor;
import hotstu.github.javacaptcha.imgseg.algorithm.ProjectionSeg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ProjectionSegTest {

	@Test
	public void testProjectionBinaryMatrix() {
		File f = new File("C:/Users/fahai/Documents/captcha/j7v3.jpg");
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			fail("载入失败");
		}
		PiccPreprocessor g = new PiccPreprocessor();
		BinaryMatrix b = g.preprocess(img);
		b.dump2bitmap("D:/captcha/tmp/j7v3.jpg");
		
		System.out.println(b);
		List<BinaryMatrix> lb = ProjectionSeg.myProjection(b);
		
		for (BinaryMatrix item : lb) {
			System.out.println(item);
			item.dump2bitmap("D:/captcha/tmp/j7v3_"+lb.indexOf(item)+".jpg");
		}

	}

}
