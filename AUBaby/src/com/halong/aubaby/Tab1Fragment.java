package com.halong.aubaby;

/**
 * 活动页页〉tab1
 * 
 */

import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.tab1.SearchActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

public class Tab1Fragment extends FragmentToOtherActivity implements
		OnClickListener {

	private RadioGroup mRadioGroup;// 说说、照片墙切换开关
	private View view;// tab1页面
	private Button searchButton;// 搜索按钮
	private ViewFlipper mViewFlipper;
	private TextView groupNameTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab1, null);
		searchButton = (Button) view.findViewById(R.id.searchBtn);
		searchButton.setOnClickListener(this);
		initView();
		groupNameTextView = (TextView) view.findViewById(R.id.tv_titlebar4);
		groupNameTextView.setText(SharedPreferencesHelper.getStringValue(
				getActivity(), ApiKeys.CLASS_NAME));

		return view;
	}

	/**
	 * 声明控件
	 */
	private void initView() {
		mViewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.diaryTypeSwitch);
		mRadioGroup.check(R.id.diaryRtb);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (mRadioGroup.getCheckedRadioButtonId()) {
				case R.id.diaryRtb:
					mViewFlipper.setDisplayedChild(0);
					break;
				case R.id.photoWallRbt:
					if (mViewFlipper.getChildCount() == 1) {
						mViewFlipper
								.addView(LayoutInflater
										.from(getActivity())
										.inflate(
												R.layout.include_fragment_photowall_viewstub,
												null));
					}
					mViewFlipper.showPrevious();
					break;
				default:
					break;
				}
			}
		});
		view.findViewById(R.id.topTitleLayout).setOnClickListener(this);
		registerBoradcastReceiver();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtn:
			toOtherActivity(SearchActivity.class);
			break;
		case R.id.topTitleLayout:
			Intent intent = new Intent();
			intent.setAction(Keys.TAB1_FRAGMENT_TOP_TITLE_ACTION);
			getActivity().sendBroadcast(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.SETTING_ACTIVITY_CHANGE_GROUP)) {
				groupNameTextView.setText(SharedPreferencesHelper
						.getStringValue(getActivity(), ApiKeys.CLASS_NAME));

			}
		}
	};

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
}
