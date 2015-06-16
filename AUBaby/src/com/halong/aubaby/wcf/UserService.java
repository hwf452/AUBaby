package com.halong.aubaby.wcf;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.entity.ClassDetailEntity;
import com.halong.aubaby.entity.OtherUserInfoEntity;
import com.halong.aubaby.entity.UserInfoEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*
 * api_user类接口
 */
public abstract class UserService {
	private Context mContext;
	private AsyncHttpClient client;
	private Gson gson;
	private String url = ContantUtil.BASE_URL + "user.ashx";
	private UserInfoEntity userInfoEntity;// 本人用户信息
	private OtherUserInfoEntity otherUserInfoEntity;// 其他用户信息
	private ClassDetailEntity classDetailEntity;// 班级信息
	private static final String LOG_TAG = "UserService";

	private static String GET_USER_INFO_OFFLINE_KEY;// 当前用户数据关键字
	private int relogin = 0;// 重新调用接口次数

	public UserService(Context context) {
		this.mContext = context;
		client = new AsyncHttpClient();
		gson = new Gson();

		GET_USER_INFO_OFFLINE_KEY = SharedPreferencesHelper.getIntValue(
				context, SharedPreferencesHelper.USERID)
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "getUserInfoOffLine";
	}

	/*
	 * 查看本人用户概览
	 */public void getUserInfo() {
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		params.put("a", SharedPreferencesHelper.getStringValue(mContext,
				Keys.CLASS_ID));
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO 自动生成的方法存根
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					setUserInfoEntity(gson.fromJson(content,
							UserInfoEntity.class));
					Boolean result = userInfoEntity.getResult();
					if (result) {
						// 保存最近的一次数据
						SharedPreferencesHelper.setStringValue(mContext,
								GET_USER_INFO_OFFLINE_KEY, content);
						getBBSpaceData();
					} else {

						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {

							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getUserInfo();
								}

								public void whileFailure() {
									getBBSpaceDataFailure();
								};
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
							.show();
					getBBSpaceDataFailure();
				}

			}

			public void onFailure(Throwable error) {
				Log.v(LOG_TAG, "Failed to parse");
				try {
					setUserInfoEntity(gson.fromJson(
							SharedPreferencesHelper.getStringValue(mContext,
									GET_USER_INFO_OFFLINE_KEY),
							UserInfoEntity.class));
					
					
					
					getBBSpaceData();
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}

			};

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		}

		);
	}

	public UserInfoEntity getUserInfoEntity() {
		return userInfoEntity;
	}

	public void setUserInfoEntity(UserInfoEntity userInfoEntity) {
		this.userInfoEntity = userInfoEntity;
	}

	/*
	 * 查看其他用户概览
	 */public void getOtherInfo(final String otherID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a", otherID);
		params.put("b", SharedPreferencesHelper.getStringValue(mContext,
				Keys.CLASS_ID));
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO 自动生成的方法存根
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					setOtherUserInfoEntity(gson.fromJson(content,
							OtherUserInfoEntity.class));
					Boolean result = otherUserInfoEntity.getResult();
					if (result) {
						getBBSpaceData();
					} else {
						Log.v(LOG_TAG, content);

						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getOtherInfo(otherID);
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
					Toast.makeText(mContext, R.string.post_error,
							Toast.LENGTH_SHORT).show();
					getBBSpaceDataFailure();
				}

			}

			public void onFailure(Throwable error) {
				Log.v(LOG_TAG, "Failed to parse");
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				getBBSpaceDataFailure();
			};

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		}

		);
	}

	public OtherUserInfoEntity getOtherUserInfoEntity() {
		return otherUserInfoEntity;
	}

	private void setOtherUserInfoEntity(OtherUserInfoEntity otherUserInfoEntity) {
		this.otherUserInfoEntity = otherUserInfoEntity;
	}

	/*
	 * 查看班级信息
	 */public void getClassDetail(final String classID, final String userInfoID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "3");
		params.put("a", classID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO 自动生成的方法存根
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					setClassDetailEntity(gson.fromJson(content,
							ClassDetailEntity.class));
					
					
					
					
					Boolean result = classDetailEntity.getResult();
					if (result) {
						// 保存最近的一次数据
						SharedPreferencesHelper.setStringValue(mContext,
								classID + userInfoID, content);
						getBBSpaceData();
					} else {

						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getClassDetail(classID, userInfoID);
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
					Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
							.show();
					getBBSpaceDataFailure();
				}

			}

			public void onFailure(Throwable error) {
				Log.v(LOG_TAG, "getClassDetail Failed to parse");
				Toast.makeText(mContext, R.string.post_error,
						Toast.LENGTH_SHORT).show();
				try {
					setClassDetailEntity(gson.fromJson(SharedPreferencesHelper
							.getStringValue(mContext, classID + userInfoID),
							ClassDetailEntity.class));

					getBBSpaceData();
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}

			};

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		}

		);
	}

	public ClassDetailEntity getClassDetailEntity() {
		return classDetailEntity;
	}

	private void setClassDetailEntity(ClassDetailEntity classDetailEntity) {
		this.classDetailEntity = classDetailEntity;
	}

	// 获取数据之后
	public abstract void getBBSpaceData();

	// 加载数据失败
	public abstract void getBBSpaceDataFailure();

}