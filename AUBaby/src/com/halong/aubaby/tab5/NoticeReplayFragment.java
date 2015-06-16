package com.halong.aubaby.tab5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.NoticeReplyEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.NoticeService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.widget.swipelistview.SwipeListView;
import com.halong.aubaby.R;

public class NoticeReplayFragment extends FragmentToOtherActivity {

	private Context mContext;

	private TextView mContentTextView, replyTextView, mDateTextView,
			mNewReplayTextView;
	private SwipeListView mListView;
	private PullToRefreshView mPullToRefreshView;

	private NoticeReplayAdapter mNoticeReplayAdapter;
	private Gson gson;
	private NoticeService noticeService;// 解析评论列表的方法
	private int mId = -1;// 通知id
	private String mCurrentPage = "";// 该项为评论id，如果为空则刷新
	private String mClassID;// 班级id

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab5_ontice_replay_list,
				null);
		View headView = inflater.inflate(R.layout.include_notice_comment_head,
				null);
		// 初始化
		initView(view, headView);
		// 加载最新消息
		postNoticeReplay(true);
		// 上下拉的方法
		pullUpdateData();
		return view;
	}

	/**
	 * 初始化基本参数和控件
	 * 
	 * @param view
	 * @param headView
	 */

	private void initView(View view, View headView) {
		// TODO Auto-generated method stub

		mContext = getActivity();

		gson = new Gson();
		mClassID = SharedPreferencesHelper.getStringValue(mContext,
				Keys.CLASS_ID);
		mId = getActivity().getIntent().getIntExtra(Keys.NOTICE_REPLY_COMMENT,
				-1);
		// view
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mListView = (SwipeListView) view.findViewById(R.id.onlyListView);

		// headView
		mContentTextView = (TextView) headView
				.findViewById(R.id.contentTextView);
		replyTextView = (TextView) headView.findViewById(R.id.replyTextView);
		replyTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), ReplyNoticeCommentActivity.class);
				intent.putExtras(getActivity().getIntent().getExtras());
				startActivityForResult(intent, Keys.NOTICE_REPLY_FRAGMENT);
				getActivity().overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});
		mDateTextView = (TextView) headView.findViewById(R.id.dateTextView);
		mNewReplayTextView = (TextView) headView
				.findViewById(R.id.newReplayTextView);

		mListView.addHeaderView(headView);

		mNoticeReplayAdapter = new NoticeReplayAdapter(mContext, mId);
		mListView.setAdapter(mNoticeReplayAdapter);

		noticeService = new NoticeService(getActivity()) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				parseNoticeReplay(content);

			}
		};

	}

	/**
	 * 上下拉刷新监听
	 */
	private void pullUpdateData() {
		// PullToRefreshView
		mPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						// TODO Auto-generated method stub
						postNoticeReplay(true);
					}
				});
		mPullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						// TODO Auto-generated method stub
						postNoticeReplay(false);
					}
				});
	}

	/**
	 * 设置内容
	 * 
	 * @param str
	 */
	public void setContentTextView(String str) {
		mContentTextView.setText(str);
	}

	/**
	 * 设置时间
	 * 
	 * @param str
	 */
	public void setDateTextView(String str) {
		//
		mDateTextView.setText(str);
	}

	/**
	 * 设置最近评论数
	 * 
	 * @param str
	 */
	public void setNewReplayTextView(String str) {
		mNewReplayTextView.setText(str);
	}

	/**
	 * 获取评论信息
	 */
	public void postNoticeReplay(boolean downRefresh) {
		// TODO Auto-generated method stub

		if (downRefresh) {
			mCurrentPage = "";
		} else {

			if (mNoticeReplayAdapter.listLastObect() == null) {
				mCurrentPage = "";
			} else {
				mCurrentPage = mNoticeReplayAdapter.listLastObect().getId();
			}
		}

		noticeService.getNoticeReplay(mId, mClassID, mCurrentPage);

	}

	/**
	 * 解析通知回复列表
	 * 
	 * @param content
	 */
	private void parseNoticeReplay(String content) {
		NoticeReplyEntity entity = gson.fromJson(content,
				NoticeReplyEntity.class);
		if (mCurrentPage.equals("")) {
			mNoticeReplayAdapter.clearList();
		}

		for (int i = 0; i < entity.getCommentList().getComment().length; i++) {
			mNoticeReplayAdapter.addListObect(entity.getCommentList()
					.getComment()[i]);
		}
		mNoticeReplayAdapter.notifyDataSetChanged();
		mPullToRefreshView.onHeaderRefreshComplete();
		mPullToRefreshView.onFooterRefreshComplete();
	}

	/**
	 * 增加评论
	 * 
	 * @param content
	 */
	private void addNoticeReplay(String content) {
		NoticeReplyEntity entity = gson.fromJson(content,
				NoticeReplyEntity.class);

		for (int i = 0; i < entity.getCommentList().getComment().length; i++) {
			mNoticeReplayAdapter.addListObectPosition(i, entity
					.getCommentList().getComment()[i]);
		}
		mNoticeReplayAdapter.notifyDataSetChanged();
	}

	/**
	 * 替换评论
	 * 
	 * @param content
	 */
	public void replaceNoticeReplay(String content) {
		NoticeReplyEntity entity = gson.fromJson(content,
				NoticeReplyEntity.class);

		for (int i = 0; i < mNoticeReplayAdapter.getAllList().size(); i++) {
			if (mNoticeReplayAdapter.getAllList().get(i).getId()
					.equals(entity.getCommentList().getComment()[0].getId())) {
				mNoticeReplayAdapter.replaceListObectPosition(i, entity
						.getCommentList().getComment()[0]);
				mNoticeReplayAdapter.notifyDataSetChanged();
				return;
			}

		}
		mNoticeReplayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Keys.REPLY_NOTICE_COMMENT_ACTIVITY
				&& requestCode == Keys.NOTICE_REPLY_FRAGMENT) {
			addNoticeReplay(data.getStringExtra(Keys.COMMENT_LIST));
		}
		if (resultCode == Keys.NOTICE_REPLY_ADAPTER
				&& requestCode == Keys.NOTICE_REPLY_FRAGMENT) {
			replaceNoticeReplay(data.getStringExtra(Keys.COMMENT_LIST));
		}
	}

}
