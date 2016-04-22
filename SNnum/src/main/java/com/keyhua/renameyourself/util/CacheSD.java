package com.keyhua.renameyourself.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class CacheSD {

	private final String TAG = this.getClass().toString();
	public final static int CACHE_SIZE = 20; // 设置保存图片文件夹为20M
	public final static int FREE_SD_SPACE_NEEDED_TO_CACHE = 20;// 设置缓存大小
	public final static int MB = 1024 * 1024;
	public final int OVER_TIME = 14 * 24 * 60 * 60 * 1000; // 过期时间总数是两个星期
	private static String path = Environment.getExternalStorageDirectory()
			.getPath() + "/ViewPager";

	/**
	 * 获取缓存目录
	 * 
	 * @param filename
	 * @return
	 */
	private static String getDirectory(String imageUrl) {
		return imageUrl.substring(0, imageUrl.lastIndexOf("/") + 1)
				.toLowerCase();
	}

	/**
	 * 截取url获取文件名
	 * 
	 * @param url
	 * @return
	 */
	private static String convertUrlToFileName(String url) {
		return path
				+ "/cache/image/"
				+ url.substring(url.lastIndexOf("/") + 1, url.length())
						.toLowerCase();
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static int freeSpaceOnSd() {
		boolean sdCardExit = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// 判断sdcard是否存在
		if (sdCardExit) {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getPath());
			double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
					.getBlockSize()) / MB;
			return (int) sdFreeMB;
		} else {
			Log.i(null, "sdCard is not exist");
			return 0;
		}
	}

	/**
	 * 修改文件的最后修改时间
	 * 
	 * @param 文件目录
	 * @param 文件名称
	 */
	private static void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 * 
	 * @param dirPath
	 * @param filename
	 */
	private void removeCache(String dirPath) {
		File dir = new File(getDirectory(dirPath));
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			dirSize += files[i].length();
		}
		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i(TAG, "Clear some expiredcache files");
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
		}
	}

	/**
	 * 删除过期文件
	 * 
	 * @param dirPath
	 * @param filename
	 */
	public void removeExpiredCache(String dirPath) {
		File file = new File(dirPath);
		if (System.currentTimeMillis() - file.lastModified() > OVER_TIME) {
			Log.i(TAG, "Clear some expiredcache files ");
			file.delete();
		}
	}

	/**
	 *  根据文件的最后修改时间进行排序 *
	 */
	class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 删除图片目录
	 * 
	 * @param url
	 */
	public static void deleteDir(String url) {
		File f = new File(url);
		if (f.isDirectory()) {
			f.delete();
		}
	}

	public void saveBitmap(Bitmap bitmap, String url) {
		try {
			// 获得文件名字
			final File fileName = new File(convertUrlToFileName(url));

			// 判断sdcard上的空间
			if (CACHE_SIZE > freeSpaceOnSd()) {
				Log.w(TAG, "Low free space onsd, do not cache");
				return;
			}
			// 如果空间不够删除过期图片
			removeExpiredCache(url);
			// 要是还是不够大，删除近期没用过的图片
			removeCache(getDirectory(fileName.toString()));
			// 创建图片缓存文件夹

			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				File looklo = new File(path);
				File cache = new File(path + "/cache");
				File image = new File(path + "/cache" + "/image");
				// 如果文件夹不存在
				if (!looklo.exists()) {
					// 按照指定的路径创建looklo文件夹
					looklo.mkdir();
				}
				if (!cache.exists()) {
					// 按照指定的路径创建cache文件夹
					cache.mkdir();
				}
				if (!image.exists()) {
					// 按照指定的路径创建image文件夹
					image.mkdir();
				}
				// 检查图片是否存在
				if (!fileName.exists()) {
					fileName.createNewFile();
				}
			}

			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(fileName));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用SD卡上的图片
	 * 
	 */
	public static Bitmap getBitmap(String imageUrl) {

		Bitmap bitmap = null;

		// 获得文件路径
		String imageSDCardPath = convertUrlToFileName(imageUrl);
		File file = new File(imageSDCardPath);
		// 检查图片是否存在
		if (!file.exists()) {
			return null;
		}
		bitmap = BitmapFactory.decodeFile(imageSDCardPath, null);
		if (bitmap != null) {
			// 修改文件时间
			updateFileTime(getDirectory(imageUrl), imageSDCardPath);
			return bitmap;
		} else
			return null;
	}
}
