package com.halong.aubaby.wcf;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.entity.DiaryDetailEntity;
import com.halong.aubaby.entity.SDiaryEntity;
import com.halong.aubaby.entity.VDiaryEntity;
import com.halong.aubaby.entity.WDiaryEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/*
 * api_diary类接口，用户日记接口
 */
public abstract class DiaryService {
	private Context mContext;
	private AsyncHttpClient client;
	private String url = ContantUtil.BASE_URL + "diary.ashx";// 用户日记接口
	private Gson gson;
	private VDiaryEntity vEntity;// V视图式查看时数据
	private WDiaryEntity wEntity;// W瀑布流查看时数据
	private SDiaryEntity sEntity;// S单瀑布流查看时数据
	private DiaryDetailEntity dEntity;// 日记详情数据
	private final static String LOG_TAG = "DiaryService";

	private static String USERID;
	private static String GET_DIARY_V_OFFLINE_KEY;// V视图式全班日记数据关键字
	private static String GET_DIARY_W_OFFLINE_KEY;// W瀑布全班日记流数据关键字
	private static String GET_DIARY_S_OFFLINE_KEY;// S瀑布全班日记流数据关键字
	private static String GET_DIARY_V_USER_INFO_ID_OFFLINE_KEY;// V视图式当前用户数据关键字
	private static String GET_DIARY_W_USER_INFO_ID_OFFLINE_KEY;// W瀑布当前用户流数据关键字

	private int relogin = 0;// 重新调用接口次数

	public DiaryService(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		client = new AsyncHttpClient();

		gson = new Gson();
		USERID = String.valueOf(SharedPreferencesHelper.getIntValue(context,
				SharedPreferencesHelper.USERID));
		GET_DIARY_V_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "getDiaryVOffLine";
		GET_DIARY_W_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "getDiaryWOffLine";

		GET_DIARY_S_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "getDiarySOffLine";

		GET_DIARY_V_USER_INFO_ID_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "userInfoIDGetDiaryVOffLine";

		GET_DIARY_W_USER_INFO_ID_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "userInfoIDGetDiaryWOffLine";
	}

	/**
	 * 上拉下拉获得用户所在的班级更多日记,默认获取6条
	 */
	public void getDiaryV(final String firstDiaryID, final String lastDiaryID,
			final String pullState, final String diaryType) {
		getDiaryV(6 + "", null, firstDiaryID, lastDiaryID, null, null, null,
				pullState, diaryType);
	}

	/**
	 * 通过关键字搜索日记
	 */
	public void getDiaryV(final String firstDiaryID, final String lastDiaryID,
			final String startDate, final String endDate,
			final String searchKey, final String pullState,
			final String diaryType) {
		getDiaryV(null, null, firstDiaryID, lastDiaryID, startDate, endDate,
				searchKey, pullState, diaryType);
	}

	/**
	 * 获得userID用户的最新日记,"V"形展示方式
	 */
	public void getDiaryV(final String userInfoID, final String firstDiaryID,
			final String lastDiaryID, final String pullState,
			final String diaryType) {
		getDiaryV(6 + "", userInfoID, firstDiaryID, lastDiaryID, null, null,
				null, pullState, diaryType);
	}

	/**
	 * 获得用户所在的班级最新日记,"V"形展示方式 String firstDiaryID,final String
	 * lastDiaryID不可同时提交，获取最新数据提交firstDiaryID，加载更多数据提交firstDiaryID
	 * diaryType提交P，仅返回图片类型的日记，提交V仅返回视频类型的日记，不提交无效果。请在成长日记中提交P，在视频中心提交V
	 */
	public void getDiaryV(final String diaryCount, final String userInfoID,
			final String firstDiaryID, final String lastDiaryID,
			final String startDate, final String endDate,
			final String searchKey, final String pullState,
			final String diaryType) {
		// Object publishObject
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		if (pullState == Keys.ON_REFRESH) {
			// 提交最新id，用户刷新
			params.put("f", firstDiaryID);
		} else if (pullState == Keys.ON_LOAD) {
			// 提交列表中的最后一个id，用户加载更多
			params.put("a", lastDiaryID);
		} else {
		}
		params.put("b",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));// 班级id
		params.put("c", "V");// 得到获取的日记列表类型
		params.put("d", userInfoID);

