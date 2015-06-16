package com.halong.aubaby.tab2;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class UserFeedBackActivity extends BackActivity {
	
	private Button mFeedbackButton;
	private EditText mEditText;
	private LinearLayout mProgressLinearLayout;
	
	private String mText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feedback);
		
		initView();
		
		bindListener();
	}

	/**
	 * 绑定监听
	 */
	private void bindListener() {
		// TODO Auto-generated method stub
		mFeedbackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mText = mEditText.getText().toString().trim();
				if (mText != null&& mText.length()>0) {
					postFeedBack();
				}
			}
		});
	}

	/**
	 * 发送反馈的请求
	 */
	private void postFeedBack() {
		// TODO Auto-generated method stub
		
		mProgressLinearLayout.setVisibility(View.VISIBLE);
		
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setCookieStore(new PersistentCookieStore(this));
		RequestParams params = new RequestParams();
		params.put("methodName", "6");
		params.put("a", mText);
		
		asyncHttpClient.post(this, ContantUtil.PUBLISH_URL, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v("postFeedBack", content+"");
				try {
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						Toast.makeText(UserFeedBackActivity.this,"感谢你的反馈", Toast.LENGTH_SHORT	).show();
					}else {
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(UserFeedBackActivity.this, getString(R.string.error),
							Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(UserFeedBackActivity.this, getString(R.string.post_error),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				mProgressLinearLayout.setVisibility(View.GONE);
			}
			
			
		});
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		// TODO Auto-generated method stub
		mFeedbackButton = (Button) findViewById(R.id.feedback);
		mEditText=(EditText) findViewById(R.id.editText1);
		mProgressLinearLayout=(LinearLayout) findViewById(R.id.progress);
		
		mProgressLinearLayout.setVisibility(View.GONE);
	}

}
