package com.halong.aubaby.tab1;

import java.util.ArrayList;
import java.util.HashMap;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.DiaryCommentsEntity;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.entity.VDiaryEntity.ObjInfo.Content.Comments.CommentList;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.wcf.AlbumService;
import com.halong.aubaby.wcf.CommentService;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.wcf.PublishService;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryAdapter extends BaseAdapter implements View.OnClickListener {
	private Context mContext;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private DisplayImageOptions headOptions;
	private Boolean mUserInfoDetail;// 是否可以查看用户详情
	private AlbumService albumService;
	private DiaryService diaryService;// 获取日记列表
	private DiaryService deleteDiaryService;// 删除日记方法
	private PublishService publishService;// 发表日记的方法
	private ArrayList<VDiaryEntity.ObjInfo> mDiaryList;// 数据
	private ArrayList<String> mDiaryStateList;// 执行getview方法时，日记上传的状态
	private ArrayList<VDiaryEntity.ObjInfo> publishList;// 要发表的日记
	private VDiaryEntity vEntity;// 发表成功后刷新日记数据
	private static final String DIARY_UPLOAD = "upload";// 日记上传当中
	private static final String DIARY_FAILURE = "failure";// 开启日记上传服务
	private static final String DIARY_SUCCESS = "success";// 日记上传成功
	private static final String NO_UPLOAD = "noUpload";// 不需要上传

	public DiaryAdapter(Context context, ImageLoader imageLoader,
			DisplayImageOptions options, Boolean userInfoDetail) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.imageLoader = imageLoader;
		this.options = options;
		this.headOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.head).cacheInMemory(true)
				.cacheOnDisc(true).build();
		this.mUserInfoDetail = userInfoDetail;
		this.mDiaryStateList = new ArrayList<String>();
		this.mDiaryList = new ArrayList<VDiaryEntity.ObjInfo>();
		this.publishList = new ArrayList<VDiaryEntity.ObjInfo>();
		this.albumService = new AlbumService(mContext) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

			}
		};

		this.diaryService = new DiaryService(mContext) {
			@Override
			public void onRefreshSuccsess() {
				// TODO Auto-generated method stub
				vEntity = diaryService.getvEntity();
				if (vEntity != null) {
					// 添加从服务器获取的日记
					for (int i = vEntity.getObjInfo().length - 1; i >= 0; i--) {
						addNetDiaryTopPosition(vEntity.getObjInfo()[i]);
						DiaryAdapter.this.notifyDataSetChanged();
					}

				}
				super.onRefreshSuccsess();
			}

			// 加载本地或离线内容
			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
			}
		};
		registerBoradcastReceiver();
	}

	public ArrayList<VDiaryEntity.ObjInfo> getPublishList() {
		return publishList;
	}

	public HashMap<String, Object> getmDiaryList() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Keys.DIARY, this.mDiaryList);
		map.put(Keys.DIARYSTATE, this.mDiaryStateList);
		return map;

	}

	public ArrayList<VDiaryEntity.ObjInfo> getALLMDiaryList() {

		return mDiaryList;

	}

	public void clearDiaryList() {
		mDiaryList.clear();
		publishList.clear();
		mDiaryStateList.clear();
	}

	public VDiaryEntity.ObjInfo getFirstNetDiary() {
		if (mDiaryList.size() > publishList.size()) {
			return mDiaryList.get(publishList.size());
		} else {
			return null;
		}
	}

	public VDiaryEntity.ObjInfo getLastNetDiary() {
		if (mDiaryList.size() > publishList.size()) {
			return mDiaryList.get(mDiaryList.size() - 1);
		} else {
			return null;
		}
	}

	public void addNetDiaryListAtTopPosition(
			ArrayList<VDiaryEntity.ObjInfo> list) {
		// TODO Auto-generated method stub
		// 从网络获取的日记要添加在发表中的日记后边
		for (int i = 0; i < list.size(); i++) {
			this.mDiaryStateList.add(this.publishList.size(), NO_UPLOAD);
		}
		this.mDiaryList.addAll(this.publishList.size(), list);
	}

	public void addNetDiaryListAtLastPosition(
			ArrayList<VDiaryEntity.ObjInfo> list) {
		// TODO Auto-generated method stub
		// 从网络获取的日记添加到最后边
		for (int i = 0; i < list.size(); i++) {
			this.mDiaryStateList.add(this.mDiaryList.size(), NO_UPLOAD);
		}
		this.mDiaryList.addAll(this.mDiaryList.size(), list);
	}

	public void addNetDiaryTopPosition(VDiaryEntity.ObjInfo mDiary) {
		// TODO Auto-generated method stub
		this.mDiaryList.add(publishList.size(), mDiary);
		this.mDiaryStateList.add(publishList.size(), NO_UPLOAD);
	}

	public void removeDiary(VDiaryEntity.ObjInfo mDiary) {
		// TODO Auto-generated method stub
		int diaryPosition = mDiaryList.lastIndexOf(mDiary);
		if (diaryPosition > -1) {
			this.mDiaryStateList.remove(diaryPosition);
			this.mDiaryList.remove(mDiary);
			this.publishList.remove(mDiary);
		}
	}

	public void setPublishDiaryState(VDiaryEntity.ObjInfo mDiary,
			String diaryState) {
		// TODO Auto-generated method stub
		int diaryPosition = mDiaryList.lastIndexOf(mDiary);
		if (diaryPosition > -1 && diaryPosition + 1 <= publishList.size()) {
			this.mDiaryStateList.set(diaryPosition, diaryState);
		}

	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.include_items_listview_diarys, null);

			holder = new ViewHolder();
			holder.mGridView = (com.halong.aubaby.widget.NoScrollGridView) view
					.findViewById(R.id.photosGridView);
			holder.shareButton = (Button) view.findViewById(R.id.shareBtn);
			holder.mheadImageButton = (ImageButton) view
					.findViewById(R.id.headImg);
			holder.mUserNameTextView = (TextView) view
					.findViewById(R.id.userNameTXT);
			holder.mPostedTextView = (TextView) view
					.findViewById(R.id.postedTxt);
			holder.mZanButton = (Button) view.findViewById(R.id.zanBtn);
			holder.mPingLunButton = (Button) view.findViewById(R.id.pinglunBtn);
			holder.mDiaryNeiRongTextView = (TextView) view
					.findViewById(R.id.diaryNeiRongTxt);
			holder.collectBtn = (Button) view.findViewById(R.id.collectBtn);
			holder.deleteButton = (Button) view.findViewById(R.id.deleteBtn);
			holder.progress = (ProgressBar) view.findViewById(R.id.progress);
			holder.retryButton = (Button) view.findViewById(R.id.retryBtn);

			holder.commentRelLayout = (RelativeLayout) view
					.findViewById(R.id.commentRelLayout);
			holder.commentImg = (ImageButton) view
					.findViewById(R.id.commentImg);
			holder.commentNameTXT = (TextView) view
					.findViewById(R.id.commentNameTXT);
			holder.commentPostedTxt = (TextView) view
					.findViewById(R.id.commentPostedTxt);
			holder.commentNeiRongTxt = (TextView) view
					.findViewById(R.id.commentNeiRongTxt);

			holder.commentRelLayout2 = (RelativeLayout) view
					.findViewById(R.id.commentRelLayout2);
			holder.commentImg2 = (ImageButton) view
					.findViewById(R.id.commentImg2);
			holder.commentNameTXT2 = (TextView) view
					.findViewById(R.id.commentNameTXT2);
			holder.commentPostedTxt2 = (TextView) view
					.findViewById(R.id.commentPostedTxt2);
			holder.commentNeiRongTxt2 = (TextView) view
					.findViewById(R.id.commentNeiRongTxt2);

			holder.commentRelLayout3 = (RelativeLayout) view
					.findViewById(R.id.commentRelLayout3);
			holder.commentImg3 = (ImageButton) view
					.findViewById(R.id.commentImg3);
			holder.commentNameTXT3 = (TextView) view
					.findViewById(R.id.commentNameTXT3);
			holder.commentPostedTxt3 = (TextView) view
					.findViewById(R.id.commentPostedTxt3);
			holder.commentNeiRongTxt3 = (TextView) view
					.findViewById(R.id.commentNeiRongTxt3);
			holder.teacherImageView = (ImageView) view
					.findViewById(R.id.tecImg);
			holder.commentImageView = (ImageView) view
					.findViewById(R.id.commentTecImg);
			holder.commentImageView2 = (ImageView) view
					.findViewById(R.id.commentTecImg2);
			holder.commentImageView3 = (ImageView) view
					.findViewById(R.id.commentTecImg3);
			view.setTag(holder);
		} else {

			holder = (ViewHolder) view.getTag();
		}
		if (mDiaryList.get(arg0).getUser().getIsAdmin().equals("1")) {
			holder.teacherImageView.setVisibility(View.VISIBLE);
		} else {
			holder.teacherImageView.setVisibility(View.GONE);
		}
		if (mDiaryList.get(arg0).getContent().getCountOfPics() == 1) {
			// 日记携带的图片是几列
			holder.mGridView.setNumColumns(1);

		} else if (mDiaryList.get(arg0).getContent().getCountOfPics() == 2
				|| mDiaryList.get(arg0).getContent().getCountOfPics() == 4) {
			holder.mGridView.setNumColumns(2);
		} else {
			holder.mGridView.setNumColumns(3);
		}
		holder.mheadImageButton.setImageResource(R.drawable.head);
		imageLoader.displayImage(ContantUtil.PICTURE_URL
				+ mDiaryList.get(arg0).getUser().getImg(),
				holder.mheadImageButton, headOptions);

		holder.mUserNameTextView.setText(mDiaryList.get(arg0).getUser()
				.getName());
		holder.mPostedTextView.setText(mDiaryList.get(arg0).getContent()
				.getDate());
		holder.mZanButton.setText(mContext.getResources().getString(
				R.string.zan)
				+ " " + mDiaryList.get(arg0).getContent().getZan());
		holder.mPingLunButton.setText(mContext.getResources().getString(
				R.string.pinglun)
				+ " " + mDiaryList.get(arg0).getContent().getPinglun());
		holder.mDiaryNeiRongTextView.setText(mDiaryList.get(arg0).getContent()
				.getTitle());

		holder.shareButton.setOnClickListener(this);
		if (mUserInfoDetail) {
			holder.mheadImageButton
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent().setClass(mContext,
									OtherUserInfoActivity.class);
							intent.putExtra(Keys.USER_INFO_ID,
									mDiaryList.get(arg0).getUser().getCode());
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
		}

		final ImageView collectImageView = (ImageView) view
				.findViewById(R.id.collectDiaryImg);
		holder.collectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scaleAnimation(collectImageView);
				albumService.collectAlbum(mDiaryList.get(arg0).getContent()
						.getId());
			}
		});
		// 如果该日记是自己发布的，收藏按钮隐藏
		if (TextUtils.equals(mDiaryList.get(arg0).getUser().getCode(), String
				.valueOf(SharedPreferencesHelper.getIntValue(mContext,
						SharedPreferencesHelper.USERID)))) {
			holder.collectBtn.setVisibility(View.GONE);
		} else {
			holder.collectBtn.setVisibility(View.VISIBLE);
		}
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage(R.string.delete_this_diary)
						.setPositiveButton(R.string.delete,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										deleteDiary(mDiaryList.get(arg0)
												.getContent().getId(),
												mDiaryList.get(arg0));
									}
								})
						.setNegativeButton(R.string.cancel,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).show();

			}
		});

		holder.mAdapter = new PhotosGridViewAdapter(mContext, imageLoader,
				options, mDiaryList, arg0);
		holder.mGridView.setAdapter(holder.mAdapter);

		// 如果是待发布日记，则不允许查看图片详情、删除日记。以及后台发布日记
		if (mDiaryList.get(arg0).getContent().getId().equals(Keys.LOCAL_DATA)) {
			holder.mPostedTextView.setVisibility(View.GONE);
			if (mDiaryStateList.get(arg0).equals(DIARY_UPLOAD)) {

				holder.retryButton.setVisibility(View.GONE);
				holder.progress.setVisibility(View.VISIBLE);
				holder.deleteButton.setVisibility(View.GONE);
			} else if (mDiaryStateList.get(arg0).equals(DIARY_FAILURE)) {
				holder.deleteButton.setVisibility(View.VISIBLE);
				holder.retryButton.setVisibility(View.VISIBLE);
				holder.progress.setVisibility(View.GONE);
			} else if (mDiaryStateList.get(arg0).equals(DIARY_SUCCESS)) {
				holder.retryButton.setVisibility(View.GONE);
				holder.progress.setVisibility(View.GONE);
				holder.deleteButton.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(mContext,
						arg0 + "数据错乱" + mDiaryStateList.size(),
						Toast.LENGTH_SHORT).show();
				holder.progress.setVisibility(View.GONE);
				holder.retryButton.setVisibility(View.GONE);
				holder.mPostedTextView.setVisibility(View.GONE);
				holder.deleteButton.setVisibility(View.GONE);
			}
			holder.mGridView.setOnItemClickListener(null);
			holder.mZanButton.setOnClickListener(null);
			holder.mPingLunButton.setOnClickListener(null);
		} else {
			holder.deleteButton.setVisibility(View.VISIBLE);
			holder.mPostedTextView.setVisibility(View.VISIBLE);
			holder.progress.setVisibility(View.GONE);
			holder.retryButton.setVisibility(View.GONE);
			holder.mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							ShuoShuoDetailActivity.class);
					Bundle bundle = new Bundle();
					// 将数据传递过去
					bundle.putString(Keys.DIARY_ID, mDiaryList.get(arg0)
							.getContent().getId());
					bundle.putString(Keys.USER_INFO_ID, mDiaryList.get(arg0)
							.getUser().getCode());
					bundle.putInt(Keys.CLICK_GRIDVIEW_ITEM, position);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					((Activity) mContext).overridePendingTransition(
							R.anim.push_right_in, R.anim.push_left_out);
				}
			});
			final ImageView zanImageView = (ImageView) view
					.findViewById(R.id.praiseDiaryImg);
			holder.mZanButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					scaleAnimation(zanImageView);

					CommentService commentService = new CommentService(mContext) {
						@Override
						public void praiseDiaryOrPhotoSuccess(
								String praiseNumber) {
							// TODO Auto-generated method stub
							mDiaryList.get(arg0).getContent()
									.setZan(praiseNumber);
							DiaryAdapter.this.notifyDataSetChanged();

						}
					};
					commentService.praiseDiary(mDiaryList.get(arg0)
							.getContent().getId());
				}
			});

			holder.mPingLunButton
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(mContext,
									ShuoShuoDetailActivity.class);
							Bundle bundle = new Bundle();
							// 将数据传递过去
							bundle.putString(Keys.DIARY_ID, mDiaryList
									.get(arg0).getContent().getId());
							bundle.putString(Keys.USER_INFO_ID,
									mDiaryList.get(arg0).getUser().getCode());
							bundle.putInt(Keys.FRAGMENT_PAGE,
									Keys.COMMENT_FRAGMENT);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
		}

		holder.retryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VDiaryEntity.ObjInfo deleteObject = mDiaryList.get(arg0);
				deleteDiary(deleteObject.getContent().getId(), deleteObject);
				publishDiary(deleteObject);
				DiaryAdapter.this.notifyDataSetChanged();

			}
		});

		RelativeLayout[] commentLayouts = { holder.commentRelLayout,
				holder.commentRelLayout2, holder.commentRelLayout3 };
		ImageButton[] commentImgsButtons = { holder.commentImg,
				holder.commentImg2, holder.commentImg3 };
		ImageView[] commentImageViews = { holder.commentImageView,
				holder.commentImageView2, holder.commentImageView3 };
		TextView[] commentPostedTxts = { holder.commentPostedTxt,
				holder.commentPostedTxt2, holder.commentPostedTxt3, };
		TextView[] commentNameTXTs = { holder.commentNameTXT,
				holder.commentNameTXT2, holder.commentNameTXT3 };
		TextView[] commentNeiRongTxts = { holder.commentNeiRongTxt,
				holder.commentNeiRongTxt2, holder.commentNeiRongTxt3 };
		// 加载评论区内容及功能
		for (int i = 0; i < 3; i++) {
			commentLayouts[i].setVisibility(View.GONE);
		}
		if (mDiaryList.get(arg0).getContent().getComments() != null
				&& !mDiaryList.get(arg0).getContent().getId()
						.equals(Keys.LOCAL_DATA)) {
			for (int i = 0; i < mDiaryList.get(arg0).getContent().getComments()
					.getCommentList().length; i++) {

				// 显示有评论的内容
				commentLayouts[i].setVisibility(View.VISIBLE);
				commentImgsButtons[i].setImageResource(R.drawable.head);
				imageLoader.displayImage(ContantUtil.PICTURE_URL
						+ mDiaryList.get(arg0).getContent().getComments()
								.getCommentList()[i].getUserHeadPhotoURL(),
						commentImgsButtons[i], headOptions);

				final int commentUser = i;
				commentImgsButtons[i]
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View view) {
								// TODO Auto-generated method stub
								Intent intent = new Intent().setClass(mContext,
										OtherUserInfoActivity.class);
								intent.putExtra(Keys.USER_INFO_ID, mDiaryList
										.get(arg0).getContent().getComments()
										.getCommentList()[commentUser]
										.getUserid());
								mContext.startActivity(intent);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.push_right_in,
												R.anim.push_left_out);
							}
						});
				if (mDiaryList.get(arg0).getContent().getComments()
						.getCommentList()[i].getIsAdmin().equals("1")) {
					commentImageViews[i].setVisibility(View.VISIBLE);

				} else {
					commentImageViews[i].setVisibility(View.GONE);
				}

				commentNameTXTs[i].setText(mDiaryList.get(arg0).getContent()
						.getComments().getCommentList()[i].getUsername());
				commentPostedTxts[i]
						.setText(mDiaryList.get(arg0).getContent()
								.getComments().getCommentList()[i]
								.getCommentDatetime());
				commentNeiRongTxts[i].setText(mDiaryList.get(arg0).getContent()
						.getComments().getCommentList()[i].getCommentContent());

			}
		}

		return view;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDiaryList.size();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shareBtn:
			Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 删除日记
	 */
	private void deleteDiary(String diaryID,
			final VDiaryEntity.ObjInfo deleteObject) {
		// TODO Auto-generated method stub
		if (diaryID.equals(Keys.LOCAL_DATA)) {
			removeDiary(deleteObject);
			this.notifyDataSetChanged();

		} else {
			deleteDiaryService = new DiaryService(mContext) {

				@Override
				public void getBBSpaceData() {
					// TODO Auto-generated method stub

					Toast.makeText(mContext, R.string.delete_diary_success,
							Toast.LENGTH_SHORT).show();
					removeDiary(deleteObject);
					DiaryAdapter.this.notifyDataSetChanged();

				}

				@Override
				public void getBBSpaceDataFailure() {
				}
			};
			deleteDiaryService.deleteDiary(diaryID);
		}

	}

	/*
	 * 
	 * 发送日记，先将图片传到服务器返回guid，然后将日记内容和guid发送到服务器
	 */
	public void publishDiary(final VDiaryEntity.ObjInfo object) {
		// TODO Auto-generated method stub
		// 刷新界面
		mDiaryStateList.add(0, DIARY_UPLOAD);
		mDiaryList.add(0, object);
		publishList.add(0, object);
		this.notifyDataSetChanged();

		// 要上传的图片地址 ,在adapter中显示的图片路径中有file:/,上传时需去掉
		final ArrayList<String> urlList = new ArrayList<String>();
		for (int i = 0; i < object.getContent().getPics().getPicList().length; i++) {
			urlList.add(object.getContent().getPics().getPicList()[i].getUrl()
					.substring(6));
		}
		// 后台发布图片
		final ArrayList<String> guid = new ArrayList<String>();
		publishService = new PublishService(mContext) {

			@Override
			public void publishDiarySuccess() {
				// TODO Auto-generated method stub
				super.publishDiarySuccess();

				Toast.makeText(mContext, R.string.publish_diary_success,
						Toast.LENGTH_SHORT).show();
				setPublishDiaryState(object, DIARY_SUCCESS);
				removeDiary(object);
				DiaryAdapter.this.notifyDataSetChanged();
				if (mDiaryList.size() > publishList.size()) {
					diaryService.getDiaryV(mDiaryList.get(publishList.size())
							.getContent().getId(), null, Keys.ON_REFRESH,
							Keys.DIARY_P_Type);
				} else {
					diaryService.getDiaryV(null, null, Keys.ON_REFRESH,
							Keys.DIARY_P_Type);
				}

			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.publish_diary_failure,
						Toast.LENGTH_SHORT).show();

				setPublishDiaryState(object, DIARY_FAILURE);
				DiaryAdapter.this.notifyDataSetChanged();

			}

			@Override
			public void getBBSpaceData(String guidString) {
				// TODO Auto-generated method stub
				guid.add(guidString);
				if (guid.size() < urlList.size()) {
					// 发送下一张图片
					publishService.publishPhotos(urlList.get(guid.size()));
				} else {
					String guids = "";
					for (int j = 0; j < guid.size(); j++) {
						guids = guids + "," + guid.get(j) + ",";
					}
					// 发送日记
					publishService.publishDiary(object.getContent().getTitle(),
							guids);
				}

			}
		};
		// 发送图片
		publishService.publishPhotos(urlList.get(guid.size()));
	}

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.SHUOSHUO_DETAIL_ACTIVITY_ACTION);
		// 注册广播
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.SHUOSHUO_DETAIL_ACTIVITY_ACTION)) {

				if (intent.getExtras() == null) {
					return;
				} else {
					if (!intent.getExtras().containsKey(Keys.DIARY_ID)
							|| !intent.getExtras()
									.containsKey(Keys.COMMENT_NUM)
							|| !intent.getExtras().containsKey(Keys.PRIASE_NUM)
							|| !intent.getExtras().containsKey(
									Keys.COMMENT_LIST)) {
						return;
					}
				}
				// 更新界面的评论

				String diaryID = intent.getStringExtra(Keys.DIARY_ID);
				int postion = -1;
				for (int i = 0; i < mDiaryList.size(); i++) {
					if (diaryID.equals(mDiaryList.get(i).getContent().getId())) {
						postion = i;
						break;
					}
				}
				if (postion < 0) {
					return;
				}

				mDiaryList
						.get(postion)
						.getContent()
						.setPinglun(
								intent.getStringExtra(Keys.COMMENT_NUM) + "");
				mDiaryList.get(postion).getContent()
						.setZan(intent.getStringExtra(Keys.PRIASE_NUM) + "");
				if (intent.getSerializableExtra(Keys.COMMENT_LIST) != null) {
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<?>> list = (ArrayList<ArrayList<?>>) intent
							.getSerializableExtra(Keys.COMMENT_LIST);
					@SuppressWarnings("unchecked")
					ArrayList<DiaryCommentsEntity.ObjInfo> commentList = (ArrayList<DiaryCommentsEntity.ObjInfo>) list
							.get(0);
					int commentCount = 0;
					if (commentList.size() > 3) {
						commentCount = 3;
					} else {
						commentCount = commentList.size();
					}
					VDiaryEntity.ObjInfo.Content.Comments.CommentList[] vCommentList = new VDiaryEntity.ObjInfo.Content.Comments.CommentList[commentCount];
					for (int i = 0; i < commentCount; i++) {
						VDiaryEntity.ObjInfo.Content.Comments.CommentList comment = new VDiaryEntity.ObjInfo.Content.Comments.CommentList();
						comment.setCommentContent(commentList.get(i).getText());
						comment.setCommentDatetime(commentList.get(i).getTime());
						comment.setCommentid(commentList.get(i).getId());
						comment.setUserHeadPhotoURL(commentList.get(i).getImg());
						comment.setUserid(commentList.get(i).getCode());
						comment.setUsername(commentList.get(i).getName());
						comment.setIsAdmin(commentList.get(i).getIsAdmin());
						vCommentList[i] = comment;
						if (i == 3) {
							break;
						}
					}
					mDiaryList.get(postion).getContent().getComments()
							.setCommentList(vCommentList);
				} else {
					CommentList[] commentLists = {};
					mDiaryList.get(postion).getContent().getComments()
							.setCommentList(commentLists);
				}
				DiaryAdapter.this.notifyDataSetChanged();
			}
		}

	};

	/**
	 * 收藏点赞按钮点击后的动画
	 * 
	 * @param view
	 */
	private void scaleAnimation(final View view) {
		// TODO Auto-generated method stub
		Animation scaleAnimation = new ScaleAnimation(0f, 2f, 0f, 2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画时间
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(false);
		view.startAnimation(scaleAnimation);
		view.setVisibility(View.VISIBLE);
		scaleAnimation.startNow();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				view.setVisibility(View.GONE);
			}
		}, 1000);
	}
}
