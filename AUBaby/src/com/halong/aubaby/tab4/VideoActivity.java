package com.halong.aubaby.tab4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.halong.aubaby.BaseFragmentActivity;
import com.halong.aubaby.R;

public class VideoActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ViewPager mViewPager;// 左右滑动视图容器
	private FragmentPagerAdapter mAdapter;// 左右滑动fragment适配器
	private Button mSeekButton;// 搜索按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab4_video);
		initView();
		bindViewPager();
		bindListener();
	}

	private void bindListener() {
		// TODO Auto-generated method stub
		mSeekButton.setOnClickListener(this);
	}

	private void bindViewPager() {
		// TODO Auto-generated method stub
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					return new VideoInforFragment();
				case 1:
					return new VideoReplayFragment();

				default:
					return null;
				}

			}
		};
		mViewPager.setAdapter(mAdapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mSeekButton = (Button) findViewById(R.id.seekButton);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.seekButton:
			Log.v("VideoActivity", v.getId() + "");
			break;

		default:
			break;
		}
	}

}
