package com.halong.aubaby.tab1;

import java.util.ArrayList;

import com.halong.aubaby.BaseFragmentActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.DiaryCommentsEntity;
import com.halong.aubaby.wcf.CommentService;
import com.halong.aubaby.widget.swipelistview.SwipeListView;
import com.halong.aubaby.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShuoShuoDetailActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ViewPager mViewPager;// 左右滑动视图容器
	private FragmentPagerAdapter mAdapter;// 左右滑动fragment适配器
	private Button searchButton, sendBtn;// 搜索按钮,发表评论按钮
	private EditText sendEditText;
	private CommentService commentService;// 发表评论线程
	private View progress;// 进度条
	private DiaryCommentsEntity diaryCommentsEntity;
	private String diaryID;
	private Button toLeftButton,toRightButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		diaryID = getIntent().getStringExtra(Keys.DIARY_ID);
		setContentView(R.layout.activity_shuoshuo_detail);

		searchButton = (Button) findViewById(R.id.searchBtn);
		searchButton.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					return new ShuoShuoDetailContentFragment();

				case 1:
					return new DiaryCommentFragment();
				default:
					return null;
				}

			}
		};
		mViewPager.setAdapter(mAdapter);
		int fragmentPage = getIntent().getIntExtra(Keys.FRAGMENT_PAGE, 0);
		mViewPager.setCurrentItem(fragmentPage);
		 ViewPagerSelectPager( fragmentPage);
		// 发表评论
		sendComment();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtn:
			toOtherActivity(SearchActivity.class);
			break;
		case R.id.sendBtn:
			String commentString = sendEditText.getText().toString().trim();
			if (commentString.length() == 0) {
				Toast.makeText(getApplicationContext(), R.string.comment_null,
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			// 显示或者隐藏输入法
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			progress.setVisibility(View.VISIBLE);
			commentService.postComments(
					getIntent().getStringExtra(Keys.DIARY_ID), commentString);
			break;
		default:
			break;
		}
	}

	/**
	 * 发表评论
	 */

	private void sendComment() {
		// TODO Auto-generated method stub
		sendBtn = (Button) findViewById(R.id.sendBtn);
		sendEditText = (EditText) findViewById(R.id.sendEditText);
		commentService = new CommentService(getApplicationContext()) {
			@Override
			public void commentSussess() {
				// TODO Auto-generated method stub
				super.commentSussess();
				sendEditText.getText().clear();
				progress.setVisibility(View.GONE);
				// 发送成功后更新日记详情页面评论数
				diaryCommentsEntity = commentService.getDiaryCommentsEntity();
				((Button) findViewById(R.id.pinglunBtn)).setText(getResources()
						.getText(R.string.pinglun)
						+ diaryCommentsEntity.getCountOfComments());
				// 发送成功后更新评论页面评论数
				((TextView) findViewById(R.id.newReplayTextView))
						.setText(diaryCommentsEntity.getCountOfComments());

				// 将发表的评论添加的评论列表顶端
				HeaderViewListAdapter hAdapter = (HeaderViewListAdapter) ((ListView) findViewById(R.id.onlyListView))
						.getAdapter();
				DiaryCommentsAdapter diaryCommentsAdapter = (DiaryCommentsAdapter) hAdapter
						.getWrappedAdapter();
				DiaryCommentsEntity.ObjInfo[] objInfos = diaryCommentsEntity
						.getObjInfo();

				diaryCommentsAdapter.addCommentsFirst(objInfos[0]);
				diaryCommentsAdapter.notifyDataSetChanged();
			}

			@Override
			public void commentFailure() {
				// TODO Auto-generated method stub
				super.commentFailure();
				progress.setVisibility(View.GONE);
			}
		};
		progress = (View) findViewById(R.id.progress);
		sendBtn.setOnClickListener(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			refreshDiary();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void backButton(View view) {
		// TODO Auto-generated method stub
		refreshDiary();
		super.backButton(view);
	}

	/**
	 * 返回时更新日记列表的界面
	 */
	private void refreshDiary() {
		// TODO Auto-generated method stub

		Intent intent = new Intent(Keys.SHUOSHUO_DETAIL_ACTIVITY_ACTION);
		Bundle bundle = new Bundle();
		bundle.putString(Keys.DIARY_ID, diaryID);
		String priaseNum = ((TextView) findViewById(R.id.numTextView))
				.getText().toString();
		if (priaseNum.length() > getResources().getString(R.string.zan)
				.length()) {
			bundle.putString(Keys.PRIASE_NUM, priaseNum
					.substring(getResources().getString(R.string.zan).length()));
		} else {
			finish();
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
			return;
		}

		String commentNum = ((TextView) findViewById(R.id.pinglunBtn))
				.getText().toString();
		if (commentNum.length() > getResources().getString(R.string.pinglun)
				.length()) {
			bundle.putString(
					Keys.COMMENT_NUM,
					commentNum.substring(getResources().getString(
							R.string.pinglun).length()));
		}

		DiaryCommentsAdapter diaryCommentsAdapter = (DiaryCommentsAdapter) ((HeaderViewListAdapter) (((SwipeListView) findViewById(R.id.onlyListView))
				.getAdapter())).getWrappedAdapter();
		if (diaryCommentsAdapter.getCommentList().size() == 0) {
			bundle.putSerializable(Keys.COMMENT_LIST, null);
		} else {
			ArrayList<ArrayList<?>> list = new ArrayList<ArrayList<?>>();
			list.add(diaryCommentsAdapter.getCommentList());
			bundle.putSerializable(Keys.COMMENT_LIST, list);
		}

		intent.putExtras(bundle);
		sendBroadcast(intent);
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}
	private void  ViewPagerSelectPager(int fragmentPage){

		toLeftButton=(Button)findViewById(R.id.toLeftBtn);
		toRightButton=(Button)findViewById(R.id.toRightBtn);
		switch (fragmentPage) {
		case 0:
			toLeftButton.setVisibility(View.GONE);
			break;

		default:
			toRightButton.setVisibility(View.GONE);
			break;
		}
	mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
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
				mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
			}
		});
		toRightButton.setOnClickListener(new View. OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
			}
		});
	}
}
