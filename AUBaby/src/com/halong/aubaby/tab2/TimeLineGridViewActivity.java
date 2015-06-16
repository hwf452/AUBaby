package com.halong.aubaby.tab2;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.AlbumPhotosEntity;
import com.halong.aubaby.entity.AlbumPhotosEntity.itemList.item;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.AlbumService;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.TimeLineGridView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.TimeLineGridView.OnPositionChangedListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimeLineGridViewActivity extends BackActivity implements
		OnPositionChangedListener, OnFooterRefreshListener {
	private Context mContext;

	private TimeLineGridView mGridView;
	private BaseAdapter mAdapter;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private String albumID;// 要查看相册id
	private AlbumService albumService;
	private AlbumPhotosEntity albumPhotosEntity;
	private ArrayList<AlbumPhotosEntity.itemList.item> mList;
	private int itemHeight, itemWidth;// itmes高度,宽度
	private PullToRefreshView mPullToRefreshView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);

		mContext = this;

		Intent intent = getIntent();

		albumID = intent.getStringExtra(Keys.ALBUM_ID);
		if (albumID == null) {
			return;
		}
		mList = new ArrayList<AlbumPhotosEntity.itemList.item>();

		itemHeight = (getWindowManager().getDefaultDisplay().getWidth() - 8 * 4) / 3;
		itemWidth = itemHeight;
		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		initView();

		albumService = new AlbumService(mContext) {
			@Override
			public void unCollectPhoto(String collectID, item item) {
				// TODO Auto-generated method stub
				super.unCollectPhoto(collectID, item);
				mList.remove(item);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void getAlbumPhotosMoreSuccess() {
				// TODO Auto-generated method stub

				albumPhotosEntity = albumService.getAlbumPhotosEntity();
				for (int i = 0; i < albumPhotosEntity.getItemList().getItem().length; i++) {
					mList.add(albumPhotosEntity.getItemList().getItem()[i]);
				}
				mAdapter.notifyDataSetChanged();
				mPullToRefreshView.onFooterRefreshComplete();
				super.getAlbumPhotosMoreSuccess();
			}

			@Override
			public void getAlbumPhotosMoreFailure() {
				// TODO Auto-generated method stub
				super.getAlbumPhotosMoreFailure();
				mPullToRefreshView.onFooterRefreshComplete();
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
				albumPhotosEntity = albumService.getAlbumPhotosEntity();
				for (int i = 0; i < albumPhotosEntity.getItemList().getItem().length; i++) {
					mList.add(albumPhotosEntity.getItemList().getItem()[i]);
				}
				mAdapter.notifyDataSetChanged();
			}
		};
		albumService.getAlbumPhotos(albumID);

	}

	/**
	 * 声明控件
	 */
	private void initView() {
		mGridView = (TimeLineGridView) findViewById(R.id.grid);

		mAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				View showView = getLayoutInflater().inflate(
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
							+ Keys.S_VIEW + mList.get(position).getUrl(),
							mImageView, mOptions);
				} catch (Exception e) {
					// TODO: handle exception
				}

				return showView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}
		};
		mGridView.setAdapter(mAdapter);
		mGridView.setFocusable(false);
		mGridView.setOnPositionChangedListener(this);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TimeLineGridViewActivity.this,
						PictureShowActivity.class);
				intent.putExtra(Keys.CLICK_GRIDVIEW_ITEM, mList.get(position)
						.getUrl());
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});

		mGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						deletePic(mList.get(arg2).getId(), mList.get(arg2));
						return true;
					}
				});
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnStopHeadRefresh(true);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	public void onPositionChanged(TimeLineGridView girdView, int position,
			View scrollBarPanel) {
		if (position < 0) {
			return;
		}
		((TextView) scrollBarPanel).setText(mList.get(position)
				.getPublishDate());
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if (mList.size() > 0) {
			albumService.getAlbumPhotosMore(albumID, mList
					.get(mList.size() - 1).getId());

		} else {
			albumService.getAlbumPhotosMore(albumID, null);
		}

	}

	private void deletePic(final String id,
			final AlbumPhotosEntity.itemList.item item) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(R.string.un_collect_photo)
				.setPositiveButton(R.string.delete, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						albumService.unCollectPhoto(id, item);
					}
				}).setNegativeButton(R.string.cancel, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();

	}

}
