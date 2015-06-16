package com.halong.aubaby.tab5;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ApiKeys;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.NoticeDetailEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.util.TimeFormatUtil;
import com.halong.aubaby.wcf.NoticeService;
import com.halong.aubaby.BaseFragmentActivity;
import com.halong.aubaby.R;

public class NoticeActivity extends BaseFragmentActivity {

	private Context mContext;

	private ViewPager mViewPager;
	private TextView mTv_titlebar4;
	private LinearLayout mProgressLinearLayout;

	private NoticeInfoFragment mNoticeInfoFragment;// 消息详情
	private NoticeReplayFragment mNoticeReplayFragment;// 消息评论
	private List<Fragment> mList;// 保存fragment

	private int mId = -1;// 通知id
	private int mNoticeReplyCnt = -1;// 评论数
	private Button toLeftButton, toRightButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		mId = getIntent().getIntExtra(Keys.NOTICE_REPLY_COMMENT, -1);
		if (mId == -1) {
			// 如果得不到通知的id就结束
			Toast.makeText(this, R.string.access_error, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		setContentView(R.layout.activity_tab5_notice);
		// 初始化基本控件
		initView();
		// 判断是否加载评论页面
		initViewPager();
		// 查看通知详细内容
		postNoticeInfo();
	}

	/**
	 * 初始化基本控件
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mTv_titlebar4 = (TextView) findViewById(R.id.tv_titlebar4);
		mProgressLinearLayout = (LinearLayout) findViewById(R.id.progress);
		mTv_titlebar4.setText(SharedPreferencesHelper.getStringValue(mContext,
				ApiKeys.CLASS_NAME));
		toLeftButton = (Button) findViewById(R.id.toLeftBtn);
		toRightButton = (Button) findViewById(R.id.toRightBtn);
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		mList = new ArrayList<Fragment>();

		mNoticeInfoFragment = new NoticeInfoFragment();
		mList.add(mNoticeInfoFragment);

		// 是否有评论数
		mNoticeReplyCnt = getIntent().getIntExtra(Keys.NOTICE_REPLY_CNT, -1);

		if (mNoticeReplyCnt >= 0) {
			// 添加fragment
			mNoticeReplayFragment = new NoticeReplayFragment();
			mList.add(mNoticeReplayFragment);
		} else {
			toLeftButton.setVisibility(View.GONE);
			toRightButton.setVisibility(View.GONE);
		}

		mViewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public boolean isViewFromObject(View view, Object object) {
				// TODO Auto-generated method stub
				return super.isViewFromObject(view, object);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mList.get(arg0);
			}
		});

		int fragmentPage = getIntent().getIntExtra(Keys.FRAGMENT_PAGE, 0);
		mViewPager.setCurrentItem(fragmentPage);
		switch (fragmentPage) {
		case 0:
			toLeftButton.setVisibility(View.GONE);
			break;

		default:
			toRightButton.setVisibility(View.GONE);
			break;
		}
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						switch (arg0) {
						case 0:
							toLeftButton.setVisibility(View.GONE);
							toRightButton.setVisibility(View.VISIBLE);
							break;
						case 1:
							toLeftButton.setVisibility(View.VISIBLE);
							toRightButton.setVisibility(View.GONE);
							break;
						default:
							break;
						}

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
		toLeftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			}
		});
		toRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			}
		});
	}

	/**
	 * 获取通知详细信息
	 */
	private void postNoticeInfo() {
		// TODO Auto-generated method stub
		NoticeService noticeService = new NoticeService(this) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				mProgressLinearLayout.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				parseNoticeInfo(content);
				mProgressLinearLayout.setVisibility(View.GONE);
			}
		};
		noticeService.getNoticeInfoDetail(mId);

	}

	/**
	 * 解析通知详情
	 * 
	 * @param content
	 */
	private void parseNoticeInfo(String content) {
		try {
			NoticeDetailEntity entity = new Gson().fromJson(content,
					NoticeDetailEntity.class);
			JSONObject jsonObject = new JSONObject(content);

			JSONObject noticeEntity = jsonObject.getJSONObject("noticeEntity");
			// 设置发布人
			mNoticeInfoFragment.setUserNameTextView(noticeEntity
					.getString("userGroupName"));
			// 设置日期
			mNoticeInfoFragment.setDateTextView(noticeEntity
					.getString("publishTime"));
			// 设置内容信息
			mNoticeInfoFragment.setContentTextView(noticeEntity
					.getString("content"));
			// 设置图片附件消息
			if (jsonObject.getJSONObject("attachments").getJSONArray(
					"attachment") != null) {
				if (jsonObject.getJSONObject("attachments")
						.getJSONArray("attachment").length() != 0) {
					String type = jsonObject.getJSONObject("attachments")
							.getJSONArray("attachment").getJSONObject(0)
							.getString("type");
					String url = jsonObject.getJSONObject("attachments")
							.getJSONArray("attachment").getJSONObject(0)
							.getString("url");
					String size = jsonObject.getJSONObject("attachments")
							.getJSONArray("attachment").getJSONObject(0)
							.getString("size");
					if (type.equals("P")) {
						mNoticeInfoFragment.setFileImage(
								ContantUtil.PICTURE_URL + url, size);
					} else if (!type.equals("")) {
						mNoticeInfoFragment.setFile(entity);
					}
				}

			}
			// 设置查看详情
			mNoticeInfoFragment.setRecevier(entity.getReceiverDetail()
					.getListA(), entity.getReceiverDetail().getListB(), entity
					.getReceiverDetail().getListC(),entity.getReceiverDetail().getReceveOverview());
			// 如果有评论
			if (mNoticeReplayFragment != null) {
				mNoticeReplayFragment.setContentTextView(noticeEntity
						.getString("content") + "");
				mNoticeReplayFragment.setDateTextView(TimeFormatUtil
						.getTimeFormatText(noticeEntity
								.getString("publishTime")));
				mNoticeReplayFragment.setNewReplayTextView(noticeEntity
						.getString("countOfComment"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mContext, getString(R.string.error),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Keys.REPLY_NOTICE_COMMENT_ACTIVITY
				&& requestCode == Keys.NOTICE_REPLY_ADAPTER) {
			mNoticeReplayFragment.replaceNoticeReplay(data
					.getStringExtra(Keys.COMMENT_LIST));
		}
	}
}
