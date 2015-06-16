package com.halong.aubaby.tab5;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.photosalbum.ImgCallBack;
import com.halong.aubaby.photosalbum.Util;
import com.halong.aubaby.tab3.GetPicOrVideo;
import com.halong.aubaby.tab3.PublishPhotoShowActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.PublishService;
import com.halong.aubaby.R;

public class PublishNoticeActivity extends BackActivity implements
		OnClickListener {

	private Context mContext;

	private Button mPublishButton, receiversButton;
	private EditText mNoticeEditText;
	private CheckBox mReplyCheckBox;
	private TextView receiversTextView;
	private ImageButton mAddImageButton;
	private ImageView imageView;
	private ArrayList<String> listfile = new ArrayList<String>();// 选中的图片路径
	private Util util;
	private ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	private ArrayList<String> addPhotosList;// 新增的图片

	private PublishService publishService;
	private View progress;
	private String publishContent;// 要发布的内容
	private String receiverID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab5_publish);
		// 选择本地图片最多可以选择几张
		SharedPreferencesHelper.setIntValue(this, Keys.MAST_PHOTO, 1);
		//不允许拍摄视频
		SharedPreferencesHelper.setStringValue(this, Keys.IS_OPEN_VEDIO, Keys.VEDIO_CLOSED);
		mContext = this;
		initview();

	}

	/**
	 * 初始化view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		progress = (View) findViewById(R.id.progress);
		receiversTextView = (TextView) findViewById(R.id.receiverTxt);
		mPublishButton = (Button) findViewById(R.id.publishButton);
		mPublishButton.setOnClickListener(this);

		receiversButton = (Button) findViewById(R.id.receiversBtn);
		receiversButton.setOnClickListener(this);
		mNoticeEditText = (EditText) findViewById(R.id.noticeEditText);

		mReplyCheckBox = (CheckBox) findViewById(R.id.replyCheckBox);

		util = new Util(this);
		mAddImageButton = (ImageButton) findViewById(R.id.addImageButton);
		mAddImageButton.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.image);
		// 发布通知线程
		publishService = new PublishService(mContext) {
			@Override
			public void publishNoticeSuccess() {
				// TODO Auto-generated method stub
				super.publishNoticeSuccess();
				finish();
			}

			@Override
			public void publishNoticeFailure() {
				// TODO Auto-generated method stub
				super.publishNoticeFailure();
				progress.setVisibility(View.GONE);
				mPublishButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				super.getBBSpaceData(content);
				// 图片上传成功后发布通知
				int e = 0;
				if (mReplyCheckBox.isChecked()) {
					e += 2;
				}
				publishService.pblishNotice(receiverID, publishContent, e,
						content);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			
				progress.setVisibility(View.GONE);
				mPublishButton.setVisibility(View.VISIBLE);
				super.getBBSpaceDataFailure();
				Toast.makeText(mContext, R.string.photo_upload_failure,
						Toast.LENGTH_SHORT).show();
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 发表通知
		case R.id.publishButton:
			postPublish();
			break;
		case R.id.addImageButton:
			// 选择要上传的图片
			Intent intent = new Intent();
			intent.setClass(PublishNoticeActivity.this, GetPicOrVideo.class);
			startActivityForResult(intent, Keys.PUBLISH_ACTIVIY);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.receiversBtn:
			Intent receiversIntent = new Intent();
			receiversIntent.setClass(PublishNoticeActivity.this,
					ReceiversActivity.class);
			startActivityForResult(receiversIntent,
					Keys.PUBLISH_NOTICE_ACTIVITY);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		default:
			break;
		}
	}

	private void postPublish() {
		// TODO Auto-generated method stub
		// 发布内容不得为空
		publishContent = mNoticeEditText.getText().toString().trim();
		if (publishContent == null || publishContent.length() <= 0) {
			Toast.makeText(mContext, R.string.comment_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// 是否允许评论
		int e = 0;
		if (mReplyCheckBox.isChecked()) {
			e += 2;
		}
		// 发布通知（是否带图片）
		if (listfile.size() == 0) {

			publishService.pblishNotice(receiverID, publishContent, e, "");
		} else {
			publishService.publishNoticePhotos(listfile.get(0));
		}
		// 加载loading状态
		progress.setVisibility(View.VISIBLE);
		mPublishButton.setVisibility(View.GONE);
	}

	// 接收新增的图片,只允许上传一张
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Keys.CLOSE_ACTIVITY) {
			if (listfile.size() > 0) {
				Toast.makeText(
						mContext,
						getResources().getString(R.string.reduce_photos) + 1
								+ getResources().getString(R.string.zhang),
						Toast.LENGTH_SHORT).show();
				listfile.clear();
			}
			Bundle bundle = data.getExtras();
			addPhotosList = new ArrayList<String>();
			addPhotosList = bundle
					.getStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY);
			if (addPhotosList.size() > 1) {
				Toast.makeText(
						mContext,
						getResources().getString(R.string.reduce_photos) + 1
								+ getResources().getString(R.string.zhang),
						Toast.LENGTH_SHORT).show();
			}
			listfile.addAll(addPhotosList);
			util.imgExcute(imageView, imgCallBack, addPhotosList.get(0));
			imageView.setVisibility(View.VISIBLE);
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					listfile.clear();
					imageView.setVisibility(View.GONE);

				}
			});
			imageView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent().setClass(
							PublishNoticeActivity.this,
							PublishPhotoShowActivity.class);
					intent.putExtra(Keys.PUBLISH_PHOTO_PATH,
							addPhotosList.get(0));
					startActivity(intent);
					return false;
				}
			});

			findViewById(R.id.noticeTextView).setVisibility(View.VISIBLE);
		} else if (resultCode == Keys.RECEIVERS_ACTIVITY
				&& requestCode == Keys.PUBLISH_NOTICE_ACTIVITY) {
			if (data.getExtras() == null) {
				return;
			}
			receiverID = data.getStringExtra(Keys.RECEIVERS_ID);
			String receiverName = data.getStringExtra(Keys.RECEIVERS_NAME);
			receiversTextView.setText(receiverName);
		}
	}
}
