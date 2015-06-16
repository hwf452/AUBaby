package com.halong.aubaby;

import com.halong.aubaby.tab4.LivesiteFragment;
import com.halong.aubaby.tab4.VideoChoiceFragment;
import com.halong.aubaby.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Tab4Fragment extends FragmentToOtherActivity {

	private RadioGroup mRadioGroup;

	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private LivesiteFragment mLivesiteFragment;
	private VideoChoiceFragment mVideoChoiceFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tab4, null);
//		mRadioGroup = (RadioGroup) view.findViewById(R.id.tb4RadioGroup);
//
//		bindListener();
//
//		showFragment();

		return view;
	}

	private void bindListener() {
		// TODO Auto-generated method stub
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				showFragment();
			}
		});
	}

	private void showFragment() {
		// TODO Auto-generated method stub
		mFragmentManager = getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();

		switch (mRadioGroup.getCheckedRadioButtonId()) {
		case R.id.tb4LeftRadioButton:
			if (mLivesiteFragment == null) {
				mLivesiteFragment = new LivesiteFragment();
				mFragmentTransaction.add(R.id.tb4FrameLayout, mLivesiteFragment);
			} else {
				mFragmentTransaction.show(mLivesiteFragment);
			}
			hideFragment(mVideoChoiceFragment);
			break;
		case R.id.tb4RightRadioButton:
			if (mVideoChoiceFragment == null) {
				mVideoChoiceFragment = new VideoChoiceFragment();
				mFragmentTransaction
						.add(R.id.tb4FrameLayout, mVideoChoiceFragment);
			} else {
				mFragmentTransaction.show(mVideoChoiceFragment);
			}
			hideFragment(mLivesiteFragment);
			break;

		default:
			break;
		}
		mFragmentTransaction.commit();
	}
	
	private void hideFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		if (fragment !=null) {
			mFragmentTransaction.hide(fragment);
		}
		
	}

}
