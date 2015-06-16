package com.halong.aubaby.tab1;

import java.util.List;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotosGridViewAdapter extends BaseAdapter {
	/**
	 * 说说中的照片适配器
	 */
	private Context mContext;
	private List<VDiaryEntity.ObjInfo> mList;// 数据源
	private int mItem;// 从上层listview的第几个item传来的数据
	private String[] mImgUrls;// 日记携带图片的链接
	private int count;// 日记携带图片的个数
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private int itemHeight;// itmes高度
	private int bigItemHeight;// itmes高度
	private int middleItemHeight;// itmes高度

	@SuppressWarnings("deprecation")
	public PhotosGridViewAdapter(Context context, ImageLoader imageLoader,
			DisplayImageOptions options, List<VDiaryEntity.ObjInfo> list,
			int item) {
		mContext = context;
		mImageLoader = imageLoader;
		mOptions = options;
		mList = list;
		mItem = item;
		itemHeight = (((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth() - 8 * 4) / 3;
		bigItemHeight = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth() - 8 * 2;
		middleItemHeight = (((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth() - 8 * 3) / 2;
		try {
			mImgUrls = new String[mList.get(mItem).getContent()
					.getCountOfPics()];
			// 最多返回9个图片链接，返回的图片个数大于9时，设为显示9张图
			if (mImgUrls.length > 9) {
				count = 9;
			} else {
				count = mImgUrls.length;
			}
			for (int i = 0; i < count; i++) {
				switch (count) {
				case 1:// 如果路径以file:/开头，则是本地图片，其他的都是网络图片
					
					if ("file:/".equals(mList.get(mItem).getContent()
							.getPics().getPicList()[i].getUrl().substring(0,6))) {
						mImgUrls[i] = mList.get(mItem).getContent().getPics()
								.getPicList()[i].getUrl();
					} else {
						mImgUrls[i] = ContantUtil.PICTURE_URL
								+ Keys.L_VIEW
								+ mList.get(mItem).getContent().getPics()
										.getPicList()[i].getUrl();
					}
					break;

				default:
					// 如果路径以file:/开头，则是本地图片，其他的都是网络图片
					if ("file:/".equals(mList.get(mItem).getContent()
							.getPics().getPicList()[i].getUrl().substring(0,6))) {
						mImgUrls[i] = mList.get(mItem).getContent().getPics()
								.getPicList()[i].getUrl();
					} else {
						mImgUrls[i] = ContantUtil.PICTURE_URL
								+ Keys.S_VIEW
								+ mList.get(mItem).getContent().getPics()
										.getPicList()[i].getUrl();
					}
					break;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.include_images_in_photo_wall, null);
		}

		ImageView imageView = (ImageView) arg1.findViewById(R.id.photo);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		if (getCount() == 1) {
			layoutParams.height = bigItemHeight;

		} else if (getCount() == 2 || getCount() == 4) {
			layoutParams.height = middleItemHeight;
		} else {
			layoutParams.height = itemHeight;
		}
		imageView.setLayoutParams(layoutParams);
		mImageLoader.displayImage(mImgUrls[arg0], imageView, mOptions);
		return arg1;
	}

}
