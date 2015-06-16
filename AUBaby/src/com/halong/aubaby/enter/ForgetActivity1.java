package com.halong.aubaby.enter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ForgetActivity1 extends BackActivity {

	private Context mContext;
	private MyAppStackManager mMyAppStackManager;

	private Button mNextButton;
	private EditText mPhonEditText;
	private ProgressDialog mProgressDialog;

	private String mPhoneString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_enter_forget1);
		mContext = this;

		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);

		initView();

		bindListener();

	}

	private void bindListener() {
		mNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPhoneString = mPhonEditText.getText().toString().trim();

				if (mPhoneString != null && mPhoneString.length() > 0)
					postData();

				else
					Toast.makeText(mContext, getString(R.string.phone_error),
							Toast.LENGTH_SHORT).show();

			}
		});
	}

	/**
	 * 发起忘记密码验证码请求
	 */
	private void postData() {
		// 弹出进度条
		mProgressDialog = ProgressDialog.show(mContext, "",
				getString(R.string.post_message), true, true);

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("methodName", "8");
		params.put("a", mPhoneString);

		asyncHttpClient.post(ContantUtil.AU_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						Log.v("forget", content);
						Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();

						try {
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");
							if (result) {

								toOtherActivity(ForgetActivity2.class,
										mPhoneString);
							}else {
								Toast.makeText(mContext, getString(R.string.phone_error),
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
		mNextButton = (Button) findViewById(R.id.nextButton);
		mPhonEditText = (EditText) findViewById(R.id.phoneEditText);
	}
}
