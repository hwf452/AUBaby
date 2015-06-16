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

public abstract class NoticeService {

	private Context mContext;
	private AsyncHttpClient mHttpClient;

	private final String UNREADNUM = "UnreadNum"; // 未读通知数量
	private final String HNOTICELIST = "HNoticeList"; // H类型的通知列表
	private final String SNOTICELIST = "SNoticeList";// S类型的通知列表
	private final String APARTUNREADNOTICE = "aPartUnreadNotice";// 最新消息概要
	private final String URL = ContantUtil.BASE_URL + "notice.ashx";
	private int relogin = 0;// 重新调用接口次数

	public NoticeService(Context context) {
		this.mContext = context;
		this.mHttpClient = new AsyncHttpClient();
	}

	/**
	 * 获取未读消息数量
	 */
	public void getAPartUnreadNotice() {
		RequestParams params = new RequestParams();
		params.put("methodName", "15");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));

		post(params, APARTUNREADNOTICE);
	}

	/**
	 * 获取未读消息数量
	 */
	public void getUnreadNum() {
		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));

		post(params, UNREADNUM);
	}

	/**
	 * 获取H通知
	 */
	public void getHNoticeList(String classid, int currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", "H");
		params.put("b", classid);
		params.put("c", currentPage + "");

		if (currentPage == 0) {
			post(params, HNOTICELIST);
		} else {
			post(params, "");
		}
	}

	/**
	 * 获取S通知
	 */
	public void getSNoticeList(String classid, int currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", "S");
		params.put("b", classid);
		params.put("c", currentPage + "");

		if (currentPage == 0) {
			post(params, SNOTICELIST);
		} else {
			post(params, "");
		}
	}

	/**
	 * 将通知标记为已读
	 */
	public void setNoticeInfoIsRead(int id) {
		RequestParams params = new RequestParams();
		params.put("methodName", "16");
		params.put("a", id + "");

		post(params, "");
	}

	/**
	 * 查看通知详情
	 */
	public void getNoticeInfoDetail(int id) {
		RequestParams params = new RequestParams();
		params.put("methodName", "5");
		params.put("a", id + "");

		post(params, "");
	}

	/**
	 * 将通知标记为已读
	 */
	public void setNoticeInfoIsRead(String id) {
		RequestParams params = new RequestParams();
		params.put("methodName", "16");
		params.put("a", id);

		post(params, "");
	}

	/**
	 * 获取评论信息
	 * 
	 */
	public void getNoticeReplay(int id, String classid, int currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "6");
		params.put("a", id + "");
		params.put("b", classid);
		params.put("c", currentPage + "");

		post(params, "");
	}

	public void getNoticeReplay(int id, String classid, String currentPage) {
		RequestParams params = new RequestParams();
		params.put("methodName", "6");
		params.put("a", id + "");
		params.put("b", classid);
		params.put("c", currentPage);

		post(params, "");
	}

	private void post(final RequestParams params, final String savePath) {
		Log.v("NoticeService", params.toString());
		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					Log.v("NoticeService", content);
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
							Toast.makeText(mContext, R.string.access_error,
									Toast.LENGTH_SHORT).show();
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
						String loacl = SharedPreferencesHelper.getStringValue(
								mContext,
								SharedPreferencesHelper.getIntValue(mContext,
										SharedPreferencesHelper.USERID)
										+ SharedPreferencesHelper
												.getStringValue(mContext,
														Keys.CLASS_ID)
										+ savePath);
						if (loacl != null) {
							if (!loacl.equals("")) {
								getBBSpaceData(loacl);
								Toast.makeText(mContext, R.string.local_entity,
										Toast.LENGTH_SHORT).show();
								return;
							}
						}
						getBBSpaceDataFailure();

					} else {
						getBBSpaceDataFailure();
						Toast.makeText(mContext, R.string.access_error,
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
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
