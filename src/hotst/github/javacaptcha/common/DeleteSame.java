package hotst.github.javacaptcha.common;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;

import javax.imageio.ImageIO;


public class DeleteSame {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		HashSet<String> hs=new HashSet<String>();
		String path="D:/captcha/picc/processDimgs";
		  File file=new File(path);
		  File[] tempList = file.listFiles();
		  System.out.println("该目录下对象个数："+tempList.length);
		  for (int i = 0; i < tempList.length; i++) {
		   if (tempList[i].isFile()) {
		   //读取某个文件夹下的所有文件
		    BufferedImage bufferedImage = ImageIO.read(tempList[i].getAbsoluteFile()); 
		    String temp=getString(bufferedImage);
			    if(! hs.contains(temp)){
			    	hs.add(temp);
			    }else
			    {
			    	System.out.println("删除文件："+tempList[i]);
			    	tempList[i].delete();
			    }
		   }
/*		   if (tempList[i].isDirectory()) {
		    //读取某个文件夹下的所有文件夹
		    System.out.println("文件夹："+tempList[i]);
		   }*/
		  }
		 }
	
	public static String getString(BufferedImage img) throws Exception {
		String temp="";
		final int width = img.getWidth();
		final int height = img.getHeight();
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
				 int r = (img.getRGB(x, y) & 0xff0000) >> 16;
				if(r<50){
				temp=temp + 0;}
				else
				{temp=temp + 1;}
			 }
		   }
		
		return temp;
	}
}
