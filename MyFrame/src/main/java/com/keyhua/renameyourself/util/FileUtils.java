package com.keyhua.renameyourself.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class FileUtils {
	private static final String TAG = "Detail_PhotosActivity";

	public static boolean removeFile(String path) {
		Log.i("delete", "delete");
		File file = new File(path);
		if (file.exists()) {
			boolean bool = file.delete();
			Log.i("deletebool", "delete" + bool);
			return bool;
		}
		return false;
	}

	public static boolean fileExist(String path) {
		File file = new File(path);
		boolean bool = file.exists();
		LogOut.logD(TAG, "fileExist:" + bool);
		return bool;
	}

	public synchronized static boolean reName(String oldFullName,
			String newFullName) {
		Log.i("rename", oldFullName);
		Log.i("rename", newFullName);
		File file = new File(oldFullName);
		if (!file.exists()) {
			return false;
		}
		File newfile = new File(newFullName);
		if (newfile.exists()) {
			newfile.delete();
		}

		boolean bool = file.renameTo(newfile);
		Log.i("renamebool", "rename" + bool);
		return bool;
	}

	public synchronized static void openFile(Context context,
			String fileFullName) {
		File file = new File(fileFullName);
		String type = getMIMEType(file);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
	}

	/**
	 * @param f
	 * @return
	 * @判断对应属性
	 */
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (!end.equals("apk")) {
			type += "/*";
		}
		return type;

	}

	public static final String getFilePath(final String fileFullName) {
		return fileFullName.substring(0, fileFullName.lastIndexOf('/') + 1);
	}

	public static final String getFileExe(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return null;
		}
		return fileName.substring(index + 1);
	}

	public static final String getFileName(final String fileFullName) {
		return fileFullName.substring(fileFullName.lastIndexOf('/') + 1);
	}

	public synchronized static boolean saveFile(String fileFullName,
			byte[] bytes, long offset) {
		Log.i("saveFile11111111111111", fileFullName + "-" + offset);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(fileFullName, true);
			fos.write(bytes, 0, bytes.length);
			fos.close();
			fos = null;
			File file = new File(fileFullName);

			Log.i("filesize", file.length() + "");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static File writeFileToSDCard(String path, String fileName,
			InputStream is) {
		File file = null;
		OutputStream os = null;
		if (path != null && fileName != null && is != null) {
			try {
				createSDDir(path);
				Log.v("TAG", "writeFileToSDCard>>>>>>>>>>>>" + path
						+ "fileName:" + fileName);
				file = createSDFile(path + fileName);
				if (file.exists()) {
					os = new FileOutputStream(file);
					int len = -1;
					byte[] buffer = new byte[1024];
					while ((len = is.read(buffer)) != -1) {
						os.write(buffer, 0, len);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					if (os != null) {
						os.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return file;
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(dirName);// Constants.SDCARD_PATH + dirName
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	

}
