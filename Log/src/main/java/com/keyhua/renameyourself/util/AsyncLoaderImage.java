package com.keyhua.renameyourself.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class AsyncLoaderImage {
	private static String TAG = "AsyncLoaderImage";
	private static HashMap<String, SoftReference<Bitmap>> bitmapCache;

	public AsyncLoaderImage() {
		if (bitmapCache == null) {
			bitmapCache = new HashMap<String, SoftReference<Bitmap>>();
		}
	}

	/**
	 * 先从软引用中获取图片未取到则从SDCard上取
	 * 
	 * @param url
	 * @param imageCallback
	 * @return
	 */
	public Bitmap loadBitmap(final String url, final ImageCallback imageCallback) {
		Bitmap bitmap;
		/**
		 * 从软引用中获取图片
		 */
		if (bitmapCache.containsKey(url)) {
			SoftReference<Bitmap> softReference = bitmapCache.get(url);
			bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		/**
		 * 从SD卡中获取图片
		 */
		bitmap = CacheSD.getBitmap(getImageName(url));
		if (bitmap != null) {
			/**
			 * 取到后放入软引用中
			 */
			bitmapCache.put(url, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				imageCallback.imageLoaded((Bitmap) msg.obj, url);
			}
		};
		/**
		 * 都未取到则开启线程下载图片
		 */
		new Thread() {
			@Override
			public void run() {
				try {
					Bitmap bitmap = DownLoadBitmap(url);
					if (bitmap != null) {
						/**
						 * 下载完成后入软引用中
						 */
						// bitmapCache.put(url, new
						// SoftReference<Bitmap>(bitmap));
						/**
						 * 将图片保存到本地
						 */
						// new CacheSD().saveBitmap(bitmap, getImageName(url));
						handler.obtainMessage(0, bitmap).sendToTarget();
					}
				} catch (OutOfMemoryError error) {
					Log.i(TAG, error.toString());
					System.gc();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return null;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap imageBitmap, String imageUrl);
	}

	/**
	 * 线程中调用此方法加载图片
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static Bitmap DownLoadBitmap(String path) {
		Bitmap bitmap = null;
		HttpURLConnection conn;
		URL url = null;
		InputStream is = null;

		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap != null) {
				/**
				 * 创建一个固定宽高的bitmap对象
				 */
				bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
						bitmap.getHeight(), true);
			}
		} catch (OutOfMemoryError e) {
			System.gc();
			Log.i(TAG, e.toString());
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public String getImageName(String url) {
		String imageName = null;
		if (url != null) {
			imageName = url.substring(url.lastIndexOf("/") + 1);
		}
		return imageName;
	}
}
