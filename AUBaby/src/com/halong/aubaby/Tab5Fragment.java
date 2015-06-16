package com.halong.aubaby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.JuHeEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.tab1.ShuoShuoDetailActivity;
import com.halong.aubaby.tab2.SettingActivity;
import com.halong.aubaby.tab5.SystemMessageActivity;
import com.halong.aubaby.wcf.NoticeService;
import com.halong.aubaby.web.WebHtmlActivity;
import com.halong.aubaby.widget.PullToRefreshView;
import com.halong.aubaby.widget.PullToRefreshView.OnFooterRefreshListener;
import com.halong.aubaby.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Tab5Fragment extends FragmentToOtherActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	private PullToRefreshView mPullToRefreshView;
	private ExpandableListView expandableListView;
	private ExpandableListAdapter mAdapter;
	private ArrayList<ParentContent> mList;
	private JuHeEntity entity;
	private Gson gson;
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions options;// 图片加载设置
	private NoticeService noticeService;
	private NoticeService isReadNoticeService;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日    HH:mm:ss");
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	private Button mSetting,leftButton;//标题栏左右两边的按钮

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab5, null);
		userSetting(view);
		leftButtonOnclick(view);
		registerBoradcastReceiver();
		gson = new Gson();

		DemoApplication app = (DemoApplication) getActivity()
				.getApplicationContext();
		imageLoader = app.getImageLoader();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		isReadNoticeService = new NoticeService(getActivity()) {
		};
		mList = new ArrayList<Tab5Fragment.ParentContent>();
		initNoticeList();

		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		expandableListView = (ExpandableListView) view
				.findViewById(R.id.expandableListView);
		mAdapter = new ExpandableListAdapter() {

			@Override
			public void unregisterDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGroupExpanded(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGroupCollapsed(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChildSelectable(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getGroupView(int arg0, boolean arg1, View arg2,
					ViewGroup arg3) {
				// TODO Auto-generated method stub
				View view = LayoutInflater.from(getActivity()).inflate(
						R.layout.include_juhe_parent, null);
				TextView dateTextView = (TextView) view
						.findViewById(R.id.dateTextView);
				dateTextView.setText(mList.get(arg0).getDate());
				TextView typeTextView = (TextView) view
						.findViewById(R.id.typeTxt);
				typeTextView.setText(mList.get(arg0).getType());
				Drawable drawable = getResources().getDrawable(
						mList.get(arg0).getDrableLeftPath());
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				typeTextView.setCompoundDrawables(drawable, null, null, null);
				TextView numberTextView = (TextView) view
						.findViewById(R.id.numSTextView);
				switch (arg0) {
				case 0:
					if (mList.get(arg0).getList() != null) {
						numberTextView.setText(mList.get(arg0).getList().length
								+ "");
					}

					break;
				case 1:
					if (mList.get(arg0).getInfo() != null) {
						numberTextView.setText(mList.get(arg0).getInfo().length
								+ "");
					}

					break;
				case 2:
					if (mList.get(arg0).getSysInfo() != null) {
						numberTextView
								.setText(mList.get(arg0).getSysInfo().length
										+ "");
					}

					break;
				default:
					break;
				}

				switch (arg0) {
				case 0:
					view.findViewById(R.id.parentLayout).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent = new Intent().setClass(
											getActivity(),
											SystemMessageActivity.class);
									intent.putExtra(Keys.FRAGMENT_PAGE, 0);
									startActivity(intent);
									getActivity().overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_left_out);
								}
							});
					break;
				case 1:
					view.findViewById(R.id.parentLayout).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent = new Intent().setClass(
											getActivity(),
											SystemMessageActivity.class);
									intent.putExtra(Keys.FRAGMENT_PAGE, 1);
									startActivity(intent);
									getActivity().overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_left_out);
								}
							});
					break;
				case 2:

					view.findViewById(R.id.parentLayout).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent = new Intent().setClass(
											getActivity(),
											SystemMessageActivity.class);
									intent.putExtra(Keys.FRAGMENT_PAGE, 2);
									startActivity(intent);
									getActivity().overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_left_out);
								}
							});
					break;

				default:
					break;
				}
				// 这段代码是为了解决一些特殊机型造成的触摸不灵敏的情况。大部分情况下没有这段代码也行
				view.findViewById(R.id.parentLayout).setOnTouchListener(
						new View.OnTouchListener() {

							@Override
							public boolean onTouch(View arg0, MotionEvent arg1) {
								// TODO Auto-generated method stub

								curP.x = arg1.getX();
								curP.y = arg1.getY();

								if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
									// 记录按下时候的坐标
									// 切记不可用 downP = curP
									// ，这样在改变curP的时候，downP也会改变
									downP.x = arg1.getX();
									downP.y = arg1.getY();
									// 此句代码是为了通知他的父容易现在进行的是本控件的操作，不要对我的操作进行干扰
									arg0.getParent()
											.requestDisallowInterceptTouchEvent(
													true);
								}

								if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
									// 上下移动的距离和左右移动的距离做对比，判断该上下移动还是左右移动
									float moveY = downP.y - curP.y;
									float moveX = downP.x - curP.x;
									if (Math.abs(moveY) > Math.abs(moveX)) {
										// 如果上下移动距离较大，把焦点返回父容器，并且消耗完毕触摸事件
										arg0.getParent()
												.requestDisallowInterceptTouchEvent(
														false);
										return true;
									} else {
										// 此句代码是为了通知他的父容器现在进行的是本控件的操作，不要对我的操作进行干扰
										arg0.getParent()
												.requestDisallowInterceptTouchEvent(
														true);
									}

								}

								return false;
							}
						});
				return view;
			}

			@Override
			public long getGroupId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}

			@Override
			public Object getGroup(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getCombinedGroupId(long arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getCombinedChildId(long arg0, long arg1) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getChildrenCount(int arg0) {
				// TODO Auto-generated method stub
				if (mList == null) {
					return 0;
				}
				if (mList.size() == 0) {
					return 0;
				}
				switch (arg0) {
				case 0:
					if (mList.get(arg0).getList() == null) {
						return 0;
					}
					return mList.get(arg0).getList().length;
				case 1:
					if (mList.get(arg0).getInfo() == null) {
						return 0;
					}
					return mList.get(arg0).getInfo().length;
				case 2:
					if (mList.get(arg0).getSysInfo() == null) {
						return 0;
					}
					return mList.get(arg0).getSysInfo().length;
				default:
					return 0;
				}
			}

			@Override
			public View getChildView(final int arg0, final int arg1,
					boolean arg2, View arg3, ViewGroup arg4) {
				// TODO Auto-generated method stub
				View view = null;
				switch (arg0) {
				case 0:
					if (arg1 == 0) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_first_child_style1, null);
					} else if (arg1 == mList.get(arg0).getList().length - 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_last_child_style1, null);
					} else {
						view = LayoutInflater
								.from(getActivity())
								.inflate(
										R.layout.include_juhe_middle_child_style1,
										null);
					}

					if (mList.get(arg0).getList().length == 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_one_child_style1, null);
					}
					((TextView) view.findViewById(R.id.remarkTxt))
							.setText(mList.get(arg0).getList()[arg1]
									.getRemark());
					((TextView) view.findViewById(R.id.dateTextView))
							.setText(mList.get(arg0).getList()[arg1]
									.getSignTime());
					((TextView) view.findViewById(R.id.nameTxt)).setText(mList
							.get(arg0).getList()[arg1].getOperatorName());

					view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent().setClass(
									getActivity(), SystemMessageActivity.class);
							intent.putExtra(Keys.FRAGMENT_PAGE, 0);
							startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
					break;

				case 1:
					if (arg1 == 0) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_first_child_style2, null);
					} else if (arg1 == mList.get(arg0).getInfo().length - 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_last_child_style2, null);
					} else {
						view = LayoutInflater
								.from(getActivity())
								.inflate(
										R.layout.include_juhe_middle_child_style2,
										null);
					}

					if (mList.get(arg0).getInfo().length == 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_one_child_style2, null);
					}
					ImageView headImageView = (ImageView) view
							.findViewById(R.id.headImg);
					headImageView
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View view) {
									// TODO Auto-generated method stub
									Intent intent = new Intent().setClass(
											getActivity(),
											OtherUserInfoActivity.class);
									intent.putExtra(Keys.USER_INFO_ID, mList
											.get(arg0).getInfo()[arg1]
											.getPublish_userid());
									getActivity().startActivity(intent);
									getActivity().overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_left_out);
								}
							});
					imageLoader.displayImage(
							ContantUtil.PICTURE_URL
									+ mList.get(arg0).getInfo()[arg1]
											.getHeadPhotoURL(), headImageView,
							options);
					if (mList.get(arg0).getInfo()[arg1].getIsAdmin()
							.equals("1")) {
						((ImageView) view.findViewById(R.id.tecImgX))
								.setVisibility(View.VISIBLE);
					} else {
						view.findViewById(R.id.tecImgX)
								.setVisibility(View.GONE);
					}
					((TextView) view.findViewById(R.id.userNameTXT))
							.setText(mList.get(arg0).getInfo()[arg1]
									.getPublish_username());
					((TextView) view.findViewById(R.id.postedTxt))
							.setText(mList.get(arg0).getInfo()[arg1].getTime());
					((TextView) view.findViewById(R.id.shuoShuoTxt))
							.setText(mList.get(arg0).getInfo()[arg1].getTitle());

					view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent().setClass(
									getActivity(), SystemMessageActivity.class);
							intent.putExtra(Keys.FRAGMENT_PAGE, 1);
							startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
					break;
				case 2:
					if (arg1 == 0) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_first_child_style3, null);
					} else if (arg1 == mList.get(arg0).getSysInfo().length - 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_last_child_style3, null);
					} else {
						view = LayoutInflater
								.from(getActivity())
								.inflate(
										R.layout.include_juhe_middle_child_style3,
										null);
					}

					if (mList.get(arg0).getSysInfo().length == 1) {
						view = LayoutInflater.from(getActivity()).inflate(
								R.layout.include_juhe_one_child_style3, null);
					}
					ImageView sysImageView = (ImageView) view
							.findViewById(R.id.sheadImg);

					sysImageView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							Intent intent = new Intent().setClass(
									getActivity(), OtherUserInfoActivity.class);
							intent.putExtra(Keys.USER_INFO_ID, mList.get(arg0)
									.getSysInfo()[arg1].getNewTitle().getU());
							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);

						}
					});
					imageLoader.displayImage(ContantUtil.PICTURE_URL
							+ mList.get(arg0).getSysInfo()[arg1].getNewTitle()
									.getH(), sysImageView, options);

					mList.get(arg0).getSysInfo()[arg1].getNewTitle().getA();
					if (mList.get(arg0).getSysInfo()[arg1].getNewTitle().getA()
							.equals("1")) {
						view.findViewById(R.id.stecImg).setVisibility(
								View.VISIBLE);
					} else {
						view.findViewById(R.id.stecImg)
								.setVisibility(View.GONE);
					}
					((TextView) view.findViewById(R.id.dateTextView))
							.setText(mList.get(arg0).getSysInfo()[arg1]
									.getTime());
					((TextView) view.findViewById(R.id.systemInfoTxt))
							.setText(mList.get(arg0).getSysInfo()[arg1]
									.getNewTitle().getContent());
					view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							isReadNoticeService.setNoticeInfoIsRead(mList.get(
									arg0).getSysInfo()[arg1].getId());
							Intent intent = new Intent(getActivity(),
									ShuoShuoDetailActivity.class);
							intent.putExtra(Keys.DIARY_ID, mList.get(arg0)
									.getSysInfo()[arg1].getNewTitle().getID());
							intent.putExtra(Keys.USER_INFO_ID, mList.get(arg0)
									.getSysInfo()[arg1].getNewTitle().getU());
							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.push_right_in, R.anim.push_left_out);
						}
					});
					break;

				default:
					break;
				}
				return view;
			}

			@Override
			public long getChildId(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getChild(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		expandableListView.setAdapter(mAdapter);

		getAPartUnreadNotice();

		return view;

	}

	public void getAPartUnreadNotice() {

		noticeService = new NoticeService(getActivity()) {

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
			}

			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub

				if (content == null) {
					mPullToRefreshView.onHeaderRefreshComplete();
					mPullToRefreshView.onFooterRefreshComplete();
					return;
				}
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result == false) {
						mPullToRefreshView.onHeaderRefreshComplete();
						mPullToRefreshView.onFooterRefreshComplete();
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					mPullToRefreshView.onHeaderRefreshComplete();
					mPullToRefreshView.onFooterRefreshComplete();
					e.printStackTrace();
					return;
				}
				mList.clear();
				initNoticeList();
				entity = gson.fromJson(content, JuHeEntity.class);

				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setInfo(entity.getListJiaXiaoTong().getInfo());
					mList.get(i).setList(entity.getListSign().getList());
					mList.get(i).setSysInfo(
							entity.getListXiTongXinxi().getInfo());
					expandableListView.collapseGroup(i);
				}

				for (int j = 0; j < mList.size(); j++) {
					switch (j) {
					case 0:
						if (mList.get(j).getList().length > 0) {
							mList.get(j).setDate(entity.getDate());
						}
						break;
					case 1:
						if (mList.get(j).getInfo().length > 0) {
							mList.get(j).setDate(entity.getDate());
						}
						break;
					case 2:
						if (mList.get(j).getSysInfo().length > 0) {
							mList.get(j).setDate(entity.getDate());
						}
						break;
					default:
						break;
					}
				}
				((BaseAdapter) expandableListView.getAdapter())
						.notifyDataSetChanged();
				for (int i = 0; i < mList.size(); i++) {
					expandableListView.expandGroup(i);
				}
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				mPullToRefreshView.onHeaderRefreshComplete(simpleDateFormat
						.format(new Date(System.currentTimeMillis())));
			}
		};

		noticeService.getAPartUnreadNotice();
	}

	private void initNoticeList() {
		// TODO Auto-generated method stub

		for (int i = 0; i < 3; i++) {
			ParentContent parentContent = new ParentContent();
			parentContent
					.setDate(getResources().getString(R.string.today_none));
			switch (i) {
			case 0:
				parentContent.setType(R.string.sign_in_information);
				parentContent.setDrableLeftPath(R.drawable.icon_021);
				break;
			case 1:
				parentContent.setType(R.string.home_and_school_information);
				parentContent.setDrableLeftPath(R.drawable.icon_020);
				break;
			case 2:
				parentContent.setType(R.string.system_information);
				parentContent.setDrableLeftPath(R.drawable.icon_022);
				break;
			default:
				break;
			}
			mList.add(parentContent);

		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		noticeService.getAPartUnreadNotice();

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		noticeService.getAPartUnreadNotice();

	}

	/**
	 * 接收广播后更新日记列表数据
	 */

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Keys.SETTING_ACTIVITY_CHANGE_GROUP)) {
				for (int i = 0; i < mList.size(); i++) {
					expandableListView.collapseGroup(i);
				}
				mList.clear();
				((BaseAdapter) expandableListView.getAdapter())
						.notifyDataSetChanged();
				noticeService.getAPartUnreadNotice();
			}
		}
	};

	/**
	 * 注册广播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Keys.SETTING_ACTIVITY_CHANGE_GROUP);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	/**
	 * 设置按钮事件
	 * 
	 * @param view
	 */
	private void userSetting(View view) {
		mSetting = (Button) view.findViewById(R.id.button1);
		mSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toOtherActivity(SettingActivity.class);
			}
		});

	};
