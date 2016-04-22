package com.keyhua.renameyourself.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.app.App;


public class ImageControl {

	private static String TAG = "ImageControl";
	public static String path;
	public static final int PHOTO_BY_CAMERA = 1;
	public static final int PHOTO_BY_ALBUMS = 0;

	public static File getAlbumDir() {
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "outdoor");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 通过传入位图,新的宽.高比进行位图的缩放操作
	 */
	public static Bitmap BitmapZoom(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int w = App.getInstance().getScreenWidth();
		int h = App.getInstance().getScreenHeight();
		try {
			if (width > w || height > h) {
				Matrix matrix = new Matrix();
				if (width > w) {
					// 计算缩放比
					float scaleWidht = ((float) w / width);
					// 缩放操作
					matrix.postScale(scaleWidht, scaleWidht);
					Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
					return newbmp;
				} else {
					return bitmap;
				}
			} else if (width < w && height < h) {
				// 取当前bitmap对象的宽高与屏幕的宽高的比例取最小值进行放大操作
				return Enlarge(bitmap, (float) w / width < (float) h / height ? (float) w / width : (float) h / height);
			} else if (width == h || height == h) {
				return bitmap;
			}
		} catch (OutOfMemoryError error) {
			Log.i(TAG, error.toString());
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return null;
	}

	// 放大图片
	public static Bitmap Enlarge(Bitmap bitmap, double scale) {
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		float scaleWidth = 1;
		float scaleHeight = 1;
		/* 计算这次要放大的比例 */
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		Bitmap resizeBmp = null;
		try {
			/* 产生reSize后的Bitmap对象 */
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
		} catch (OutOfMemoryError e) {
			if (!resizeBmp.isRecycled()) {
				resizeBmp.recycle();
			}
			System.gc();
			Log.i(TAG, e.toString());
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return resizeBmp;
	}

	public static Bitmap getBitmap(String srcPath) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * @param srcPath
	 * @return
	 * @throws Exception
	 *             本地图片操作
	 */
	public static Bitmap getBitmaptz(String srcPath) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// newOpts.inSampleSize = 2;// 设置缩放比例
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		int fw = bitmap.getWidth() / 2;
		int fh = bitmap.getHeight() / 2;
		int sw = DensityUtils.dp2px(App.getInstance().getApplicationContext(), 45);
		int sh = DensityUtils.dp2px(App.getInstance().getApplicationContext(), 45);

		if (sw >= fw) {
			fw = 0;
		}
		if (sh >= fh) {
			fh = 0;
		}
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, fw, fh, sw, sh);
		return resizeBmp;// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressImage(Bitmap image) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 按正方形裁切图片
	 * 
	 * @param width
	 *            裁剪的宽度
	 * @param height
	 *            裁剪的高度 height=0裁剪成高宽为width的正方形
	 */
	public static Bitmap BitmapCrop(Bitmap bitmap, int width, int height) {

		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();

		// 如果bitmap宽度小于裁剪宽度则return null
		if (bitmap.getWidth() < width) {
			return null;
		}

		int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
		int retY = w > h ? 0 : (h - w) / 2;
		// 如果height=0则按宽度裁剪成正文形否则按宽度，高度裁剪
		return Bitmap.createBitmap(bitmap, retX, retY, width, height == 0 ? width : height, null, false);
	}

	// 拍照
	public static void getPicFromCapture(Context context) {
		try {
			// 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			((Activity) context).startActivityForResult(intent, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 从相册 中选择图片
	public static void getPicFromAlbums(Context context) {
		try {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			((Activity) context).startActivityForResult(intent, 1);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 调用系统裁剪图片
	public static void SysCropBitmap(Uri uri, Context context) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("scale", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("return-data", false);
			intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);

			((Activity) context).startActivityForResult(intent, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 上传图片工具类 返回bitmap
	public static void getPhotoPath(Context context, Uri uri, int requestCode, Intent data) {
		Bitmap bitmap = null;
		if (uri != null) {
			try {
				bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
				// 获取上传图片文件名
				String[] proj = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = ((Activity) context).managedQuery(uri, proj, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				// 获取图片路径
				path = cursor.getString(column_index);
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}
			// 裁剪拍照图片
			SysCropBitmap(uri, context);
		} else {
			Bundle extras = data.getExtras();
			if (extras != null) {
				// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
				bitmap = extras.getParcelable("data");
				if (bitmap != null) {
					SysCropBitmap(uri, context);
				}
			}
		}
	}

	/**
	 * 从服务器取图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 2;
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			bitmap = BitmapFactory.decodeResource(App.getInstance().getApplicationContext().getResources(),
					R.mipmap.dmbg_default);
		}
		return bitmap;
	}

	// 生成圆角图片
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			final float roundPx = 90;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	public static String saveImageToGallery(Context context, Bitmap bmp, int index) {
		// 0outdoor 1huodong 2topimage 3youji
		File file = null;
		// 首先保存图片
		try {
			File appDir = null;
			switch (index) {
			case 0:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdoor");
				} else {
					appDir = new File(context.getFilesDir(), "outdoor");
				}
				break;
			case 1:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdoorhuodong");
				} else {
					appDir = new File(context.getFilesDir(), "outdoorhuodong");
				}
				break;
			case 2:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdoortopimage");
				} else {
					appDir = new File(context.getFilesDir(), "outdoortopimage");
				}
				break;
			case 3:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdooryouji");
				} else {
					appDir = new File(context.getFilesDir(), "outdooryouji");
				}
				break;
			case 4:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdoormingdan");
				} else {
					appDir = new File(context.getFilesDir(), "outdoormingdan");
				}
				break;
			case 5:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appDir = new File(Environment.getExternalStorageDirectory(), "outdoorhuodongleader");
				} else {
					appDir = new File(context.getFilesDir(), "outdoorhuodongleader");
				}
				break;

			default:
				break;
			}
			if (!appDir.exists()) {
				appDir.mkdir();
			}
			String fileName = System.currentTimeMillis() + ".jpg";
			file = new File(appDir, fileName);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bmp.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 其次把文件插入到系统图库
			// try {
			// MediaStore.Images.Media.insertImage(context.getContentResolver(),
			// file.getAbsolutePath(), fileName,
			// null);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

}
