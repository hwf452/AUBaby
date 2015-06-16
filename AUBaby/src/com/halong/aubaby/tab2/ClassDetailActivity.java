package com.halong.aubaby.tab2;

/**
 * 班级详情
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.ClassDetailEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.wcf.UserService;
import com.halong.aubaby.widget.NoScrollGridView;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClassDetailActivity extends BackActivity {
	/**
	 * 根据不同用户uid显示不同的用户信息
	 */
	private NoScrollGridView mGridView;// 用户列表
	private Button mToPhoneList;// 用户电话
	private UserService userService;
	private ClassDetailEntity cEntity;// 班级详情数据源
	private List<ClassDetailEntity.ObjectInfo.User> mList;
	private TextView schoolNmaeTextView, classNameTextView, joinDateTextView,
			isClassAdminTextView, titleTextView;// 校名，班名，加入时间，是否是管理员，标题栏
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private LinearLayout layout;// 进度条
	private ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_detail);

		Intent intent = getIntent();
		String classID = intent.getStringExtra(Keys.CLASS_ID);
		String userInfoID = intent.getStringExtra(Keys.USER_INFO_ID);

		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).build();
		layout = (LinearLayout) findViewById(R.id.progress);
		userService = new UserService(getApplicationContext()) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

				cEntity = userService.getClassDetailEntity();
				mList = new ArrayList<ClassDetailEntity.ObjectInfo.User>();
				for (int i = 0; i < cEntity.getObjinfo().getUser().length; i++) {
					mList.add(cEntity.getObjinfo().getUser()[i]);
				}
				initView();
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

	/**
	 * 绑定事件
	 */

	private void initView() {
		mScrollView = (ScrollView) findViewById(R.id.sccroll);
		mScrollView.setVisibility(View.VISIBLE);

		schoolNmaeTextView = (TextView) findViewById(R.id.schoolNmaeText);
		schoolNmaeTextView.setText(cEntity.getObjinfo().getSchoolName());
		classNameTextView = (TextView) findViewById(R.id.classNameTxt);
		classNameTextView.setText(cEntity.getObjinfo().getName());
		titleTextView = (TextView) findViewById(R.id.tv_titlebar4);
		titleTextView.setText(cEntity.getObjinfo().getName());
		joinDateTextView = (TextView) findViewById(R.id.joinDateTxt);
		joinDateTextView.setText(cEntity.getObjinfo().getTime());
		isClassAdminTextView = (TextView) findViewById(R.id.isClassAdminTxt);
		if (TextUtils.equals("1", cEntity.getObjinfo().getIsClassAdmin())) {
			isClassAdminTextView.setText(R.string.head_teacher);
		} else {
			isClassAdminTextView.setText(R.string.parents);
		}

		mGridView = (NoScrollGridView) findViewById(R.id.gridView1);
		mToPhoneList = (Button) findViewById(R.id.button1);

		mGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					view = getLayoutInflater().inflate(
							R.layout.item_tab2_picshow, null);
				}

				TextView nameTextView = (TextView) view
						.findViewById(R.id.nameTxt);
				nameTextView.setText(mList.get(position).getName());
				ImageView headImageView = (ImageView) view
						.findViewById(R.id.headImg);
				mImageLoader.displayImage(
						ContantUtil.PICTURE_URL + mList.get(position).getImg(),
						headImageView, mOptions);
				ImageView teacImageView = (ImageView) view
						.findViewById(R.id.tecImg);
				if (mList.get(position).getIsAdmin().equals("1")) {
					teacImageView.setVisibility(View.VISIBLE);
				} else {
					teacImageView.setVisibility(View.GONE);
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
				return mList.size();
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ClassDetailActivity.this,
						OtherUserInfoActivity.class);
				intent.putExtra(Keys.USER_INFO_ID, mList.get(position)
						.getCode());
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);

			}
		});
		mGridView.setFocusable(false);
		mToPhoneList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ClassDetailActivity.this,
						PhoneListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Keys.CLASS_DETAIL_ENTITY, cEntity);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);

			}
		});
		mToPhoneList.setVisibility(View.VISIBLE);
	}


}
