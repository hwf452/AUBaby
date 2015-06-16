package com.halong.aubaby.tab1;

import java.util.ArrayList;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.WDiaryEntity;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserInfoAdapter extends BaseAdapter {
	private Context mContext;
	private ImageView imageView, imageView2, imageView3;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private ArrayList<WDiaryEntity.Pic> photoList;
	private FrameLayout frameLayout;

	public UserInfoAdapter(Context context, ImageLoader imageLoader,
			DisplayImageOptions options) {
		mContext = context;
		mImageLoader = imageLoader;
		mOptions = options;
		photoList = new ArrayList<WDiaryEntity.Pic>();
	}

	public void addPhotoListAtTopPosition(ArrayList<WDiaryEntity.Pic> list) {

		photoList.addAll(0, list);
	}

	public void addPhotoListAtLastPosition(ArrayList<WDiaryEntity.Pic> list) {
		photoList.addAll(list);
	}

	public void clearPhotoList() {
		// TODO Auto-generated method stub
		photoList.clear();
	}

	public WDiaryEntity.Pic getFirstPhoto() {
		// TODO Auto-generated method stub
		if (photoList.size() > 0) {
			return photoList.get(0);
		} else {
			return null;
		}

	}

	public WDiaryEntity.Pic getLastPhoto() {
		// TODO Auto-generated method stub
		if (photoList.size() > 0) {
			return photoList.get(photoList.size() - 1);
		} else {
			return null;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (photoList.size() % 3 == 0) {
			return photoList.size() / 3;
		} else {
			return photoList.size() / 3 + 1;
		}

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

	@SuppressLint("ServiceCast")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(mContext).inflate(
				R.layout.include_items_listview_photo_wall, null);
		frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);

		imageView = (ImageView) view.findViewById(R.id.photo);
		imageView2 = (ImageView) view.findViewById(R.id.photo2);
		imageView3 = (ImageView) view.findViewById(R.id.photo3);

		int height = (((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth() - 32) / 3;
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frameLayout
				.getLayoutParams();
		layoutParams.height = height;
		frameLayout.setLayoutParams(layoutParams);
		if (3 * position < photoList.size()) {
			mImageLoader
					.displayImage(ContantUtil.PICTURE_URL + Keys.S_VIEW
							+ photoList.get(3 * position).getUrl(), imageView,
							mOptions);
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					toShowPic(photoList.get(3 * position).getUrl());
				}
			});
			imageView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					TextView textView = (TextView) view
							.findViewById(R.id.postedTxt);
					textView.setText(photoList.get(3 * position)
							.getPublishTime());
					textView.setVisibility(View.VISIBLE);
					return true;
				}
			});
		}

		if (3 * position + 1 < photoList.size()) {
			mImageLoader.displayImage(ContantUtil.PICTURE_URL + Keys.S_VIEW
					+ photoList.get(3 * position + 1).getUrl(), imageView2,
					mOptions);
			imageView2.setVisibility(View.VISIBLE);
			imageView2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					toShowPic(photoList.get(3 * position + 1).getUrl());
				}
			});
			imageView2.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					TextView textView2 = (TextView) view
							.findViewById(R.id.postedTxt2);
					textView2.setText(photoList.get(3 * position + 1)
							.getPublishTime());
					textView2.setVisibility(View.VISIBLE);
					return true;
				}
			});
		}

		if (3 * position + 2 < photoList.size()) {
			mImageLoader.displayImage(ContantUtil.PICTURE_URL + Keys.S_VIEW
					+ photoList.get(3 * position + 2).getUrl(), imageView3,
					mOptions);
			imageView3.setVisibility(View.VISIBLE);
			imageView3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					toShowPic(photoList.get(3 * position + 2).getUrl());
				}
			});

			imageView3.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					TextView textView3 = (TextView) view
							.findViewById(R.id.postedTxt3);
					textView3.setText(photoList.get(3 * position + 2)
							.getPublishTime());
					textView3.setVisibility(View.VISIBLE);
					return true;
				}
			});
		}

		return view;
	}

	public void toShowPic(String url) {
		Intent intent = new Intent();
		intent.setClass(mContext, PictureShowActivity.class);
		intent.putExtra(Keys.CLICK_GRIDVIEW_ITEM, url);
		mContext.startActivity(intent);
		((Activity) mContext).overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

}
