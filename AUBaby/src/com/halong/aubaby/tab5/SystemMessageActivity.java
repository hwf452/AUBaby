package com.halong.aubaby.tab5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.halong.aubaby.BaseFragmentActivity;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.NoticeService;

public class SystemMessageActivity extends BaseFragmentActivity {
	private Button mPublishButton;
	private TextView mSNumTextView, mHNumTextView, signTextView,titleTextView;
	private RadioGroup mRadioGroup;

	private int mSNum = 0;
	private int mHNum = 0;
	private int signNum = 0;
	private NoticeService noticeService;

	private ViewFlipper mViewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_message);

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mSNumTextView = (TextView) findViewById(R.id.numSTextView);
		mHNumTextView = (TextView) findViewById(R.id.numHTextView);
		signTextView = (TextView) findViewById(R.id.signTextView);
		mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		mPublishButton = (Button) findViewById(R.id.publishButton);
		titleTextView=(TextView)findViewById(R.id.title);
		titleTextView.setText(SharedPreferencesHelper.getStringValue(this, ApiKeys.CLASS_NAME));
		bindListener();
		postUnreadNum();
	}

	/**
	 * 绑定监听
	 */
	private void bindListener() {
		// TODO Auto-generated method stub

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (mRadioGroup.getCheckedRadioButtonId()) {
				case R.id.leftRadioButton:
					mViewFlipper.setDisplayedChild(0);
//					mRadioGroup.check(R.id.leftRadioButton);
					break;
				case R.id.middleRadioButton:
					mViewFlipper.setDisplayedChild(1);
//					mRadioGroup.check(R.id.middleRadioButton);
					break;
				case R.id.rightRadioButton:
					mViewFlipper.setDisplayedChild(2);
//					mRadioGroup.check(R.id.rightRadioButton);
					break;
				default:
					break;
				}
			}
		});
//		mRadioGroup.check(R.id.leftRadioButton);
//		mRadioGroup.check(R.id.middleRadioButton);
//		mRadioGroup.check(R.id.rightRadioButton);
		int page = getIntent().getIntExtra(Keys.FRAGMENT_PAGE, 0);
		switch (page) {
		case 0:
			mRadioGroup.check(R.id.leftRadioButton);
			break;
		case 1:
			mRadioGroup.check(R.id.middleRadioButton);
			break;
		case 2:
			mRadioGroup.check(R.id.rightRadioButton);
			break;

		default:
			break;
		}
		if (SharedPreferencesHelper.getStringValue(getApplicationContext(),
				Keys.IS_CLASS_ADMIN).equals("1")) {
			mPublishButton.setVisibility(View.VISIBLE);
			// 只有用户是教师时才有管理员权限
			mPublishButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					toOtherActivity(PublishNoticeActivity.class);
				}
			});
		}

	}

	public void postUnreadNum() {

		noticeService = new NoticeService(getApplicationContext()) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				parseData(content);
			}
		};

		noticeService.getUnreadNum();
	}

	private void parseData(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			boolean result = jsonObject.getBoolean("result");
			if (result) {
				mSNum = 0;
				mHNum = 0;
				JSONArray itemArray = jsonObject.getJSONArray("item");
				JSONObject itemObject = null;
				for (int i = 0; i < itemArray.length(); i++) {
					itemObject = itemArray.getJSONObject(i);
					if (itemObject.getString("mType").equals("S")) {
						mSNum += itemObject.getInt("unread");
					}
					if (itemObject.getString("mType").equals("H")) {
						mHNum += itemObject.getInt("unread");
					}

					if (itemObject.getString("mType").equals("G")) {
						signNum += itemObject.getInt("unread");
					}
				}
				setSNumTextView();
				setHNumTextView();
				setSignNumTextView();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error), Toast.LENGTH_SHORT).show();
				hideNumTextView();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), getString(R.string.error),
					Toast.LENGTH_SHORT).show();
			hideNumTextView();
		}
	}

	private void hideNumTextView() {
		mHNumTextView.setVisibility(View.GONE);
		mSNumTextView.setVisibility(View.GONE);
		signTextView.setVisibility(View.GONE);
	}

	private void setSNumTextView() {
		if (mSNum > 0) {
			mSNumTextView.setVisibility(View.VISIBLE);
			mSNumTextView.setText(mSNum + "");
		} else {
			mSNumTextView.setVisibility(View.GONE);
		}
	}

	private void setHNumTextView() {
		if (mHNum > 0) {
			mHNumTextView.setVisibility(View.VISIBLE);
			mHNumTextView.setText(mHNum + "");
		} else {
			mHNumTextView.setVisibility(View.GONE);
		}
	}

	private void setSignNumTextView() {
		if (signNum > 0) {
			signTextView.setVisibility(View.VISIBLE);
			signTextView.setText(signNum + "");
		} else {
			signTextView.setVisibility(View.GONE);
		}
	}
}
