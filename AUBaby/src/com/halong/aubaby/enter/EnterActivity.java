package com.halong.aubaby.enter;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.halong.aubaby.BackActivity;
import com.halong.aubaby.MainActivity;
import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.push.Utils;
import com.halong.aubaby.util.MyAppStackManager;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class EnterActivity extends BackActivity implements OnClickListener {

	private Context mContext;

	/**
	 * 等待时间
	 */
	private int DELAYMILLI = 0;

	private Button mLoginButton, mRegisterButton;// 登录、注册按钮

	private MyAppStackManager mMyAppStackManager;
	private LinearLayout progerssLayout;// 进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_enter);

		mContext = this;

		mMyAppStackManager = MyAppStackManager.getMyAppStackManager();
		mMyAppStackManager.addActivity(this);

		initView();
		initWait();

	}

	/*
	 * 初始化控件
	 */
	private void initView() {
		mRegisterButton = (Button) findViewById(R.id.registerButton);
		mRegisterButton.setOnClickListener(this);
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mLoginButton.setOnClickListener(this);
		progerssLayout = (LinearLayout) findViewById(R.id.progress);
	}

	/**
	 * loading状态，后台判断是否登录等操作
	 */
	private void initWait() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				postLogin();
			}
		}, DELAYMILLI);
	}

	/**
	 * 判断是否登录，发起登录请求
	 */
	private void postLogin() {

		final int userid = SharedPreferencesHelper.getIntValue(mContext,
				SharedPreferencesHelper.USERID);
		String account = SharedPreferencesHelper.getStringValue(mContext,
				SharedPreferencesHelper.ACCOUNT);
		String password = SharedPreferencesHelper.getStringValue(mContext,
				SharedPreferencesHelper.PASSWORD);
		String imei = SharedPreferencesHelper.getStringValue(mContext,
				SharedPreferencesHelper.IMEI);
		String className = SharedPreferencesHelper.getStringValue(mContext,
				ApiKeys.CLASS_NAME);
		String classID = SharedPreferencesHelper.getStringValue(mContext,
				Keys.CLASS_ID);
		// 空时不进行请求
		if (TextUtils.isEmpty(account)

		|| TextUtils.isEmpty(password) || TextUtils.isEmpty(imei)
				|| TextUtils.isEmpty(className) || TextUtils.isEmpty(classID)) {
			// 隐藏进度条，方便用户输入账号或注册
			progerssLayout.setVisibility(View.GONE);

			return;
		}

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a", account);
		params.put("b", password);
		params.put("c", imei);
		params.put("e", "1");

		asyncHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		asyncHttpClient.post(ContantUtil.AU_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						Log.v("11111111", content);
						try {
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");

							result = userid == jsonObject.getInt("userid") ? true
									: false;
							if (result) {
								// 用户名正确，直接登陆
								mMyAppStackManager.finishAllActivity();
								toOtherActivity(MainActivity.class);
								startPull();
							} else {
								// 密码不正确时，提示用户重新登陆

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
							// 解析失败
							Toast.makeText(mContext, getString(R.string.error),
									Toast.LENGTH_SHORT).show();

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						// 无网络且之前登录过时，加载最后一次登录的内容
						Toast.makeText(mContext,
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();
						mMyAppStackManager.finishAllActivity();
						toOtherActivity(MainActivity.class);
						startPull();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						progerssLayout.setVisibility(View.GONE);
					}

				});
	}

	/*
	 * （非 Javadoc） 绑定点击事件
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.loginButton:
			toOtherActivity(LoginActivity.class);
			break;
		case R.id.registerButton:
			toOtherActivity(RegisterActivity1.class);
			break;
		default:
			break;
		}
	}

	private void startPull() {
		Utils.logStringCache = Utils.getLogText(getApplicationContext());

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();

		// TODO Auto-generated method stub
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		if (!Utils.hasBind(getApplicationContext())) {
			Log.d("YYY", "before start work at "
					+ Calendar.getInstance().getTimeInMillis());
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					Utils.getMetaValue(this, "api_key"));
			Log.d("YYY", "after start work at "
					+ Calendar.getInstance().getTimeInMillis());
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			PushManager.enableLbs(getApplicationContext());
			Log.d("YYY", "after enableLbs at "
					+ Calendar.getInstance().getTimeInMillis());
		}

		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
				getApplicationContext(), resource.getIdentifier(
						"notification_custom_builder", "layout", pkgName),
				resource.getIdentifier("notification_icon", "id", pkgName),
				resource.getIdentifier("notification_title", "id", pkgName),
				resource.getIdentifier("notification_text", "id", pkgName));
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		cBuilder.setLayoutDrawable(resource.getIdentifier(
				"simple_notification_icon", "drawable", pkgName));
		PushManager.setNotificationBuilder(this, 1, cBuilder);
	}

}
