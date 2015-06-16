package com.halong.aubaby.contant;

public class Keys {

	public final static String DIARYENTITY = "diaryEntity";// 传递DiaryEntity网络数据的key
	public final static String CLICK_GRIDVIEW_ITEM = "clickGridItem";// 传递点击gridview的item
	public final static String USER_INFO_ID = "userInfoID";// 传递所查看用户详情的用户id
	public final static String DIARY_ID = "diaryID";// 传递所查看日记的ID
	public final static String FRAGMENT_PAGE = "fragmentPage";// 传递要加载的fragment关键字
	public final static int COMMENT_FRAGMENT = 1;// 传递要加载的fragment
	public final static String O_VIEW = "/O";// 原始图片
	public final static String L_VIEW = "/L";// 大号缩放（单瀑布流）
	public final static String M_VIEW = "/M"; // 中号缩放（暂不用）
	public final static String S_VIEW = "/S";// 大号缩放（视图式）
	public final static String SEARCH_START_DATE = "searchStartDate";// 搜索日记开始时间
	public final static String SEARCH_END_DATE = "searchEndDate";// 搜索日记结束时间
	public final static String SEARCH_KEY = "searchKey";// 搜索日记关键字

	public final static String CLASS_ID = "code";// 班级ID

	public final static String USERS_NUMBER = "users_number";// 班级电话
	public final static String CLASS_DETAIL_ENTITY = "classDetailEntity";// 传递classDetailEntity网络数据的key

	public final static String ALBUM_ID = "albumID";// 传递的相册id

	public final static String PUBLISH_PHOTO_PATH = "publish_photo_path";// 要发表图片路径的key

	public final static int IMG_FILE_LIST_ACTIVITY = 1;// requestCode为ImgFileListActivity
	public final static int GET_PIC_OR_VIDEO = 2;// requestCode为GetPicOrVideo
	public final static int MAIN_ACTIVITY = 3;// requestCode为MainActivity
	public final static int PUBLISH_ACTIVIY = 4;// ResultCode为publishActivity
	public final static int ADD_PHOTOS = 10;// 返回上一页值为ADD_PHOTOS时，添加图片
	public final static int CLOSE_ACTIVITY = 11;// 返回上一页值为CLOSE_ACTIVITY时，关闭页面
	public final static int PUBLISH_NOTICE_ACTIVITY = 30;// ResultCode为publishNoticeActivity
	public final static int RECEIVERS_ACTIVITY = 31;// resultCode为ReceiversActivity
	public final static int REPLY_NOTICE_COMMENT_ACTIVITY = 32;// resultCode为ReplyNoticeCommentActivity
	public final static int NOTICE_REPLY_FRAGMENT=33;//requestCode为NoticeReplayFragment
	public final static int NOTICE_REPLY_ADAPTER=34;//requestCode为NoticeReplayAdapter
	public final static int MODIFY_PRIVATE_INFO_ACTIVITY=35;//resultCode为ModifyPrivateImfoActivity
	public final static int PRIVATE_INFO_ACTIVITY=36;//requestCode为PrivateImfoActivity
	
	public final static String PHOTOS_LIST = "photosList";// 保存要上传图片列表的key
	public final static String PHOTOS_LIST_TO_OTHER_ACTIVITY = "photosListToOtherActivity";// 传递要上传图片的key

	public final static String PUBLISH_DIARY_CONTENT = "publishDiaryContent";// 传递要发表日记的key

	public final static String DELETE_OBJECT = "deleteObject";// adpter通知listview要删除的的对象
	public final static String RETRY_OBJECT = "retryObject";// adpter通知listview重新发布的对象
	public final static String ON_REFRESH = "onRefresh";// 刷新状态
	public final static String ON_LOAD = "onOload";// 加载状态
	public final static String START_APP = "startApp";// 启动app

	public final static String CLASS_DIARY_FRAGMENT_ACTION = "classDiaryFragmentAction";// 注册接收广播的action
	public final static String PUBLISH_ACTIVITY_ACTION = "publishActivityAction";// 注册接收广播的action
	public final static String TAB1_FRAGMENT_TOP_TITLE_ACTION = "tab1FragmentTopTitleAction";// 注册接收广播的action
	public final static String SETTING_ACTIVITY_CHANGE_GROUP = "settingActivityChangeGroup";// 注册接收广播的action,切换班级
	public final static String SHUOSHUO_DETAIL_ACTIVITY_ACTION = "shuoshuoDetailActivityAction";// 注册接收广播的action，日记详情发送的

	public final static String COMMENT_LIST = "commentList";// 传递评论列表的关键字

	public final static String DIARY = "diary";// hashmap中的key，对象是日记
	public final static String DIARYSTATE = "diaryState";// /hashmap中的key，对象是日记状态

	public final static String LOCAL_DATA = "localData";// 发表日记的id
	public final static String DIARY_P_Type = "P";// 请求的日记类型
	public final static String IS_CLASS_ADMIN = "isClassAdmin";// 是否是所请求班级的管理员
	public final static String USER_HEAD_PHOTO = "userHeadPhoto";// 用户头像

	public final static String PRIASE_NUM = "praiseNum";// 点赞数传递key
	public final static String COMMENT_NUM = "commentNum";// 评论数传递key

	public final static String MAST_PHOTO = "mastPhotoCount";// 最多能上传几张图片的关键字
	public final static String PHOTO_URL = "photoUrl";// 传递完整图片路径的关键字
	public final static String RECEIVERS_ID = "receiversID";// 传递私信接收者ID的关键字
	public final static String RECEIVERS_NAME = "receiversName";// 传递私信接收者NAME的关键字
	public final static String NOTICE_REPLY_CNT = "noticeReplyCnt";// 传递是否有通知评论的key
	public final static String NOTICE_REPLY_COMMENT = "noticeReplyComment";// 传递获取通知评论id的key
	public final static String REPLY_COMMENT_ID = "replyCommentID";// 要回复的评论id

	public final static String IS_OPEN_VEDIO = "vdeioClosed";// 是否允许拍摄视频
	public final static String VEDIO_CLOSED = "vdeioClosed";// 不允许拍摄视频
	public final static String VEDIO_OPENED = "vdeioOpened";// 不允许拍摄视频

	public final static String AUBABY = "/AUBaby";// 宝贝空间文件保存路径
	public final static String POST_INFO="postInfo";//要提交的资料
	public final static String RETURN_POST_INFO="returnPostInfo";//返回提交成功的资料
	public final static String BANJI_ITEM="banJiItem";//传递第几个班级的关键字
	public final static String BANJI_ID="banJiID";//传递班级id的key
	public final static int POST_USER_NAME=40;//intent传递的key，表示要提交修改用户名
	public final static int POST_EMAIL=41;//intent传递的key，表示要提交修改邮箱
	public final static int POST_QQ=42;//intent传递的key，表示要提交修改QQ
	
	public final static String ACTIVITY_KEY="activityKey";//要传递的activity的key
	public final static String REGISTER_ACTIVITY="registerActivity";//传递registerActivity的value
	public final static String SETTING_ACTIVITY_HELP="settingActivityHelp";//传递settingActivity中help的value
}
