package com.halong.aubaby.tab2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.entity.PersonalInformationEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PrivateImfoActivity extends BackActivity implements
		OnClickListener {

	private TextView mobileTxexView, userNameTextView, eMailTextView,
			QQTextView, accountStateTextView;
	private Button mCamala, mShowPic, mCancle;
	private ScrollView mScrollview1;
	private LinearLayout userNameLayout, emaiLayout, QQLayout;
	private LinearLayout mProgressLinearLayout, banjiLayout;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private List<View> viewList;
	private View translucentLayout;
	private final static int REQUESTCODE_CAMERA = 41;
	private final static int REQUESTCODE_PICTER = 42;
	private final static int REQUESTCODE_CROP = 43;
	private int relogin = 0;// 重新调用接口次数
	private static String classID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_info);

		initView();

		postPrivateInfo();
	}

	private void initView() {
		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.showImageOnFail(R.drawable.head).build();

		mScrollview1 = (ScrollView) findViewById(R.id.scrollview1);

		mCamala = (Button) findViewById(R.id.cameraBtn);
		mShowPic = (Button) findViewById(R.id.picsBtn);
		mCancle = (Button) findViewById(R.id.cancelBtn);

		mobileTxexView = (TextView) findViewById(R.id.mobileTxt);
		userNameTextView = (TextView) findViewById(R.id.userNameTXT);
		eMailTextView = (TextView) findViewById(R.id.emailTxt);
		QQTextView = (TextView) findViewById(R.id.QQTxt);
		accountStateTextView = (TextView) findViewById(R.id.accountStateTxt);

		userNameLayout = (LinearLayout) findViewById(R.id.userNameLayout);
		emaiLayout = (LinearLayout) findViewById(R.id.emailLayout);
		QQLayout = (LinearLayout) findViewById(R.id.QQLayout);

		mProgressLinearLayout = (LinearLayout) findViewById(R.id.progress);
		banjiLayout = (LinearLayout) findViewById(R.id.banjiLayout);

		translucentLayout = (View) findViewById(R.id.translucentLayout);
		mCamala.setOnClickListener(this);
		mShowPic.setOnClickListener(this);
		mCancle.setOnClickListener(this);
		userNameLayout.setOnClickListener(this);
		emaiLayout.setOnClickListener(this);
		QQLayout.setOnClickListener(this);
	}

	/**
	 * 设置个人资料
	 */
	private void postPrivateInfo() {
		// TODO Auto-generated method stub
		mProgressLinearLayout.setVisibility(View.VISIBLE);
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setCookieStore(new PersistentCookieStore(this));

		RequestParams params = new RequestParams();
		params.put("methodName", "0");
		asyncHttpClient.post(this, ContantUtil.USER_URL, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						try {
							JSONObject jsonObject = new JSONObject(content);
							if (jsonObject.getBoolean("result")) {
								final PersonalInformationEntity entity = new Gson()
										.fromJson(content,
												PersonalInformationEntity.class);
								mobileTxexView.setText(entity.getUser()
										.getMobile());
								userNameTextView.setText(entity.getUser()
										.getName());
								eMailTextView.setText(entity.getUser()
										.getMailbox());
								QQTextView.setText(entity.getUser().getQQ());
								if (entity.getUser().getStats().equals("1")) {
									accountStateTextView
											.setText(R.string.state_true);
								} else {
									accountStateTextView
											.setText(R.string.state_false);
								}
								viewList = new ArrayList<View>();
								for (int i = 0; i < entity.getBanjilist()
										.getBanji().length; i++) {
									final int item = i;
									LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
											LinearLayout.LayoutParams.MATCH_PARENT,
											LinearLayout.LayoutParams.WRAP_CONTENT);
									View banjiView = getLayoutInflater()
											.inflate(
													R.layout.include_banji_detail,
													null);
									viewList.add(banjiView);
									banjiLayout
											.addView(banjiView, layoutParams);
									ImageView headImageView = (ImageView) banjiView
											.findViewById(R.id.headImg);
									mImageLoader.displayImage(
											ContantUtil.PICTURE_URL
													+ entity.getBanjilist()
															.getBanji()[i]
															.getUserHeadPhoto(),
											headImageView, mOptions);
									headImageView
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													// TODO Auto-generated
													// method stub
													translucentLayout
															.setVisibility(View.VISIBLE);
													classID = entity
															.getBanjilist()
															.getBanji()[item]
															.getClassid();
												}
											});
									if (entity.getBanjilist().getBanji()[i]
											.getIsAdmin().equals("1")) {
										banjiView.findViewById(R.id.tecImg)
												.setVisibility(View.VISIBLE);
									} else {

									}
									TextView nickNameTextView = (TextView) banjiView
											.findViewById(R.id.nickNameTxt);
									nickNameTextView.setText(entity
											.getBanjilist().getBanji()[i]
											.getNickName());

									banjiView.findViewById(R.id.nickLyout)
											.setOnClickListener(
													new View.OnClickListener() {

														@Override
														public void onClick(
																View arg0) {
															// TODO
															// Auto-generated
															// method stub
															Intent intent = new Intent(
																	PrivateImfoActivity.this,
																	ModifyPrivateImfoActivity.class);
															intent.putExtra(
																	Keys.POST_INFO,
																	R.id.nickNameTxt);
															intent.putExtra(
																	Keys.BANJI_ID,
																	entity.getBanjilist()
																			.getBanji()[item]
																			.getClassid());
															intent.putExtra(
																	Keys.BANJI_ITEM,
																	item);
															startActivityForResult(
																	intent,
																	Keys.PRIVATE_INFO_ACTIVITY);
															overridePendingTransition(
																	R.anim.push_left_in,
																	R.anim.push_right_out);
														}
													});
									TextView nameTextView = (TextView) banjiView
											.findViewById(R.id.nameTxt);
									nameTextView.setText(entity.getBanjilist()
											.getBanji()[i].getMyClassName());

									TextView classRelationTextView = (TextView) banjiView
											.findViewById(R.id.classRelationTxt);
									classRelationTextView.setText(entity
											.getBanjilist().getBanji()[i]
											.getMyClassRelation());

									TextView schoolTextView = (TextView) banjiView
											.findViewById(R.id.schollTxt);
									schoolTextView.setText(entity
											.getBanjilist().getBanji()[i]
											.getSchoolName());

									TextView classTextView = (TextView) banjiView
											.findViewById(R.id.classTxt);
									classTextView.setText(entity.getBanjilist()
											.getBanji()[i].getClassname());

									TextView jifenTextView = (TextView) banjiView
											.findViewById(R.id.jifenTxt);
									jifenTextView.setText(entity.getBanjilist()
											.getBanji()[i].getPoint());

									TextView detailTextView = (TextView) banjiView
											.findViewById(R.id.detailTxt);
									detailTextView.setText(getString(R.string.publish)
											+ "："
											+ entity.getBanjilist().getBanji()[i]
													.getFabiao()
											+ "   "
											+ getString(R.string.zan)
											+ "："
											+ entity.getBanjilist().getBanji()[i]
													.getZan()
											+ "   "
											+ getString(R.string.pinglun)
											+ "："
											+ entity.getBanjilist().getBanji()[i]
													.getPinglun());

								}
							} else {
								Toast.makeText(PrivateImfoActivity.this,
										R.string.access_error,
										Toast.LENGTH_SHORT).show();
								mScrollview1.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							Toast.makeText(PrivateImfoActivity.this,
									getString(R.string.error),
									Toast.LENGTH_SHORT).show();
							mScrollview1.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						mScrollview1.setVisibility(View.GONE);
						Toast.makeText(PrivateImfoActivity.this,
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						mProgressLinearLayout.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.userNameLayout:
			Intent intent1 = new Intent(this, ModifyPrivateImfoActivity.class);
			intent1.putExtra(Keys.POST_INFO, Keys.POST_USER_NAME);
			startActivityForResult(intent1, Keys.PRIVATE_INFO_ACTIVITY);
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
			break;
		case R.id.emailLayout:
			Intent intent2 = new Intent(this, ModifyPrivateImfoActivity.class);
			intent2.putExtra(Keys.POST_INFO, Keys.POST_EMAIL);
			startActivityForResult(intent2, Keys.PRIVATE_INFO_ACTIVITY);
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
			break;
		case R.id.QQLayout:
			Intent intent3 = new Intent(this, ModifyPrivateImfoActivity.class);
			intent3.putExtra(Keys.POST_INFO, Keys.POST_QQ);
			startActivityForResult(intent3, Keys.PRIVATE_INFO_ACTIVITY);
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
			break;
		case R.id.cancelBtn:
			translucentLayout.setVisibility(View.GONE);
			break;
		case R.id.picsBtn:
			Intent intent4 = new Intent(Intent.ACTION_PICK, null);

			/*
			 * 下面这句话，与其它方式写是一样的效果，如：
			 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			 * intent.setType(""image/*");设置数据类型
			 * 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
			 */
			intent4.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent4, REQUESTCODE_PICTER);
			translucentLayout.setVisibility(View.GONE);
			break;
		case R.id.cameraBtn:
			Intent intent5 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			intent5.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory() + Keys.AUBABY,
					"head.jpg")));
			startActivityForResult(intent5, REQUESTCODE_CAMERA);
			translucentLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Keys.PRIVATE_INFO_ACTIVITY
				&& resultCode == Keys.MODIFY_PRIVATE_INFO_ACTIVITY) {
			switch (data.getIntExtra(Keys.POST_INFO, 0)) {

			case Keys.POST_EMAIL:
				eMailTextView.setText(data
						.getStringExtra(Keys.RETURN_POST_INFO));
				break;
			case Keys.POST_QQ:
				QQTextView.setText(data.getStringExtra(Keys.RETURN_POST_INFO));
				break;
			case Keys.POST_USER_NAME:
				userNameTextView.setText(data
						.getStringExtra(Keys.RETURN_POST_INFO));
				break;
			case R.id.nickNameTxt:
				int item = data.getIntExtra(Keys.BANJI_ITEM, -1);
				if (item >= 0) {
					((TextView) viewList.get(item).findViewById(
							R.id.nickNameTxt)).setText(data
							.getStringExtra(Keys.RETURN_POST_INFO));
				}
				break;

			default:
				return;
			}
		}

		if (requestCode == REQUESTCODE_CAMERA) {
			Toast.makeText(getApplicationContext(), "cammer",
					Toast.LENGTH_SHORT).show();
			File temp = new File(Environment.getExternalStorageDirectory()
					+ Keys.AUBABY, "head.jpg");
			startPhotoZoom(Uri.fromFile(temp));
		}
		if (requestCode == REQUESTCODE_PICTER) {
			Toast.makeText(getApplicationContext(), "pic", Toast.LENGTH_SHORT)
					.show();
			startPhotoZoom(data.getData());
		}
		if (requestCode == REQUESTCODE_CROP) {
			if (data != null) {
				setPicToView(data);
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 调用Android自带的裁剪图片控件
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, REQUESTCODE_CROP);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			try {
				Bitmap photoBitmap = extras.getParcelable("data");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				byte[] buffer = out.toByteArray();

				byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
				String photo = new String(encode);
				postSave(photo);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	/**
	 * 提交新头像
	 */

	private void postSave(final String pic) {
		// // TODO Auto-generated method stub
		mProgressLinearLayout.setVisibility(View.VISIBLE);
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setCookieStore(new PersistentCookieStore(this));
		RequestParams params = new RequestParams();
		params.put("methodName", "7");
		params.put("a", classID);
		params.put("file", pic);
		asyncHttpClient.post(this, ContantUtil.USER_URL, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						try {
							JSONObject jsonObject = new JSONObject(content);

							if (jsonObject.getBoolean("result")) {
								Toast.makeText(getApplicationContext(), R.string.refresh_success,
										Toast.LENGTH_SHORT).show();
							} else {
								// 返回错误原因,重新调用次数小于5次时重新调用
								if (relogin < 5) {
									// 返回错误原因
									new ReturnValue() {

										@Override
										public void onReloginSuccess() {
											// TODO Auto-generated method stub
											// session已过期或不存在,后台登陆后重新获取数据
											postSave(pic);
										}

										public void whileFailure() {
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.post_error),
													Toast.LENGTH_SHORT).show();
										};
									}.resultFalse(getApplicationContext(),
											content);
								} else {
									Toast.makeText(getApplicationContext(),
											getString(R.string.post_error),
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									getString(R.string.error),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Toast.makeText(getApplicationContext(),
								getString(R.string.post_error),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						mProgressLinearLayout.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (translucentLayout.getVisibility() == View.VISIBLE) {
				translucentLayout.setVisibility(View.GONE);
				return true;
			} else {
				finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_right_out);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
