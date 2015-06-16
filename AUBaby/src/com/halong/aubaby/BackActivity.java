package com.halong.aubaby;


import com.halong.aubaby.R;

import android.view.KeyEvent;
import android.view.View;

public class BackActivity extends ToOtherActivity {

	public void backButton(View view) {
		// TODO Auto-generated method stub
		// setResult(1, getIntent());
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
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
