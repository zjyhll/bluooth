package com.keyhua.clippicture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.ImageControl;

/**
 * @author Administrator 整体思想是：截取屏幕的截图，然后截取矩形框里面的图片
 */
public class ClipPictureActivity extends BaseActivity implements OnTouchListener, OnClickListener {
	private ImageView srcPic;
	private Button sure;
	private ClipView clipview;

	// These matrices will be used to move and zoom image
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private static final String TAG = "11";
	private int mode = NONE;
	private Bitmap bitmap;
	private DisplayMetrics dm;
	private float minScaleR = 0f;// 最小缩放比例
	private static final float MAX_SCALE = 100f;// 最大缩放比例
	// Remember some things for zooming
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private int statusBarHeight = 0;
	private int titleBarHeight = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_clippic);
		init();
	}

	@Override
	protected void onInitData() {
		titleStr="剪切图片";
		initHeaderOther("", "剪切图片", true, false, false);
		srcPic = (ImageView) this.findViewById(R.id.src_pic);
		sure = (Button) this.findViewById(R.id.sure);
		clipview = (ClipView) this.findViewById(R.id.clipview);
	}

	@Override
	protected void onResload() {
		// 获取分辨率
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 将图片放上去
		if (!TextUtils.isEmpty(App.getInstance().getSelectPath())) {
			try {
				bitmap = ImageControl.getBitmap(App.getInstance().getSelectPath());
				srcPic.setImageBitmap(bitmap);
				minZoom();
				center();
				srcPic.setImageMatrix(matrix);
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void setMyViewClick() {
		srcPic.setOnTouchListener(this);
		sure.setOnClickListener(this);
	}

	/* 这里实现了多点触摸放大缩小，和单点移动图片的功能，参考了论坛的代码 */
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			// 設置初始點位置
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		CheckView();
		return true; // indicate event was handled
	}

	/**
	 * 限制最大最小缩放比例，自动居中
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < 0.2f) {
				matrix.setScale(0.2f, 0.2f);
			}
			if (p[0] > MAX_SCALE) {
				matrix.setScale(MAX_SCALE, MAX_SCALE);
			}
			center();
		}
	}

	/**
	 * 最小缩放比例，最大为100%
	 */
	private void minZoom() {
		try {
			minScaleR = Math.min((float) dm.widthPixels / (float) bitmap.getWidth(),
					(float) dm.heightPixels / (float) bitmap.getHeight());
			if (minScaleR < 1.0) {
				float minScaleN = Math.min((float) dm.widthPixels, (float) dm.heightPixels);
				matrix.postScale(minScaleN, minScaleN);
			}
		} catch (Exception e) {
		}
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/* 完成剪切 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_itv_back:
			finish();
			break;
		case R.id.sure:
			// 拿到图片路径
			if (!TextUtils.isEmpty(App.getInstance().getSelectPath())) {
				showdialogtext("正在截图中...");
				try {
					File f = new File(App.getInstance().getSelectPath());
					Bitmap bm = getBitmap();
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(new File(ImageControl.getAlbumDir(), "small_" + f.getName()));
						bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
					} finally {
						try {
							if (fos != null)
								fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// 赋新值
					App.getInstance()
							.setSelectPathTemp(ImageControl.getAlbumDir() + File.separator + "small_" + f.getName());
				} catch (Exception e) {
					Log.e(TAG, "error", e);
				}
				if (isshowdialog()) {
					closedialog();
				}
//				openActivity(GenRenZiLiaoActivity.class);
			}
			break;

		default:
			break;
		}
	}

	/* 获取矩形区域内的截图 */
	private Bitmap getBitmap() {
		getBarHeight();
		Bitmap screenShoot = takeScreenShot();
		// clipview.measure(0, 0);
		// int width = clipview.getMeasuredWidth();
		// int height = clipview.getMeasuredHeight();
		clipview = (ClipView) this.findViewById(R.id.clipview);
		int width = clipview.getWidth();
		int height = clipview.getHeight();
		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, (width - height / 3) / 2,
				height / 3 + titleBarHeight + statusBarHeight, height / 3, height / 3);
		return finalBitmap;
	}

	private void getBarHeight() {
		// 获取状态栏高度
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;

		int contenttop = this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight是上面所求的状态栏的高度
		titleBarHeight = contenttop - statusBarHeight;

		Log.v(TAG, "statusBarHeight = " + statusBarHeight + ", titleBarHeight = " + titleBarHeight);
	}

	// 获取Activity的截屏
	private Bitmap takeScreenShot() {
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {

		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			sure.measure(0, 0);
			int Heightsure = sure.getMeasuredHeight();
			int screenHeight = dm.heightPixels - Heightsure / 2;
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = srcPic.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}
}