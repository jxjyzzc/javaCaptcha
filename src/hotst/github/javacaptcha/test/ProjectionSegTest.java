package hotst.github.javacaptcha.test;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgprocessor.GenericPreprocessor;
import hotstu.github.javacaptcha.imgseg.algorithm.ProjectionSeg;

public class ProjectionSegTest {

	@Test
	public void testCfsBinaryMatrix() {
		File f = new File("D:/采集项目/captcha/h3cv.jpg");

		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			fail("载入失败");
		}
		GenericPreprocessor g = new GenericPreprocessor();
		BinaryMatrix b = g.preprocess(img);
		List<BinaryMatrix> lb = ProjectionSeg.myProjection(b);
		
		for (BinaryMatrix item : lb) {
			System.out.println(item);
		}

	}

}