		params.put("k", diaryCount);// 要获取日记的数量
		params.put("l", diaryType);// 请求的视频类型
		params.put("h", startDate);
		params.put("i", endDate);
		params.put("j", searchKey);
		Log.v(LOG_TAG, params.toString());
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				// Log.v(LOG_TAG+"getDiaryV", content);
				// int maxLogSize = 2000;
				// for (int i = 0; i <= content.length() / maxLogSize; i++) {
				// int start = i * maxLogSize;
				// int end = (i + 1) * maxLogSize;
				// end = end > content.length() ? content.length() : end;
				// Log.v(LOG_TAG, content.substring(start, end));
				// }

				try {
					setvEntity(gson.fromJson(content, VDiaryEntity.class));
					if (vEntity.getResult()) {
						// 保存最近的一次数据
						Log.v(LOG_TAG, "getResult");
						if (TextUtils.equals(pullState, Keys.ON_LOAD)) {
							onLoadMoreSuccsess();
						} else if (TextUtils.equals(pullState, Keys.ON_REFRESH)) {
							// if (vEntity.getObjInfo().length>0) {
							// 如果服务器有数据就保存
							// SharedPreferencesHelper.setStringValue(mContext,
							// GET_DIARY_V_OFFLINE_KEY, content);
							// }

							onRefreshSuccsess();
						} else if (TextUtils.equals(pullState, Keys.START_APP)) {
							// SharedPreferencesHelper.setStringValue(mContext,
							// GET_DIARY_V_OFFLINE_KEY, content);
							onRefreshSuccsess();
						}

					} else {
						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getDiaryV(diaryCount, userInfoID,
											firstDiaryID, lastDiaryID,
											startDate, endDate, searchKey,
											pullState, diaryType);
								}
							}.resultFalse(mContext, content);

						} else {
							Toast.makeText(mContext, "getDiaryV Success异常",
									Toast.LENGTH_SHORT).show();
						}
						if (TextUtils.equals(pullState, Keys.ON_LOAD)) {
							onLoadMoreFailure();
						} else if (TextUtils.equals(pullState, Keys.ON_REFRESH)) {
							onRefreshFailure();
						} else if (TextUtils.equals(pullState, Keys.START_APP)) {
							onRefreshFailure();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(mContext, "getDiaryV onFailure异常",
							Toast.LENGTH_SHORT).show();
					if (TextUtils.equals(pullState, Keys.ON_LOAD)) {
						onLoadMoreFailure();
					} else if (TextUtils.equals(pullState, Keys.ON_REFRESH)) {
						onRefreshFailure();
					} else if (TextUtils.equals(pullState, Keys.START_APP)) {
						onRefreshFailure();
					}

				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				// 获取数据失败
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				// 加载以往数据
				try {

					if (TextUtils.equals(pullState, Keys.ON_LOAD)) {
						onLoadMoreFailure();
					} else if (TextUtils.equals(pullState, Keys.ON_REFRESH)) {
						onRefreshFailure();
					} else if (TextUtils.equals(pullState, Keys.START_APP)) {
						setvEntity(gson.fromJson(SharedPreferencesHelper
								.getStringValue(mContext,
										GET_DIARY_V_OFFLINE_KEY),
								VDiaryEntity.class));
						onRefreshSuccsess();
					}
				} catch (Exception e) {
					// TODO: handle exception
					if (TextUtils.equals(pullState, Keys.ON_LOAD)) {
						onLoadMoreFailure();
					} else if (TextUtils.equals(pullState, Keys.ON_REFRESH)) {
						onRefreshFailure();
					} else if (TextUtils.equals(pullState, Keys.START_APP)) {
						onRefreshFailure();
					}
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	/*
	 * 获得用户所在的班级携带最新日记,"W"形展示方式
	 */
	public void getDiaryW(final String userInfoID, final String firstDiaryID,
			final String lastDiaryID, final String imgID, final String state,
			final String diaryType) {
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		if (state == Keys.ON_REFRESH) {
			// 提交最新id，用户刷新
			params.put("f", firstDiaryID);
		} else if (state == Keys.ON_LOAD) {
			// 提交列表中的最后一个id，用户加载更多
			params.put("a", lastDiaryID);
		} else {
		}
		params.put("b",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));// 班级id
		params.put("c", "W");// 得到获取的日记列表类型
		params.put("d", userInfoID);// 请求的用户
		params.put("e", imgID);
		params.put("k", "24");// 要获取日记的数量
		params.put("l", diaryType);// 请求的视频类型
		Log.v(LOG_TAG, params.toString());
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				// Log.v(LOG_TAG, content);
				// int maxLogSize = 2000;
				// for (int i = 0; i <= content.length() / maxLogSize; i++) {
				// int start = i * maxLogSize;
				// int end = (i + 1) * maxLogSize;
				// end = end > content.length() ? content.length() : end;
				// Log.v(LOG_TAG, content.substring(start, end));
				// }

				try {
					setwEntity(gson.fromJson(content, WDiaryEntity.class));
					if (wEntity.getResult()) {
						// 保存最近的一次数据

						if (TextUtils.equals(state, Keys.ON_LOAD)) {
							onLoadMoreSuccsess();
						} else if (TextUtils.equals(state, Keys.ON_REFRESH)) {
							// if (vEntity.getObjInfo().length>0) {
							// 如果服务器有数据就保存
							// SharedPreferencesHelper.setStringValue(mContext,
							// GET_DIARY_V_OFFLINE_KEY, content);
							// }

							onRefreshSuccsess();
						} else if (TextUtils.equals(state, Keys.START_APP)) {
							// SharedPreferencesHelper.setStringValue(mContext,
							// GET_DIARY_V_OFFLINE_KEY, content);
							onRefreshSuccsess();
							SharedPreferencesHelper.setStringValue(mContext,
									GET_DIARY_W_OFFLINE_KEY, content);
						}

						// getBBSpaceData();
					} else {

						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									getDiaryW(userInfoID, firstDiaryID,
											lastDiaryID, imgID, state,
											diaryType);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}
						if (TextUtils.equals(state, Keys.ON_LOAD)) {
							onLoadMoreFailure();
						} else if (TextUtils.equals(state, Keys.ON_REFRESH)) {
							onRefreshFailure();
						} else if (TextUtils.equals(state, Keys.START_APP)) {
							onRefreshFailure();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.v(LOG_TAG, "w post error");
					if (TextUtils.equals(state, Keys.ON_LOAD)) {
						onLoadMoreFailure();
					} else if (TextUtils.equals(state, Keys.ON_REFRESH)) {
						onRefreshFailure();
					} else if (TextUtils.equals(state, Keys.START_APP)) {
						onRefreshFailure();
					}
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "w onFailure");
				// 获取数据失败
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				// // 加载以往数据
				// try {
				// setwEntity(gson.fromJson(SharedPreferencesHelper
				// .getStringValue(mContext, GET_DIARY_W_OFFLINE_KEY),
				// WDiaryEntity.class));
				// getBBSpaceData();
				// } catch (Exception e) {
				// // TODO: handle exception
				// getBBSpaceDataFailure();
				// }

				if (TextUtils.equals(state, Keys.ON_LOAD)) {
					onLoadMoreFailure();
				} else if (TextUtils.equals(state, Keys.ON_REFRESH)) {
					onRefreshFailure();
				} else if (TextUtils.equals(state, Keys.START_APP)) {
					onRefreshFailure();
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	/*
	 * /* 获得userID用户的最新日记, "W"形展示方式
	 */
	public void getDiaryW(final String userInfoID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		params.put("c", "W");
		params.put("b",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		params.put("d", userInfoID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				// Log.v(LOG_TAG, content);
				// int maxLogSize = 2000;
				// for (int i = 0; i <= content.length() / maxLogSize; i++) {
				// int start = i * maxLogSize;
				// int end = (i + 1) * maxLogSize;
				// end = end > content.length() ? content.length() : end;
				// Log.v(LOG_TAG, content.substring(start, end));
				// }

				try {
					setwEntity(gson.fromJson(content, WDiaryEntity.class));
					if (wEntity.getResult()) {
						// 保存最近的一次数据，将本人数据和其他用户数据分开保存
						SharedPreferencesHelper.setStringValue(mContext,
								GET_DIARY_W_USER_INFO_ID_OFFLINE_KEY
										+ userInfoID, content);
						getBBSpaceData();
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									getDiaryW(userInfoID);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.v(LOG_TAG, "w_user post error");
					getBBSpaceDataFailure();
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "w_user onFailure");
				// 获取数据失败
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				// 加载以往数据
				try {
					setwEntity(gson.fromJson(SharedPreferencesHelper
							.getStringValue(mContext,
									GET_DIARY_W_USER_INFO_ID_OFFLINE_KEY
											+ userInfoID), WDiaryEntity.class));
					getBBSpaceData();
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	/*
	 * 获得用户所在的班级携带最新日记,单瀑布流"S"形展示方式
	 */
	public void getDiaryS() {
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		params.put("c", "S");
		params.put("b",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				// Log.v(LOG_TAG, content);
				// int maxLogSize = 2000;
				// for (int i = 0; i <= content.length() / maxLogSize; i++) {
				// int start = i * maxLogSize;
				// int end = (i + 1) * maxLogSize;
				// end = end > content.length() ? content.length() : end;
				// Log.v(LOG_TAG, content.substring(start, end));
				// }

				try {
					setsEntity(gson.fromJson(content, SDiaryEntity.class));
					if (sEntity.getResult()) {
						// 保存最近的一次数据
						SharedPreferencesHelper.setStringValue(mContext,
								GET_DIARY_S_OFFLINE_KEY, content);
						// 如果数据返回正确，执行方法
						getBBSpaceData();
					} else {

						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									getDiaryS();
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.v(LOG_TAG, "s post error");
					getBBSpaceDataFailure();
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "s onFailure");
				// 获取数据失败
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				// 加载以往数据
				try {
					setsEntity(gson.fromJson(SharedPreferencesHelper
							.getStringValue(mContext, GET_DIARY_S_OFFLINE_KEY),
							SDiaryEntity.class));
					getBBSpaceData();
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	/*
	 * 供其它类获取数据
	 */
	public VDiaryEntity getvEntity() {
		return vEntity;
	}

	/*
	 * 获取数据
	 */
	private void setvEntity(VDiaryEntity entity) {
		this.vEntity = entity;
	}

	/*
	 * 获取数据
	 */
	public WDiaryEntity getwEntity() {
		return wEntity;
	}

	/*
	 * 供其它类获取数据
	 */
	private void setwEntity(WDiaryEntity wEntity) {
		this.wEntity = wEntity;
	}

	public SDiaryEntity getsEntity() {
		return sEntity;
	}

	private void setsEntity(SDiaryEntity sEntity) {
		this.sEntity = sEntity;
	}

	public void deleteDiary(final String diaryID) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", diaryID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						getBBSpaceData();
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									deleteDiary(diaryID);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				getBBSpaceDataFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	/*
	 * 搜索日记详情
	 */
	public void getDiaryDetail(final String diaryID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a", diaryID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				int maxLogSize = 2000;
				for (int i = 0; i <= content.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > content.length() ? content.length() : end;
					Log.v(LOG_TAG, content.substring(start, end));
				}

				try {

					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");

					if (result) {
						setdEntity(gson.fromJson(content,
								DiaryDetailEntity.class));
						getBBSpaceData();
					} else {
						Log.v(LOG_TAG, "false" + relogin + "");
						if (relogin < 5) {// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据

									getDiaryDetail(diaryID);
								}

								public void whileFailure() {
									getBBSpaceDataFailure();
								};
							}.resultFalse(mContext, content);
							relogin++;
							Log.v(LOG_TAG, relogin + "++");

						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
							getBBSpaceDataFailure();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.v(LOG_TAG, "detail post error");
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
					getBBSpaceDataFailure();
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "detail onFailure");
				// 获取数据失败
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				getBBSpaceDataFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public DiaryDetailEntity getdEntity() {
		return dEntity;
	}

	private void setdEntity(DiaryDetailEntity dEntity) {
		this.dEntity = dEntity;
	}

	// 加载网络或本地数据
	public abstract void getBBSpaceData();

	// 加载数据失败
	public abstract void getBBSpaceDataFailure();

	public void onRefreshSuccsess() {
	};

	public void onRefreshFailure() {
	};

	public void onLoadMoreSuccsess() {
	};

	public void onLoadMoreFailure() {
	};
}
