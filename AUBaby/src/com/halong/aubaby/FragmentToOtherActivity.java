package com.halong.aubaby;

import com.halong.aubaby.R;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class FragmentToOtherActivity extends Fragment {

	public void toOtherActivity(Class<?> activity) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), activity);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

	/**
	 * 
	 */
	public void toOtherActivity(Class<?> activity, String value) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), activity);
		intent.putExtra("which", value);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

}
