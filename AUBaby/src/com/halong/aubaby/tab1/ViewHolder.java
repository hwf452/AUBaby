package com.halong.aubaby.tab1;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewHolder {
	public com.halong.aubaby.widget.NoScrollGridView mGridView;// 说说中的照片
	public PhotosGridViewAdapter mAdapter;
	public Button shareButton;// 分享按钮
	public ImageButton mheadImageButton;// 用户头像
	public TextView mUserNameTextView;// 用户名
	public TextView mPostedTextView;// 发表日期
	public Button mZanButton;// 赞的个数
	public Button mPingLunButton;// 评论个数
	public TextView mDiaryNeiRongTextView;// 日志标题
	public Button collectBtn;// 收藏相册
	public Button deleteButton;// 删除日志
	public ProgressBar progress;// 上传进度条
	public Button retryButton;// 重发按钮
	public RelativeLayout commentRelLayout;// 第一个评论布局
	public ImageButton commentImg;// 第一个评论用户头像
	public TextView commentNameTXT;// 第一个评论用户名称
	public TextView commentPostedTxt;// 第一个评论发表时间
	public TextView commentNeiRongTxt;// 第一个评论内容
	public RelativeLayout commentRelLayout2;// 第二个评论布局
	public ImageButton commentImg2;// 第二个评论用户头像
	public TextView commentNameTXT2;// 第二个评论用户名称
	public TextView commentPostedTxt2;// 第二个评论发表时间
	public TextView commentNeiRongTxt2;// 第二个评论内容
	public RelativeLayout commentRelLayout3;// 第三个评论布局
	public ImageButton commentImg3;// 第三个评论用户头像
	public TextView commentNameTXT3;// 第三个评论用户名称
	public TextView commentPostedTxt3;// 第三个评论发表时间
	public TextView commentNeiRongTxt3;// 第三个评论内容
	public ImageView teacherImageView;//发表日记用户的教师标志
	public ImageView commentImageView,commentImageView2,commentImageView3;//评论用户的教师标志
}