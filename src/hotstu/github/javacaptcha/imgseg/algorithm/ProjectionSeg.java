package hotstu.github.javacaptcha.imgseg.algorithm;

import hotst.github.javacaptcha.common.ImageCommons;
import hotst.github.javacaptcha.common.PiccConstants;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotst.github.javacaptcha.model.SubRect;

import java.util.ArrayList;
import java.util.List;

/**
 * 投影分割
 * 图像对应方向的投影，就是在该方向取一条直线，统计垂直于该直线（轴）的图像上的像素的黑点数量，累加求和作为该轴该位置的值；
 * 基于图像投影的切割就是将图像映射成这种特征后，基于这种特征判定图像的切割位置（坐标），用这个坐标来切割原图像，得到目标图像
 * @author zc
 *
 */
public class ProjectionSeg {
	
		public static List<BinaryMatrix> myProjection( BinaryMatrix img){
			List<SubRect> subImgList = new ArrayList<SubRect>();
			int h = img.getHeight();
			int[] xpro = img.xpro();
			char currentGroup = 0;
			for(int i=0;i<xpro.length;i++){
				int length=0;
				while(i<xpro.length && xpro[i]>0){
					i++;
					length++;
				}
				if(length>PiccConstants.MIN_RECT_WIDTH){
					currentGroup ++;
					SubRect rect = new SubRect(currentGroup);			//保存当前字符块的坐标点
					rect.left=i - length-1;
					rect.top= 0;
					rect.right= i;
					rect.bottom=h-1;
					subImgList.add(rect);
				}
			}
	
		List<BinaryMatrix> projectionList = new ArrayList<BinaryMatrix>();
		for (SubRect r : subImgList) {
			BinaryMatrix image = BinaryMatrix.fromBlank(r.getWidth(), r.getHeight());
			for (int i = 0; i < r.getWidth(); i++) {
				for (int j = 0; j < r.getHeight(); j++) {
					if(img.getValue(r.left+i, r.top+j))
						image.setTrue(i, j);
				}
			}
			if(image.getHeight()>=PiccConstants.AVER_CHAR_HEIGHT+PiccConstants.CHAR_RANG_WINDOW){
				image.navieRemoveNoise(2);
			}
			projectionList.add(ImageCommons.cutHistogram(image,(char)subImgList.indexOf(r)));
		}
		return projectionList;
	 }
}
