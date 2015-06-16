package com.halong.aubaby.tab5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.NoticeService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;

public class SNoticeListFragment extends FragmentToOtherActivity {

	private Context mContext;

	private ListView mNoticeListView;
	private PullToRefreshView mPullToRefreshView;

	private List<NoticeListEntity> mList;
	private SNoticeListAdapter mAdapter;
	private NoticeService noticeService;
	private int mCurrentPage = 0;
	private View progress;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab5_ontice_list, null);
		mContext = getActivity();
		progress = (View) view.findViewById(R.id.progress);
		mNoticeListView = (ListView) view.findViewById(R.id.onlyListView);
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);

		mList = new ArrayList<NoticeListEntity>();

		initView();

		postNoticeList();

		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		// ListVIew
		mAdapter = new SNoticeListAdapter(getActivity(), mList);
		// 填充listview头尾空间
		View view1 = new View(getActivity());
		view1.setMinimumHeight((int) getResources().getDimension(
				R.dimen.view_smallsmallsmall_margin));
		mNoticeListView.addHeaderView(view1);
		View view2 = new View(getActivity());
		view2.setMinimumHeight((int) getResources().getDimension(
				R.dimen.view_smallsmallsmall_margin));
		mNoticeListView.addFooterView(view2);

		mNoticeListView.setAdapter(mAdapter);

		// PullToRefreshView
		mPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						// TODO Auto-generated method stub
						mCurrentPage = 0;
						postNoticeList();

						((SystemMessageActivity) getActivity()).postUnreadNum();
					}
				});
		mPullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						// TODO Auto-generated method stub
						postNoticeList();
						((SystemMessageActivity) getActivity()).postUnreadNum();
					}
				});

		noticeService = new NoticeService(getActivity()) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				progress.setVisibility(View.GONE);
				
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				parseSNoticeList(content);
			}
		};
	}

	/**
	 * 发送获取S类型信息请求
	 */
	private void postNoticeList() {

		noticeService
				.getSNoticeList(SharedPreferencesHelper.getStringValue(
						mContext, Keys.CLASS_ID), mCurrentPage);
	}

	private void parseSNoticeList(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.getBoolean("result")) {
				progress.setVisibility(View.GONE);
				return;
			}
			if (mCurrentPage == 0) {
				mList.clear();
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));
			}
			NoticeListEntity entity = null;
			JSONArray info = jsonObject.getJSONObject("list").getJSONArray(
					"info");
			JSONObject entityObject = null;
			for (int i = 0; i < info.length(); i++) {
				entityObject = info.getJSONObject(i);
				entity = new NoticeListEntity();
				entity.setId(entityObject.getInt("id"));
				entity.setIsAdmin(entityObject.getInt("isAdmin"));
				entity.setIsRead(entityObject.getString("isRead"));
				entity.setNoticeCanReply(entityObject.getInt("noticeCanReply"));
				entity.setIsUrgency(entityObject.getInt("isUrgency"));
				entity.setNoticeType(entityObject.getString("noticeType"));
				entity.setNoticeReplyCnt(entityObject.getInt("noticeReplyCnt"));
				entity.setTime(entityObject.getString("time"));
				entity.setTitle(entityObject.getString("title"));
				JSONObject newTitleObject = entityObject
						.getJSONObject("newTitle");
				NoticeListEntity.newTitle newTitle = new NoticeListEntity.newTitle();
				newTitle.setA(newTitleObject.getString("A"));
				newTitle.setContent(newTitleObject.getString("content"));
				newTitle.setH(newTitleObject.getString("H"));
				newTitle.setID(newTitleObject.getString("ID"));
				newTitle.setJUMP(newTitleObject.getString("JUMP"));
				newTitle.setU(newTitleObject.getString("U"));
				entity.setNewTitle(newTitle);
				mList.add(entity);

				if (info.length() == i + 1) {
					mCurrentPage = entityObject.getInt("id");
				}
			}
			progress.setVisibility(View.GONE);
			mAdapter.notifyDataSetChanged();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			progress.setVisibility(View.GONE);
			Toast.makeText(mContext, getString(R.string.error),
					Toast.LENGTH_SHORT).show();
		}

		mPullToRefreshView.onHeaderRefreshComplete();
		mPullToRefreshView.onFooterRefreshComplete();
	}

}
