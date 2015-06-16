package com.halong.aubaby;

/**
 * 活动页页tab2
 * 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.UserInfoEntity;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.entity.WDiaryEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.DiaryAdapter;
import com.halong.aubaby.tab1.ShuoShuoDetailActivity;
import com.halong.aubaby.tab1.UserInfoAdapter;
import com.halong.aubaby.tab2.ClassDetailActivity;
import com.halong.aubaby.tab2.MyAlbumsBookActivity;
import com.halong.aubaby.tab2.PrivateImfoActivity;
import com.halong.aubaby.tab2.SettingActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.wcf.UserService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Tab2Fragment extends FragmentToOtherActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	/**
	 * 声明控件
	 */
	private Context mContext;
	private RadioGroup mRadioGroup;
	private Button mSetting, mToDetail, mToPhotoBook;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions, headOptions;
	private UserService userService;// 获取用户资料服务
	private UserInfoEntity userInfoEntity;// 查看当前用户数据源

	private View progress;// 进度条
	private int banji = 0;// 选取返回数据中班级列表的位置

	private String userInfoID;// 要查看的用户id
	private ListView listView;
	private DiaryAdapter diaryAdapter;// 用户日记列表适配器
	private DiaryService diaryService;// 用户日记列表数据获取线程

	private UserInfoAdapter userInfoAdapter;// 用户照片墙列表适配器
	private DiaryService mDiaryService;// 用户照片墙获取数据方法
	private WDiaryEntity wEntity;
	private View view;// fragment要加载的试图

	private PullToRefreshView mPullToRefreshView;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");

	private ImageView headImageView, teacherImageView;
	private TextView banjiTextView, myClassNameTextView, qianmingTextView,
			fabiaoTextView, zanTextView, liulanTextView, pinglunTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_tab2, null);
		mContext = getActivity();

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		mImageLoader = app.getImageLoader();

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		headOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.head).cacheInMemory(true)
				.cacheOnDisc(true).build();
		progress = view.findViewById(R.id.progress);

		registerBoradcastReceiver();

		// 初始化基本控件
		initView(view);
		// 初始化切换控件
		initDiaryPhotoWallPhotos();
		// 设置按钮
		userSetting(view);

		// 获取用户基本资料
		getUserInfo();

		// 获取用户日记列表
		getUserDiary();

		// 获取用户照片墙
		getUserPhotoWall();
		return view;
	}

	/**
	 * 初始化数据，绑定事件
	 */

	private void initView(View view) {
		view.findViewById(R.id.relate1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						listView.setSelection(0);

					}
				});
		// 添加基本个人信息
		listView = (ListView) view.findViewById(R.id.diaryListView);
		listView.addHeaderView(LayoutInflater.from(mContext).inflate(
				R.layout.include_user_info_detail, null));
		diaryAdapter = new DiaryAdapter(mContext, mImageLoader, mOptions, false);
		userInfoAdapter = new UserInfoAdapter(mContext, mImageLoader, mOptions);
		listView.setAdapter(diaryAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mRadioGroup.getCheckedRadioButtonId() == R.id.radio0) {
					// 日记详情页面
					Intent intent = new Intent(getActivity(),
							ShuoShuoDetailActivity.class);
					Bundle bundle = new Bundle();
					// 将数据传递过去
					bundle.putString(Keys.DIARY_ID, diaryAdapter
							.getALLMDiaryList().get(position - 1).getContent()
							.getId());
					bundle.putString(
							Keys.USER_INFO_ID,
							SharedPreferencesHelper.getIntValue(mContext,
									SharedPreferencesHelper.USERID) + "");
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				} else if (mRadioGroup.getCheckedRadioButtonId() == R.id.radio1) {

				}

			}
		});
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		headImageView = (ImageView) view.findViewById(R.id.imageView1);
		banjiTextView = ((TextView) view.findViewById(R.id.tv_titlebar4));
		myClassNameTextView = ((TextView) view.findViewById(R.id.textView2));
		qianmingTextView = ((TextView) view.findViewById(R.id.textView3));
		fabiaoTextView = ((TextView) view.findViewById(R.id.textView4));
		zanTextView = ((TextView) view.findViewById(R.id.textView6));
		liulanTextView = ((TextView) view.findViewById(R.id.textView8));
		pinglunTextView = ((TextView) view.findViewById(R.id.textView10));
		mToDetail = (Button) view.findViewById(R.id.classDetailBtn);
		teacherImageView = (ImageView) view.findViewById(R.id.tecImg);
	}

	/**
	 * 设置按钮事件
	 * 
	 * @param view
	 */
	private void userSetting(View view) {
		mSetting = (Button) view.findViewById(R.id.button1);
		mSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toOtherActivity(SettingActivity.class);
			}
		});

	};

	/**
	 * 获取用户基本资料
	 */
	private void getUserInfo() {
		userService = new UserService(getActivity()) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
				// 保存用户头像和是否是班级管理员。先清空以往记录
				SharedPreferencesHelper.setStringValue(getActivity(),
						Keys.IS_CLASS_ADMIN, "0");
				SharedPreferencesHelper.setStringValue(getActivity(),
						Keys.USER_HEAD_PHOTO, " ");

				userInfoEntity = userService.getUserInfoEntity();

				userInfoEntity = userService.getUserInfoEntity();
				if (userInfoEntity == null) {
					progress.setVisibility(View.GONE);
					return;
				}
				for (int i = 0; i < userInfoEntity.getBanjilist().getBanji().length; i++) {
					// 保存和当前班级对应的用户信息
					if (TextUtils.equals(SharedPreferencesHelper
							.getStringValue(getActivity(), Keys.CLASS_ID),
							userInfoEntity.getBanjilist().getBanji()[i]
									.getCode())) {
						SharedPreferencesHelper.setStringValue(getActivity(),
								Keys.IS_CLASS_ADMIN, userInfoEntity
										.getBanjilist().getBanji()[i]
										.getIsAdmin());
						SharedPreferencesHelper.setStringValue(getActivity(),
								Keys.USER_HEAD_PHOTO, userInfoEntity
										.getBanjilist().getBanji()[i]
										.getUserHeadPhoto());
						if (userInfoEntity.getBanjilist().getBanji()[i]
								.getIsAdmin().equals("1")) {
							teacherImageView.setVisibility(View.VISIBLE);
						} else {
							teacherImageView.setVisibility(View.GONE);
						}
					}
				}
				disPlayUserInfo();
				view.findViewById(R.id.userInfoDetailLayout).setVisibility(
						View.VISIBLE);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				// 如果获取失败，清空数据，防止发生错误
				SharedPreferencesHelper.setStringValue(getActivity(),
						Keys.IS_CLASS_ADMIN, "0");
				SharedPreferencesHelper.setStringValue(getActivity(),
						Keys.USER_HEAD_PHOTO, "");
				view.findViewById(R.id.progress).setVisibility(View.GONE);
			}
		};
		userService.getUserInfo();
	}

	/**
	 * 显示个人资料
	 */
	private void disPlayUserInfo() {
		// TODO Auto-generated method stub

		mImageLoader.displayImage(
				ContantUtil.PICTURE_URL
						+ SharedPreferencesHelper.getStringValue(mContext,
								Keys.USER_HEAD_PHOTO), headImageView,
				headOptions);
		headImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toOtherActivity(PrivateImfoActivity.class);

			}
		});

		for (int i = 0; i < userInfoEntity.getBanjilist().getBanji().length; i++) {
			// 判断返回数据中的班级列表的哪个班级才是要查看的班级
			if (SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID)
					.equals(userInfoEntity.getBanjilist().getBanji()[i]
							.getCode())) {
				banji = i;
			}
		}
		banjiTextView.setText(userInfoEntity.getBanjilist().getBanji()[banji]
				.getMyClassName() + getString(R.string.zone));

		myClassNameTextView
				.setText(userInfoEntity.getBanjilist().getBanji()[banji]
						.getMyClassName());

		qianmingTextView.setText(userInfoEntity.getUser().getQianming());

		fabiaoTextView.setText(String.valueOf(userInfoEntity.getUser()
				.getFabiao()));

		zanTextView.setText(String.valueOf(userInfoEntity.getUser().getZan()));

		liulanTextView.setText(String.valueOf(userInfoEntity.getUser()
				.getPinglun()));

		pinglunTextView.setText(String.valueOf(userInfoEntity.getUser()
				.getLiulan()));

		/*
		 * 点击进入到班级详情
		 */

		mToDetail.setText(userInfoEntity.getBanjilist().getBanji()[banji]
				.getName());
		mToDetail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mContext, ClassDetailActivity.class);
				intent.putExtra(Keys.CLASS_ID, userInfoEntity.getBanjilist()
						.getBanji()[banji].getCode());
				intent.putExtra(Keys.USER_INFO_ID, userInfoEntity.getUser()
						.getCode());

				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});

	}

	private void initDiaryPhotoWallPhotos() {
		// TODO Auto-generated method stub
		mToPhotoBook = (Button) view.findViewById(R.id.button3);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);

		/**
		 * 选择图片展现方试
		 */
		mRadioGroup.check(R.id.radio0);
		mRadioGroup.getChildAt(0).setSelected(true);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio0:
					if (userInfoAdapter.getLastPhoto() != null
							&& diaryAdapter.getLastNetDiary() == null) {
						diaryService.getDiaryV(userInfoID, null, null,
								Keys.START_APP, Keys.DIARY_P_Type);
						progress.setVisibility(View.VISIBLE);
					}
					listView.setAdapter(diaryAdapter);
					listView.setSelection(1);
					break;
				case R.id.radio1:
					// 当用户发表过日记时自动刷新
					if (userInfoAdapter.getLastPhoto() == null
							&& diaryAdapter.getLastNetDiary() != null) {
						mDiaryService.getDiaryW(userInfoID, null, null, null,
								Keys.START_APP, Keys.DIARY_P_Type);
						progress.setVisibility(View.VISIBLE);
					}
					listView.setAdapter(userInfoAdapter);
					listView.setSelection(1);
					break;
				default:
					break;
				}

			}
		});
		/*
		 * 点击进入到我的像册
		 */
		mToPhotoBook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toOtherActivity(MyAlbumsBookActivity.class);
			}
		});
	}

	/**
	 * 获取登陆用户日记列表
	 */
	private void getUserDiary() {
		// TODO Auto-generated method stub

		diaryService = new DiaryService(getActivity()) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub
				ArrayList<VDiaryEntity.ObjInfo> list = new ArrayList<VDiaryEntity.ObjInfo>();
				VDiaryEntity vEntity = diaryService.getvEntity();

				switch (vEntity.getObjInfo().length) {
				case 0:
					break;
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					for (int i = 0; i < vEntity.getObjInfo().length; i++) {
						list.add(i, vEntity.getObjInfo()[i]);
					}
					// 添加从服务器获取的日记
					diaryAdapter.addNetDiaryListAtTopPosition(list);
					diaryAdapter.notifyDataSetChanged();
					break;
				default:
					// 如果返回的日记熟超过请求日记数（6条），说明数据过旧，需要清空重新加载
					diaryAdapter.clearDiaryList();

					for (int i = 0; i < vEntity.getObjInfo().length; i++) {
						list.add(i, vEntity.getObjInfo()[i]);
					}
					// 添加从服务器获取的日记
					diaryAdapter.addNetDiaryListAtTopPosition(list);
					diaryAdapter.notifyDataSetChanged();
					break;
				}
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));
				progress.setVisibility(View.GONE);
				super.onRefreshSuccsess();
			}

			@Override
			public void onRefreshFailure() {
				// TODO Auto-generated method stub

				mPullToRefreshView.onHeaderRefreshComplete();
				progress.setVisibility(View.GONE);
				super.onRefreshFailure();
			}

			@Override
			public void onLoadMoreFailure() {
				// TODO Auto-generated method stub

				mPullToRefreshView.onFooterRefreshComplete();
				super.onLoadMoreFailure();
			}

			@Override
			public void onLoadMoreSuccsess() {
				// TODO Auto-generated method stub
				ArrayList<VDiaryEntity.ObjInfo> list = new ArrayList<VDiaryEntity.ObjInfo>();
				VDiaryEntity vEntity = diaryService.getvEntity();
				switch (vEntity.getObjInfo().length) {
				case 0:
					Toast.makeText(mContext, R.string.no_more_diarys,
							Toast.LENGTH_SHORT).show();
					break;
				default:
					// 从服务器获取的日记要设置为NO_UPLOAD
					for (int i = 0; i < vEntity.getObjInfo().length; i++) {
						list.add(vEntity.getObjInfo()[i]);
					}
					diaryAdapter.addNetDiaryListAtLastPosition(list);
					diaryAdapter.notifyDataSetChanged();
					break;
				}
				mPullToRefreshView.onFooterRefreshComplete();
				super.onLoadMoreSuccsess();
			}

			// 加载本地或离线内容
			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}
		};
		userService.getUserInfo();
		userInfoID = String.valueOf(SharedPreferencesHelper.getIntValue(
				getActivity(), SharedPreferencesHelper.USERID));
		diaryService.getDiaryV(userInfoID, null, null, Keys.START_APP,
				Keys.DIARY_P_Type);
	}

	/**
	 * 获取登陆用户照片墙列表
	 */
	private void getUserPhotoWall() {
		// TODO Auto-generated method stub
		mDiaryService = new DiaryService(getActivity()) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub

				wEntity = mDiaryService.getwEntity();
				ArrayList<WDiaryEntity.Pic> list = new ArrayList<WDiaryEntity.Pic>();
				if (wEntity.getPic().length >= 24) {

					userInfoAdapter.clearPhotoList();

				}
				for (int i = 0; i < wEntity.getPic().length; i++) {
					list.add(i, wEntity.getPic()[i]);
				}
				userInfoAdapter.addPhotoListAtTopPosition(list);
				userInfoAdapter.notifyDataSetChanged();
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));
				progress.setVisibility(View.GONE);
				super.onRefreshSuccsess();
			}

			@Override
			public void onRefreshFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onHeaderRefreshComplete();
				progress.setVisibility(View.GONE);
				super.onRefreshFailure();
			}

			@Override
			public void onLoadMoreSuccsess() {
				// TODO Auto-generated method stub
				wEntity = mDiaryService.getwEntity();
				ArrayList<WDiaryEntity.Pic> list = new ArrayList<WDiaryEntity.Pic>();
				for (int i = 0; i < wEntity.getPic().length; i++) {
					list.add(wEntity.getPic()[i]);
				}
				mPullToRefreshView.onFooterRefreshComplete();
				userInfoAdapter.addPhotoListAtLastPosition(list);
				userInfoAdapter.notifyDataSetChanged();

				super.onLoadMoreSuccsess();
			}

			@Override
			public void onLoadMoreFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onFooterRefreshComplete();
				super.onLoadMoreFailure();
			}

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}
		};
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		// 刷新整个页面的信息
		userService.getUserInfo();

		if (mRadioGroup.getCheckedRadioButtonId() == R.id.radio0) {
			if (diaryAdapter.getFirstNetDiary() != null) {
				diaryService.getDiaryV(userInfoID, null, diaryAdapter
						.getLastNetDiary().getContent().getId(), Keys.ON_LOAD,
						Keys.DIARY_P_Type);
			} else {
				diaryService.getDiaryV(userInfoID, null, null, Keys.ON_LOAD,
						Keys.DIARY_P_Type);
			}

		} else {
			if (userInfoAdapter.getLastPhoto() != null) {
				mDiaryService.getDiaryW(userInfoID, null, userInfoAdapter
						.getLastPhoto().getDiaryid(), userInfoAdapter
						.getLastPhoto().getPicid(), Keys.ON_LOAD,
						Keys.DIARY_P_Type);
			} else {
				mDiaryService.getDiaryW(userInfoID, null, null, null,
						Keys.ON_LOAD, Keys.DIARY_P_Type);
			}
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		// 如果listview加载的是日记列表刷新日记列表，否则刷新照片墙
		userService.getUserInfo();
		if (mRadioGroup.getCheckedRadioButtonId() == R.id.radio0) {
			if (diaryAdapter.getLastNetDiary() != null) {
				diaryService.getDiaryV(userInfoID, diaryAdapter
						.getFirstNetDiary().getContent().getId(), null,
						Keys.ON_REFRESH, Keys.DIARY_P_Type);

			} else {
				// Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
				// 如果日记列表没有日记，当成刚进入应用
				diaryService.getDiaryV(userInfoID, null, null, Keys.START_APP,
						Keys.DIARY_P_Type);

			}
		} else {

			if (userInfoAdapter.getFirstPhoto() != null) {
				mDiaryService.getDiaryW(userInfoID, userInfoAdapter
						.getFirstPhoto().getDiaryid(), null, null,
						Keys.ON_REFRESH, Keys.DIARY_P_Type);
			} else {
				mDiaryService.getDiaryW(userInfoID, null, null, null,
						Keys.ON_REFRESH, Keys.DIARY_P_Type);
			}
		}
	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.SETTING_ACTIVITY_CHANGE_GROUP)) {
				progress.setVisibility(View.VISIBLE);
				userService.getUserInfo();
				diaryAdapter.clearDiaryList();
				userInfoAdapter.clearPhotoList();
				switch (mRadioGroup.getCheckedRadioButtonId()) {
				case R.id.radio0:
					diaryService.getDiaryV(userInfoID, null, null,
							Keys.START_APP, Keys.DIARY_P_Type);
					break;
				case R.id.radio1:
					mDiaryService.getDiaryW(userInfoID, null, null, null,
							Keys.START_APP, Keys.DIARY_P_Type);
				default:
					break;
				}

			}
		}
	};

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
}
