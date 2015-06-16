package com.halong.aubaby.wcf;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.entity.DiaryCommentsEntity;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class CommentService {
	private Context mContext;
	private AsyncHttpClient client;
	private Gson gson;
	private String url = ContantUtil.BASE_URL + "comment.ashx";
	private int relogin = 0;// 重新调用接口次数
	private DiaryCommentsEntity diaryCommentsEntity;// 评论数据

	public CommentService(Context context) {
		mContext = context;
		client = new AsyncHttpClient();
		gson = new Gson();
	}

	public void praiseDiary(final String diaryID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", diaryID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v("111", content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						praiseDiaryOrPhotoSuccess(new JSONObject(content)
								.getString("priseCntOfDiary"));
						Toast.makeText(mContext, R.string.praise_success,
								Toast.LENGTH_SHORT).show();

					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									praiseDiary(diaryID);
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
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.praise_failure,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void praisePhoto(final String photoID) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("methodName", "5");
		params.put("a", photoID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.praise_success,
								Toast.LENGTH_SHORT).show();
						praiseDiaryOrPhotoSuccess(new JSONObject(content)
								.getString("priseCntOfDiary"));
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							praisePhotoFailure();
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									praisePhoto(photoID);
								}

								public void praisedThisPhoto() {
									praisePhotoSuccess();
								};

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
					praisePhotoFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				praisePhotoFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void postComments(final String diaryID, final String comment) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		params.put("a", diaryID);
		params.put("b", comment);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v("11111", content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.post_commets_success,
								Toast.LENGTH_SHORT).show();
						setDiaryCommentsEntity(gson.fromJson(content,
								DiaryCommentsEntity.class));
						commentSussess();
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							commentFailure();
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									postComments(diaryID, comment);
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
					commentFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				commentFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void getDiaryComments(final String diaryID, final String commentID) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("methodName", "1");
		params.put("a", diaryID);
		params.put("b", commentID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v("UserService", content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						setDiaryCommentsEntity(gson.fromJson(content,
								DiaryCommentsEntity.class));
						getDiaryCommentsSuccess();
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							commentFailure();
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									getDiaryComments(diaryID, commentID);
								}

							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
							getDiaryCommentsFailure();
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					getDiaryCommentsFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.get_comments_failure, Toast.LENGTH_SHORT)
						.show();
				getDiaryCommentsFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void deleteComment(final String commentID,
			final DiaryCommentsEntity.ObjInfo objInfo) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("methodName", "3");
		params.put("a", commentID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						deleteCommentSuccess(objInfo);
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									deleteComment(commentID, objInfo);
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
					deleteCommentFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				deleteCommentFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void praisePhotoSuccess() {
	}

	public void praiseDiaryOrPhotoSuccess(String praiseNumber) {
		// TODO Auto-generated method stub
	}

	public void praisePhotoFailure() {
		// TODO Auto-generated method stub

	}

	public void commentSussess() {
		// TODO Auto-generated method stub

	}

	public void commentFailure() {
		// TODO Auto-generated method stub

	}

	public void getDiaryCommentsSuccess() {
		// TODO Auto-generated method stub

	}

	public void getDiaryCommentsFailure() {
		// TODO Auto-generated method stub

	}

	public void deleteCommentSuccess(DiaryCommentsEntity.ObjInfo objInfo) {
	}

	public void deleteCommentFailure() {
	}

	public DiaryCommentsEntity getDiaryCommentsEntity() {
		return diaryCommentsEntity;
	}

	public void setDiaryCommentsEntity(DiaryCommentsEntity diaryCommentsEntity) {
		this.diaryCommentsEntity = diaryCommentsEntity;
	}
}
