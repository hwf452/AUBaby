package com.halong.aubaby.contant;

public class ContantUtil {

	public static String AUBABY_URL="http://api.aubaby.cn/";

	public final static String BASE_URL="http://api.aubaby.cn/japi/japi_";
	/**
	 * URL地址
	 */

	public static String VERSION_UPDATE_URL ="http://api.aubaby.cn/apk/version.xml";

	/**
	 * 关于注册登录 URL地址 普通登陆2、修改密码4、根据邀请码获得对应的班级信息5、手机注册获得验证码6、手机注册第二步7
	 * 手机忘记密码第一步获得验证码8、手机重置密码第二步9、
	 */
	public static String AU_URL = "http://api.aubaby.cn/japi/japi_au.ashx";

	/**
	 * 关于用户、个人班级 查看本人用户概览1、查看他人用户概览2、群基本信息3、查看个人用户信息4、修改个人信息5、
	 * 获得班级名片6、更新个人班级名片7、更新班级信息8、获得邀请码9、更新邀请码10、 使用邀请码加入班级11、退群12、返回个人的群的列表13
	 */
	public static String USER_URL = "http://api.aubaby.cn/japi/japi_user.ashx";

	public static String COMMENT_URL = "http://api.aubaby.cn/japi/japi_comment.ashx";
	
	/**
	 * 获得每种的通知的前两个1、获得每种通知的未读数量5行2、获得按班级归类通知数量3、通知列表4
	 * 通知详细查看5、通知评论列表6、获得通知的投票列表，仅admin7、获得通知的投票统计，群内所有人8、
	 * 通知投票9、通知签收10、通知删除11、管理员删除通知，所有人不可见12、管理员删除通知评论13 管理员获得签收列表14
	 */
	public static String NOTICE_URL = "http://api.aubaby.cn/japi/japi_notice.ashx";
	
	public static String PUBLISH_URL = "http://api.aubaby.cn/japi/japi_publish.ashx";

	/**
	 * 获得未读日记和通知的数量1
	 */
	public static String EXCHANGE_URL = "http://api.aubaby.cn/japi/japi_exchange.ashx";

	/**
	 * 图片路径
	 */
	public static String PICTURE_URL = "http://api.aubaby.cn/upload";

}
