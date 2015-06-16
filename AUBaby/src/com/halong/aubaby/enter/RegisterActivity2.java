package com.halong.aubaby.enter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.MainActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.util.MD5CodeUtil;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class RegisterActivity2 extends BackActivity {

	private Context mContext;

	private Button mRegisterButton;
	private EditText mCodeEditText;
	private ProgressDialog mProgressDialog;

	private String mCodeString, mPhoneString;

	private MyAppStackManager mMyAppStackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_enter_register2);
		mContext = this;

		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);

		mPhoneString = getIntent().getStringExtra("which");

		initView();

		bindListener();
	}

	private void bindListener() {
		mRegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mCodeString = mCodeEditText.getText().toString().trim();

				if (mCodeString != null && mCodeString.length() > 0) {
					postData();
				} else {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.code_error),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 发起注册填写验证码请求
	 */
	private void postData() {
		// 弹出进度条
		mProgressDialog = ProgressDialog.show(mContext, "",
				getString(R.string.post_message), true, true);
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		MD5CodeUtil md5CodeUtil = new MD5CodeUtil();
		mCodeString = md5CodeUtil.getMD5ofStr(mCodeString).toUpperCase();

		RequestParams params = new RequestParams();
		params.put("methodName", "7");
		params.put("a", mPhoneString);
		params.put("b", mCodeString);
		params.put("c", getPhoneIMEI());

		asyncHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		asyncHttpClient.post(ContantUtil.AU_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);

						try {
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");
							if (result) {

								int userid = jsonObject.getInt("userid");

								SharedPreferencesHelper.clear(mContext);
								SharedPreferencesHelper.setIntValue(mContext,
										SharedPreferencesHelper.USERID, userid);

								mMyAppStackManager.finishAllActivity();
								toOtherActivity(MainActivity.class);

								// String route = jsonObject.getString("route");
								// if (route.equals("1"))
								// toOtherActivity(MainActivity.class);
								// if (route.equals("2"))
								// toOtherActivity(MainActivity.class);
							} else {
								Toast.makeText(mContext, "请输入正确的验证码",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(mContext, getString(R.string.error),
									Toast.LENGTH_SHORT).show();
						} finally {
							mProgressDialog.cancel();
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Toast.makeText(mContext,
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();
						mProgressDialog.cancel();
					}

				});
	}

	/**
	 * 获取手机串号
	 * 
	 * @return imei手机串号
	 */
	private String getPhoneIMEI() {
		return ((TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	private void initView() {
		mRegisterButton = (Button) findViewById(R.id.registerButton);
		mCodeEditText = (EditText) findViewById(R.id.codeEditText);
	}
}
