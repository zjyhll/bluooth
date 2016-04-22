package com.keyhua.renameyourself.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageToBlur {

	/**
	 * 转换成圆角
	 * 
	 * @param bmp
	 * @param roundPx
	 * @return
	 */
	public static Bitmap convertToRoundedCorner(Bitmap bmp, float roundPx) {
		Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
		// 得到画布
		Canvas canvas = new Canvas(newBmp);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// 第二个和第三个参数一样则画的是正圆的一角，否则是椭圆的一角
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bmp, rect, rect, paint);
		return newBmp;
	}

	/**
	 * 将彩色图转换为黑白图
	 * 
	 * @param 位图
	 * @return 返回转换好的位图
	 */
	public static Bitmap convertToBlackWhite(Bitmap bmp) {
		int width = bmp.getWidth(); // 获取位图的宽
		int height = bmp.getHeight(); // 获取位图的高
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];
				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);
				grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}
}
