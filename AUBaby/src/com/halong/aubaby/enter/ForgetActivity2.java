package com.halong.aubaby.enter;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.util.MD5CodeUtil;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ForgetActivity2 extends BackActivity {

	private Context mContext;

	private Button mResetButton;
	private EditText mPasswordEditText, mPasswordEditText2, mCodeEditText;
	private ProgressDialog mProgressDialog;

	private String mPhoneString, mCodeString, mPasswordString,
			mPasswordString2;

	private MyAppStackManager mMyAppStackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_enter_forget2);
		mContext = this;

		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);

		mPhoneString = getIntent().getStringExtra("which");

		initView();

		bindListener();

	}

	private void bindListener() {
		mResetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mCodeString = mCodeEditText.getText().toString().trim();
				mPasswordString = mPasswordEditText.getText().toString().trim();
				mPasswordString2 = mPasswordEditText2.getText().toString()
						.trim();

				if (mCodeString == null || mCodeString.length() <= 0) {
					Toast.makeText(mContext, getString(R.string.code_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mPasswordString == null || mPasswordString2 == null
						|| mPasswordString.length() <= 0
						|| mPasswordString2.length() <= 0) {
					Toast.makeText(mContext,
							getString(R.string.password_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!mPasswordString.equals(mPasswordString2)) {
					Toast.makeText(mContext, getString(R.string.forget_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					postData();
				}
			}
		});
	}

	/**
	 * 发起忘记密码验证码请求
	 */
	@SuppressLint("DefaultLocale") private void postData() {
		// 弹出进度条
		mProgressDialog = ProgressDialog.show(mContext, "",
				getString(R.string.post_message), true, true);

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		MD5CodeUtil md5CodeUtil = new MD5CodeUtil();
		mPasswordString = md5CodeUtil.getMD5ofStr(mPasswordString).toUpperCase();
		
		RequestParams params = new RequestParams();
		params.put("methodName", "9");
		params.put("a", mPhoneString);
		params.put("b", mCodeString);
		params.put("c", mPasswordString);

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
								mMyAppStackManager.retainFristActivity();
								toOtherActivity(LoginActivity.class);
							} else {
								Toast.makeText(mContext, "验证码错误",
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

	private void initView() {
		mResetButton = (Button) findViewById(R.id.resetButton);
		mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
		mPasswordEditText2 = (EditText) findViewById(R.id.passwordEditText2);
		mCodeEditText = (EditText) findViewById(R.id.codeEditText);
	}

}
