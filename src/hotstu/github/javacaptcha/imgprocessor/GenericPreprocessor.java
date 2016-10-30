package hotstu.github.javacaptcha.imgprocessor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import hotst.github.javacaptcha.common.ImageCommons;
import hotst.github.javacaptcha.model.BinaryMatrix;

public class GenericPreprocessor implements ICaptchaPreprocessor {
	
	
	@Override
	public BinaryMatrix preprocess(BufferedImage im) {
		double Wr = 0.299;
		double Wg = 0.587;
		double Wb = 0.114;
		
		int w = im.getWidth();
		int h = im.getHeight();
		int[][] gray = new int[w][h];
		w=w-4;h=h-4;
		im = cutImage("jpg",im,2,2,w,h);//去掉外边框
		//灰度化
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Color color = new Color(im.getRGB(x, y));
				int rgb = (int) ((color.getRed()*Wr + color.getGreen()*Wg + color.getBlue()*Wb) / 3);
				gray[x][y] = rgb;
			}
		}
		
		BinaryMatrix res = BinaryMatrix.fromBlank(w, h);
		
		//二值化
		int threshold = ImageCommons.getOstu(gray, w, h);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (gray[x][y] > threshold) {
					res.setFalse(x, y);
				}else{
					res.setTrue(x, y);
				}
				
			}
		}
		
		return res;
	}
	
	/**
	 * 裁剪图片
	 * @param imageType imageType jpg或giff 
	 * @param src 原始文件
	 * @param dest 保存位置
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws IOException
	 */
    @SuppressWarnings("rawtypes")
	public BufferedImage cutImage(String imageType,BufferedImage image,int x,int y,int w,int h) {   
           Iterator iterator = ImageIO.getImageReadersByFormatName(imageType);   
           ImageReader reader = (ImageReader)iterator.next();   
           ByteArrayOutputStream os = new ByteArrayOutputStream();  
           try {
        	   ImageIO.write(image, imageType, os); 
			   InputStream in = new ByteArrayInputStream(os.toByteArray()); 
			   ImageInputStream iis = ImageIO.createImageInputStream(in);   
			   reader.setInput(iis, true);   
			   ImageReadParam param = reader.getDefaultReadParam();   
			   Rectangle rect = new Rectangle(x, y, w,h);    
			   param.setSourceRegion(rect);   
			   BufferedImage cutbi = reader.read(0,param);  
			   return cutbi;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
        return null;
    }   


	public static void test() {
		File f = new File("D:/采集项目/captcha/h3cv.jpg");
		try {
			BufferedImage img = ImageIO.read(f);
			GenericPreprocessor g = new GenericPreprocessor();
			System.out.println(g.preprocess(img));
			g.preprocess(img).dump2bitmap("C:/tmp/cut/h3cv_pre.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		test();
		
	}
}
