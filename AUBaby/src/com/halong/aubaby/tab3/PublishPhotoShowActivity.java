package com.halong.aubaby.tab3;

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
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.photosalbum.ImgCallBack;
import com.halong.aubaby.photosalbum.Util;
import com.halong.aubaby.R;

public class PublishPhotoShowActivity extends BackActivity implements
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

	private ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	private Util util;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_show);
		findViewById(R.id.progress).setVisibility(View.GONE);
		Intent intent = getIntent();
		String url = intent.getStringExtra(Keys.PUBLISH_PHOTO_PATH);

		mImageView = (ImageView) findViewById(R.id.imageView1);
		util = new Util(this);
		util.imgExcute(mImageView, imgCallBack, url);
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
}
