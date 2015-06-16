package com.halong.aubaby.entity;

import java.io.Serializable;

public class DiaryDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3137952588241176832L;
	private Boolean result;
	private Content content;// 日记内容
	private Piclist piclist;// 日记携带的图片
	private User user;// 发表用户
	private UserPriceList userPriceList;// 点赞的人
	private Vedio vedio;// 视频链接

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Piclist getPiclist() {
		return piclist;
	}

	public void setPiclist(Piclist piclist) {
		this.piclist = piclist;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserPriceList getUserPriceList() {
		return userPriceList;
	}

	public void setUserPriceList(UserPriceList userPriceList) {
		this.userPriceList = userPriceList;
	}

	public Vedio getVedio() {
		return vedio;
	}

	public void setVedio(Vedio vedio) {
		this.vedio = vedio;
	}

	public static class Content implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6471819413706912279L;
		private int countOfPics;// 日记携带照片数
		private String date;// 发表
		private String id;// 日记id
		private String img;// 日记携带的第一张图片
		private String imgid;// 日记携带的第一张图片id
		private String liulan;// 浏览数
		private String neirong;// 日记内容
		private String ntype;// 日记携带图片类型（P，V）
		private String pinglun;// 评论数
		private String zan;// 点赞数

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

		public String getZan() {
			return zan;
		}

		public void setZan(String zan) {
			this.zan = zan;
		}
	};

	public static class Piclist implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2513717096137564358L;

		private Pic[] pic;

		public Pic[] getPic() {
			return pic;
		}

		public void setPic(Pic[] pic) {
			this.pic = pic;
		};

		public static class Pic implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8549175228068532054L;

			private String img;// 图片
			private String imgid;// 图片id
			private String type;// 图片类型

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

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}
		}

	}

	public static class User implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6868464257475117609L;
		private String code;
		private String img;
		private String name;
		private String isAdmin;

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
	};

	public static class UserPriceList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 408101061796885542L;
		private List[] list;

		public List[] getList() {
			return list;
		}

		public void setList(List[] list) {
			this.list = list;
		}

		public static class List implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -892667308963289306L;

			private String name;
			private String userHeadPhotoURL;
			private String userid;
			private String isAdmin;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
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

			public String getIsAdmin() {
				return isAdmin;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}
		}
	};

	public static class Vedio implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4151234274167518899L;

		private String url_56;
		private String url_sina;
		private String url_youku;

		public String getUrl_56() {
			return url_56;
		}

		public void setUrl_56(String url_56) {
			this.url_56 = url_56;
		}

		public String getUrl_sina() {
			return url_sina;
		}

		public void setUrl_sina(String url_sina) {
			this.url_sina = url_sina;
		}

		public String getUrl_youku() {
			return url_youku;
		}

		public void setUrl_youku(String url_youku) {
			this.url_youku = url_youku;
		}
	};

}
