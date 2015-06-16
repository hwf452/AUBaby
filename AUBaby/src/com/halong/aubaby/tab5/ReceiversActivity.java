package com.halong.aubaby.tab5;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.ClassDetailEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.UserService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReceiversActivity extends BackActivity {

	private GridView mGridView;// 用户列表
	private UserService userService;
	private ClassDetailEntity cEntity;// 班级详情数据源
	private List<ClassDetailEntity.ObjectInfo.User> mList;
	private List<Boolean> blist;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private LinearLayout layout;// 进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receivers);

		String classID = SharedPreferencesHelper.getStringValue(
				getApplicationContext(), Keys.CLASS_ID);
		String userInfoID = SharedPreferencesHelper.getIntValue(
				getApplicationContext(), SharedPreferencesHelper.USERID) + "";

		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).showImageOnFail(R.drawable.head).build();
		layout = (LinearLayout) findViewById(R.id.progress);

		mList = new ArrayList<ClassDetailEntity.ObjectInfo.User>();
		blist = new ArrayList<Boolean>();
		initView();

		userService = new UserService(getApplicationContext()) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

				cEntity = userService.getClassDetailEntity();

				for (int i = 0; i < cEntity.getObjinfo().getUser().length; i++) {
					mList.add(cEntity.getObjinfo().getUser()[i]);
					blist.add(false);
				}
				((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
				layout.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				layout.setVisibility(View.GONE);
			}
		};
		userService.getClassDetail(classID, userInfoID);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mGridView = (GridView) findViewById(R.id.gridView);

		mGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.item_receiver,
						null);
				TextView nameTextView = (TextView) view
						.findViewById(R.id.nameTxt);
				
				ImageView headImageView = (ImageView) view
						.findViewById(R.id.headImg);
				ImageView teacImageView = (ImageView) view
						.findViewById(R.id.tecImg);
				CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.checkBox1);
				if (position == mList.size()) {
					headImageView.setVisibility(View.GONE);
					checkBox.setVisibility(View.GONE);
					return view;
					
				}
				nameTextView.setText(mList.get(position).getName());
				mImageLoader.displayImage(
						ContantUtil.PICTURE_URL + mList.get(position).getImg(),
						headImageView, mOptions);
				
				if (mList.get(position).getIsAdmin().equals("1")) {
					teacImageView.setVisibility(View.VISIBLE);
				} else {
					teacImageView.setVisibility(View.GONE);
				}
				
				checkBox.setChecked(blist.get(position));
				if (blist.get(position)) {
					checkBox.setVisibility(View.VISIBLE);
				} else {
					checkBox.setVisibility(View.GONE);
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (mList.size() % 2 == 1) {
					return mList.size() + 1;
				} else {
					return mList.size();
				}

			}
		});
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2==mList.size()) {
					return;
				}
				blist.set(arg2, !blist.get(arg2));
				((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
			}
		});
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String receiverID = "";
				String receiverName = "";
				for (int i = 0; i < blist.size(); i++) {
					if (blist.get(i)) {
						receiverID = receiverID + mList.get(i).getCode() + ",";
						receiverName = receiverName + mList.get(i).getName()
								+ "；";
					}
				}
				if (!receiverID.equals("")) {
					Intent intent = new Intent();
					intent.setClass(ReceiversActivity.this,
							PublishNoticeActivity.class);
					intent.putExtra(Keys.RECEIVERS_ID, receiverID);
					intent.putExtra(Keys.RECEIVERS_NAME, receiverName);
					setResult(Keys.RECEIVERS_ACTIVITY, intent);
				}

				finish();
			}
		});
	}
}
