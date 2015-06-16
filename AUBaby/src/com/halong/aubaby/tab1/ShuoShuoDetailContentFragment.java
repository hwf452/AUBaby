package com.halong.aubaby.tab1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.DiaryDetailEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.AlbumService;
import com.halong.aubaby.wcf.CommentService;
import com.halong.aubaby.wcf.DiaryService;
import com.halong.aubaby.widget.ChildViewPager;
import com.halong.aubaby.widget.NoScrollGridView;
import com.halong.aubaby.widget.PictureShowActivity;
import com.halong.aubaby.widget.ChildViewPager.OnSingleTouchListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShuoShuoDetailContentFragment extends FragmentToOtherActivity
		implements OnClickListener {
	/**
	 * 我的说说详情
	 */
	private ChildViewPager mPager;// 大图滑动容器
	private DetailImgPagerAdapter mAdapter;
	private LinearLayout mPreviewLayout;// 预览图容器布局
	private ImageButton mheadImageButton;// 用户头像
	private ImageView teacImageView;
	private TextView mUserNametTextView, mPostedtTextView, mShuoShuoTextView,
			mZanTextView;// 用户名、发表时间、日志标题、点赞状态
	private Button zanButton, pinglunButton;// 点赞数、评论数
	private String diaryID;
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions options;// 图片加载设置
	private DisplayImageOptions headOptions;// 图片加载设置
	private DiaryService diaryService;
	private DiaryDetailEntity dEntity;
	private NoScrollGridView mGridView;// 点赞的用户
	private Button praiseBtn, collectBtn, collectDiaryBtn;
	private View view, viewPager, progress;
	private ImageView collectImageView, praiseImageView, praiseDiaryImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		diaryID = getActivity().getIntent().getStringExtra(Keys.DIARY_ID);

		view = inflater
				.inflate(R.layout.fragment_shuoshuo_detail_content, null);
		setPreviewHeight(view);

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.bg_3).cacheInMemory(true)
				.cacheOnDisc(true).build();
		headOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		initView();

		diaryService = new DiaryService(getActivity()) {

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub

				dEntity = diaryService.getdEntity();
				setDiaryDetailEntity();
				collcetPhoto();
				praisePhoto();
				collectDiary();
				diaryCommetnFragmentView();
				viewPager.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
			}
		};

		diaryService.getDiaryDetail(diaryID);
		return view;
	}

	/*
	 * 设置预览图的高度
	 */
	@SuppressWarnings("deprecation")
	private void setPreviewHeight(View view) {
		// TODO Auto-generated method stub
		FrameLayout preViewpLayout = (FrameLayout) view
				.findViewById(R.id.preview);
		LinearLayout.LayoutParams preViewParams = (LinearLayout.LayoutParams) preViewpLayout
				.getLayoutParams();
		preViewParams.height = getActivity().getWindowManager()
				.getDefaultDisplay().getHeight() * 3 / 4;
		preViewpLayout.setLayoutParams(preViewParams);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// TODO Auto-generated method stub
		mheadImageButton = (ImageButton) view.findViewById(R.id.headImg);
		mUserNametTextView = (TextView) view.findViewById(R.id.userNameTXT);
		mPostedtTextView = (TextView) view.findViewById(R.id.postedTxt);
		mShuoShuoTextView = (TextView) view.findViewById(R.id.shuoShuoTxt);
		zanButton = (Button) view.findViewById(R.id.zanBtn);
		pinglunButton = (Button) view.findViewById(R.id.pinglunBtn);
		mZanTextView = (TextView) view.findViewById(R.id.zanTxt);
		mPager = (ChildViewPager) view.findViewById(R.id.viewPager);
		mPreviewLayout = (LinearLayout) view
				.findViewById(R.id.imgPreviewLayout);
		mGridView = (NoScrollGridView) view.findViewById(R.id.praiseGridView);
		teacImageView = (ImageView) view.findViewById(R.id.tecImg);

		collectImageView = (ImageView) getActivity().findViewById(
				R.id.collectImg);
		praiseImageView = (ImageView) getActivity()
				.findViewById(R.id.praiseImg);
		praiseDiaryImageView = (ImageView) getActivity().findViewById(
				R.id.praiseDiaryImg);

	}

	// 加载日记详情数据
	private void setDiaryDetailEntity() {
		// TODO Auto-generated method stub

		mheadImageButton.setOnClickListener(this);
		if (dEntity.getUser().getIsAdmin().equals("1")) {
			teacImageView.setVisibility(View.VISIBLE);
		} else {
			teacImageView.setVisibility(View.GONE);
		}

		imageLoader.displayImage(ContantUtil.PICTURE_URL
				+ dEntity.getUser().getImg(), mheadImageButton, headOptions);
		mUserNametTextView.setText(dEntity.getUser().getName());
		mPostedtTextView.setText(dEntity.getContent().getDate());
		mShuoShuoTextView.setText(dEntity.getContent().getNeirong());
		zanButton.setText(getResources().getText(R.string.zan)
				+ dEntity.getContent().getZan());
		zanButton.setOnClickListener(this);
		if (TextUtils.equals(dEntity.getContent().getZan(), "0") == false) {
			mZanTextView.setText(getResources().getText(
					R.string.somebody_priase));
		}
		pinglunButton.setText(getResources().getText(R.string.pinglun)
				+ dEntity.getContent().getPinglun());
		// 评论页面说说数
		((TextView) getActivity().findViewById(R.id.newReplayTextView))
				.setText(dEntity.getContent().getPinglun());

		mAdapter = new DetailImgPagerAdapter(getActivity(), imageLoader,
				dEntity);
		mPager.setAdapter(mAdapter);
		preView();
		mGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					view = LayoutInflater.from(getActivity()).inflate(
							R.layout.item_tab2_picshow, null);
				}

				TextView nameTextView = (TextView) view
						.findViewById(R.id.nameTxt);
				ImageView teacherImageView = (ImageView) view
						.findViewById(R.id.tecImg);
				nameTextView
						.setText(dEntity.getUserPriceList().getList()[position]
								.getName());
				ImageView headImageView = (ImageView) view
						.findViewById(R.id.headImg);
				imageLoader.displayImage(
						ContantUtil.PICTURE_URL
								+ dEntity.getUserPriceList().getList()[position]
										.getUserHeadPhotoURL(), headImageView,
						headOptions);
				if (dEntity.getUserPriceList().getList()[position].getIsAdmin()
						.equals("1")) {
					teacherImageView.setVisibility(View.VISIBLE);
				} else {
					teacherImageView.setVisibility(View.GONE);
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return dEntity.getUserPriceList().getList().length;
			}
		});
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(getActivity(),
						OtherUserInfoActivity.class);
				intent.putExtra(Keys.USER_INFO_ID, dEntity.getUserPriceList()
						.getList()[position].getUserid());
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});

	}

	// 预览图、相册方法：根据点击上层的图片，进入相对应的图片
	private void preView() {
		// TODO Auto-generated method stub
		// 上层点击的第几个日记图片
		int clickGridViewItem = getActivity().getIntent().getIntExtra(
				Keys.CLICK_GRIDVIEW_ITEM, 0);
		// 要加载的预览图
		final ImageView[] mPreviewImageView = new ImageView[dEntity
				.getContent().getCountOfPics()];
		final ImageView[] alphaPreviewImageView = new ImageView[dEntity
		                                    				.getContent().getCountOfPics()];

		for (int i = 0; i < dEntity.getContent().getCountOfPics(); i++) {
			// 预览图所在layout
			View mPreviewView = (View) getLayoutInflater(getArguments())
					.inflate(R.layout.include_img_preview2, null);

			// 预览图容器添加预览图
			mPreviewLayout.addView(mPreviewView);

			mPreviewImageView[i] = (ImageView) mPreviewView
					.findViewById(R.id.img);
			alphaPreviewImageView[i] = (ImageView) mPreviewView
					.findViewById(R.id.alphaImg);
			if (i == 0) {
				mPreviewView.findViewById(R.id.space).setVisibility(View.GONE);
			}

			imageLoader.displayImage(ContantUtil.PICTURE_URL + Keys.S_VIEW
					+ dEntity.getPiclist().getPic()[i].getImg(),
					mPreviewImageView[i], options);

			// 点击预览图，相册转到相应位置
			final int whcih = i;
			mPreviewView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPager.setCurrentItem(whcih);
				}
			});
		}
		alphaPreviewImageView[clickGridViewItem].setVisibility(View.GONE);;
		mPreviewLayout.getChildAt(clickGridViewItem).setSelected(true);
		mPager.setCurrentItem(clickGridViewItem);
		// 相册滑动方法，当前查看相册页面，对应预览图为选中状态
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mPreviewLayout.getChildCount(); i++) {
					mPreviewLayout.getChildAt(i).setSelected(false);
				}
			
				mPreviewLayout.getChildAt(mPager.getCurrentItem()).setSelected(
						true);
				for (int i = 0; i < mPreviewImageView.length; i++) {
					alphaPreviewImageView[i].setVisibility(View.VISIBLE);
				}
				alphaPreviewImageView[arg0].setVisibility(View.GONE);

				// 点赞、收藏按钮恢复初始状态
				collectBtn.setSelected(false);
				praiseBtn.setSelected(false);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

		});
		// 预览原图
		mPager.setOnSingleTouchListener(new OnSingleTouchListener() {

			@Override
			public void onSingleTouch() {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(getActivity(),
						PictureShowActivity.class);
				intent.putExtra(Keys.CLICK_GRIDVIEW_ITEM, dEntity.getPiclist()
						.getPic()[mPager.getCurrentItem()].getImg());
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.headImg:
			Intent intent = new Intent().setClass(getActivity(),
					OtherUserInfoActivity.class);
			intent.putExtra(Keys.USER_INFO_ID, dEntity.getUser().getCode());
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.zanBtn:
			scaleAnimation(praiseDiaryImageView);
			CommentService commentService = new CommentService(getActivity()) {
				@Override
				public void praiseDiaryOrPhotoSuccess(String praiseNumber) {
					// TODO Auto-generated method stub
					zanButton.setText(getResources().getText(R.string.zan)
							+ praiseNumber);
					refreshDiaryCommentsPraise(praiseNumber);

				}
			};
			commentService.praiseDiary(dEntity.getContent().getId());
			break;
		case R.id.collectDiaryBtn:
			AlbumService albumService = new AlbumService(getActivity()) {

				@Override
				public void getBBSpaceDataFailure() {
					// TODO Auto-generated method stub
				}

				@Override
				public void getBBSpaceData() {
					// TODO Auto-generated method stub

				}
			};
			albumService.collectAlbum(diaryID);
			break;
		default:
			break;
		}

	}

	// 收藏图片
	private void collcetPhoto() {
		// TODO Auto-generated method stub
		final AlbumService albumService = new AlbumService(getActivity()) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				collectBtn.setSelected(false);
			}

			@Override
			public void getBBSpaceData() {
				// TODO Auto-generated method stub
				collectBtn.setSelected(true);
			}
		};
		collectBtn = (Button) view.findViewById(R.id.collectBtn);
		collectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				albumService.collectPhoto(dEntity.getPiclist().getPic()[mPager
						.getCurrentItem()].getImgid());
				scaleAnimation(collectImageView);
			}
		});

	}

	// 点赞图片

	private void praisePhoto() {
		// TODO Auto-generated method stub
		final CommentService commentService = new CommentService(getActivity()) {
			@Override
			public void praiseDiaryOrPhotoSuccess(String praiseNumber) {
				// TODO Auto-generated method stub
				// 更新日记详情页面点赞数
				zanButton.setText(getResources().getText(R.string.zan)
						+ praiseNumber);
				praiseBtn.setSelected(true);
				// 更新评论页面点赞数
				refreshDiaryCommentsPraise(praiseNumber);
			}

			@Override
			public void praisePhotoSuccess() {
				// TODO Auto-generated method stub
				// 已经点赞过的执行该方法
				praiseBtn.setSelected(true);
				super.praisePhotoSuccess();

			}

			@Override
			public void praisePhotoFailure() {
				// TODO Auto-generated method stub
				praiseBtn.setSelected(false);
				super.praisePhotoFailure();

			}
		};
		praiseBtn = (Button) view.findViewById(R.id.praiseBtn);
		praiseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				commentService.praisePhoto(dEntity.getPiclist().getPic()[mPager
						.getCurrentItem()].getImgid());
				scaleAnimation(praiseImageView);
			}
		});
		;

	}

	private void collectDiary() {
		// TODO Auto-generated method stub
		collectDiaryBtn = (Button) view.findViewById(R.id.collectDiaryBtn);
		collectDiaryBtn.setOnClickListener(this);
	}

	// 收藏点赞按钮点击后的动画
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

	/**
	 * 更新评论的界面（在另一个fragment）
	 */

	private void diaryCommetnFragmentView() {
		// TODO Auto-generated method stub
		if (dEntity.getContent().getNeirong().length() == 0) {
			getActivity().findViewById(R.id.contentTextView).setVisibility(
					View.GONE);
		} else {
			((TextView) getActivity().findViewById(R.id.contentTextView))
					.setText(dEntity.getContent().getNeirong());
		}
		refreshDiaryCommentsPraise(dEntity.getContent().getZan());
		((TextView) getActivity().findViewById(R.id.dateTextView))
				.setText(dEntity.getContent().getDate());
	}

	private void refreshDiaryCommentsPraise(String num) {
		// 更新评论页面点赞数
		((TextView) getActivity().findViewById(R.id.numTextView))
				.setText(getResources().getText(R.string.zan) + num);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		viewPager = getActivity().findViewById(R.id.viewPager);
		progress = getActivity().findViewById(R.id.progress);
	}
}
