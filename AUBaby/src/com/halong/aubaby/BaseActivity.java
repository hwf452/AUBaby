package com.halong.aubaby;

import com.halong.aubaby.R;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

public class BaseActivity extends Activity {
	public void backButton(View view) {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	public void toOtherActivity(Class<?> activity) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, activity);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	public void toOtherActivity(Class<?> activity, String value) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, activity);
		intent.putExtra("which", value);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);
	}
}
