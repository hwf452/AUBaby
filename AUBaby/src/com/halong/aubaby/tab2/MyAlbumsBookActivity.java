package com.halong.aubaby.tab2;

/**
 * 我的像册详情
 */

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.AlbumListEntity;
import com.halong.aubaby.entity.AlbumListEntity.FolderList.Folder;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.AlbumService;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyAlbumsBookActivity extends BackActivity {
	/**
	 * 声明控件
	 */
	private GridView mGridView;
	private AlbumService albumService;
	private AlbumListEntity aListEntity;// 用户相册数据
	private List<AlbumListEntity.FolderList.Folder> mlist;
	private TextView allPhotoCountsTextView, albumCountsTextView;// 相片总数、相册总数
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private int allPhotoCounts = 0;// 相册总数
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myphotobook);

		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).build();
		mlist = new ArrayList<AlbumListEntity.FolderList.Folder>();
		initView();
		albumService = new AlbumService(this) {
			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

				aListEntity = albumService.getaListEntity();

				for (int i = 0; i < aListEntity.getFolderList().getFolder().length; i++) {
					mlist.add(aListEntity.getFolderList().getFolder()[i]);
					allPhotoCounts = allPhotoCounts
							+ mlist.get(i).getCountOfPhotos();
				}
				albumCountsTextView.setText(String.valueOf(mlist.size()));
				allPhotoCountsTextView.setText(String.valueOf(allPhotoCounts));
				mGridView.setVisibility(View.VISIBLE);

			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub

			}

			@Override
			public void unCollectAlbumSuccess(Folder folder) {
				// TODO Auto-generated method stub
				mlist.remove(folder);
				adapter.notifyDataSetChanged();
				super.unCollectAlbumSuccess(folder);
			}
		};
		// 获取相册列表
		albumService.getAlbumList();

	}

	/**
	 * 绑定事件
	 */

	private void initView() {

		allPhotoCountsTextView = (TextView) findViewById(R.id.allPhotoCountsTxt);
		albumCountsTextView = (TextView) findViewById(R.id.albumCountsTxt);
		albumCountsTextView.setText(String.valueOf(mlist.size()));
		allPhotoCountsTextView.setText(String.valueOf(allPhotoCounts));

		mGridView = (GridView) findViewById(R.id.gridView1);
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				View view = convertView;
				if (view == null) {
					view = getLayoutInflater().inflate(
							R.layout.item_tab2_myphotobook, null);
					holder = new ViewHolder();
					holder.photoCountsTextView = (TextView) view
							.findViewById(R.id.photoCountsTxt);
					holder.photoNameTextView = (TextView) view
							.findViewById(R.id.photoNameTxt);
					holder.mImageView = (ImageView) view.findViewById(R.id.img);
					view.setTag(holder);
				}
				holder = (ViewHolder) view.getTag();
				holder.photoNameTextView.setText(mlist.get(position).getName());
				holder.photoCountsTextView.setText(mlist.get(position)
						.getCountOfPhotos()
						+ getResources().getString(R.string.photo_number));
				mImageLoader.displayImage(ContantUtil.PICTURE_URL + Keys.S_VIEW
						+ mlist.get(position).getImgUrl(), holder.mImageView,
						mOptions);
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mlist.size();
			}
		};

		mGridView.setAdapter(adapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent().setClass(MyAlbumsBookActivity.this,
						TimeLineGridViewActivity.class);
				intent.putExtra(Keys.ALBUM_ID, mlist.get(position).getId());
				startActivity(intent);

			}
		});
		mGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						deleteAlbum(mlist.get(arg2).getId(), mlist.get(arg2));
						return true;
					}
				});
	}

	public static class ViewHolder {
		private TextView photoNameTextView, photoCountsTextView;// 相册名，单个相册图片数
		private ImageView mImageView;// 相册封面
	}

	private void deleteAlbum(final String id,
			final AlbumListEntity.FolderList.Folder folder) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.uncollect_album_success)
				.setPositiveButton(R.string.delete, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						albumService.unCollectAlbum(id, folder);
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
