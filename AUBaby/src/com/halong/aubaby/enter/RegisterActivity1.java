package com.halong.aubaby.enter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.web.WebHtmlActivity;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity1 extends BackActivity {

	private Context mContext;

	private Button mGetCodeButton;
	private EditText mPhonEditText;
	private CheckBox mAgreementCheckBox;
	private ProgressDialog mProgressDialog;
	private TextView readAgreementTextView;
	private String mPhoneString;

	private MyAppStackManager mMyAppStackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_register1);
		mContext = this;

		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);

		initView();

		bindListener();

	}

	private void bindListener() {
		/**
		 * 得到验证码点击事件
		 */
		mGetCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPhoneString = mPhonEditText.getText().toString().trim();

				if (!mAgreementCheckBox.isChecked()) {
					Toast.makeText(mContext,
							getString(R.string.agreement_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mPhoneString == null || mPhoneString.length() <= 0) {
					Toast.makeText(mContext, getString(R.string.phone_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else
					postData();

			}
		});

		readAgreementTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity1.this,
						WebHtmlActivity.class);
				intent.putExtra(Keys.ACTIVITY_KEY, Keys.REGISTER_ACTIVITY);
				startActivity(intent);
			}
		});

	}

	/**
	 * 发起注册获取验证码请求
	 */
	private void postData() {
		// 弹出进度条
		mProgressDialog = ProgressDialog.show(mContext, "",
				getString(R.string.post_message), true, true);
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("methodName", "6");
		params.put("a", mPhoneString);

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

								toOtherActivity(RegisterActivity2.class,
										mPhoneString);
							} else {
								Toast.makeText(mContext,
										getString(R.string.phone_error),
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
		mGetCodeButton = (Button) findViewById(R.id.getCodeButton);
		mPhonEditText = (EditText) findViewById(R.id.phoneEditText);
		mAgreementCheckBox = (CheckBox) findViewById(R.id.agreementCheckBox);
		readAgreementTextView = (TextView) findViewById(R.id.readAgreementTxt);
	}

}
