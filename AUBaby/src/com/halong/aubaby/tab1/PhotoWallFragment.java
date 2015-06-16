package com.halong.aubaby.tab1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.WDiaryEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoWallFragment extends FragmentToOtherActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	/**
	 * 照片墙
	 */
	private GridView mGridView;// 照片墙容器
	private BaseAdapter mAdapter;
	private DiaryService mDiaryService;// 获取数据方法
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private LinearLayout progress;// 进度条
	private int itemHeight, itemWidth;// itmes高度,宽度
	private PullToRefreshView mPullToRefreshView;
	private ArrayList<WDiaryEntity.Pic> photosList;
	private WDiaryEntity wEntity;
	private Handler handler;
	private Runnable diaryTypeSwitchRunable;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.include_fragment_photowall,
				null);

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		mImageLoader = app.getImageLoader();

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		itemHeight = (getActivity().getWindowManager().getDefaultDisplay()
				.getWidth() - 8 * 4) / 3;
		itemWidth = itemHeight;

		photosList = new ArrayList<WDiaryEntity.Pic>();
		// 初始化
		initView(view, inflater);

		mDiaryService.getDiaryW(null, null, null, null, Keys.START_APP,
				Keys.DIARY_P_Type);

		registerBoradcastReceiver();

		// 日记切换类型开关，初始化时3s后消失

		handler = new Handler();

		diaryTypeSwitchRunable = new Runnable() {
			public void run() {
				getActivity().findViewById(R.id.diaryTypeSwitch).setVisibility(
						View.GONE);
				handler.removeCallbacks(diaryTypeSwitchRunable);
			}
		};
		return view;
	}

	/*
	 * 初始化
	 */
	private void initView(final View view, final LayoutInflater inflater) {
		// TODO Auto-generated method stub
		mGridView = (GridView) view.findViewById(R.id.photoWallGridView);
		progress = (LinearLayout) view.findViewById(R.id.progress);
		PhotoWall(view, inflater);
		mDiaryService = new DiaryService(getActivity()) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub

				progress.setVisibility(View.GONE);
				wEntity = mDiaryService.getwEntity();
				// 后台要求返回数据大于等于请求数24时要清空
				if (wEntity.getPic().length >= 24) {
					photosList.clear();
				}
				for (int i = 0; i < wEntity.getPic().length; i++) {
					photosList.add(i, wEntity.getPic()[i]);
				}
				mAdapter.notifyDataSetChanged();
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
			public void onLoadMoreSuccsess() {
				// TODO Auto-generated method stub
				wEntity = mDiaryService.getwEntity();
				for (int i = 0; i < wEntity.getPic().length; i++) {
					photosList.add(wEntity.getPic()[i]);
				}
				mAdapter.notifyDataSetChanged();
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
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	/*
	 * 加载照片墙
	 */
	private void PhotoWall(View view, final LayoutInflater inflater) {

		mAdapter = new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				// 加载照片墙图片
				View showView = inflater.inflate(
						R.layout.include_images_in_photo_wall, null);
				ImageView mImageView = (ImageView) showView
						.findViewById(R.id.photo);
				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mImageView
						.getLayoutParams();
				layoutParams.height = itemHeight;
				layoutParams.width = itemWidth;
				mImageView.setLayoutParams(layoutParams);
				try {
					mImageLoader.displayImage(ContantUtil.PICTURE_URL
							+ Keys.S_VIEW + photosList.get(arg0).getUrl(),
							mImageView, mOptions);
				} catch (Exception e) {
					// TODO: handle exception
				}

				return showView;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return photosList.size();
			}
		};
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), PictureShowActivity.class);
				intent.putExtra(Keys.CLICK_GRIDVIEW_ITEM,
						photosList.get(position).getUrl());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});

		mGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						TextView textView = (TextView) arg1
								.findViewById(R.id.postedTxt);
						textView.setText(photosList.get(arg2).getPublishTime());
						textView.setVisibility(View.VISIBLE);
						return true;
					}
				});
		mGridView.setOnScrollListener(new OnScrollListener() {

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
	}

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.TAB1_FRAGMENT_TOP_TITLE_ACTION);
		myIntentFilter.addAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.TAB1_FRAGMENT_TOP_TITLE_ACTION)) {
				mGridView.setSelection(1);
			} else if (action.equals(Keys.SETTING_ACTIVITY_CHANGE_GROUP)) {
				progress.setVisibility(View.VISIBLE);
				photosList.clear();
				mAdapter.notifyDataSetChanged();
				mDiaryService.getDiaryW(null, null, null, null, Keys.START_APP,
						Keys.DIARY_P_Type);
			}
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (photosList.size() > 0) {
			mDiaryService.getDiaryW(null, null,
					photosList.get(photosList.size() - 1).getDiaryid(),
					photosList.get(photosList.size() - 1).getPicid(),
					Keys.ON_LOAD, Keys.DIARY_P_Type);
		} else {
			mDiaryService.getDiaryW(null, null, null, null, Keys.ON_LOAD,
					Keys.DIARY_P_Type);
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (photosList.size() > 0) {
			mDiaryService.getDiaryW(null, photosList.get(0).getDiaryid(), null,
					null, Keys.ON_REFRESH, Keys.DIARY_P_Type);
		} else {
			mDiaryService.getDiaryW(null, null, null, null, Keys.ON_REFRESH,
					Keys.DIARY_P_Type);
		}

	}

	private void diaryTypeSwitch() {
		// TODO Auto-generated method stub
		handler.postDelayed(diaryTypeSwitchRunable, 1500);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
}
