package com.halong.aubaby.tab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.DownLoadAUBabyFile;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.tab5.MTagHandler.MTagEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.util.TimeFormatUtil;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HNoticeListAdapter extends BaseAdapter {

	private Context mContext;
	private List<NoticeListEntity> mList;
	private LayoutInflater mLayoutInflater;

	private ImageLoader mImageLoader;
	private DisplayImageOptions headOptions;
	private DisplayImageOptions mOptions;

	private String[] sign = { "#U@", "#H@", "#D@", "#S@", "点击阅读" };
	private DownLoadAUBabyFile downLoadAUBabyFile;

	static class ListItemView {
		ImageView statusImageView, headImageView, fileImageView, signImageView;
		TextView messageTextView, fileSizeTextView, fileOriginNameTextView,
				progressTextView;
		TextView commentTextView, signInTextView;
		TextView dateImageView, nameTextView;
		View commentLyout;

	}

	public HNoticeListAdapter(Context context, List<NoticeListEntity> list) {
		this.mList = list;
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DemoApplication app = (DemoApplication) context.getApplicationContext();
		this.mImageLoader = app.getImageLoader();
		this.headOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).showImageOnFail(R.drawable.head)
				.cacheOnDisc(true).build();
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		this.downLoadAUBabyFile = new DownLoadAUBabyFile() {
			@Override
			public void onDownLoadSuccess() {
				// TODO Auto-generated method stub
				super.onDownLoadSuccess();
				HNoticeListAdapter.this.notifyDataSetChanged();
			}
		};
	}

	public void setmList(List<NoticeListEntity> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mLayoutInflater.inflate(R.layout.item_tab5_message, null);
		final ListItemView listItemView = new ListItemView();
		listItemView.statusImageView = (ImageView) view
				.findViewById(R.id.statusImageView);
		listItemView.messageTextView = (TextView) view
				.findViewById(R.id.messageTextView);
		listItemView.commentTextView = (TextView) view
				.findViewById(R.id.commentTextView);
		listItemView.dateImageView = (TextView) view
				.findViewById(R.id.dateImageView);
		listItemView.headImageView = (ImageView) view
				.findViewById(R.id.headImg);
		listItemView.nameTextView = (TextView) view.findViewById(R.id.nameTxt);
		listItemView.fileImageView = (ImageView) view
				.findViewById(R.id.fileImg);
		listItemView.fileSizeTextView = (TextView) view
				.findViewById(R.id.fileSizeTxt);
		listItemView.fileOriginNameTextView = (TextView) view
				.findViewById(R.id.fileOriginNameTxt);
		listItemView.progressTextView = (TextView) view
				.findViewById(R.id.progressTextView);
		listItemView.commentLyout = (View) view.findViewById(R.id.commentLyout);
		listItemView.signInTextView = (TextView) view
				.findViewById(R.id.signInTextView);
		listItemView.signImageView = (ImageView) view.findViewById(R.id.img);

		final NoticeListEntity entity = mList.get(position);
		listItemView.nameTextView.setText(entity.getPublish_username());
		mImageLoader.displayImage(
				ContantUtil.PICTURE_URL + entity.getHeadPhotoURL(),
				listItemView.headImageView, headOptions);
		listItemView.headImageView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mContext,
								OtherUserInfoActivity.class);
						intent.putExtra(Keys.USER_INFO_ID,
								entity.getPublish_userid());
						mContext.startActivity(intent);
						((Activity) mContext).overridePendingTransition(
								R.anim.push_right_in, R.anim.push_left_out);
					}
				});
		// 设置标题
		if (entity.getTitle().contains("#U@")
				|| entity.getTitle().contains("#H@")
				|| entity.getTitle().contains("#D@")
				|| entity.getTitle().contains("#S@")) {
			subContent(listItemView, entity);
		} else {
			listItemView.messageTextView.setText(entity.getTitle());
		}
		// 签到人数
		listItemView.signInTextView.setText(entity.getNoticeReceivedCnt());
		if (!SharedPreferencesHelper.getStringValue(mContext,
				Keys.IS_CLASS_ADMIN).equals("1")) {
			listItemView.signInTextView.setVisibility(View.GONE);
		}
		// 是否可评论
		if (entity.getNoticeCanReply() == 1) {
			listItemView.commentTextView.setText(entity.getNoticeReplyCnt()
					+ "");
			listItemView.commentLyout
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							// 打开评论页面
							Intent intent = new Intent(mContext,
									NoticeActivity.class);
							intent.putExtra(Keys.NOTICE_REPLY_COMMENT,
									entity.getId());
							intent.putExtra(Keys.FRAGMENT_PAGE,
									Keys.COMMENT_FRAGMENT);
							if (entity.getNoticeCanReply() == 1) {
								intent.putExtra(Keys.NOTICE_REPLY_CNT,
										entity.getNoticeReplyCnt());
							}
							// 设置该消息为已读
							if (!entity.getIsRead().equals("1")) {
								Toast.makeText(mContext,
										R.string.success_read_notice,
										Toast.LENGTH_SHORT).show();
								entity.setIsRead("1");
								notifyDataSetChanged();

							}
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);

							// 签到图标获取不到焦点，只能手动设置点击效果
							listItemView.signImageView.setPressed(true);
						}
					});
		} else {
			listItemView.commentTextView.setVisibility(View.GONE);
		}

		// 是否紧急、可读
		if (entity.getIsUrgency() == 1) {
			listItemView.statusImageView.setImageResource(R.drawable.icon_025);
			listItemView.messageTextView.getPaint().setFakeBoldText(false);
		} else if (!entity.getIsRead().equals("1")) {
			listItemView.statusImageView.setImageResource(R.drawable.icon_026);
			listItemView.messageTextView.getPaint().setFakeBoldText(true);
		} else {
			listItemView.statusImageView.setImageResource(R.drawable.icon_027);
			listItemView.messageTextView.getPaint().setFakeBoldText(false);
		}

		// 判断星期几
		try {
			listItemView.dateImageView.setText(TimeFormatUtil
					.getTimeFormatText(entity.getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, NoticeActivity.class);
				intent.putExtra(Keys.NOTICE_REPLY_COMMENT, entity.getId());
				if (entity.getNoticeCanReply() == 1) {
					intent.putExtra(Keys.NOTICE_REPLY_CNT,
							entity.getNoticeReplyCnt());
				}

				if (!entity.getIsRead().equals("1")) {
					Toast.makeText(mContext, R.string.success_read_notice,
							Toast.LENGTH_SHORT).show();
					entity.setIsRead("1");
					notifyDataSetChanged();

				}
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_left_out);

				// 签到图标获取不到焦点，只能手动设置点击效果
				listItemView.signImageView.setPressed(true);
			}
		});
		if (entity.getAttachType().equals("P")) {
			mImageLoader.displayImage(
					ContantUtil.PICTURE_URL + entity.getAttachURL(),
					listItemView.fileImageView, mOptions);
			listItemView.fileImageView.setVisibility(View.VISIBLE);
			listItemView.fileImageView
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(mContext, PictureShowActivity.class);
							intent.putExtra(
									Keys.PHOTO_URL,
									ContantUtil.PICTURE_URL
											+ entity.getAttachURL());
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
		} else if (entity.getAttachType().equals("L")) {
			listItemView.fileOriginNameTextView.setText(entity.getAttachURL());
			listItemView.fileOriginNameTextView.setAutoLinkMask(Linkify.ALL);
			listItemView.fileOriginNameTextView.setVisibility(View.VISIBLE);
			listItemView.fileOriginNameTextView
					.setMovementMethod(LinkMovementMethod.getInstance());
			listItemView.fileOriginNameTextView.setClickable(true);

		} else if (!entity.getAttachType().equals("")) {
			if (downLoadAUBabyFile.fileIsExists(entity.getPublish_username()
					+ entity.getId() + entity.getOriginName())) {
				listItemView.fileOriginNameTextView.setText(entity
						.getOriginName() + "(已下载)");
			} else {
				listItemView.fileOriginNameTextView.setText(entity
						.getOriginName());
			}

			listItemView.fileOriginNameTextView.setVisibility(View.VISIBLE);
			listItemView.fileOriginNameTextView
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {

							downLoadAUBabyFile.downLoadFile(
									mContext,
									entity.getPublish_username()
											+ entity.getId()
											+ entity.getOriginName(),
									entity.getOriginName(),
									ContantUtil.PICTURE_URL
											+ entity.getAttachURL(),
									listItemView.progressTextView);
						}
					});
		}
		if (!entity.getSize().equals("")) {
			listItemView.fileSizeTextView.setText(entity.getSize());
			listItemView.fileSizeTextView.setVisibility(View.VISIBLE);

		}
		return view;
	}

	/**
	 * 拆分字符串，根据信息进行显示或者监听
	 * 
	 * @param listItemView
	 * @param entity
	 */
	private void subContent(final ListItemView listItemView,
			NoticeListEntity entity) {
		List<Integer> list = new ArrayList<Integer>(); // 需要监听的数字
		Map<String, String> map = new HashMap<String, String>(); // 跳转后的ID
		MTagEntity tagEntity = new MTagEntity();

		String id = "";
		String path = "";
		String notice = "";

		String msg = "";

		int num = 0; // end是需要进行截取的位置
		String html = ""; // html的字符串
		String sub = entity.getTitle(); // 不停被截取后的字符串

		int count = 0;
		for (int i = 0; i < sign.length; i++) {
			count = +getCount(sub, sign[i]);
		}

		for (int i = 0; i < count; i++) {
			num = getSignStart(sub);
			html = html + sub.substring(0, num);
			msg += sub.substring(0, num);
			sub = sub.substring(num);
			if (sub.substring(0, 3).equals(sign[0])) {
				id = getResult(sub);
				sub = getSub(sub);
			} else if (sub.substring(0, 3).equals(sign[1])) {
				path = getResult(sub);
				sub = getSub(sub);
				list.add(msg.length());
				list.add(msg.length() + 1);
				html = html + "<img src='" + ContantUtil.PICTURE_URL + path
						+ "'/>";
			} else if (sub.substring(0, 3).equals(sign[2])) {
				tagEntity.isS = false;
				notice = getResult(sub);
				sub = getSub(sub);
			} else if (sub.substring(0, 3).equals(sign[3])) {
				tagEntity.isS = true;
				notice = getResult(sub);
				sub = getSub(sub);
			} else if (sub.substring(0, 4).equals(sign[4])) {
				html += "<read>" + sign[4] + "</read>";
				sub = sub.substring(4);
				// 图片显示的时候占用了一个字符长度、但是在msg中是没有的
				if (path.equals("")) {
					list.add(msg.length());
					list.add(msg.length() + 4);
				} else {
					list.add(msg.length() + 1);
					list.add(msg.length() + 5);
				}
			}
		}
		map.put("id", id);
		map.put("notice", notice);

		tagEntity.mList = list;
		tagEntity.mMap = map;

		listItemView.messageTextView.setMovementMethod(LinkMovementMethod
				.getInstance());
		listItemView.messageTextView.setText(Html.fromHtml(html,
				new Html.ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						try {
							ImageView imageView = new ImageView(mContext);
							mImageLoader.displayImage(source, imageView,
									headOptions);
							Drawable d = imageView.getDrawable();

							d.setBounds(0, 0,
									(int) (d.getIntrinsicWidth() / 1.5),
									(int) (d.getIntrinsicHeight() / 1.5));
							return d;
						} catch (Exception e) {
							return null;
						}
					}
				}, new MTagHandler(mContext, tagEntity)));
	}

	/**
	 * 获取结果
	 * 
	 * @param sub
	 * @return
	 */
	private String getResult(String sub) {
		return sub.substring(3, getSignEnd(sub));
	}

	/**
	 * token在str出现次数
	 * 
	 * @param str
	 * @param token
	 * @return
	 */
	private int getCount(String str, String token) {
		int count = 0;
		while (str.indexOf(token) != -1) {
			count++;
			str = str.substring(str.indexOf(token) + token.length());
		}
		return count;
	}

	/**
	 * 获取剩余的字符串
	 * 
	 * @param sub
	 * @return
	 */
	private String getSub(String sub) {
		return sub.substring(getSignEnd(sub) + 1);
	}

	/**
	 * 获取最后的标记
	 * 
	 * @param subString
	 * @return
	 */
	private int getSignEnd(String subString) {
		// TODO Auto-generated method stub
		return subString.indexOf(";");
	}

	/**
	 * 获取开始标记
	 * 
	 * @param subString
	 * @return
	 */
	private int getSignStart(String subString) {
		// TODO Auto-generated method stub
		int result = -1;
		int a = -1;
		for (int i = 0; i < sign.length; i++) {

			if (subString.contains(sign[i])) {
				if (result == -1) {
					result = subString.indexOf(sign[i]);
				} else {
					a = subString.indexOf(sign[i]);
					if (result > a) {
						result = a;
					}
				}
			}
		}

		return result;
	}

}
