package com.halong.aubaby.entity;

import java.io.Serializable;


public class SDiaryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6530496624739648596L;
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
		private static final long serialVersionUID = -1307404555510678201L;
		private User user;
		private Content content;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Content getContent() {
			return content;
		}

		public void setContent(Content content) {
			this.content = content;
		}

		public static class User implements Serializable {

			/**
		 * 
		 */
			private static final long serialVersionUID = -3883142878556172678L;
			private String code;// 发布人ID
			private String img;// 发布人头像的url
			private String name;// 发布人姓名

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
		}

		public static class Content implements Serializable {

			/**
		 * 
		 */
			private static final long serialVersionUID = 7872092252966235930L;

			private int countOfPics;// 照片数
			private String date;// 发表日期
			private String id;// 日记ID
			private String zan;// 赞数
			private String img;//日记携带图片url
			private String imgid;//日记携带图片id
			private String liulan;//浏览数
			private String neirong;//内容
			private String ntype;//图片类型
			private String pinglun;//评论数

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

			public String getZan() {
				return zan;
			}

			public void setZan(String zan) {
				this.zan = zan;
			}

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public String getImgid() {
				return imgid;
			}

			public void setImgid(String imgid) {
				this.imgid = imgid;
			}

			public String getLiulan() {
				return liulan;
			}

			public void setLiulan(String liulan) {
				this.liulan = liulan;
			}

			public String getNeirong() {
				return neirong;
			}

			public void setNeirong(String neirong) {
				this.neirong = neirong;
			}

			public String getNtype() {
				return ntype;
			}

			public void setNtype(String ntype) {
				this.ntype = ntype;
			}

			public String getPinglun() {
				return pinglun;
			}

			public void setPinglun(String pinglun) {
				this.pinglun = pinglun;
			}

		}
	}
}
