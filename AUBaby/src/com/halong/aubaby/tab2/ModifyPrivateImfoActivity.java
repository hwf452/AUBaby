package com.halong.aubaby.tab2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class ModifyPrivateImfoActivity extends BackActivity {

	private TextView mTitle;
	private Button mSave;
	private EditText mTextContent;
	private int mType;
	private View progress;
	private int relogin = 0;// 重新调用接口次数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_privateimfo);
		initView();
		binfListener();

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.tv_titlebar4);
		mSave = (Button) findViewById(R.id.save);
		mTextContent = (EditText) findViewById(R.id.editText1);
		mTitle.setText(getIntent().getStringExtra("title"));
		mTextContent.setHint(getIntent().getStringExtra("title"));
		mType = getIntent().getIntExtra(Keys.POST_INFO, -1);
		progress = (View) findViewById(R.id.progress);
		switch (mType) {
		case Keys.POST_USER_NAME:
			mTitle.setText(R.string.user_name2);
			break;
		case Keys.POST_EMAIL:
			mTitle.setText(R.string.email2);
			try {
				mTextContent
						.setInputExtras(InputType.TYPE_TEXT_VARIATION_PHONETIC);
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case Keys.POST_QQ:
			mTitle.setText(R.string.QQ2);
			mTextContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			break;
		case R.id.nickNameTxt:
			mTitle.setText(R.string.nickname);
			break;
		default:
			break;
		}
	}

	private void binfListener() {

		mSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTextContent.getText().length() == 0
						|| mTextContent.getText().equals("")) {
					Toast.makeText(ModifyPrivateImfoActivity.this, R.string.comment_null,
							Toast.LENGTH_LONG).show();
					return;
				}

				if (mType == Keys.POST_EMAIL) {
					Pattern pattern = Pattern
							.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
					Matcher matcher = pattern.matcher(mTextContent.getText()
							.toString().trim());
					if (!matcher.matches()) {
						Toast.makeText(ModifyPrivateImfoActivity.this,
								R.string.input_email_style, Toast.LENGTH_SHORT).show();
						;
						return;
					}
				}

				postSave();

			}
		});

	}

	/**
	 * 提交更改
	 */

	private void postSave() {
		// // TODO Auto-generated method stub
	
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setCookieStore(new PersistentCookieStore(this));

		RequestParams params = new RequestParams();
	
		switch (mType) {
		case Keys.POST_USER_NAME:
			params.put("methodName", "5");
			params.put("a", mTextContent.getText().toString());
			break;
		case Keys.POST_EMAIL:
			params.put("methodName", "5");
			params.put("d", mTextContent.getText().toString());
			break;
		case Keys.POST_QQ:
			params.put("methodName", "5");
			params.put("e", mTextContent.getText().toString());
			break;
			
		case R.id.nickNameTxt:
			params.put("methodName", "7");
			params.put("a", getIntent().getStringExtra(Keys.BANJI_ID));
			params.put("d", mTextContent.getText().toString());
			break;
		default:
			return;
		}
		progress.setVisibility(View.VISIBLE);
		asyncHttpClient.post(this, ContantUtil.USER_URL, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						try {
							JSONObject jsonObject = new JSONObject(content);

							if (jsonObject.getBoolean("result")) {
								Toast.makeText(ModifyPrivateImfoActivity.this,
										"保存成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.putExtra(Keys.RETURN_POST_INFO,
										mTextContent.getText().toString());
								intent.putExtra(Keys.POST_INFO, mType);
								if (mType==R.id.nickNameTxt) {
									intent.putExtra(Keys.BANJI_ITEM, getIntent().getIntExtra(Keys.BANJI_ITEM, -1));
								}
								setResult(Keys.MODIFY_PRIVATE_INFO_ACTIVITY,
										intent);
								finish();
								overridePendingTransition(R.anim.push_left_in,
										R.anim.push_right_out);

							}else {
								// 返回错误原因,重新调用次数小于5次时重新调用
								if (relogin < 5) {
									// 返回错误原因
									new ReturnValue() {

										@Override
										public void onReloginSuccess() {
											// TODO Auto-generated method stub
											// session已过期或不存在,后台登陆后重新获取数据
											postSave();
										}

										public void whileFailure() {
											Toast.makeText(ModifyPrivateImfoActivity.this,
													getString(R.string.post_error),
													Toast.LENGTH_SHORT).show();
										};
									}.resultFalse(getApplicationContext(), content);
								} else {
									Toast.makeText(ModifyPrivateImfoActivity.this,
											getString(R.string.post_error),
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(ModifyPrivateImfoActivity.this,
									getString(R.string.error),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Toast.makeText(ModifyPrivateImfoActivity.this,
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						progress.setVisibility(View.GONE);
					}
				});
	}
}
