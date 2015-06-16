package com.halong.aubaby.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class MyScrollView1 extends ScrollView {

	private View inner;// 孩子View

	private float y;// 点击时y坐标

	private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

	private boolean isCount = false;// 是否开始计算

	public MyScrollView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			// 手指松开.
			if (isNeedAnimation()) {
				animation();
				isCount = false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			final float preY = y;// 按下时的y坐标
			float nowY = ev.getY();// 时时y坐标
			int deltaY = (int) (preY - nowY);// 滑动距离
			if (!isCount) {
				deltaY = 0; // 在这里要归0.
			}

			y = nowY;
			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			if (isNeedMove()) {
				// 初始化头部矩形
				if (normal.isEmpty()) {
					// 保存正常的布局位置
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}
				Log.e("jj", "矩形：" + inner.getLeft() + "," + inner.getTop()
						+ "," + inner.getRight() + "," + inner.getBottom());
				// 移动布局
				inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2,
						inner.getRight(), inner.getBottom() - deltaY / 2);
			}
			isCount = true;
			break;

		default:
			break;
		}
	}

	public void animation() {
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(200);
		inner.startAnimation(ta);
		// 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);

		Log.e("jj", "回归：" + normal.left + "," + normal.top + "," + normal.right
				+ "," + normal.bottom);

		normal.setEmpty();

	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		Log.e("jj", "scrolly=" + scrollY);
		// 0是顶部，后面那个是底部
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}