package com.halong.aubaby.contant;

import org.json.JSONException;
import org.json.JSONObject;

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

public abstract class ReturnValue {
	// public static final String ="";

	// @本文档描述系统涉及到的送给客户端的信息
	// @本文档仅包含与客户端交互的变量，为了方便交互，均以数字编码标示
	public static final String DCL001 = "DCL001";
	public static final String DCL001_TXT = "没有找到班级(群)或没有管理员权限";
	public static final String DCL002 = "DCL002";
	public static final String DCL002_TXT = "已存在相同且未过期的邀请码，请换一个试试";
	public static final String DCL003 = "DCL003";
	public static final String DCL003_TXT = "邀请码不存在或已过期";
	public static final String DCL004 = "DCL004";
	public static final String DCL004_TXT = "您的群已经达到人数上限";
	public static final String DCL005 = "DCL005";
	public static final String DCL005_TXT = "您正在申请加入官方群（班级），官方群不允许当天退群后又加入，请明天再试";
	public static final String DCL006 = "DCL006";
	public static final String DCL006_TXT = "您已经加入了此班级（群）";
	public static final String DCO001 = "DCO001";
	public static final String DCO001_TXT = "未找到相册或相册不存在";
	public static final String DCO002 = "DCO002";
	public static final String DCO002_TXT = "您没有权限查看和收藏这个图片";
	public static final String DCO003 = "DCO003";
	public static final String DCO003_TXT = "您已经在该相册收藏过这个图片了";
	public static final String DCO004 = "DCO004";
	public static final String DCO004_TXT = "未找到要收藏的图片";
	public static final String DCO005 = "DCO005";
	public static final String DCO005_TXT = "未找到要收藏的相册";
	public static final String DCO006 = "DCO006";
	public static final String DCO006_TXT = "未找到已收藏的图片";
	public static final String DDC001 = "DDC001";
	public static final String DDC001_TXT = "未找到评论或没有权限操作";
	public static final String DDC002 = "DDC002";
	public static final String DDC002_TXT = "您已经赞过这篇日记了";
	public static final String DDC003 = "DDC003";
	public static final String DDC003_TXT = "您已经赞过这这个图片了";
	public static final String DNO001 = "DNO001";
	public static final String DNO001_TXT = "未找到通知或者通知不允许投票";
	public static final String DNO002 = "DNO002";
	public static final String DNO002_TXT = "您已经投过票了";
	public static final String DNO003 = "DNO003";
	public static final String DNO003_TXT = "未找到通知或者通知不需要签收";
	public static final String DNO004 = "DNO004";
	public static final String DNO004_TXT = "您已经签收过了";
	public static final String DNO005 = "DNO005";
	public static final String DNO005_TXT = "没有找到通知或者没有权限操作";
	public static final String DPU001 = "DPU001";
	public static final String DPU001_TXT = "发布家校通必须具有对应班级（群）的管理员权限";
	public static final String DPU002 = "DPU002";
	public static final String DPU002_TXT = "接收列表中有用户不在班级中，发布失败";
	public static final String DPU003 = "DPU003";
	public static final String DPU003_TXT = "回复评论错误，未找到原评论";
	public static final String DPU004 = "DPU004";
	public static final String DPU004_TXT = "您已经回复过了";
	public static final String DPU005 = "DPU005";
	public static final String DPU005_TXT = "仅通知的发布人可以回复评论";
	// @UserDAL.cs
	public static final String DUS001 = "DUS001";
	public static final String DUS001_TXT = "用户不存在";
	public static final String DUS002 = "DUS002";
	public static final String DUS002_TXT = "昵称(20)或签名(100)长度超过限制";
	public static final String DUS003 = "DUS003";
	public static final String DUS003_TXT = "未能找到群名片";
	public static final String DUS004 = "DUS004";
	public static final String DUS004_TXT = "您没有管理员权限，无法进行此操作";
	public static final String DUS005 = "DUS005";
	public static final String DUS005_TXT = "班级（群）管理员是不能退群的";
	public static final String DUS006 = "DUS006";
	public static final String DUS006_TXT = "该用户已经不属于此班级（群）";
	public static final String EX = "EX=#";
	// public static final String EX_TXT = "在测试期间，一旦有这个标记，直接显示错误信息";
	public static final String EX_TXT = "错误信息";
	// @ClassBLL.cs
	public static final String BCL001 = "BCL001";
	public static final String BCL001_TXT = "班级编号转换错误";
	public static final String BCL002 = "BCL002";
	public static final String BCL002_TXT = "没有找到班级(群)或不是班级（群）成员";
	public static final String BCL003 = "BCL003";
	public static final String BCL003_TXT = "邀请码不能超过10位，可以有任意字符组成";
	public static final String BCL004 = "BCL004";
	public static final String BCL004_TXT = "日期格式转换错误";
	// @CollectionBLL.cs
	public static final String BCO001 = "BCO001";
	public static final String BCO001_TXT = "创建失败，存在同名相册";
	public static final String BCO002 = "创建类型错误";
	public static final String BCO002_TXT = "创建类型错误";
	public static final String BCO003 = "BCO003";
	public static final String BCO003_TXT = "新相册名称不能为空";
	public static final String BCO004 = "BCO004";
	public static final String BCO004_TXT = "收藏夹id转换失败";
	public static final String BCO005 = "BCO005";
	public static final String BCO005_TXT = "id转换失败";
	// @DiaryBLL.cs
	public static final String BDI001 = "BDI001";
	public static final String BDI001_TXT = "未找到指定的显示方式";
	public static final String BDI002 = "BDI002";
	public static final String BDI002_TXT = "日记id转换失败";
	public static final String BDI003 = "BDI003";
	public static final String BDI003_TXT = "没有找到指定的日记";
	public static final String BDI004 = "BDI004";
	public static final String BDI004_TXT = "图片id转换失败";
	// @DiaryCommentBLL.cs
	public static final String BDC001 = "BDC001";
	public static final String BDC001_TXT = "评论已被屏蔽";
	public static final String BDC002 = "BDC002";
	public static final String BDC002_TXT = "评论id转换失败";
	// @NoticeBLL.cs
	public static final String BNO001 = "BNO001";
	public static final String BNO001_TXT = "班级（群）id转换错误";
	public static final String BNO002 = "BNO002";
	public static final String BNO002_TXT = "请求的通知类型错误";
	public static final String BNO003 = "BNO003";
	public static final String BNO003_TXT = "参数转换错误";
	public static final String BNO004 = "BNO004";
	public static final String BNO004_TXT = "没有找到通知或无权限查看";
	public static final String BNO005 = "BNO005";
	public static final String BNO005_TXT = "通知未开启签收或没有权限查看";
	// @PublishBLL.cs
	public static final String BPU001 = "BPU001";
	public static final String BPU001_TXT = "只能上传PNG、JPG或GIF图片";
	public static final String BPU002 = "BPU002";
	public static final String BPU002_TXT = "手机客户端只能发布家校通和私信";
	public static final String BPU003 = "BPU003";
	public static final String BPU003_TXT = "发布私信至少应有一个接收人";
	public static final String BPU004 = "BPU004";
	public static final String BPU004_TXT = "开关参数错误";
	public static final String BPU005 = "BPU005";
	public static final String BPU005_TXT = "私信接收人转换错误";
	public static final String BPU006 = "BPU006";
	public static final String BPU006_TXT = "开启了投票但未上传任何选项";
	public static final String BPU007 = "BPU007";
	public static final String BPU007_TXT = "通知id转换失败";
	public static final String BPU008 = "BPU008";
	public static final String BPU008_TXT = "没有上传任何图片";
	// @UserBLL.cs
	public static final String BUS001 = "BUS001";
	public static final String BUS001_TXT = "用户id转换失败";
	public static final String BUS002 = "BUS002";
	public static final String BUS002_TXT = "未找到用户";
	public static final String BUS003 = "BUS003";
	public static final String BUS003_TXT = "当前用户不属于这个班级（群），无法查询";
	public static final String BUS004 = "BUS004";
	public static final String BUS004_TXT = "没有找到或无法查看客户信息";
	public static final String BUS005 = "BUS005";
	public static final String BUS005_TXT = "未找到群名片";
	// @basic
	public static final String BEX001 = "BEX001";
	public static final String BEX001_TXT = "session已过期或不存在";
	// @api
	public static final String API001 = "API001";
	public static final String API001_TXT = "接口调用失败，未提供方法名称";
	public static final String API001_FAILLUE = "接口调用失败，API011";
	public static final String API001_FAILLUE_TXT = "图片上传失败";
	public static final String API002 = "API002";
	public static final String API002_TXT = "没有找到对应的方法名称";
	public static final String API003 = "API003";
	public static final String API003_TXT = "邀请码错误或已经过期";
	public static final String API004 = "API004";
	public static final String API004_TXT = "登陆失败，用户名和密码不匹配";
	public static final String API005 = "API005";
	public static final String API005_TXT = "未提供邀请码";
	public static final String API006 = "API006";
	public static final String API006_TXT = "未提供新密码或旧密码";
	public static final String API007 = "API007";
	public static final String API007_TXT = "原密码错误";
	public static final String API008 = "API008";
	public static final String API008_TXT = "至少上传一个图片";
	public static final String API009 = "API009";
	public static final String API009_TXT = "只能上传PNG、JPG或GIF图片";
	public static final String API010 = "API010";
	public static final String API010_TXT = "未检测到上传图片，发布失败";
	public static final String API011 = "API011";
	public static final String API011_TXT = "必须上传图片";
	public static final String API012 = "API012";
	public static final String API012_TXT = "未检测到上传图片，发布失败";
	public static final String API013 = "API013";
	public static final String API013_TXT = "上传的图片不能被识别";
	// @MobileMessageBLL.cs
	public static final String MMB001 = "MMB001";
	public static final String MMB001_TXT = "手机号已被使用，请使用忘记密码功能找回密码";
	public static final String MMB002 = "MMB002";
	public static final String MMB002_TXT = "该手机号未登记，请使用注册功能注册用户";
	public static final String MMB003 = "MMB003";
	public static final String MMB003_TXT = "验证码无效或已过期，请重新获得";
	public static final String MMB004 = "MMB004";
	public static final String MMB004_TXT = "您的短信刚发送，请1分钟后再尝试获取";
	public static final String MMB005 = "MMB005";
	public static final String MMB005_TXT = "对不起，你不是白名单列表中的用户，系统谢绝了您的注册";
	// @AuDal
	public static final String AUD001 = "AUD001";
	public static final String AUD001_TXT = "您正在尝试添加的不属于本校的班级到白名单，系统已拒绝";
	public static final String AUD002 = "AUD002";
	public static final String AUD002_TXT = "白名单已存在相同手机，并且您未选择覆盖原有数据";

