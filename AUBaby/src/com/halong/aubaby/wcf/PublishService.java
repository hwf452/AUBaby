package com.halong.aubaby.wcf;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class PublishService {
	private Context mContext;
	private AsyncHttpClient mHttpClient;
	private final String URL = ContantUtil.BASE_URL + "publish.ashx";
	private final static String LOG_TAG = "PublishService";
	private int relogin = 0;

	public PublishService(Context context) {
		this.mContext = context;
		this.mHttpClient = new AsyncHttpClient() {
			@Override
			public void cancelRequests(Context context,
					boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				super.cancelRequests(context, mayInterruptIfRunning);
				mHttpClient = null;
			}
		};
	}

	/**
	 * 发表通知评论信息
	 * 
	 */
	public void getReplay(int id, String str) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", id + "");
		params.put("b", str);
		post(params, "");
	}

	public void getReplay(String id, String str) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", id);
		params.put("b", str);
		post(params, "");
	}

	public void getReplay(String id, String str, String guid) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", id);
		params.put("b", str);
		params.put("c", guid);
		post(params, "");
	}

	public void getCommentReplay(String NoticeID, String commentID, String str,
			String guid) {
		RequestParams params = new RequestParams();
		params.put("methodName", "5");
		params.put("a", NoticeID);
		params.put("b", commentID);
		params.put("c", str);
		params.put("d", guid);
		post(params, "");
	}

	public void getCommentReplay(String NoticeID, String commentID, String str) {
		RequestParams params = new RequestParams();
		params.put("methodName", "5");
		params.put("a", NoticeID);
		params.put("b", commentID);
		params.put("c", str);
		post(params, "");
	}

	private void post(final RequestParams params, final String savePath) {
		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						if (!"".equals(savePath)) {
							SharedPreferencesHelper.setStringValue(mContext,
									savePath, content);
						}
						getBBSpaceData(content);
					} else {

						// 返回错误原因,重新调用次数小于5次时重新调用
						if (relogin < 5) {
							new ReturnValue() {
								public void whileFailure() {
									getBBSpaceDataFailure();
								};

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									post(params, savePath);
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
						// 获取数据失败
						Toast.makeText(mContext, "连接服务器失败,加载旧数据",
								Toast.LENGTH_SHORT).show();
						getBBSpaceData(SharedPreferencesHelper.getStringValue(
								mContext, savePath));
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

	/*
	 * 发布日记图片
	 */
	public void publishPhotos(final String path) {
		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		RequestParams params = new RequestParams();
		params.put("methodName", "7");
		File file = new File(path);
		Log.v(LOG_TAG, file.getAbsolutePath());
		try {
			params.put("file", new File(path));
		} catch (Exception e) {
			// TODO: handle exception
			Log.v(LOG_TAG, "f1 error");
		}
		mHttpClient.setTimeout(30000);
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						String id = jsonObject.getString("id");
						getBBSpaceData(id);
					} else {
						// 返回错误原因,重新登陆次数小于5次时重新登陆
						if (relogin < 5) {
							new ReturnValue() {
								public void whileFailure() {
									getBBSpaceDataFailure();
								};

								public void onReloginFailure() {
									getBBSpaceDataFailure();
								};

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									publishPhotos(path);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							getBBSpaceDataFailure();
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "onFailure");
				Log.v(LOG_TAG, error.toString());
				getBBSpaceDataFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
				Log.v(LOG_TAG, "onStart");
				startPublishPhotos();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.v(LOG_TAG, "onFinish");
			}
		});

	}

	/*
	 * 发布日记
	 */
	public void publishDiary(final String diaryContent, final String guids) {

		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		RequestParams params = new RequestParams();
		params.put("methodName", "8");
		params.put("a", diaryContent);
		params.put("d", guids);
		params.put("e",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						publishDiarySuccess();
					} else {
						// 返回错误原因,重新登陆次数小于5次时重新登陆
						if (relogin < 5) {
							new ReturnValue() {
								@Override
								public void whileFailure() {
									// TODO Auto-generated method stub
									super.whileFailure();
									getBBSpaceDataFailure();
								}

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									publishDiary(diaryContent, guids);
								}

								public void onReloginFailure() {
									getBBSpaceDataFailure();
								};
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							getBBSpaceDataFailure();
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				getBBSpaceDataFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
				Log.v(LOG_TAG, "onStart");
			}

		});

	}

	/*
	 * 发布通知图片
	 */
	public void publishNoticePhotos(final String path) {
		mHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		RequestParams params = new RequestParams();
		params.put("methodName", "9");
		try {
			params.put("file", new File(path));
		} catch (Exception e) {
			// TODO: handle exception
			Log.v(LOG_TAG, "f1 error");
		}
		mHttpClient.setTimeout(30000);
		mHttpClient.post(mContext, URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						String id = jsonObject.getString("id");
						getBBSpaceData(id);
					} else {
						// 返回错误原因,重新登陆次数小于5次时重新登陆
						if (relogin < 5) {
							new ReturnValue() {
								public void onReloginFailure() {
									getBBSpaceDataFailure();
								};

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									publishPhotos(path);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							getBBSpaceDataFailure();
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					getBBSpaceDataFailure();
				}

			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Log.v(LOG_TAG, "onFailure");
				Log.v(LOG_TAG, error.toString());
				getBBSpaceDataFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
				Log.v(LOG_TAG, "onStart");
				startPublishPhotos();
			}

		});

	}

	public void pblishNotice(final String receiver,
			final String publishContent, final int e, final String guid) {
		// TODO Auto-generated method stub
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		// 加急开关 64（2的6次方），语音开关32（2的5次方），视频开关 16，投票开关 8，多选开关
		// 4，评论开关2，签收开关1（这个说的是发送的通知是否需要签收，比如台风停课等重要通知）

		RequestParams params = new RequestParams();
		params.put("methodName", "3");
		params.put("a",
				SharedPreferencesHelper.getStringValue(mContext, Keys.CLASS_ID));
		if (receiver.equals("")) {
			params.put("b", "H");

		} else {
			params.put("b", "P");
			params.put("f", receiver);
		}
		params.put("c", "Android");
		params.put("d", publishContent);
		params.put("e", e + "");
		params.put("g", guid);

		asyncHttpClient.setCookieStore(new PersistentCookieStore(mContext));
		asyncHttpClient.post(mContext, URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						Log.v("111111", content);
						super.onSuccess(content);
						try {
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");
							Log.v("PublishNoticeActivity", content);
							if (result) {

								publishNoticeSuccess();
								Toast.makeText(mContext, "发布成功",
										Toast.LENGTH_SHORT).show();
							} else {
								// 返回错误原因,重新登陆次数小于5次时重新登陆
								if (relogin < 5) {
									new ReturnValue() {
										public void whileFailure() {
											publishNoticeFailure();
										};

										public void onReloginFailure() {
											publishNoticeFailure();
										};

										@Override
										public void onReloginSuccess() {
											// TODO Auto-generated method stub
											// session已过期或不存在,后台登陆后重新获取数据
											pblishNotice(receiver,
													publishContent, e, guid);
										}
									}.resultFalse(mContext, content);
									relogin++;
								} else {
									publishNoticeFailure();
									Toast.makeText(mContext, R.string.error,
											Toast.LENGTH_SHORT).show();
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							publishNoticeFailure();
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Toast.makeText(mContext, R.string.post_error,
								Toast.LENGTH_SHORT).show();
						publishNoticeFailure();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						relogin = 0;
						Log.v(LOG_TAG, "onStart");
						startPublishPhotos();
					}

				});

	}

	// 加载网络或本地数据
	public void getBBSpaceData(String content) {
	};

	// 加载数据失败
	public void getBBSpaceDataFailure() {
	};

	public void publishDiarySuccess() {
	}

	public void startPublishPhotos() {
	}

	public void publishNoticeSuccess() {
	}

	public void publishNoticeFailure() {
	}

}
