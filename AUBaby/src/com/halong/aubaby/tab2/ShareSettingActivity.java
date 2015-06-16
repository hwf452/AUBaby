package com.halong.aubaby.tab2;

/**
 * 分享设置页面
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.R;

public class ShareSettingActivity extends BackActivity implements OnClickListener {
	/**
	 * 声明控件
	 */
	private Context mContext;
	private LinearLayout mLinear11,mLinear12,mLinear13,mLinear14;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_setting);
		mContext=this;
		mLinear11 = (LinearLayout) findViewById(R.id.linear11);
		mLinear12 = (LinearLayout) findViewById(R.id.linear12);
		mLinear13 = (LinearLayout) findViewById(R.id.linear13);
		mLinear14 = (LinearLayout) findViewById(R.id.linear14);
		initView();
	}
	
	/**
	 * 绑定事件监听
	 */
	private void initView() {
		mLinear11.setOnClickListener(this);
		mLinear12.setOnClickListener(this);
		mLinear13.setOnClickListener(this);
		mLinear14.setOnClickListener(this);
	}
	/**
	 * 处理事件
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear11:
			Intent intent11=new Intent(mContext,ShareSettingBindPageActivity.class);
			startActivity(intent11);

			break;
		case R.id.linear12:
			
			break;
		case R.id.linear13:
			
			break;
		case R.id.linear14:
			
			break;
		

		default:
			break;
		}

		
	}


}