/**
 * 
 * @author Administrator
 *
 */
	private void leftButtonOnclick(View view){
		
		leftButton=(Button)view.findViewById(R.id.curriculumBtn);
		leftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(), WebHtmlActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
	public class ParentContent {
		private int drableLeftPath;
		private int type;
		private String date;
		private JuHeEntity.listJiaXiaoTong.info[] info;
		private JuHeEntity.listSign.list[] list;
		private JuHeEntity.listXiTongXinxi.info[] sysInfo;

		public int getDrableLeftPath() {
			return drableLeftPath;
		}

		public String getDate() {
			return date;
		}

		public void setDrableLeftPath(int drableLeftPath) {
			this.drableLeftPath = drableLeftPath;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public JuHeEntity.listJiaXiaoTong.info[] getInfo() {
			return info;
		}

		public void setInfo(JuHeEntity.listJiaXiaoTong.info[] info) {
			this.info = info;
		}

		public JuHeEntity.listSign.list[] getList() {
			return list;
		}

		public void setList(JuHeEntity.listSign.list[] list) {
			this.list = list;
		}

		public JuHeEntity.listXiTongXinxi.info[] getSysInfo() {
			return sysInfo;
		}

		public void setSysInfo(JuHeEntity.listXiTongXinxi.info[] sysInfo) {
			this.sysInfo = sysInfo;
		}
	}
}
