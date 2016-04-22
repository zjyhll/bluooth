package com.keyhua.renameyourself.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.view.CustomDialog;


public class UpdateManager {

	private Context mContext;

	// 提示语
	private String updateMsg = "有最新的软件包哦，亲快下载吧~";

	// 返回的安装包url
	private String apkUrl = "";

	private Dialog noticeDialog;

	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/outdoor/";

	private static final String saveFileName = savePath + "outdoorapk.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private TextView progress_text;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;// 下载中是否点取消

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				progress_text.setText("          当前下载进度:" + progress + "%                            ");
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, String installUrl, Handler handler) {
		this.mContext = context;
		this.apkUrl = installUrl;
		this.handler = handler;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	/** 应用是否更新 */
	private Handler handler;

	public void showNoticeDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		builder.setCancelable(false);// 点击对话框外部不关闭对话框
		builder.setMessage(updateMsg);
		builder.setTitle("软件版本更新");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});

		builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				handler.sendEmptyMessage(3);
			}
		});
		try {
			builder.create().show();
		} catch (Exception e) {
		}
	}

	public void showDownloadDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		builder.setCancelable(false);// 点击对话框外部不关闭对话框
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		progress_text = (TextView) v.findViewById(R.id.progress_text);
		progress_text.setText("          当前下载进度:" + 0 + "%                            ");
		builder.setContentView(v);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
				handler.sendEmptyMessage(3);
			}
		});
		builder.create().show();
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (Exception e) {
				handler.sendEmptyMessage(4);
			}
		}
	};

	/**
	 * 下载apk
	 * 
	 * @param
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk文件
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		System.out.println("filepath=" + apkfile.toString() + "  " + apkfile.getPath());
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃

	}
}
