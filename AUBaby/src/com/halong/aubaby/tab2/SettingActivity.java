package com.halong.aubaby.tab2;

/**
 * 设置页面
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.enter.LoginActivity;
import com.halong.aubaby.entity.UserInfoEntity;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.util.UpdateManager;
import com.halong.aubaby.wcf.UserService;
import com.halong.aubaby.web.WebHtmlActivity;
import com.halong.aubaby.R;

public class SettingActivity extends BackActivity implements OnClickListener {
	/**
	 * 声明控件
	 */

	private Context mContext;
	private LinearLayout mLinear11, mLinear13, mLinear14, helpLayout,
			mLinear16, mLinear17, mLinear18, updateLayout;
	private UserService userService;
	private String[] banjis;
	private String[] banjiCodes;
	private UpdateManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		MyAppStackManager.getMyAppStackManager().addActivity(this);
		mContext = this;

		initView();

		bindListener();
	}

	/**
	 * 绑定事件监听
	 */
	private void bindListener() {
		mLinear11.setOnClickListener(this);
		mLinear13.setOnClickListener(this);
		mLinear14.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		mLinear16.setOnClickListener(this);
		mLinear17.setOnClickListener(this);
		mLinear18.setOnClickListener(this);
		updateLayout.setOnClickListener(this);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mLinear11 = (LinearLayout) findViewById(R.id.linear11);
		mLinear13 = (LinearLayout) findViewById(R.id.linear13);
		mLinear14 = (LinearLayout) findViewById(R.id.linear14);
		helpLayout = (LinearLayout) findViewById(R.id.helpLayout);
		mLinear16 = (LinearLayout) findViewById(R.id.linear16);
		mLinear17 = (LinearLayout) findViewById(R.id.linear17);
		mLinear18 = (LinearLayout) findViewById(R.id.linear18);
		updateLayout = (LinearLayout) findViewById(R.id.updateLayout);
		manager = new UpdateManager(this, "com.halong.aubaby",
				true);
	}

	/**
	 * 处理事件
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.linear11:
			toOtherActivity(PrivateImfoActivity.class);

			break;
		case R.id.linear12:
			// toOtherActivity(UserInfoActivity.class);

			break;
		case R.id.linear13:
			toOtherActivity(ShareSettingActivity.class);

			break;
		case R.id.linear14:
			toOtherActivity(UserFeedBackActivity.class);

			break;
		case R.id.helpLayout:
			Intent intent = new Intent(SettingActivity.this,
					WebHtmlActivity.class);
			intent.putExtra(Keys.ACTIVITY_KEY, Keys.SETTING_ACTIVITY_HELP);
			startActivity(intent);
			break;
		case R.id.linear16:

			break;
		case R.id.linear17:
			userService = new UserService(mContext) {

				@Override
				public void getBBSpaceDataFailure() {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void getBBSpaceData() {
					// TODO Auto-generated method stub
					final UserInfoEntity uEntity = userService
							.getUserInfoEntity();
					banjis = new String[uEntity.getBanjilist().getBanji().length];
					banjiCodes = new String[uEntity.getBanjilist().getBanji().length];
					for (int i = 0; i < uEntity.getBanjilist().getBanji().length; i++) {
						banjis[i] = uEntity.getBanjilist().getBanji()[i]
								.getName();
						banjiCodes[i] = uEntity.getBanjilist().getBanji()[i]
								.getCode();

					}

					new AlertDialog.Builder(mContext).setItems(banjis,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (TextUtils.equals(banjiCodes[which],
											SharedPreferencesHelper
													.getStringValue(mContext,
															Keys.CLASS_ID))) {

									} else {

										SharedPreferencesHelper.setStringValue(
												getApplicationContext(),
												Keys.IS_CLASS_ADMIN, uEntity
														.getBanjilist()
														.getBanji()[which]
														.getIsAdmin());
										SharedPreferencesHelper.setStringValue(
												getApplicationContext(),
												Keys.USER_HEAD_PHOTO, uEntity
														.getBanjilist()
														.getBanji()[which]
														.getUserHeadPhoto());

										SharedPreferencesHelper.setStringValue(
												mContext, Keys.CLASS_ID,
												banjiCodes[which]);
										SharedPreferencesHelper.setStringValue(
												mContext, ApiKeys.CLASS_NAME,
												banjis[which]);
										Intent intent = new Intent();
										intent.setAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
										sendBroadcast(intent);
										Toast.makeText(getApplicationContext(),
												R.string.change_group_success,
												Toast.LENGTH_SHORT).show();
									}

								}
							}).show();

				}
			};
			userService.getUserInfo();

			break;
		case R.id.linear18:
			SharedPreferencesHelper.clear(mContext);
			MyAppStackManager.getMyAppStackManager().finishAllActivity();
			toOtherActivity(LoginActivity.class);
			break;
		case R.id.updateLayout:
			updateApp();
			break;
		default:
			break;
		}

	}

	/**
	 * 更新app
	 */

	private void updateApp() {

		manager.checkUpdate();
	}
}
