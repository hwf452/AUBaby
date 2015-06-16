package com.halong.aubaby.tab5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.UserSignListEntity;
import com.halong.aubaby.entity.TeacherSignListEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.SignService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SignListFragment extends Fragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	private SignService teacherSignService;
	private SignService userSignService;
	private View progress;
	private Gson gson;
	private TeacherSignListEntity teacherSignListEntity;
	private UserSignListEntity userSignListEntity;

	private ListView mListView;
	private View listHeadView;
	private TeacherSignAdapter mTeacherSignAdapter;
	private UserSignAdapter mUserSignAdapter;

	private TextView headTextView1, headTextView2;
	private ImageView headImageView;

	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions headOptions;// 图片加载设置

	private PullToRefreshView mPullToRefreshView;

	private Boolean downRefresh;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab5_ontice_list, null);

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		headOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.head).cacheOnDisc(true).build();

		progress = (View) view.findViewById(R.id.progress);
		mListView = (ListView) view.findViewById(R.id.onlyListView);
		listHeadView = LayoutInflater.from(getActivity()).inflate(
				R.layout.include_sign_list_head, null);
		headTextView1 = (TextView) listHeadView.findViewById(R.id.textView1);
		headTextView2 = (TextView) listHeadView.findViewById(R.id.textView2);
		headImageView = (ImageView) listHeadView.findViewById(R.id.headImg);
		mListView.addHeaderView(listHeadView);

		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		gson = new Gson();
		// 如果用户是admin，加载教师页面 LoadTeacherView()，否则加载本人签到页面LoadStudentView()
		if (SharedPreferencesHelper.getStringValue(getActivity(),
				Keys.IS_CLASS_ADMIN).equals("1")) {
			mTeacherSignAdapter = new TeacherSignAdapter(getActivity());
			mListView.setAdapter(mTeacherSignAdapter);
			LoadTeacherView();
		} else {
			mUserSignAdapter = new UserSignAdapter(getActivity());
			mListView.setAdapter(mUserSignAdapter);
			LoadUserView();
		}

		return view;
	}

	private void LoadTeacherView() {
		// TODO Auto-generated method stub
		teacherSignService = new SignService(getActivity()) {
			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				super.getBBSpaceData(content);

				mTeacherSignAdapter.clearTeacherSignList();
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));
				teacherSignListEntity = gson.fromJson(content,
						TeacherSignListEntity.class);
				headTextView1.setText(R.string.sign_manager);
				headTextView2
						.setText(teacherSignListEntity.getClassName() + "班共有"
								+ teacherSignListEntity.getCountOfUsers() + "人");
				imageLoader.displayImage(ContantUtil.PICTURE_URL
						+ teacherSignListEntity.getTeacherHeadPhotoURL(),
						headImageView, headOptions);
				headImageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent().setClass(getActivity(),
								SignListActivity.class);
						intent.putExtra(
								Keys.USER_INFO_ID,
								SharedPreferencesHelper.getIntValue(
										getActivity(),
										SharedPreferencesHelper.USERID)
										+ "");
						startActivity(intent);
						getActivity().overridePendingTransition(
								R.anim.push_right_in, R.anim.push_left_out);
					}
				});
				ArrayList<TeacherSignListEntity.users> teacherList = new ArrayList<TeacherSignListEntity.users>();
				for (int i = 0; i < teacherSignListEntity.getUsers().length; i++) {
					teacherList.add(teacherSignListEntity.getUsers()[i]);
				}
				mTeacherSignAdapter.addTeacherSignList(teacherList);
				mTeacherSignAdapter.notifyDataSetChanged();
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				progress.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				super.getBBSpaceDataFailure();
				progress.setVisibility(View.GONE);
			}
		};
		teacherSignService.getTeacherSignList();
	}

	private void LoadUserView() {
		// TODO Auto-generated method stub
		userSignService = new SignService(getActivity()) {
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
		userSignService.getUserSignList("");
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

		if (SharedPreferencesHelper.getStringValue(getActivity(),
				Keys.IS_CLASS_ADMIN).equals("1")) {
			teacherSignService.getTeacherSignList();
		} else {
			// 上拉，加载更多
			downRefresh = false;
			userSignService.getUserSignList(mUserSignAdapter
					.getUserListLastObject().getValue()[mUserSignAdapter
					.getUserListLastObject().getValue().length - 1].getId_sw());
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

		if (SharedPreferencesHelper.getStringValue(getActivity(),
				Keys.IS_CLASS_ADMIN).equals("1")) {
			teacherSignService.getTeacherSignList();
		} else {
			// 刷新，清空数据，加载数据
			downRefresh = true;
			userSignService.getUserSignList("");
		}
	}

}