	public static final String DCO007 = "DCO007";
	public static final String DCO007_TXT = "您已经收藏过该日记的所有图片了";

	public static final String SIG001 = "SIG001";
	public static final String SIG001_TXT = "没有找到签入记录，无法签出";
	public static final String SIG002 = "SIG002";
	public static final String SIG002_TXT = "签入后至少1分钟才能签出";
	public static final String[] keys = { DCL001, DCL002, DCL003, DCL004,
			DCL005, DCL006, DCO001, DCO002, DCO003, DCO004, DCO005, DCO006,
			DDC001, DDC002, DDC003, DNO001, DNO002, DNO003, DNO004, DNO005,
			DPU001, DPU002, DPU003, DPU004, DPU005, DUS001, DUS002, DUS003,
			DUS004, DUS005, DUS006, EX, BCL001, BCL002, BCL003, BCL004, BCO001,
			BCO002, BCO003, BCO004, BCO005, BDI001, BDI002, BDI003, BDI004,
			BDC001, BDC002, BNO001, BNO002, BNO003, BNO004, BNO005, BPU001,
			BPU002, BPU003, BPU004, BPU005, BPU006, BPU007, BPU008, BUS001,
			BUS002, BUS003, BUS004, BUS005, BEX001, API001, API001_FAILLUE,
			API002, API003, API004, API005, API006, API007, API008, API009,
			API010, API011, API012, API013, MMB001, MMB002, MMB003, MMB004,
			MMB005, AUD001, AUD002, DCO007, SIG001, SIG002 };
	public static final String[] values = { DCL001_TXT, DCL002_TXT, DCL003_TXT,
			DCL004_TXT, DCL005_TXT, DCL006_TXT, DCO001_TXT, DCO002_TXT,
			DCO003_TXT, DCO004_TXT, DCO005_TXT, DCO006_TXT, DDC001_TXT,
			DDC002_TXT, DDC003_TXT, DNO001_TXT, DNO002_TXT, DNO003_TXT,
			DNO004_TXT, DNO005_TXT, DPU001_TXT, DPU002_TXT, DPU003_TXT,
			DPU004_TXT, DPU005_TXT, DUS001_TXT, DUS002_TXT, DUS003_TXT,
			DUS004_TXT, DUS005_TXT, DUS006_TXT, EX_TXT, BCL001_TXT, BCL002_TXT,
			BCL003_TXT, BCL004_TXT, BCO001_TXT, BCO002_TXT, BCO003_TXT,
			BCO004_TXT, BCO005_TXT, BDI001_TXT, BDI002_TXT, BDI003_TXT,
			BDI004_TXT, BDC001_TXT, BDC002_TXT, BNO001_TXT, BNO002_TXT,
			BNO003_TXT, BNO004_TXT, BNO005_TXT, BPU001_TXT, BPU002_TXT,
			BPU003_TXT, BPU004_TXT, BPU005_TXT, BPU006_TXT, BPU007_TXT,
			BPU008_TXT, BUS001_TXT, BUS002_TXT, BUS003_TXT, BUS004_TXT,
			BUS005_TXT, BEX001_TXT, API001_TXT, API001_FAILLUE_TXT, API002_TXT,
			API003_TXT, API004_TXT, API005_TXT, API006_TXT, API007_TXT,
			API008_TXT, API009_TXT, API010_TXT, API011_TXT, API012_TXT,
			API013_TXT, MMB001_TXT, MMB002_TXT, MMB003_TXT, MMB004_TXT,
			MMB005_TXT, AUD001_TXT, AUD002_TXT, DCO007_TXT, SIG001_TXT,
			SIG002_TXT };

