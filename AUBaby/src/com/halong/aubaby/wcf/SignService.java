package com.halong.aubaby.wcf;

import org.json.JSONObject;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public abstract class SignService {

	private Context mContext;
	private AsyncHttpClient mHttpClient;

	private final String TEACHERSIGNLIST = "TeacherSignList"; // 教师签到列表
	private final String USERSIGNLIST = "UserSignList";// 本人签到列表
	private final String OTHERUSERSIGNLIST = "OtherUserSignList";// 本人签到列表
	private final String URL = ContantUtil.BASE_URL + "sign.ashx";
	private int relogin = 0;// 重新调用接口次数

	public SignService(Context context) {
		this.mContext = context;
		this.mHttpClient = new AsyncHttpClient();
	}

	/**
	 * 教师在管理界面获得签到列表准备签到
	 */
	public void getTeacherSignList() {
		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));

		post(params, TEACHERSIGNLIST);
	}

	/**
	 * 本人的签到表
	 */
	public void getUserSignList(String currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		params.put("b", currentPage);
		if (currentPage.equals("")) {
			post(params, USERSIGNLIST);
		} else {
			post(params, "");
		}

	}

	/**
	 * 签入
	 */

	public void signIn(String userID) {

		RequestParams params = new RequestParams();
		params.put("methodName", "3");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		params.put(
				"b",
				userID);
		post(params, "");
	}

	/**
	 * 签出
	 */

	public void signOut(String userID) {

		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		params.put(
				"b",
				userID);
		post(params, "");
	}
	/**
	 * 查看他人的签到表
	 */
	public void getOtherUserSignList(String userID, String currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "5");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		params.put("b", currentPage);
		params.put("c", userID);
		if (currentPage.equals("")) {
			post(params, OTHERUSERSIGNLIST);
		} else {
			post(params, "");
		}

	}

	private void post(final RequestParams params, final String savePath) {
		Log.v("SignService", params.toString());
		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					Log.v("SignService", content);
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						if (!"".equals(savePath)) {
							SharedPreferencesHelper.setStringValue(
									mContext,
									SharedPreferencesHelper.getIntValue(
											mContext,
											SharedPreferencesHelper.USERID)
											+ SharedPreferencesHelper
													.getStringValue(mContext,
															Keys.CLASS_ID)
											+ savePath, content);
						}
						getBBSpaceData(content);
					} else {
						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									post(params, savePath);
								}

								public void whileFailure() {
									getBBSpaceDataFailure();
								};
							}.resultFalse(mContext, content);
						} else {
							getBBSpaceDataFailure();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				// 加载以往数据
				try {
					if (!"".equals(savePath)) {
						getBBSpaceData(SharedPreferencesHelper.getStringValue(
								mContext,
								SharedPreferencesHelper.getIntValue(mContext,
										SharedPreferencesHelper.USERID)
										+ SharedPreferencesHelper
												.getStringValue(mContext,
														Keys.CLASS_ID)
										+ savePath));
					} else {
						getBBSpaceDataFailure();
					}
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

	// 加载网络或本地数据
	public void getBBSpaceData(String content) {
	};

	// 加载数据失败
	public void getBBSpaceDataFailure() {
	};

}
