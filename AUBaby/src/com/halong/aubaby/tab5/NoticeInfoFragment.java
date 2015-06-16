package com.halong.aubaby.tab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.DownLoadAUBabyFile;
import com.halong.aubaby.entity.NoticeDetailEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab5.MTagHandler.MTagEntity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoticeInfoFragment extends FragmentToOtherActivity {

	private Context mContext;

	private TextView mUserNameTextView, mDateTextView, mContentTextView;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private String[] sign = { "#U@", "#H@", "#D@", "#S@" };
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions options;// 图片加载设置
	private ImageView imageView;
	private TextView sizeTextView, fileOriginNameTextView, progressTextView;
	private TextView recevier1, recevier2, recevier3, receveOverview;
	private DownLoadAUBabyFile downLoadAUBabyFile;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab5_notice_info, null);
		mUserNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
		mDateTextView = (TextView) view.findViewById(R.id.dateTextView);
		mContentTextView = (TextView) view.findViewById(R.id.contentTextView);

		imageView = (ImageView) view.findViewById(R.id.imageView);
		sizeTextView = (TextView) view.findViewById(R.id.sizeTxt);
		fileOriginNameTextView = (TextView) view
				.findViewById(R.id.fileOriginNameTxt);
		progressTextView = (TextView) view.findViewById(R.id.progressTextView);

		recevier1 = (TextView) view.findViewById(R.id.receive1);
		recevier2 = (TextView) view.findViewById(R.id.receive2);
		recevier3 = (TextView) view.findViewById(R.id.receive3);
		receveOverview = (TextView) view.findViewById(R.id.receveOverview);
		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		mContext = getActivity();
		downLoadAUBabyFile = new DownLoadAUBabyFile() {
			@Override
			public void onDownLoadSuccess() {
				// TODO Auto-generated method stub
				super.onDownLoadSuccess();
				progressTextView.setVisibility(View.GONE);
				fileOriginNameTextView.setText(fileOriginNameTextView.getText()
						+ "(已完成)");
			}
		};
		return view;
	}

	/**
	 * 设置接收详情
	 */
	public void setRecevier(String recevier1, String recevier2,
			String recevier3, String receveOverview) {
		if (recevier1.equals("")) {
			this.recevier1.setVisibility(View.GONE);
		} else {
			this.recevier1.setText(recevier1);
		}

		if (recevier2.equals("")) {
			this.recevier2.setVisibility(View.GONE);
		} else {
			this.recevier2.setText(recevier2);
		}

		if (recevier3.equals("")) {
			this.recevier3.setVisibility(View.GONE);
		} else {
			this.recevier3.setText(recevier3);
		}
		if (receveOverview.equals("")) {
			this.receveOverview.setVisibility(View.GONE);
		} else {
			this.receveOverview.setText(receveOverview);
		}

	}

	/**
	 * 设置发布人
	 */
	public void setUserNameTextView(String str) {
		mUserNameTextView.setText(str);
	}

	/**
	 * 设置日期
	 */
	public void setDateTextView(String str) {
		mDateTextView.setText(str);
	}

	/**
	 * 设置内容信息
	 */
	public void setContentTextView(String str) {
		if (str.contains("#U@") || str.contains("#H@") || str.contains("#D@")
				|| str.contains("#S@")) {
			subContent(str);
		} else {
			mContentTextView.setText(str);
		}
	}

	/**
	 * 加载图片
	 */
	public void setFileImage(String url, String size) {
		imageLoader.displayImage(url, imageView, options);
		sizeTextView.setText(size);
		imageView.setVisibility(View.VISIBLE);
		sizeTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 加载
	 */
	public void setFile(final NoticeDetailEntity entity) {

		if (downLoadAUBabyFile.fileIsExists(entity.getNoticeEntity()
				.getUserGroupName()
				+ entity.getNoticeEntity().getId()
				+ entity.getAttachments().getAttachment()[0].getOriginName())) {
			fileOriginNameTextView.setText(entity.getAttachments()
					.getAttachment()[0].getOriginName() + "(已下载)");
		} else {
			fileOriginNameTextView.setText(entity.getAttachments()
					.getAttachment()[0].getOriginName());
		}

		fileOriginNameTextView.setVisibility(View.VISIBLE);
		fileOriginNameTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				downLoadAUBabyFile.downLoadFile(
						mContext,
						entity.getNoticeEntity().getUserGroupName()
								+ entity.getNoticeEntity().getId()
								+ entity.getAttachments().getAttachment()[0]
										.getOriginName(),
						entity.getAttachments().getAttachment()[0]
								.getOriginName(),
						ContantUtil.PICTURE_URL
								+ entity.getAttachments().getAttachment()[0]
										.getUrl(), progressTextView);
			}
		});
	}

	/**
	 * 拆分字符串，根据信息进行显示或者监听
	 * 
	 * @param listItemView
	 * @param entity
	 */
	private void subContent(String str) {
		List<Integer> list = new ArrayList<Integer>(); // 需要监听的数字
		Map<String, String> map = new HashMap<String, String>(); // 跳转后的ID
		MTagEntity tagEntity = new MTagEntity();

		String id = "";
		String path = "";
		String notice = "";

		String msg = "";

		int num = 0; // end是需要进行截取的位置
		String html = ""; // html的字符串
		String sub = str; // 不停被截取后的字符串

		int count = 0;
		for (int i = 0; i < sign.length; i++) {
			count += getCount(sub, sign[i]);
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

		mContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mContentTextView.setText(Html.fromHtml(html, new Html.ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				try {
					ImageView imageView = new ImageView(mContext);
					mImageLoader.displayImage(source, imageView, mOptions);
					Drawable d = imageView.getDrawable();

					d.setBounds(0, 0, (int) (d.getIntrinsicWidth() / 1.5),
							(int) (d.getIntrinsicHeight() / 1.5));
					return d;
				} catch (Exception e) {
					return null;
				}
			}
		}, new MTagHandler(mContext, tagEntity)));
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
