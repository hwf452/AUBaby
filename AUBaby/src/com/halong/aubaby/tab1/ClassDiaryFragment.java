package com.halong.aubaby.tab1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.Content;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.User;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.Content.Comments;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.Content.Pics;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.Content.Pics.PicList;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClassDiaryFragment extends FragmentToOtherActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	/**
	 * 成长空间日记V视图页面
	 */
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;// 说说列表
	private DiaryAdapter mAdapter;// 日记适配器
	private DiaryService diaryService;// 获取日记列表
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions options;// 图片加载设置
	private LinearLayout progress;// 进度条
	private Context mContext;
	private Handler handler;
	private Runnable diaryTypeSwitchRunable;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");

	/*
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.include_fragment_diarys, null);
		mContext = getActivity();

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		// 日记切换类型开关，初始化时3s后消失

		handler = new Handler();

		diaryTypeSwitchRunable = new Runnable() {
			public void run() {
				getActivity().findViewById(R.id.diaryTypeSwitch).setVisibility(
						View.GONE);
				handler.removeCallbacks(diaryTypeSwitchRunable);
			}
		};
		diaryTypeSwitch();

		// 初始化控件
		initView(view);
		diaryService.getDiaryV(null, null, Keys.START_APP, Keys.DIARY_P_Type);

		return view;
	}

	private void initView(final View view) {
		// TODO Auto-generated method stub
		// 初始化图片加载线程
		registerBoradcastReceiver();

		progress = (LinearLayout) view.findViewById(R.id.progress);
		mListView = (ListView) view.findViewById(R.id.diaryListView);
		mAdapter = new DiaryAdapter(mContext, imageLoader, options, true);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				getActivity().findViewById(R.id.diaryTypeSwitch).setVisibility(
						View.VISIBLE);
				if (arg1 == SCROLL_STATE_IDLE) {
					diaryTypeSwitch();
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		diaryService = new DiaryService(mContext) {
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
					mAdapter.addNetDiaryListAtTopPosition(list);
					mAdapter.notifyDataSetChanged();
					break;
				default:
					// 如果返回的日记数等于超过请求日记数（6条），说明数据过旧，需要清空重新加载
					mAdapter.clearDiaryList();

					for (int i = 0; i < vEntity.getObjInfo().length; i++) {
						list.add(i, vEntity.getObjInfo()[i]);
					}
					// 添加从服务器获取的日记
					mAdapter.addNetDiaryListAtTopPosition(list);
					mAdapter.notifyDataSetChanged();
					break;
				}
				progress.setVisibility(View.GONE);
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));

				super.onRefreshSuccsess();
			}

			@Override
			public void onRefreshFailure() {
				// TODO Auto-generated method stub

				progress.setVisibility(View.GONE);
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
					Toast.makeText(mContext, R.string.no_more_diarys,
							Toast.LENGTH_SHORT).show();
					break;

				default:
					// 从服务器获取的日记要设置为NO_UPLOAD
					for (int i = 0; i < vEntity.getObjInfo().length; i++) {
						list.add(vEntity.getObjInfo()[i]);
					}
					mAdapter.addNetDiaryListAtLastPosition(list);
					mAdapter.notifyDataSetChanged();
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

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (mAdapter.getALLMDiaryList().get(position).getContent().getId()
				.equals(Keys.LOCAL_DATA)) {

		} else {
			// 日记详情页面
			Intent intent = new Intent(mContext, ShuoShuoDetailActivity.class);
			Bundle bundle = new Bundle();
			// 将数据传递过去
			bundle.putString(Keys.DIARY_ID,
					mAdapter.getALLMDiaryList().get(position).getContent()
							.getId());
			bundle.putString(Keys.USER_INFO_ID, mAdapter.getALLMDiaryList()
					.get(position).getUser().getCode());
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(
					R.anim.push_right_in, R.anim.push_left_out);
		}

	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.PUBLISH_ACTIVITY_ACTION)) {

				VDiaryEntity.ObjInfo objInfo = new VDiaryEntity.ObjInfo();// adapter显示用户发布日记
				final ArrayList<String> urlList = intent.getExtras()
						.getStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY);// 要上传的图片
				if (urlList == null) {
					return;
				}
				// 图片个数
				PicList[] picLists = new PicList[urlList.size()];

				for (int i = 0; i < urlList.size(); i++) {
					// 图片路径
					PicList picList = new PicList();
					picList.setUrl("file:/" + urlList.get(i));
					picLists[i] = picList;
				}
				Pics pics = new Pics();
				pics.setPicList(picLists);
				// 日记内容
				Comments comments = null;
				Content content = new Content();
				content.setCountOfPics(urlList.size());// 图片个数
				content.setPics(pics);
				content.setZan("0");
				content.setPinglun("0");
				content.setDate(simpleDateFormat.format(new Date(System
						.currentTimeMillis())));
				content.setComments(comments);
				String diaryContent = intent.getExtras().getString(
						Keys.PUBLISH_DIARY_CONTENT);
				content.setTitle(diaryContent);
				content.setId(Keys.LOCAL_DATA);// 标注为本地数据
				objInfo.setContent(content);
				User user = new User();
				user.setImg(SharedPreferencesHelper.getStringValue(mContext,
						Keys.USER_HEAD_PHOTO));
				user.setCode(SharedPreferencesHelper.getIntValue(mContext,
						SharedPreferencesHelper.USERID) + "");
				user.setName(context.getString(R.string.publishing_diary));
				user.setIsAdmin(SharedPreferencesHelper.getStringValue(
						mContext, Keys.IS_CLASS_ADMIN));
				objInfo.setUser(user);

				// 上传
				mAdapter.publishDiary(objInfo);
				((RadioGroup) getActivity().findViewById(R.id.radioGroup1))
						.check(R.id.radio0);
				((RadioGroup) getActivity().findViewById(R.id.diaryTypeSwitch))
						.check(R.id.diaryRtb);
				mListView.setSelectionAfterHeaderView();
			} else if (action.equals(Keys.TAB1_FRAGMENT_TOP_TITLE_ACTION)) {
				mListView.setSelectionAfterHeaderView();

			} else if (action.equals(Keys.SETTING_ACTIVITY_CHANGE_GROUP)) {
				progress.setVisibility(View.VISIBLE);
				mAdapter.clearDiaryList();
				mAdapter.notifyDataSetChanged();
				diaryService.getDiaryV(null, null, Keys.START_APP,
						Keys.DIARY_P_Type);
			}
		}

	};

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.PUBLISH_ACTIVITY_ACTION);
		myIntentFilter.addAction(Keys.TAB1_FRAGMENT_TOP_TITLE_ACTION);
		myIntentFilter.addAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
		// 注册广播
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (mAdapter.getLastNetDiary() != null) {
			diaryService.getDiaryV(null, mAdapter.getLastNetDiary()
					.getContent().getId(), Keys.ON_LOAD, Keys.DIARY_P_Type);

		} else {
			// 如果日记列表没有日记，当成刚进入应用
			diaryService.getDiaryV(null, null, Keys.ON_LOAD, Keys.DIARY_P_Type);
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (mAdapter.getFirstNetDiary() != null) {

			diaryService.getDiaryV(mAdapter.getFirstNetDiary().getContent()
					.getId(), null, Keys.ON_REFRESH, Keys.DIARY_P_Type);

		} else {
			diaryService.getDiaryV(null, null, Keys.ON_REFRESH,
					Keys.DIARY_P_Type);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	private void diaryTypeSwitch() {
		// TODO Auto-generated method stub
		handler.postDelayed(diaryTypeSwitchRunable, 1500);
	}


}
