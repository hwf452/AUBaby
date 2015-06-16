package com.halong.aubaby.tab5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.halong.aubaby.BaseActivity;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.UserSignListEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.SignService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SignListActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	private SignService userSignService;
	private View progress;
	private Gson gson;

	private UserSignListEntity userSignListEntity;

	private ListView mListView;
	private View listHeadView;

	private UserSignAdapter mUserSignAdapter;

	private TextView headTextView1, headTextView2;
	private ImageView headImageView;

	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions headOptions;// 图片加载设置

	private PullToRefreshView mPullToRefreshView;

	private Boolean downRefresh, isUser;

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_list);

		DemoApplication app = (DemoApplication) getApplicationContext();
		imageLoader = app.getImageLoader();
		headOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.head).cacheOnDisc(true).build();

		progress = (View) findViewById(R.id.progress);
		mListView = (ListView) findViewById(R.id.onlyListView);
		listHeadView = LayoutInflater.from(this).inflate(
				R.layout.include_sign_list_head, null);
		headTextView1 = (TextView) listHeadView.findViewById(R.id.textView1);
		headTextView2 = (TextView) listHeadView.findViewById(R.id.textView2);
		headImageView = (ImageView) listHeadView.findViewById(R.id.headImg);
		mListView.addHeaderView(listHeadView);

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		gson = new Gson();
		mUserSignAdapter = new UserSignAdapter(this);
		mListView.setAdapter(mUserSignAdapter);
		LoadUserView();
	}

	private void LoadUserView() {
		// TODO Auto-generated method stub
		userSignService = new SignService(this) {
			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				super.getBBSpaceData(content);
				if (downRefresh) {
					mUserSignAdapter.clearUserList();
					mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
							.format(new Date(System.currentTimeMillis())));
				}

				userSignListEntity = gson.fromJson(content,
						UserSignListEntity.class);
				headTextView1.setText(userSignListEntity.getClassName());
				headTextView2.setText(R.string.sign_newsrell);
				imageLoader.displayImage(ContantUtil.PICTURE_URL
						+ userSignListEntity.getHeadPhotoURL(), headImageView,
						headOptions);
				ArrayList<UserSignListEntity.list> userList = new ArrayList<UserSignListEntity.list>();
				for (int i = 0; i < userSignListEntity.getList().length; i++) {
					userList.add(userSignListEntity.getList()[i]);
				}
				mUserSignAdapter.addUserSignList(userList);
				mUserSignAdapter.notifyDataSetChanged();

				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				progress.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				super.getBBSpaceDataFailure();
				progress.setVisibility(View.GONE);
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
			}
		};

		downRefresh = true;
		if (getIntent().getStringExtra(Keys.USER_INFO_ID).equals(
				SharedPreferencesHelper.getIntValue(this,
						SharedPreferencesHelper.USERID) + "")) {
			isUser = true;
			userSignService.getUserSignList("");
		} else {
			isUser = false;
			userSignService.getOtherUserSignList(
					getIntent().getStringExtra(Keys.USER_INFO_ID), "");
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

		// 上拉，加载更多
		downRefresh = false;
		if (isUser) {
			userSignService.getUserSignList(mUserSignAdapter
					.getUserListLastObject().getValue()[0].getId_sw());
		} else {
			userSignService
					.getOtherUserSignList(
							getIntent().getStringExtra(Keys.USER_INFO_ID),
							mUserSignAdapter.getUserListLastObject().getValue()[mUserSignAdapter
									.getUserListLastObject().getValue().length - 1]
									.getId_sw());
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

		// 刷新，清空数据，加载数据
		downRefresh = true;
		if (isUser) {
			userSignService.getUserSignList("");
		} else {
			userSignService.getOtherUserSignList(
					getIntent().getStringExtra(Keys.USER_INFO_ID), "");
		}
	}
}
