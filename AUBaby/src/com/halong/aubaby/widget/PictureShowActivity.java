package com.halong.aubaby.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PictureShowActivity extends BackActivity implements
		OnTouchListener {

	/** Called when the activity is first created. */

	// 放大缩小
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist;

	private ImageView mImageView;

	// 模式
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_show);
		Intent intent = getIntent();
		String url;
		if (intent.getExtras().containsKey(Keys.CLICK_GRIDVIEW_ITEM)) {
			url = ContantUtil.PICTURE_URL + Keys.O_VIEW
					+ intent.getStringExtra(Keys.CLICK_GRIDVIEW_ITEM);
		} else if (intent.getExtras().containsKey(Keys.PHOTO_URL)) {
			url = getIntent().getStringExtra(Keys.PHOTO_URL);
		} else {
			url = ContantUtil.PICTURE_URL + Keys.O_VIEW
					+ intent.getStringExtra(Keys.CLICK_GRIDVIEW_ITEM);
		}

		DemoApplication app = (DemoApplication) getApplication();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageLoader.displayImage(url, mImageView, mOptions,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub
						findViewById(R.id.progress).setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						findViewById(R.id.progress).setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});
		mImageView.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView mImageView = (ImageView) v;
		mImageView.setScaleType(ScaleType.MATRIX);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模式
		case MotionEvent.ACTION_DOWN:
			matrix.set(mImageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;

		// 设置多点触摸模式
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		// 若为DRAG模式，则点击移动图片
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			}
			// 若为ZOOM模式，则点击触摸缩放
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					// 设置硕放比例和图片的中点位置
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		mImageView.setImageMatrix(matrix);
		return true;
	}

	// 计算移动距离
	@SuppressLint("FloatMath")
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 计算中点位置
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mImageLoader.clearMemoryCache();
	}
}
