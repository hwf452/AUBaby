package com.halong.aubaby.web;

import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class WebHtmlActivity extends Activity {
	private WebView mWebView;
	private View progress;
	private String url;
	private TextView titleTextView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_html);
		titleTextView = (TextView) findViewById(R.id.tv_titlebar4);
		if (getIntent().getExtras() == null) {
			Toast.makeText(this, "无该页面的信息", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if (!getIntent().getExtras().containsKey(Keys.ACTIVITY_KEY)) {
			Toast.makeText(this, "无该页面的信息", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		String activityValue = getIntent().getStringExtra(Keys.ACTIVITY_KEY);
		if (activityValue.equals(Keys.REGISTER_ACTIVITY)) {
			url = ContantUtil.AUBABY_URL + "/other/proxy.htm";
			titleTextView.setText(R.string.read_agreement);
		}else if (activityValue.equals(Keys.SETTING_ACTIVITY_HELP)) {
			url = ContantUtil.AUBABY_URL +"/other/mobileHelp.html";
			titleTextView.setText(R.string.tab2_tv_list5);
		} else {
			Toast.makeText(this, R.string.access_error, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}
		progress = (View) findViewById(R.id.progress);
		mWebView = (WebView) findViewById(R.id.webView);
		// 设置WebView属性，能够执行Javascript脚本
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
	}

	public void backButton(View view) {
		// TODO Auto-generated method stub
		// setResult(1, getIntent());
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mWebView.canGoBack()) {
				mWebView.goBack(); // goBack()表示返回WebView的上一页面
			} else {
				finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_right_out);
			}
			return true;
		}
		return false;
	}
}
