package com.halong.aubaby.entity;

import java.io.Serializable;

public class VDiaryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6328763794007888740L;

	private Boolean result;// 是否返回成功

	private ObjInfo[] objInfo;// 返回内容

	public ObjInfo[] getObjInfo() {
		return objInfo;
	}

	public void setObjInfo(ObjInfo[] objInfo) {
		this.objInfo = objInfo;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public static class ObjInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5370392369441331745L;
		private Content content;// 返回的日记
		private User user;// 发布用户消息

		public Content getContent() {
			return content;
		}

		public void setContent(Content content) {
			this.content = content;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public static class Content implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1356127144679122844L;
			private Comments comments;// 评论
			private int countOfPics;// 照片数
			private String date;// 发表日期
			private String id;// 日记ID
			private String liuyan;// 留言数
			private String pinglun;// 评论数
			private String title;// 标题
			private String zan;// 赞数
			private Pics pics;// 发布照片

			public Comments getComments() {
				return comments;
			}

			public void setComments(Comments comments) {
				this.comments = comments;
			}

			public int getCountOfPics() {
				return countOfPics;
			}

			public void setCountOfPics(int countOfPics) {
				this.countOfPics = countOfPics;
			}

			public String getDate() {
				return date;
			}

			public void setDate(String date) {
				this.date = date;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getLiuyan() {
				return liuyan;
			}

			public void setLiuyan(String liuyan) {
				this.liuyan = liuyan;
			}

			public String getPinglun() {
				return pinglun;
			}

			public void setPinglun(String pinglun) {
				this.pinglun = pinglun;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getZan() {
				return zan;
			}

			public void setZan(String zan) {
				this.zan = zan;
			}

			public Pics getPics() {
				return pics;
			}

			public void setPics(Pics pics) {
				this.pics = pics;
			}

			public static class Comments implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 980097113520901209L;
				private CommentList[] commentList;

				public CommentList[] getCommentList() {
					return commentList;
				}

				public void setCommentList(CommentList[] commentList) {
					this.commentList = commentList;
				}

				public static class CommentList implements Serializable {

					/**
					 * 
					 */
					private static final long serialVersionUID = -608517550757368528L;

					private String commentContent;
					private String commentDatetime;
					private String commentid;
					private String userHeadPhotoURL;
					private String userid;
					private String username;
					private String isAdmin;

					public String getCommentContent() {
						return commentContent;
					}

					public void setCommentContent(String commentContent) {
						this.commentContent = commentContent;
					}

					public String getCommentDatetime() {
						return commentDatetime;
					}

					public void setCommentDatetime(String commentDatetime) {
						this.commentDatetime = commentDatetime;
					}

					public String getCommentid() {
						return commentid;
					}

					public void setCommentid(String commentid) {
						this.commentid = commentid;
					}

					public String getUserHeadPhotoURL() {
						return userHeadPhotoURL;
					}

					public void setUserHeadPhotoURL(String userHeadPhotoURL) {
						this.userHeadPhotoURL = userHeadPhotoURL;
					}

					public String getUserid() {
						return userid;
					}

					public void setUserid(String userid) {
						this.userid = userid;
					}

					public String getUsername() {
						return username;
					}

					public void setUsername(String username) {
						this.username = username;
					}

					public String getIsAdmin() {
						return isAdmin;
					}

					public void setIsAdmin(String isAdmin) {
						this.isAdmin = isAdmin;
					}
				}
			}

			public static class Pics implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 56356287456977929L;
				private PicList[] picList;// 图片集合

				public PicList[] getPicList() {
					return picList;
				}

				public void setPicList(PicList[] picList) {
					this.picList = picList;
				}

				public static class PicList implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 393763414677995703L;
					private String picid;// 日记携带图片id
					private String type;// 日记携带图片类型（P，V）
					private String url;// 日记携带图片url

					public String getPicid() {
						return picid;
					}

					public void setPicid(String picid) {
						this.picid = picid;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public String getUrl() {
						return url;
					}

					public void setUrl(String url) {
						this.url = url;
					}
				}
			}
		}

		public static class User implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3870994244362690540L;
			private String code;// 发布人ID
			private String img;// 发布人头像的url
			private String name;// 发布人姓名
			private String isAdmin;//是否是教师

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}
		}

	}

}
