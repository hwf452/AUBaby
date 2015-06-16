package com.halong.aubaby.tab1;

import java.util.ArrayList;

import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.DiaryCommentsEntity;
import com.halong.aubaby.entity.DiaryCommentsEntity.ObjInfo;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.CommentService;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.swipelistview.BaseSwipeListViewListener;
import com.halong.aubaby.widget.swipelistview.SwipeListView;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DiaryCommentFragment extends Fragment implements
		OnFooterRefreshListener {

	private Context mContext;

	private SwipeListView mListView;
	private PullToRefreshView mPullToRefreshView;
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions options;// 图片加载设置
	private DiaryCommentsAdapter diaryCommentsAdapter;// 评论列表适配器
	private CommentService commentService;// 获取评论方法
	private String diaryID;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		diaryID = getActivity().getIntent().getStringExtra(Keys.DIARY_ID);

		View view = inflater.inflate(R.layout.fragment_tab5_ontice_replay_list,
				null);
		View headView = inflater.inflate(R.layout.item_tab5_replay1, null);

		// 上拉控件
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnStopHeadRefresh(true);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		// 评论列表
		mListView = (SwipeListView) view.findViewById(R.id.onlyListView);
		mListView.addHeaderView(headView);

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		diaryCommentsAdapter = new DiaryCommentsAdapter(mContext, imageLoader,
				options);
		mListView.setAdapter(diaryCommentsAdapter);
		mListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClickBackView(int position) {

				commentService.deleteComment(
						((DiaryCommentsEntity.ObjInfo) diaryCommentsAdapter
								.getItem(position - 1)).getId(),
						(DiaryCommentsEntity.ObjInfo) diaryCommentsAdapter
								.getItem(position - 1));

			}

		});
		if (TextUtils.equals(SharedPreferencesHelper.getStringValue(
				getActivity(), Keys.IS_CLASS_ADMIN), "1")) {

			// 如果是管理员或者本人，可以删除评论
			mListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);

		}
		if (getActivity().getIntent().getExtras()
				.containsKey(Keys.USER_INFO_ID)) {
			if (TextUtils.equals(
					SharedPreferencesHelper.getIntValue(getActivity(),
							SharedPreferencesHelper.USERID) + "", getActivity()
							.getIntent().getStringExtra(Keys.USER_INFO_ID))) {
				mListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
			}
		}
		// 获取评论方法
		commentService = new CommentService(mContext) {
			@Override
			public void getDiaryCommentsSuccess() {
				// TODO Auto-generated method stub

				ArrayList<DiaryCommentsEntity.ObjInfo> list = new ArrayList<DiaryCommentsEntity.ObjInfo>();
				DiaryCommentsEntity.ObjInfo[] objInfos = commentService
						.getDiaryCommentsEntity().getObjInfo();
				for (int i = 0; i < objInfos.length; i++) {
					list.add(objInfos[i]);
				}
				diaryCommentsAdapter.addCommentsList(list);
				diaryCommentsAdapter.notifyDataSetChanged();
				mPullToRefreshView.onFooterRefreshComplete();

				super.getDiaryCommentsSuccess();
			}

			@Override
			public void getDiaryCommentsFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onFooterRefreshComplete();
				super.getDiaryCommentsFailure();
			}

			@Override
			public void deleteComment(String commentID, ObjInfo objInfo) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), R.string.delete_comment_success,
						Toast.LENGTH_SHORT).show();
				diaryCommentsAdapter.deleteComment(objInfo);
				diaryCommentsAdapter.notifyDataSetChanged();
				super.deleteComment(commentID, objInfo);
			}

			@Override
			public void deleteCommentFailure() {
				// TODO Auto-generated method stub
				super.deleteCommentFailure();
			}
		};
		commentService.getDiaryComments(diaryID, null);

		return view;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

		if (diaryCommentsAdapter.commentNumber() > 0) {
			commentService.getDiaryComments(getActivity().getIntent()
					.getStringExtra(Keys.DIARY_ID), diaryCommentsAdapter
					.getLastCommentID());
		} else {
			commentService.getDiaryComments(getActivity().getIntent()
					.getStringExtra(Keys.DIARY_ID), null);
		}

	}

	public ArrayList<DiaryCommentsEntity.ObjInfo> getCommentList() {
		// TODO Auto-generated method stub
		return diaryCommentsAdapter.getCommentList();
	}

}
