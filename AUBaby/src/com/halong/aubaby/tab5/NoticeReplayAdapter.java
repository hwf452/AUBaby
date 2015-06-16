package com.halong.aubaby.tab5;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.NoticeReplyEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoticeReplayAdapter extends BaseAdapter {

	private Context mContext;
	private List<NoticeReplyEntity.commentList.comment> mList;
	private LayoutInflater mLayoutInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private DisplayImageOptions mheadOptions;
	private int commentListID;

	public NoticeReplayAdapter(Context context, int commentListID) {
		this.mList = new ArrayList<NoticeReplyEntity.commentList.comment>();
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DemoApplication app = (DemoApplication) mContext
				.getApplicationContext();
		mImageLoader = app.getImageLoader();
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		this.mheadOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).showImageOnFail(R.drawable.head)
				.cacheOnDisc(true).build();
		this.commentListID = commentListID;
	}

	public void addList(List<NoticeReplyEntity.commentList.comment> list) {
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	public void clearList() {
		this.mList.clear();
		notifyDataSetChanged();
	}

	public void addListObect(NoticeReplyEntity.commentList.comment object) {
		this.mList.add(object);
	}

	public void addListObectPosition(int position,
			NoticeReplyEntity.commentList.comment object) {
		this.mList.add(position, object);
	}

	public void replaceListObectPosition(int position,
			NoticeReplyEntity.commentList.comment object) {
		this.mList.set(position, object);
	}

	public List<NoticeReplyEntity.commentList.comment> getAllList() {
		return mList;
	}

	public NoticeReplyEntity.commentList.comment listLastObect() {
		if (mList.size() > 0) {
			return mList.get(mList.size() - 1);
		} else {
			return null;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = mLayoutInflater.inflate(R.layout.item_tab5_replay2, null);
		ListItemView listItemView = null;
		listItemView = new ListItemView(view);

		final NoticeReplyEntity.commentList.comment entity = mList
				.get(position);
		if (entity.getIsAdmin_comment().equals("1")) {
			listItemView.tecImageView.setVisibility(View.VISIBLE);
		}
		listItemView.nameTextView.setText(entity.getUsername_comment());
		listItemView.dateTextView.setText(entity.getTime_comment());
		listItemView.contentTextView.setText(entity.getContent_comment());

		mImageLoader.displayImage(
				ContantUtil.PICTURE_URL + entity.getHeadPhoto_comment(),
				listItemView.headImageButton, mheadOptions);

		listItemView.headImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						OtherUserInfoActivity.class);
				intent.putExtra(Keys.USER_INFO_ID, entity.getUserid_comment());
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_left_out);
			}
		});

		if (!entity.getUserid_reply().equals("")) {
			listItemView.replyTextView.setVisibility(View.GONE);
			listItemView.replyCommentLayout.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(
					ContantUtil.PICTURE_URL + entity.getHeadPhoto_reply(),
					listItemView.replyHeadImageButton, mheadOptions);
			listItemView.replyNameTextView.setText(entity.getUsername_reply());
			listItemView.replyDateTextView.setText(entity.getTime_reply());
			listItemView.replycontentTextView
					.setText(entity.getContent_reply());
			if (entity.getIsAdmin_reply().equals("1")) {
				listItemView.replyTecImageView.setVisibility(View.VISIBLE);
			} else {
				listItemView.replyTecImageView.setVisibility(View.GONE);
			}

		} else {
			listItemView.replyCommentLayout.setVisibility(View.GONE);
			if (SharedPreferencesHelper.getStringValue(mContext, Keys.IS_CLASS_ADMIN).equals("1")) {
				listItemView.replyTextView.setVisibility(View.VISIBLE);
				listItemView.replyTextView
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent().setClass(mContext,
										ReplyNoticeCommentActivity.class);
								intent.putExtra(Keys.REPLY_COMMENT_ID,
										entity.getId());
								intent.putExtra(Keys.NOTICE_REPLY_COMMENT,
										commentListID);
								((NoticeActivity) mContext)
										.startActivityForResult(intent,
												Keys.NOTICE_REPLY_ADAPTER);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.push_right_in,
												R.anim.push_left_out);
							}
						});

			} else {
				listItemView.replyTextView.setVisibility(View.GONE);
			}

		}

		if (entity.getCommentAttachments().getAttachment()[0].getType().equals(
				"P")) {
			mImageLoader.displayImage(
					ContantUtil.PICTURE_URL
							+ entity.getCommentAttachments().getAttachment()[0]
									.getUrl(), listItemView.fileImageView,
					mOptions);
			listItemView.fileImageView.setVisibility(View.VISIBLE);
			listItemView.fileImageView
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(mContext, PictureShowActivity.class);
							intent.putExtra(
									Keys.PHOTO_URL,
									ContantUtil.PICTURE_URL
											+ entity.getCommentAttachments()
													.getAttachment()[0]
													.getUrl());
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
			listItemView.fileSizeTextView.setVisibility(View.VISIBLE);
		}

		if (!entity.getCommentAttachments().getAttachment()[0].getSize()
				.equals("")) {
			listItemView.fileSizeTextView.setText(entity
					.getCommentAttachments().getAttachment()[0].getSize());

		}

		if (entity.getReplyAttachments().getAttachment()[0].getType().equals(
				"P")) {
			mImageLoader.displayImage(ContantUtil.PICTURE_URL
					+ entity.getReplyAttachments().getAttachment()[0].getUrl(),
					listItemView.replyFileImageView, mOptions);
			listItemView.replyFileImageView.setVisibility(View.VISIBLE);
			listItemView.replyFileImageView
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(mContext, PictureShowActivity.class);
							intent.putExtra(
									Keys.PHOTO_URL,
									ContantUtil.PICTURE_URL
											+ entity.getReplyAttachments()
													.getAttachment()[0]
													.getUrl());
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
			listItemView.replyFileSizeTextView.setVisibility(View.VISIBLE);
		}

		if (!entity.getReplyAttachments().getAttachment()[0].getSize().equals(
				"")) {
			listItemView.replyFileSizeTextView.setText(entity
					.getReplyAttachments().getAttachment()[0].getSize());

		}
		return view;
	}

	static class ListItemView {
		ImageButton headImageButton, replyHeadImageButton;
		ImageView tecImageView, replyTecImageView;
		TextView nameTextView, replyNameTextView;
		TextView dateTextView, replyDateTextView;
		TextView contentTextView, replycontentTextView, replyTextView;
		View replyCommentLayout;

		ImageView fileImageView, replyFileImageView;
		TextView fileSizeTextView, replyFileSizeTextView;

		public ListItemView(View view) {
			// 回复内容控件的初始化
			headImageButton = (ImageButton) view
					.findViewById(R.id.headImageButton);
			nameTextView = (TextView) view.findViewById(R.id.nameTextView);
			dateTextView = (TextView) view.findViewById(R.id.dateTextView);
			contentTextView = (TextView) view
					.findViewById(R.id.contentTextView);
			tecImageView = (ImageView) view.findViewById(R.id.tecImg);
			// 评论回复内容控件的初始化
			replyCommentLayout = (View) view
					.findViewById(R.id.replyCommentLayout);
			replyHeadImageButton = (ImageButton) view
					.findViewById(R.id.replyHeadImageButton);
			replyNameTextView = (TextView) view
					.findViewById(R.id.replyNameTextView);
			replyDateTextView = (TextView) view
					.findViewById(R.id.replyDateTextView);
			replycontentTextView = (TextView) view
					.findViewById(R.id.replyContentTextView);
			replyTecImageView = (ImageView) view.findViewById(R.id.tecImgX);
			replyTextView = (TextView) view.findViewById(R.id.replyTxt);

			fileImageView = (ImageView) view.findViewById(R.id.fileImg);
			replyFileImageView = (ImageView) view
					.findViewById(R.id.replyFileImg);
			fileSizeTextView = (TextView) view.findViewById(R.id.fileSizeTxt);
			replyFileSizeTextView = (TextView) view
					.findViewById(R.id.replyFileSizeTxt);
		}
	}
}
