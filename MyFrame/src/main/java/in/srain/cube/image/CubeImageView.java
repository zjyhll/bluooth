package in.srain.cube.image;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import in.srain.cube.image.drawable.RecyclingBitmapDrawable;

/**
 * Sub-class of ImageView which:
 * <ul>
 * <li>automatically notifies the Drawable when it is being displayed.
 * </ul>
 * <p/>
 * Part of the code is taken from the Android best practice of displaying
 * Bitmaps
 * <p/>
 * <a href=
 * "http://developer.android.com/training/displaying-bitmaps/index.html">
 * Displaying Bitmaps Efficiently</a>.
 */
public class CubeImageView extends ImageView {

	private ImageLoader mImageLoader;

	private ImageTask mImageTask;
	private boolean mClearWhenDetached = true;
	private String mStr;
	private ImageLoadRequest mRequest;

	public CubeImageView(Context context) {
		super(context);
		// init(context, null);
	}

	public CubeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// init(context, attrs);
	}

	public CubeImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// init(context, attrs);
	}

	private Paint paint;
	private int LiftUp = 0;
	private int RightUp = 0;
	private int LiftDown = 0;
	private int RightDown = 0;
	private Paint paint2;

//	private void init(Context context, AttributeSet attrs) {
//		if (attrs != null) {
//			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
//			LiftUp = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_LiftUp, LiftUp);
//			RightUp = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_RightUp, RightUp);
//			LiftDown = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_LiftDown, LiftDown);
//			RightDown = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_RightDown, RightDown);
//		} else {
//			float density = context.getResources().getDisplayMetrics().density;
//			LiftUp = (int) (LiftUp * density);
//			RightUp = (int) (RightUp * density);
//			LiftDown = (int) (LiftDown * density);
//			RightDown = (int) (RightDown * density);
//		}
//
//		paint = new Paint();
//		paint.setColor(Color.WHITE);
//		paint.setAntiAlias(true);
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//
//		paint2 = new Paint();
//		paint2.setXfermode(null);
//	}
//
//	@Override
//	public void draw(Canvas canvas) {
//		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
//		Canvas canvas2 = new Canvas(bitmap);
//		super.draw(canvas2);
//		drawLiftUp(canvas2);
//		drawRightUp(canvas2);
//		drawLiftDown(canvas2);
//		drawRightDown(canvas2);
//		canvas.drawBitmap(bitmap, 0, 0, paint2);
//		// 先判断是否已经回收
//		if (bitmap != null && !bitmap.isRecycled()) {
//			// 回收并且置为null
//			bitmap.recycle();
//			bitmap = null;
//		}
//	}
//
//	private void drawLiftUp(Canvas canvas) {
//		Path path = new Path();
//		path.moveTo(0, LiftUp);
//		path.lineTo(0, 0);
//		path.lineTo(LiftUp, 0);
//		path.arcTo(new RectF(0, 0, LiftUp * 2, LiftUp * 2), -90, -90);
//		path.close();
//		canvas.drawPath(path, paint);
//	}
//
//	private void drawLiftDown(Canvas canvas) {
//		Path path = new Path();
//		path.moveTo(0, getHeight() - LiftDown);
//		path.lineTo(0, getHeight());
//		path.lineTo(LiftDown, getHeight());
//		path.arcTo(new RectF(0, getHeight() - LiftDown * 2, 0 + LiftDown * 2, getHeight()), 90, 90);
//		path.close();
//		canvas.drawPath(path, paint);
//	}
//
//	private void drawRightDown(Canvas canvas) {
//		Path path = new Path();
//		path.moveTo(getWidth() - RightDown, getHeight());
//		path.lineTo(getWidth(), getHeight());
//		path.lineTo(getWidth(), getHeight() - RightDown);
//		path.arcTo(new RectF(getWidth() - RightDown * 2, getHeight() - RightDown * 2, getWidth(), getHeight()), 0, 90);
//		path.close();
//		canvas.drawPath(path, paint);
//	}
//
//	private void drawRightUp(Canvas canvas) {
//		Path path = new Path();
//		path.moveTo(getWidth(), RightUp);
//		path.lineTo(getWidth(), 0);
//		path.lineTo(getWidth() - RightUp, 0);
//		path.arcTo(new RectF(getWidth() - RightUp * 2, 0, getWidth(), 0 + RightUp * 2), -90, 90);
//		path.close();
//		canvas.drawPath(path, paint);
//	}

	/**
	 * Notifies the drawable that it's displayed state has changed.
	 *
	 * @param drawable
	 * @param isDisplayed
	 */
	private static void notifyDrawable(Drawable drawable, final boolean isDisplayed) {
		if (drawable instanceof RecyclingBitmapDrawable) {
			// The drawable is a CountingBitmapDrawable, so notify it
			((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
		} else if (drawable instanceof LayerDrawable) {
			// The drawable is a LayerDrawable, so recurse on each layer
			LayerDrawable layerDrawable = (LayerDrawable) drawable;
			for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
				notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
			}
		}
	}

	public void setClearDrawableWhenDetached(boolean clearWhenDetached) {
		mClearWhenDetached = clearWhenDetached;
	}

	public void clearDrawable() {
		setImageDrawable(null);

		if (null != mImageTask && null != mImageLoader) {
			mImageLoader.detachImageViewFromImageTask(mImageTask, this);
			clearLoadTask();
		}
	}

	/**
	 * @see ImageView#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		// This has been detached from Window, so clear the drawable
		// If this view is recycled ???
		if (mClearWhenDetached) {
			clearDrawable();
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mClearWhenDetached) {
			tryLoadImage();
		}
	}

	/**
	 * @see ImageView#setImageDrawable(Drawable)
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {

		// Keep hold of previous Drawable
		final Drawable previousDrawable = getDrawable();

		super.setImageDrawable(drawable);

		// Notify new Drawable that it is being displayed
		notifyDrawable(drawable, true);

		// Notify old Drawable so it is no longer being displayed
		notifyDrawable(previousDrawable, false);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		clearLoadTask();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		clearLoadTask();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		clearLoadTask();
	}

	public void onLoadFinish() {
	}

	private void clearLoadTask() {
		mImageTask = null;
	}

	public void loadImage(ImageLoader imageLoader, String url) {
		loadImage(imageLoader, url, 0, 0, null);
	}

	public void loadImage(ImageLoader imageLoader, String url, ImageReuseInfo imageReuseInfo) {
		loadImage(imageLoader, url, 0, 0, imageReuseInfo);
	}

	public void loadImage(ImageLoader imageLoader, String url, int specifiedSize) {
		loadImage(imageLoader, url, specifiedSize, specifiedSize, null);
	}

	public void loadImage(ImageLoader imageLoader, String url, int specifiedSize, ImageReuseInfo imageReuseInfo) {
		loadImage(imageLoader, url, specifiedSize, specifiedSize, imageReuseInfo);
	}

	public void loadImage(ImageLoader imageLoader, String url, int specifiedWidth, int specifiedHeight) {
		loadImage(imageLoader, url, specifiedWidth, specifiedHeight, null);
	}

	public void loadImage(ImageLoader imageLoader, ImageLoadRequest request) {
		mRequest = request;
		mImageLoader = imageLoader;

		if (request == null || TextUtils.isEmpty(request.getUrl())) {
			mImageLoader.getImageLoadHandler().onLoadError(mImageTask, this, ImageTask.ERROR_EMPTY_URL);
			mImageTask = null;
			return;
		}
		tryLoadImage();
	}

	public void loadImage(ImageLoader imageLoader, String url, int specifiedWidth, int specifiedHeight,
			ImageReuseInfo imageReuseInfo) {
		mRequest = new ImageLoadRequest(url, specifiedWidth, specifiedHeight, 0, imageReuseInfo);
		loadImage(imageLoader, mRequest);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		tryLoadImage();
	}

	private void tryLoadImage() {

		if (mRequest == null || TextUtils.isEmpty(mRequest.getUrl())) {
			return;
		}

		int width = getWidth();
		int height = getHeight();

		LayoutParams lyp = getLayoutParams();
		boolean isFullyWrapContent = lyp != null && lyp.height == LayoutParams.WRAP_CONTENT
				&& lyp.width == LayoutParams.WRAP_CONTENT;
		// if the view's bounds aren't known yet, and this is not a
		// wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		mRequest.setLayoutSize(width, height);

		// 1. Check the previous ImageTask related to this ImageView
		if (null != mImageTask) {

			// duplicated ImageTask, return directly.
			if (mImageTask.isLoadingThisUrl(mRequest)) {
				return;
			}
			// ImageView is reused, detach it from the related ImageViews of the
			// previous ImageTask.
			else {
				mImageLoader.detachImageViewFromImageTask(mImageTask, this);
			}
		}

		// 2. Let the ImageView hold this ImageTask. When ImageView is reused
		// next time, check it in step 1.
		ImageTask imageTask = mImageLoader.createImageTask(mRequest);
		// .createImageTask(mUrl, width, height, mImageReuseInfo);
		mImageTask = imageTask;

		// 3. Query cache, if hit, return at once.
		boolean hitCache = mImageLoader.queryCache(imageTask, this);
		if (hitCache) {
			return;
		} else {
			mImageLoader.addImageTask(mImageTask, this);
		}
	}

	@Override
	public String toString() {
		if (mStr == null) {
			mStr = "[CubeImageView@" + Integer.toHexString(hashCode()) + ']';
		}
		return mStr;
	}
}