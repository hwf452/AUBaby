package com.halong.aubaby.util.picutil1;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.halong.aubaby.util.DisplayUtil;
import com.halong.aubaby.R;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageAdapter extends BaseAdapter{
	private ImageLoader imageLoader;
	private Context mContext;
	DisplayImageOptions options;
	String[] imageUrls;
	private Integer picWidth;
	
	
	public ImageAdapter(ImageLoader imageLoader,Context mContext,String[] imageUrls,Integer width ){
		this.imageLoader=imageLoader;
		this.mContext=mContext;
		this.imageUrls=imageUrls;
		DisplayUtil displayUtil=new DisplayUtil();
		int pid=displayUtil.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tab2_gridPiding));
		 picWidth=(int) ((width-pid*4)/3);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)

		.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image,
					parent, false);
			holder = new ViewHolder();
			
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picWidth));
			
			holder.progressBar = (ProgressBar) view
					.findViewById(R.id.progress);
			holder.progressBar.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, LayoutParams.WRAP_CONTENT));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage(imageUrls[position], holder.imageView,
				options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						holder.progressBar.setProgress(0);
						holder.progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri,
							View view, Bitmap loadedImage) {
						holder.progressBar.setVisibility(View.GONE);
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri,
							View view, int current, int total) {
						holder.progressBar.setProgress(Math.round(100.0f
								* current / total));
					}
				});

		return view;
	}

	class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}

	
	
}