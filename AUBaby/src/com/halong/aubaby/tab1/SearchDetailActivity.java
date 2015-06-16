package com.halong.aubaby.tab1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchDetailActivity extends BackActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView mListView;
	private DiaryAdapter mAdapter;
	private String startDate = null;// 开始日期
	private String endDate = null;// 结束日期
	private String searchKey = null;// 关键字
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private DiaryService diaryService;
	private LinearLayout progress;// 进度条
	private PullToRefreshView mPullToRefreshView;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_detail);

		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		Intent intent = getIntent();
		startDate = intent.getStringExtra(Keys.SEARCH_START_DATE);
		endDate = intent.getStringExtra(Keys.SEARCH_END_DATE);
		searchKey = intent.getStringExtra(Keys.SEARCH_KEY);

		initView();
		diaryService.getDiaryV(null, null, startDate, endDate, searchKey,
				Keys.ON_REFRESH, Keys.DIARY_P_Type);

	}

	private void initView() {
		// TODO Auto-generated method stub
		// 初始化图片加载线程

		progress = (LinearLayout) findViewById(R.id.progress);
		mListView = (ListView) findViewById(R.id.diaryListView);
		mAdapter = new DiaryAdapter(SearchDetailActivity.this, mImageLoader,
				mOptions, true);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		diaryService = new DiaryService(getApplicationContext()) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub
				ArrayList<VDiaryEntity.ObjInfo> list = new ArrayList<VDiaryEntity.ObjInfo>();
				VDiaryEntity vEntity = diaryService.getvEntity();
				if (vEntity.getObjInfo().length >= 24) {
					mAdapter.clearDiaryList();
				}
				for (int i = 0; i < vEntity.getObjInfo().length; i++) {
					list.add(i, vEntity.getObjInfo()[i]);
				}
				// 添加从服务器获取的日记
				mAdapter.addNetDiaryListAtTopPosition(list);
				mAdapter.notifyDataSetChanged();
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
					Toast.makeText(getApplicationContext(),
							R.string.no_more_diarys, Toast.LENGTH_SHORT).show();
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
			Intent intent = new Intent(getApplicationContext(),
					ShuoShuoDetailActivity.class);
			Bundle bundle = new Bundle();
			// 将数据传递过去
			bundle.putString(Keys.DIARY_ID,
					mAdapter.getALLMDiaryList().get(position).getContent()
							.getId());
			bundle.putString(Keys.USER_INFO_ID, mAdapter.getALLMDiaryList()
					.get(position).getUser().getCode());
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (mAdapter.getLastNetDiary() != null) {

			diaryService.getDiaryV(null, mAdapter.getLastNetDiary()
					.getContent().getId(), startDate, endDate, searchKey,
					Keys.ON_LOAD, Keys.DIARY_P_Type);

		} else {
			// 如果日记列表没有日记，当成刚进入应用
			diaryService.getDiaryV(null, null, startDate, endDate, searchKey,
					Keys.ON_LOAD, Keys.DIARY_P_Type);
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (mAdapter.getFirstNetDiary() != null) {
			diaryService.getDiaryV(mAdapter.getFirstNetDiary().getContent()
					.getId(), null, startDate, endDate, searchKey,
					Keys.ON_REFRESH, Keys.DIARY_P_Type);
		} else {
			diaryService.getDiaryV(null, null, startDate, endDate, searchKey,
					Keys.ON_REFRESH, Keys.DIARY_P_Type);
		}
	}

}
