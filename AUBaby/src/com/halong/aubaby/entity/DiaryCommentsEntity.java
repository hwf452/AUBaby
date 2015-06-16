package com.halong.aubaby.entity;

import java.io.Serializable;

public class DiaryCommentsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5547049563372544088L;

	private Boolean result;
	private ObjInfo[] objInfo;// 返回内容
	private String countOfComments;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public ObjInfo[] getObjInfo() {
		return objInfo;
	}

	public void setObjInfo(ObjInfo[] objInfo) {
		this.objInfo = objInfo;
	}

	public String getCountOfComments() {
		return countOfComments;
	}

	public void setCountOfComments(String countOfComments) {
		this.countOfComments = countOfComments;
	}

	public static class ObjInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5248372395527051566L;
		private String code;// 评论人id
		private String id;// 评论id
		private String img;// 评论人头像
		private String name;// 评论人姓名
		private String text;// 评论内容
		private String time;// 评论时间
		private String isAdmin;//评论人是否是管理员
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
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

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(String isAdmin) {
			this.isAdmin = isAdmin;
		}
	}
}
