package me.nereo.multi_image_selector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.importotherlib.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * 多图选择 Created by Nereo on 2015/4/7.
 */

public class MultiImageSelectorActivity_Header extends FragmentActivity implements MultiImageSelectorFragment.Callback {

	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;

	private ArrayList<String> resultList = new ArrayList<String>();
	private Button mSubmitButton;
	private int mDefaultCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default);
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}

		Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

		getSupportFragmentManager().beginTransaction()
				.add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
				.commit();

		// 返回按钮
		findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		// 完成按钮
		mSubmitButton = (Button) findViewById(R.id.commit);
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText("完成");
			mSubmitButton.setEnabled(false);
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
			mSubmitButton.setEnabled(true);
		}
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() > 0) {
					// 返回已选择的图片数据
					Intent data = new Intent();
					data.putStringArrayListExtra(EXTRA_RESULT, resultList);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANCELED);
	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		resultList.add(path);
		data.putStringArrayListExtra(EXTRA_RESULT, resultList);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText("完成");
			mSubmitButton.setEnabled(false);
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			try {
				// 压缩图片
				Bitmap bitmap = compressBySize(imageFile.getAbsolutePath(), 480, 320);
				// 保存图片
				saveFile(bitmap, imageFile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent data = new Intent();
			resultList.add(imageFile.getAbsolutePath());
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));// 刷新系统相册
			finish();
		}
	}

	// 压缩图片尺寸
	public Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || widthRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}

	// 存储进SD卡
	public void saveFile(Bitmap bm, String fileName) throws Exception {
		File dirFile = new File(fileName);
		// 检测图片是否存在
		if (dirFile.exists()) {
			dirFile.delete(); // 删除原图片
		}
		File myCaptureFile = new File(fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		// 100表示不进行压缩，70表示压缩率为30%
		bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		bos.flush();
		bos.close();
	}
}