	private int relogin = 0;// 重新登陆次数

	/*
	 * 判断错误的原因。 先判断是否为BEX001（session已过期或不存在），如果过期，则后台重新登陆。 否则显示错误原因
	 */
	public void resultFalse(Context context, String content) {
		// TODO Auto-generated method stub

		String errorContent;
		try {
			errorContent = new JSONObject(content).getString("content");
			if (TextUtils.equals(errorContent, BEX001)) {
				// session已过期或不存在,后台登陆
				relogin(context);
			} else {
				onReloginFailure();
				int a = 0;// 若不是以下情况，则访问出错
				for (int i = 0; i < keys.length; i++) {

					if (TextUtils.equals(errorContent, keys[i])) {

						a++;
						Toast.makeText(context, values[i], Toast.LENGTH_SHORT)
								.show();
						if (TextUtils.equals(errorContent, DCO003)) {
							collectedThisPhoto();
							return;
						}
						if (TextUtils.equals(errorContent, DDC003)) {
							praisedThisPhoto();
							return;
						}
						if (TextUtils.equals(errorContent, SIG001)) {
							return;
						}
						if (TextUtils.equals(errorContent, SIG002)) {
							return;
						}

						whileFailure();
					}

				}
				if (a == 0) {
					whileFailure();
					Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT)
							.show();
					Log.e("ReturnValue",
							context.getString(R.string.unknow_error));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void relogin(final Context context) {
		// TODO Auto-generated method stub

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		final int userid = SharedPreferencesHelper.getIntValue(context,
				SharedPreferencesHelper.USERID);// 用户ID
		String account = SharedPreferencesHelper.getStringValue(context,
				SharedPreferencesHelper.ACCOUNT);// 账号
		String password = SharedPreferencesHelper.getStringValue(context,
				SharedPreferencesHelper.PASSWORD);// 密码
		String imei = SharedPreferencesHelper.getStringValue(context,
				SharedPreferencesHelper.IMEI);// 手机IMEI
		String classid = SharedPreferencesHelper.getStringValue(context,
				Keys.CLASS_ID);// 班级id
		params.put("methodName", "2");
		params.put("a", account);
		params.put("b", password);
		params.put("c", imei);
		params.put("f", classid);

		asyncHttpClient.setCookieStore(new PersistentCookieStore(context));
		asyncHttpClient.post(ContantUtil.AU_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						// 登陆正确
						try {
							JSONObject jsonObject = new JSONObject(content);
							boolean result = jsonObject.getBoolean("result");

							result = userid == jsonObject.getInt("userid") ? true
									: false;

							String accessClass = jsonObject
									.getString("accessClass");
							if (result) {
								if (TextUtils.equals(accessClass, "1")) {
									// 登陆正确
									onReloginSuccess();
								} else {
									Toast.makeText(context,
											R.string.no_class_permission,
											Toast.LENGTH_SHORT).show();
								}

							} else {
								if (relogin < 5) {
									resultFalse(context, content);

									relogin++;

								} else {
									Toast.makeText(context, R.string.error,
											Toast.LENGTH_SHORT).show();
									onReloginFailure();
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// 解析失败

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						Toast.makeText(context, R.string.post_error,
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	public void collectedThisPhoto() {
		// TODO Auto-generated method stub

	}

	public void praisedThisPhoto() {
		// TODO Auto-generated method stub

	}

	public abstract void onReloginSuccess();

	// 后台登录成功

	public void onReloginFailure() {
	};

	public void whileFailure() {
		// TODO Auto-generated method stub

	}
}
