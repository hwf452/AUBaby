package com.halong.aubaby.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ChildHorizontalScrollView extends HorizontalScrollView {

	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	public ChildHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ChildHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ChildHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		curP.x = ev.getX();
		curP.y = ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = ev.getX();
			downP.y = ev.getY();
		}

		if (ev.getAction() == MotionEvent.ACTION_UP) {
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (Math.abs(downP.x - curP.x) < 10
					&& Math.abs(downP.y - curP.y) < 10) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		// 每次进行onTouch事件都记录当前的按下的坐标
		getParent().requestDisallowInterceptTouchEvent(true);

		return super.onTouchEvent(arg0);
	}
}
