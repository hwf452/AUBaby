package com.halong.aubaby.enter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.MainActivity;
import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.util.MD5CodeUtil;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends BackActivity {

	private Context mContext;

	private Button mLoginButton;
	private TextView mForgetTextView;
	private EditText mAccountEditText, mPasswordEditText;
	private CheckBox mRememverCheckBox;
	private LinearLayout mProgress;

	private String mAccount, mPassword;

	private MyAppStackManager mMyAppStackManager;

	private final static String LOG_TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_login);
		mContext = LoginActivity.this;
		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);
		initView();
		bindListener();
	}

	private void bindListener() {
		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mAccount = mAccountEditText.getText().toString().trim();
				mPassword = mPasswordEditText.getText().toString().trim();

				if (mAccount == null || mAccount.length() <= 0) {
					Toast.makeText(mContext, getString(R.string.account_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mPassword == null || mPassword.length() <= 0) {
					Toast.makeText(mContext,
							getString(R.string.password_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else
					postLogin();

			}
		});

		mForgetTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toOtherActivity(ForgetActivity1.class);
			}
		});
	}

	/**
	 * 发起登录请求
	 */
	private void postLogin() {
		// 弹出进度条
		mProgress = (LinearLayout) findViewById(R.id.progress);
		mProgress.setVisibility(View.VISIBLE);

		MD5CodeUtil md5CodeUtil = new MD5CodeUtil();
		mPassword = md5CodeUtil.getMD5ofStr(mPassword).toUpperCase();

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a", mAccount);
		params.put("b", mPassword);
		params.put("c", getPhoneIMEI());
		params.put("e", "1");

		asyncHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		asyncHttpClient.post(ContantUtil.AU_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						SharedPreferencesHelper.clear(mContext);
						 int maxLogSize = 2000;
						 for (int i = 0; i <= content.length() / maxLogSize; i++) {
						 int start = i * maxLogSize;
						 int end = (i + 1) * maxLogSize;
						 end = end > content.length() ? content.length() : end;
						 Log.v(LOG_TAG, content.substring(start, end));}
						try {
							
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");
							if (result) {
								Log.v(LOG_TAG, "result is true");

								int userid = jsonObject.getInt("userid");
								// 用户班级id
								String classid = jsonObject
										.getString("classid");
								// 用户班级名
								String classname = jsonObject
										.getString("classname");
								SharedPreferencesHelper.setIntValue(mContext,
										SharedPreferencesHelper.USERID, userid);
								SharedPreferencesHelper.setStringValue(
										mContext, Keys.CLASS_ID, classid);
								
								SharedPreferencesHelper
								.setStringValue(mContext,
										ApiKeys.CLASS_NAME, classname);
								if (mRememverCheckBox.isChecked()) {
									SharedPreferencesHelper.setStringValue(
											mContext,
											SharedPreferencesHelper.ACCOUNT,
											mAccount);
									SharedPreferencesHelper.setStringValue(
											mContext,
											SharedPreferencesHelper.PASSWORD,
											mPassword);
									SharedPreferencesHelper.setStringValue(
											mContext,
											SharedPreferencesHelper.IMEI,
											getPhoneIMEI());
									Log.v(LOG_TAG,
											"password saved successfully");
								}
								mMyAppStackManager.finishAllActivity();
								toOtherActivity(MainActivity.class);
							} else {
								Log.v(LOG_TAG, content);
								new ReturnValue() {

									@Override
									public void onReloginSuccess() {
										// TODO Auto-generated method stub

									}
								}.resultFalse(mContext, content);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(mContext, getString(R.string.error),
									Toast.LENGTH_SHORT).show();
							Log.v(LOG_TAG, "Failed to login");
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Log.v("LoginActivity", "Failed to parse");
						Toast.makeText(mContext,
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();

						return;
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						mProgress.setVisibility(View.GONE);
						;
					}
				});

	}

	/**
	 * 获取手机串号
	 * 
	 * @return imei手机串号
	 */
	private String getPhoneIMEI() {
		String imei = ((TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = "pad";
		}
		return imei;
	}

	private void initView() {
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mForgetTextView = (TextView) findViewById(R.id.forgetTextView);
		mAccountEditText = (EditText) findViewById(R.id.accountEditText);
		mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
		mRememverCheckBox = (CheckBox) findViewById(R.id.rememverCheckBox);
	}
}
