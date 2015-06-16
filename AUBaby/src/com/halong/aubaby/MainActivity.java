package com.halong.aubaby;

import java.util.Timer;

import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.UserInfoEntity;
import com.halong.aubaby.tab3.PublishActivity;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.UserService;
import com.halong.aubaby.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {
	public TabHost tabHost;
	public static MainActivity mainActivity;
	public RadioButton rb1, rb2, rb4, rb5;
	public Button mPublishButton;// 发表说说
	private Timer timer;

	public RadioGroup radioGroup;

	private UserService userService;// 获取用户资料服务
	private UserInfoEntity userInfoEntity;// 查看当前用户数据源

	public MainActivity() {
		super();
		mainActivity = this;
	}

	public static MainActivity getInstance() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		MyAppStackManager.getMyAppStackManager().addActivity(this);
		mPublishButton = (Button) findViewById(R.id.publishBtn);
		getUserInfo();
		initTabView();
	}

	private void initTabView() {

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		rb1 = (RadioButton) findViewById(R.id.radio0);
		rb2 = (RadioButton) findViewById(R.id.radio1);

		rb4 = (RadioButton) findViewById(R.id.radio3);
		rb5 = (RadioButton) findViewById(R.id.radio4);
		mPublishButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PublishActivity.class);
				startActivity(intent);
			}
		});
		tabHost.setup();
		tabHost.addTab(tabHost
				.newTabSpec(getResources().getString(R.string.tab1))
				.setContent(R.id.fragment1)
				.setIndicator(getResources().getString(R.string.tab1)));

		tabHost.addTab(tabHost
				.newTabSpec(getResources().getString(R.string.tab2))
				.setContent(R.id.fragment2)
				.setIndicator(getResources().getString(R.string.tab2)));

		tabHost.addTab(tabHost
				.newTabSpec(getResources().getString(R.string.tab4))
				.setContent(R.id.fragment4)
				.setIndicator(getResources().getString(R.string.tab4)));
		tabHost.addTab(tabHost
				.newTabSpec(getResources().getString(R.string.tab5))
				.setContent(R.id.fragment5)
				.setIndicator(getResources().getString(R.string.tab5)));
		tabHost.setCurrentTab(0);
		radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		switch (checkedId) {
		case R.id.radio0:
			tabHost.setCurrentTab(0);
			break;
		case R.id.radio1:
			tabHost.setCurrentTab(1);
			break;

		case R.id.radio3:
			tabHost.setCurrentTab(2);
			break;
		case R.id.radio4:
			tabHost.setCurrentTab(3);
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:

			new AlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("您确定要退出宝贝空间?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (timer != null) {
										timer.cancel();

									}

									finish();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();

			break;

		}
		return true;

	}

	/**
	 * 获取用户基本资料
	 */
	private void getUserInfo() {
		userService = new UserService(this) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
				// 保存用户头像和是否是班级管理员。先清空以往记录
				SharedPreferencesHelper.setStringValue(getApplicationContext(),
						Keys.IS_CLASS_ADMIN, "0");
				SharedPreferencesHelper.setStringValue(getApplicationContext(),
						Keys.USER_HEAD_PHOTO, "");

				userInfoEntity = userService.getUserInfoEntity();
				if (userInfoEntity == null) {
					return;
				}
				for (int i = 0; i < userInfoEntity.getBanjilist().getBanji().length; i++) {

					// 保存和当前班级对应的用户信息
					if (TextUtils.equals(SharedPreferencesHelper
							.getStringValue(getApplicationContext(),
									Keys.CLASS_ID), userInfoEntity
							.getBanjilist().getBanji()[i].getCode())) {
						SharedPreferencesHelper.setStringValue(
								getApplicationContext(), Keys.IS_CLASS_ADMIN,
								userInfoEntity.getBanjilist().getBanji()[i]
										.getIsAdmin());
						SharedPreferencesHelper.setStringValue(
								getApplicationContext(), Keys.USER_HEAD_PHOTO,
								userInfoEntity.getBanjilist().getBanji()[i]
										.getUserHeadPhoto());
					}
				}
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				// 如果获取失败，清空数据，防止发生错误
				SharedPreferencesHelper.setStringValue(getApplicationContext(),
						Keys.IS_CLASS_ADMIN, "0");
				SharedPreferencesHelper.setStringValue(getApplicationContext(),
						Keys.USER_HEAD_PHOTO, "");

			}
		};
		userService.getUserInfo();
	}
	
}
