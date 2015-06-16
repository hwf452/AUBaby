package com.halong.aubaby.tab1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.halong.aubaby.BaseActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.OtherUserInfoEntity;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.entity.WDiaryEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab2.ClassDetailActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.wcf.UserService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OtherUserInfoActivity extends BaseActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private Button mToDetail;
	private RadioGroup mRadioGroup;// 说说、照片墙切换开关
	private Button searchButton;// 搜索按钮
	private UserService userService;// 获取用户资料服务
	private OtherUserInfoEntity otherUserInfoEntity;// 查看其他用户数据源
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private String userInfoID;// 用户id
	private ListView listView;
	private DiaryAdapter diaryAdapter;// 用户日记列表适配器
	private DiaryService diaryService;// 用户日记列表数据获取线程

	private UserInfoAdapter userInfoAdapter;// 用户照片墙列表适配器
	private DiaryService mDiaryService;// 用户照片墙获取数据方法
	private WDiaryEntity wEntity;

	private ImageView headImageView, teacherImageView;// 用户头像,教师标志
	private LinearLayout progress;// 进度条
	private int banji = 0;// 选取返回数据中班级列表的位置

	private PullToRefreshView mPullToRefreshView;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");
	private TextView banjiTextView, myClassNameTextView, qianmingTextView,
			fabiaoTextView, zanTextView, liulanTextView, pinglunTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user_info);
		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mImageLoader.init(ImageLoaderConfiguration
				.createDefault(getApplicationContext()));
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		searchButton = (Button) findViewById(R.id.searchBtn);
		searchButton.setOnClickListener(this);

		progress = (LinearLayout) findViewById(R.id.progress);
		// 判断上层是否传来用户id
		whichUser();
		// 初始化基本控件
		initView();
		// 获取用户信息
		getUserInfo();
		// 初始化切换控件
		initDiaryPhotoWallPhotos();
		// 获取用户日记列表
		getUserDiary();
		// 获取用户照片墙
		getUserPhotoWall();
	}

	/**
	 * 判断用户
	 */
	private void whichUser() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		// 判断是否接收到Keys.USER_INFO_ID
		if (intent.getExtras().containsKey(Keys.USER_INFO_ID)) {
			userInfoID = intent.getExtras().getString(Keys.USER_INFO_ID);
		} else {
			return;
		}

	}

	/**
	 * 初始化数据，绑定事件
	 */

	private void initView() {

		// 添加基本个人信息
		listView = (ListView) findViewById(R.id.diaryListView);
		listView.addHeaderView(LayoutInflater.from(this).inflate(
				R.layout.include_other_user_info_detail, null));
		diaryAdapter = new DiaryAdapter(this, mImageLoader, mOptions, false);
		userInfoAdapter = new UserInfoAdapter(this, mImageLoader, mOptions);
		listView.setAdapter(diaryAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mRadioGroup.getCheckedRadioButtonId() == R.id.diaryRtb) {
					// 日记详情页面
					Intent intent = new Intent(OtherUserInfoActivity.this,
							ShuoShuoDetailActivity.class);
					Bundle bundle = new Bundle();
					// 将数据传递过去
					bundle.putString(Keys.DIARY_ID, diaryAdapter
							.getALLMDiaryList().get(position - 1).getContent()
							.getId());
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.push_right_in,
							R.anim.push_left_out);
				} else if (mRadioGroup.getCheckedRadioButtonId() == R.id.photoWallRbt) {

				}

			}
		});
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		headImageView = (ImageView) findViewById(R.id.imageView1);
		teacherImageView = (ImageView) findViewById(R.id.tecImg);
		banjiTextView = ((TextView) findViewById(R.id.tv_titlebar4));
		myClassNameTextView = ((TextView) findViewById(R.id.textView2));
		qianmingTextView = ((TextView) findViewById(R.id.textView3));
		fabiaoTextView = ((TextView) findViewById(R.id.textView4));
		zanTextView = ((TextView) findViewById(R.id.textView6));
		liulanTextView = ((TextView) findViewById(R.id.textView8));
		pinglunTextView = ((TextView) findViewById(R.id.textView10));
		mToDetail = (Button) findViewById(R.id.classDetailBtn);
	}

	/**
	 * 获取用户基本资料
	 */
	private void getUserInfo() {
		userService = new UserService(this) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

				otherUserInfoEntity = userService.getOtherUserInfoEntity();

				if (otherUserInfoEntity == null) {
					progress.setVisibility(View.GONE);
					return;
				}

				disPlayUserInfo();
				findViewById(R.id.otherUserInfoDetailLayout).setVisibility(
						View.VISIBLE);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
			}
		};
		userService.getOtherInfo(userInfoID);
	}

	/**
	 * 显示个人资料
	 */
	private void disPlayUserInfo() {
		// TODO Auto-generated method stub

		mImageLoader.displayImage(ContantUtil.PICTURE_URL
				+ otherUserInfoEntity.getUser().getImg(), headImageView,
				mOptions);

		for (int i = 0; i < otherUserInfoEntity.getBanjilist().getBanJi().length; i++) {
			// 判断返回数据中的班级列表的哪个班级才是要查看的班级
			if (SharedPreferencesHelper.getStringValue(this, Keys.CLASS_ID)
					.equals(otherUserInfoEntity.getBanjilist().getBanJi()[i]
							.getCode())) {
				banji = i;
			}
		}
		if (otherUserInfoEntity.getBanjilist().getBanJi()[banji].getIsAdmin()
				.equals("1")) {
			teacherImageView.setVisibility(View.VISIBLE);
		} else {
			teacherImageView.setVisibility(View.GONE);
		}
		banjiTextView
				.setText(otherUserInfoEntity.getBanjilist().getBanJi()[banji]
						.getMyClassName()+getString(R.string.zone));

		myClassNameTextView.setText(otherUserInfoEntity.getBanjilist()
				.getBanJi()[banji].getMyClassName());

		qianmingTextView.setText(otherUserInfoEntity.getUser().getQianming());

		fabiaoTextView.setText(String.valueOf(otherUserInfoEntity.getUser()
				.getFabiao()));

		zanTextView.setText(String.valueOf(otherUserInfoEntity.getUser()
				.getZan()));

		liulanTextView.setText(String.valueOf(otherUserInfoEntity.getUser()
				.getPinglun()));

		pinglunTextView.setText(String.valueOf(otherUserInfoEntity.getUser()
				.getLiulan()));

		/*
		 * 点击进入到班级详情
		 */

		mToDetail.setText(otherUserInfoEntity.getBanjilist().getBanJi()[banji]
				.getName());
		mToDetail.setOnClickListener(this);

	}

	private void initDiaryPhotoWallPhotos() {
		// TODO Auto-generated method stub
		mRadioGroup = (RadioGroup) findViewById(R.id.diaryTypeSwitch);
		mRadioGroup.setVisibility(View.VISIBLE);

		/**
		 * 选择图片展现方试
		 */
		mRadioGroup.check(R.id.diaryRtb);
		mRadioGroup.getChildAt(0).setSelected(true);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.diaryRtb:
					if (userInfoAdapter.getLastPhoto() != null
							&& diaryAdapter.getLastNetDiary() == null) {
						diaryService.getDiaryV(userInfoID, null, null,
								Keys.START_APP, Keys.DIARY_P_Type);
						progress.setVisibility(View.VISIBLE);
					}
					listView.setAdapter(diaryAdapter);
					listView.setSelection(1);
					break;
				case R.id.photoWallRbt:
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
	}

	/**
	 * 获取登陆用户日记列表
	 */
	private void getUserDiary() {
		// TODO Auto-generated method stub

		diaryService = new DiaryService(getApplicationContext()) {
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
				case 6:
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
				super.onRefreshSuccsess();
			}

			@Override
			public void onRefreshFailure() {
				// TODO Auto-generated method stub

				mPullToRefreshView.onHeaderRefreshComplete();
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
					Toast.makeText(getApplicationContext(),
							R.string.no_more_diarys, Toast.LENGTH_SHORT).show();
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
		diaryService.getDiaryV(userInfoID, null, null, Keys.START_APP,
				Keys.DIARY_P_Type);
	}

	/**
	 * 获取登陆用户照片墙列表
	 */
	private void getUserPhotoWall() {
		// TODO Auto-generated method stub
		mDiaryService = new DiaryService(getApplicationContext()) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub

				wEntity = mDiaryService.getwEntity();
				ArrayList<WDiaryEntity.Pic> list = new ArrayList<WDiaryEntity.Pic>();
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
				super.onRefreshFailure();
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onLoadMoreSuccsess() {
				// TODO Auto-generated method stub
				wEntity = mDiaryService.getwEntity();
				ArrayList<WDiaryEntity.Pic> list = new ArrayList<WDiaryEntity.Pic>();
				for (int i = 0; i < wEntity.getPic().length; i++) {
					list.add(wEntity.getPic()[i]);
				}

				userInfoAdapter.addPhotoListAtLastPosition(list);
				userInfoAdapter.notifyDataSetChanged();
				mPullToRefreshView.onFooterRefreshComplete();
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.classDetailBtn:

			Intent intent = new Intent(OtherUserInfoActivity.this,
					ClassDetailActivity.class);
			intent.putExtra(Keys.CLASS_ID, otherUserInfoEntity.getBanjilist()
					.getBanJi()[banji].getCode());
			intent.putExtra(Keys.USER_INFO_ID, otherUserInfoEntity.getUser()
					.getCode());

			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.searchBtn:
			toOtherActivity(SearchActivity.class);
			break;
		default:
			break;
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		// 刷新整个页面的信息
		userService.getUserInfo();

		if (mRadioGroup.getCheckedRadioButtonId() == R.id.diaryRtb) {
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
		if (mRadioGroup.getCheckedRadioButtonId() == R.id.diaryRtb) {
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

}
