package com.halong.aubaby.tab5;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.tab1.ShuoShuoDetailActivity;
import com.halong.aubaby.util.TimeFormatUtil;
import com.halong.aubaby.wcf.NoticeService;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SNoticeListAdapter extends BaseAdapter {

	private Context mContext;
	private List<NoticeListEntity> mList;
	private LayoutInflater mLayoutInflater;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	// 去除已读状态
	private NoticeService noticeService;

	private String[] sign = { "#U@", "#H@", "#D@", "#S@" };

	static class ListItemView {
		ImageView statusImageView;
		TextView messageTextView;
		TextView commentTextView;
		TextView dateImageView;
		ImageView imageView, tecImageView;

	}

	public SNoticeListAdapter(Context context, List<NoticeListEntity> list) {
		this.mList = list;
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DemoApplication app = (DemoApplication) context.getApplicationContext();
		this.mImageLoader = app.getImageLoader();
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.head).cacheOnDisc(true).build();
		this.noticeService = new NoticeService(mContext) {
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
		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_tab5_message_s,
					null);
			listItemView = new ListItemView();
			listItemView.statusImageView = (ImageView) convertView
					.findViewById(R.id.statusImageView);
			listItemView.messageTextView = (TextView) convertView
					.findViewById(R.id.messageTextView);
			listItemView.commentTextView = (TextView) convertView
					.findViewById(R.id.commentTextView);
			listItemView.dateImageView = (TextView) convertView
					.findViewById(R.id.dateImageView);
			listItemView.imageView = (ImageView) convertView
					.findViewById(R.id.imageview);
			listItemView.tecImageView = (ImageView) convertView
					.findViewById(R.id.tecImg);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		NoticeListEntity entity = mList.get(position);
		if (entity.getNewTitle().getA().equals("1")) {
			listItemView.tecImageView.setVisibility(View.VISIBLE);
		} else {
			listItemView.tecImageView.setVisibility(View.GONE);
		}

		// 解析字符串
		if (entity.getTitle().contains("#U@")
				|| entity.getTitle().contains("#H@")
				|| entity.getTitle().contains("#D@")
				|| entity.getTitle().contains("#S@")) {
			subContent(convertView, listItemView, entity, position);
		} else {
			listItemView.messageTextView.setText(entity.getTitle());
		}

		// 因为接口调整的原因，listItemView.messageTextView加载的内容已更改，但是解析字符串里边的其他功能还在使用，所以解析字符串的功能保留
		listItemView.messageTextView.setText(entity.getNewTitle().getContent());

		// 是否可评论
		if (entity.getNoticeCanReply() == 1) {
			listItemView.commentTextView.setVisibility(View.VISIBLE);
			listItemView.commentTextView.setText(entity.getNoticeReplyCnt()
					+ "");
		} else {
			listItemView.commentTextView.setVisibility(View.GONE);
		}

		// 是否紧急、可读
		if (entity.getIsUrgency() == 1) {
			listItemView.statusImageView
					.setBackgroundResource(R.drawable.icon_025);
			listItemView.messageTextView.getPaint().setFakeBoldText(false);
		} else if (!entity.getIsRead().equals("1")) {
			listItemView.statusImageView
					.setBackgroundResource(R.drawable.icon_026);
			listItemView.messageTextView.getPaint().setFakeBoldText(true);
		} else {
			listItemView.statusImageView
					.setBackgroundResource(R.drawable.icon_027);
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

		return convertView;
	}

	/**
	 * 拆分字符串，根据信息进行显示或者监听
	 * 
	 * @param listItemView
	 * @param entity
	 */
	public void subContent(View convertView, final ListItemView listItemView,
			NoticeListEntity entity, final int position) {

		int num = 0; // end是需要进行截取的位置
		String html = ""; // html的字符串
		String sub = entity.getTitle(); // 不停被截取后的字符串

		int count = 0;
		for (int i = 0; i < sign.length; i++) {
			count += getCount(sub, sign[i]);
		}

		for (int i = 0; i < count; i++) {
			num = getSignStart(sub);
			html = html + sub.substring(0, num);
			sub = sub.substring(num);
			if (sub.substring(0, 3).equals(sign[0])) {
				final String id = getResult(sub);
				// 获取userId，点击图片可以跳转到用户界面
				listItemView.imageView
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(mContext,
										OtherUserInfoActivity.class);
								intent.putExtra(Keys.USER_INFO_ID, id);
								mContext.startActivity(intent);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.push_right_in,
												R.anim.push_left_out);
							}
						});

				sub = getSub(sub);
			} else if (sub.substring(0, 3).equals(sign[1])) {

				// html = html + "<img src='" + ContantUtil.PICTURE_URL + path
				// + "'/>";
				String path = getResult(sub);
				// 初始化头像
				// if (!(path.equals("") || path == null)) {
				mImageLoader.displayImage(ContantUtil.PICTURE_URL + path,
						listItemView.imageView, mOptions);
				// }

				sub = getSub(sub);

			} else if (sub.substring(0, 3).equals(sign[2])) {

				final String notice = getResult(sub);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 将该消息设置为已读
						noticeService.setNoticeInfoIsRead(mList.get(position)
								.getId());

						if (!mList.get(position).getIsRead().equals("1")) {
							mList.get(position).setIsRead("1");
							SNoticeListAdapter.this.notifyDataSetChanged();
						}

						listItemView.messageTextView.getPaint()
								.setFakeBoldText(false);
						listItemView.statusImageView
								.setBackgroundResource(R.drawable.icon_027);
						Intent intent = new Intent(mContext,
								ShuoShuoDetailActivity.class);
						intent.putExtra(Keys.DIARY_ID, notice);
						intent.putExtra(Keys.USER_INFO_ID, mList.get(position)
								.getId());
						// intent.putExtra(Keys.FRAGMENT_PAGE,
						// Keys.COMMENT_FRAGMENT);
						mContext.startActivity(intent);
						((Activity) mContext).overridePendingTransition(
								R.anim.push_right_in, R.anim.push_left_out);
					}
				});

				sub = getSub(sub);
			} else if (sub.substring(0, 3).equals(sign[3])) {
				final String notice = getResult(sub);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!mList.get(position).getIsRead() .equals("1")) {
							mList.get(position).setIsRead("1");
							notifyDataSetChanged();
						}

						Intent intent = new Intent(mContext,
								NoticeActivity.class);
						intent.putExtra("id", Integer.parseInt(notice));
						mContext.startActivity(intent);
						((Activity) mContext).overridePendingTransition(
								R.anim.push_right_in, R.anim.push_left_out);
					}
				});
				sub = getSub(sub);
			}
		}

		listItemView.messageTextView.setText(html);

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
	 * 获取结果
	 * 
	 * @param sub
	 * @return
	 */
	private String getResult(String sub) {
		return sub.substring(3, getSignEnd(sub));
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
