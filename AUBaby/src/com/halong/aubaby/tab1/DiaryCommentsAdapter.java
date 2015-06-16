package com.halong.aubaby.tab1;

import java.util.ArrayList;

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
import com.halong.aubaby.entity.DiaryCommentsEntity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DiaryCommentsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<DiaryCommentsEntity.ObjInfo> mList;
	private LayoutInflater mLayoutInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	public DiaryCommentsAdapter(Context context, ImageLoader imageLoader,
			DisplayImageOptions options) {
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = imageLoader;
		this.mOptions = options;
		mList = new ArrayList<DiaryCommentsEntity.ObjInfo>();
	}

	public void addCommentsList(ArrayList<DiaryCommentsEntity.ObjInfo> list) {
		mList.addAll(list);
	}

	public void addCommentsFirst(DiaryCommentsEntity.ObjInfo objInfo) {
		mList.add(0, objInfo);
	}

	public void deleteComment(DiaryCommentsEntity.ObjInfo objInfo) {
		// TODO Auto-generated method stub
		mList.remove(objInfo);
	}

	public String getLastCommentID() {
		// TODO Auto-generated method stub
		return mList.get(mList.size() - 1).getId();
	}

	public String getFirstCommentID() {
		// TODO Auto-generated method stub
		return mList.get(0).getId();
	}

	public ArrayList<DiaryCommentsEntity.ObjInfo> getCommentList() {
			return mList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int commentNumber() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		View view = mLayoutInflater.inflate(R.layout.item_diary_comment, null);
		listItemView = new ListItemView();
		listItemView.headImageButton = (ImageButton) view
				.findViewById(R.id.headImageButton);
		listItemView.nameTextView = (TextView) view
				.findViewById(R.id.nameTextView);
		listItemView.dateTextView = (TextView) view
				.findViewById(R.id.dateTextView);
		listItemView.contentTextView = (TextView) view
				.findViewById(R.id.contentTextView);
		listItemView.teacherImageView=(ImageView)view.findViewById(R.id.tecImg);
		

		listItemView.nameTextView.setText(mList.get(position).getName());
		listItemView.dateTextView.setText(mList.get(position).getTime());
		listItemView.contentTextView.setText(mList.get(position).getText());

		mImageLoader.displayImage(ContantUtil.PICTURE_URL
				+ mList.get(position).getImg(), listItemView.headImageButton,
				mOptions);

		listItemView.headImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						OtherUserInfoActivity.class);
				intent.putExtra(Keys.USER_INFO_ID, mList.get(position)
						.getCode());
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_left_out);
			}
		});

		if (mList.get(position).getIsAdmin().equals("1")) {
			listItemView.teacherImageView.setVisibility(View.VISIBLE);
		}else  {
			listItemView.teacherImageView.setVisibility(View.GONE);
		}
		return view;
	}

	static class ListItemView {
		ImageButton headImageButton;
		TextView nameTextView;
		TextView dateTextView;
		TextView contentTextView;
		ImageView teacherImageView;
	}
}
