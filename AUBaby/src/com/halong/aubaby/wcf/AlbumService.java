package com.halong.aubaby.wcf;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.contant.ReturnValue;
import com.halong.aubaby.entity.AlbumListEntity;
import com.halong.aubaby.entity.AlbumPhotosEntity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public abstract class AlbumService {
	private Context mContext;
	private AsyncHttpClient client;
	private String url = ContantUtil.BASE_URL + "ablum.ashx";// 用户日记接口
	private final static String LOG_TAG = "AlbumService";
	private AlbumListEntity aListEntity;
	private AlbumPhotosEntity albumPhotosEntity;
	private Gson gson;
	private static String USERID;// 用户id
	private static String ALBUM_LIST_OFFLINE_KEY;// V视图式全班日记数据关键字
	private int relogin = 0;// 重新调用线程次数

	public AlbumService(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		client = new AsyncHttpClient();
		gson = new Gson();
		USERID = String.valueOf(SharedPreferencesHelper.getIntValue(context,
				SharedPreferencesHelper.USERID));
		ALBUM_LIST_OFFLINE_KEY = USERID
				+ SharedPreferencesHelper.getStringValue(mContext,
						Keys.CLASS_ID) + "albumListOffLine";
	}

	// public void deleteAlbum(String albumID) {
	// // TODO Auto-generated method stub
	// RequestParams params = new RequestParams();
	// params.put("methodName", "4");
	// params.put("a", albumID);
	// client.setCookieStore(new PersistentCookieStore(mContext));
	// client.post(url, params, new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String content) {
	// // TODO Auto-generated method stub
	// super.onSuccess(content);
	// try {
	// if (new JSONObject(content).getBoolean("result")) {
	//
	// } else {
	// // 返回错误原因,重新调用次数小于5次时重新调用
	// if (relogin < 5) {
	// // 返回错误原因
	// new ReturnValue() {
	//
	// @Override
	// public void onReloginSuccess() {
	// // TODO Auto-generated method stub
	// // session已过期或不存在,后台登陆后重新获取数据
	// getAlbumList();
	// }
	// }.resultFalse(mContext, content);
	// relogin++;
	// } else {
	// Toast.makeText(mContext, R.string.error,
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// relogin = 0;
	// }
	// });
	// }

	public void getAlbumList() {
		RequestParams params = new RequestParams();
		params.put("methodName", "2");
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				// Log.v(LOG_TAG, content);
				try {
					setaListEntity(gson
							.fromJson(content, AlbumListEntity.class));
					if (aListEntity.getResult()) {
						// 保存最近的一次数据
						SharedPreferencesHelper.setStringValue(mContext,
								ALBUM_LIST_OFFLINE_KEY, content);
						getBBSpaceData();
					} else {
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getAlbumList();
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
					Log.v(LOG_TAG, "albumList post error");
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				Log.v(LOG_TAG, "albumList Failed to parse");
				Toast.makeText(mContext, R.string.local_entity,
						Toast.LENGTH_SHORT).show();
				try {
					setaListEntity(gson.fromJson(SharedPreferencesHelper
							.getStringValue(mContext, ALBUM_LIST_OFFLINE_KEY),
							AlbumListEntity.class));
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

	public void getAlbumPhotosMore(final String ablumID, final String id) {

		RequestParams params = new RequestParams();
		params.put("methodName", "8");
		params.put("a", ablumID);
		params.put("b", id);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				// Log.v(LOG_TAG, content);
				try {
					setAlbumPhotosEntity(gson.fromJson(content,
							AlbumPhotosEntity.class));
					if (albumPhotosEntity.getResult()) {
						getAlbumPhotosMoreSuccess();
					} else {
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getAlbumPhotosMore(ablumID, id);
								}
							}.resultFalse(mContext, content);
							relogin++;
						} else {
							Toast.makeText(mContext, R.string.error,
									Toast.LENGTH_SHORT).show();
							getAlbumPhotosMoreFailure();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
					getAlbumPhotosMoreFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				getAlbumPhotosMoreFailure();

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});

	}

	public void getAlbumPhotos(final String ablumID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "8");
		params.put("a", ablumID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				// Log.v(LOG_TAG, content);
				try {
					setAlbumPhotosEntity(gson.fromJson(content,
							AlbumPhotosEntity.class));
					if (albumPhotosEntity.getResult()) {
						// 保存最近的一次数据
						SharedPreferencesHelper.setStringValue(mContext,
								ALBUM_LIST_OFFLINE_KEY + ablumID, content);
						getBBSpaceData();
					} else {
						if (relogin < 5) {
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									getAlbumPhotos(ablumID);
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
					Log.v(LOG_TAG, "AlbumPhotos post error");
					Toast.makeText(mContext, R.string.access_error,
							Toast.LENGTH_SHORT).show();
					getBBSpaceDataFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				Log.v(LOG_TAG, "AlbumPhotos Failed to parse");
				Toast.makeText(mContext, R.string.local_entity,
						Toast.LENGTH_SHORT).show();
				try {
					setAlbumPhotosEntity(gson.fromJson(SharedPreferencesHelper
							.getStringValue(mContext, ALBUM_LIST_OFFLINE_KEY
									+ ablumID), AlbumPhotosEntity.class));
					if (albumPhotosEntity == null) {
						return;
					}
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

	public void collectAlbum(final String ablumID) {
		RequestParams params = new RequestParams();
		params.put("methodName", "9");
		params.put("a", ablumID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.v(LOG_TAG, content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.collect_success,
								Toast.LENGTH_SHORT).show();
					} else {
						Log.v(LOG_TAG, content);
						if (relogin < 5) {
							// 返回错误原因,重新调用次数小于5次时重新调用
							// 返回错误原因
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									// session已过期或不存在,后台登陆后重新获取数据
									collectAlbum(ablumID);
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
				Log.v(LOG_TAG, "albumList Failed to parse");
				Toast.makeText(mContext, R.string.post_error,
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

	public void collectPhoto(final String photoID) {
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

				Log.v(LOG_TAG, content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.collect_success,
								Toast.LENGTH_SHORT).show();
						getBBSpaceData();
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							getBBSpaceDataFailure();
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									collectPhoto(photoID);
								}

								public void collectedThisPhoto() {
									getBBSpaceData();
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

	public void unCollectPhoto(final String collectID,
			final AlbumPhotosEntity.itemList.item item) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("methodName", "6");
		params.put("a", collectID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				Log.v(LOG_TAG, content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.uncollect_success,
								Toast.LENGTH_SHORT).show();
						unCollectPhotoSuccess(item);
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									unCollectPhoto(collectID, item);
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
					unCollectPhotoFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				unCollectPhotoFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public void unCollectAlbum(final String albumID,
			final AlbumListEntity.FolderList.Folder folder) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("methodName", "4");
		params.put("a", albumID);
		client.setCookieStore(new PersistentCookieStore(mContext));
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				Log.v(LOG_TAG, content);
				try {
					if (new JSONObject(content).getBoolean("result")) {
						Toast.makeText(mContext, R.string.uncollect_success,
								Toast.LENGTH_SHORT).show();
						unCollectAlbumSuccess(folder);
					} else {
						if (relogin < 5) {// session已过期或不存在,后台登陆
							new ReturnValue() {

								@Override
								public void onReloginSuccess() {
									// TODO Auto-generated method stub
									unCollectAlbum(albumID, folder);
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
					unCollectAlbumFailure();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT)
						.show();
				unCollectAlbumFailure();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				relogin = 0;
			}
		});
	}

	public AlbumListEntity getaListEntity() {
		return aListEntity;
	}

	private void setaListEntity(AlbumListEntity aListEntity) {
		this.aListEntity = aListEntity;
	}

	// 获取数据之后
	public abstract void getBBSpaceData();

	// 加载数据失败
	public abstract void getBBSpaceDataFailure();

	public AlbumPhotosEntity getAlbumPhotosEntity() {
		return albumPhotosEntity;
	}

	private void setAlbumPhotosEntity(AlbumPhotosEntity albumPhotosEntity) {
		this.albumPhotosEntity = albumPhotosEntity;
	}

	public void unCollectPhotoSuccess(AlbumPhotosEntity.itemList.item item) {
	}

	public void unCollectPhotoFailure() {
	}

	public void unCollectAlbumSuccess(AlbumListEntity.FolderList.Folder folder) {
	}

	public void unCollectAlbumFailure() {
	}

	public void getAlbumPhotosMoreSuccess() {
		// TODO Auto-generated method stub
	}

	public void getAlbumPhotosMoreFailure() {
	}
}
