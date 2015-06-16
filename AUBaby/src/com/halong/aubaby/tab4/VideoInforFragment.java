package com.halong.aubaby.tab4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.R;

public class VideoInforFragment extends FragmentToOtherActivity implements
		OnClickListener {

	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mView = inflater.inflate(R.layout.fragment_tab4_video_info, null);

		return mView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.headImg:
			break;
		default:
			break;
		}

	}

}
