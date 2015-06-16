package com.halong.aubaby.tab3;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.photosalbum.ImgCallBack;
import com.halong.aubaby.photosalbum.Util;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;

public class PublishActivity extends BackActivity {
	private ArrayList<String> listfile = new ArrayList<String>();// 选中的图片路径
	private LinearLayout layout;// 预览图布局
	private Util util;
	private ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	private Bundle listfileBundle;// 选中的图片数据
	private ArrayList<String> addPhotosList;// 新增的图片
	private EditText contentEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		// 选择本地图片最多可以选择几张
		SharedPreferencesHelper.setIntValue(this, Keys.MAST_PHOTO, 9);
		//允许拍摄视频
		SharedPreferencesHelper.setStringValue(this, Keys.IS_OPEN_VEDIO, Keys.VEDIO_OPENED);
		contentEditText = (EditText) findViewById(R.id.contentEdtTxt);

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (listfile.size() == 0) {
							Toast.makeText(getApplicationContext(),
									R.string.selected_photos_please,
									Toast.LENGTH_SHORT).show();
						} else if (listfile.size() > 9) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.reduce_photos)
											+ 9
											+ getResources().getString(
													R.string.zhang),
									Toast.LENGTH_SHORT).show();
						} else {
							listfileBundle = new Bundle();
							listfileBundle.putString(
									Keys.PUBLISH_DIARY_CONTENT, contentEditText
											.getText().toString().trim());
							listfileBundle.putStringArrayList(
									Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY,
									listfile);
							Intent intent = new Intent(
									Keys.PUBLISH_ACTIVITY_ACTION);
							intent.putExtras(listfileBundle);

							// 发送广播
							sendBroadcast(intent);
							finish();
						}

					}
				});
		Bundle bundle = getIntent().getExtras();
		// 接收选中的图片路径
		if (bundle != null) {
			if (bundle.getStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY) != null) {
				listfile = bundle
						.getStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY);
			}
		}
		layout = (LinearLayout) findViewById(R.id.preview);
		util = new Util(this);
		// 将选中的图片进行预览。在最后添加一个增加图片按钮
		for (int i = 0; i < listfile.size() + 1; i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					100, 100);

			if (i < listfile.size()) {
				params.setMargins(0, 0, 10, 0);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				util.imgExcute(imageView, imgCallBack, listfile.get(i));
			} else {
				// 继续添加图片
				params.setMargins(0, 0, 0, 0);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setImageResource(R.drawable.btn46_selector);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(PublishActivity.this,
								GetPicOrVideo.class);
						startActivityForResult(intent, Keys.PUBLISH_ACTIVIY);
					}
				});
			}

			imageView.setLayoutParams(params);
			layout.addView(imageView);
		}
	}

	// 接收新增的图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Keys.CLOSE_ACTIVITY) {
			Bundle bundle = data.getExtras();
			addPhotosList = new ArrayList<String>();
			addPhotosList = bundle
					.getStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY);
			listfile.addAll(addPhotosList);
			for (int i = 0; i < addPhotosList.size(); i++) {
				final ImageView imageView = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						100, 100);

				params.setMargins(0, 0, 10, 0);
				util.imgExcute(imageView, imgCallBack, addPhotosList.get(i));
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageView.setLayoutParams(params);
				layout.addView(imageView, layout.getChildCount() - 1);
				// 点击预览图可删除
				final int postion = i;
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						layout.removeView(imageView);
						listfile.remove(listfile.size() - addPhotosList.size()
								+ postion);

					}
				});
				// 长按查看大图
				final int path = i;
				imageView
						.setOnLongClickListener(new View.OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent().setClass(
										PublishActivity.this,
										PublishPhotoShowActivity.class);
								intent.putExtra(Keys.PUBLISH_PHOTO_PATH,
										addPhotosList.get(path));
								startActivity(intent);
								return false;
							}
						});

			}

			findViewById(R.id.noticeTextView).setVisibility(View.VISIBLE);
		}
	}
}
