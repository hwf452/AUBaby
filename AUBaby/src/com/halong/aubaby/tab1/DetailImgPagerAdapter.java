package com.halong.aubaby.tab1;

import java.util.ArrayList;
import java.util.List;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.DiaryDetailEntity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DetailImgPagerAdapter extends PagerAdapter {
	private Context mContext;
	private DiaryDetailEntity dEntity;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private List<View> viewList;

	public DetailImgPagerAdapter(Context context, ImageLoader imageLoader,
			 DiaryDetailEntity entity) {
		mContext = context;
		dEntity = entity;
		mImageLoader = imageLoader;
		mOptions = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.bg_3).cacheOnDisc(true).build();
		viewList = new ArrayList<View>();
		for (int i = 0; i < dEntity.getContent().getCountOfPics(); i++) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.include_img_preview, null);
			viewList.add(view);
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dEntity.getContent().getCountOfPics();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// TODO Auto-generated method stub
		container.addView(viewList.get(position), 0);
		final ImageView imageView = (ImageView) viewList.get(position)
				.findViewById(R.id.img);
		mImageLoader.displayImage(ContantUtil.PICTURE_URL + Keys.L_VIEW
				+ dEntity.getPiclist().getPic()[position].getImg(),
				imageView, mOptions);
		return viewList.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(viewList.get(position));
	}
}
