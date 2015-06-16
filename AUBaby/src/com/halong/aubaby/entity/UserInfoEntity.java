package com.halong.aubaby.entity;

import java.io.Serializable;

public class UserInfoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7797039746831482536L;

	private Boolean result;
	private User user;
	private BanJiList banjilist;

	public  Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BanJiList getBanjilist() {
		return banjilist;
	}

	public void setBanjilist(BanJiList banjilist) {
		this.banjilist = banjilist;
	}

	public static class User {
		private String name;
		private String img;
		private String code;
		private int liulan;
		private int pinglun;
		private int zan;
		private int fabiao;
		private String qianming;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getLiulan() {
			return liulan;
		}

		public void setLiulan(int liulan) {
			this.liulan = liulan;
		}

		public int getPinglun() {
			return pinglun;
		}

		public void setPinglun(int pinglun) {
			this.pinglun = pinglun;
		}

		public int getZan() {
			return zan;
		}

		public void setZan(int zan) {
			this.zan = zan;
		}

		public int getFabiao() {
			return fabiao;
		}

		public void setFabiao(int fabiao) {
			this.fabiao = fabiao;
		}

		public String getQianming() {
			return qianming;
		}

		public void setQianming(String qianming) {
			this.qianming = qianming;
		}

	}

	public  static class BanJiList {
		private Banji[] banji;

		public Banji[] getBanji() {
			return banji;
		}

		public void setBanji(Banji[] banji) {
			this.banji = banji;
		}

		public static class Banji {
			private String type;
			private String name;
			private String code;
			private String isAdmin;
			private String userHeadPhoto ;
			private String myClassName;
			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}

		
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getMyClassName() {
				return myClassName;
			}

			public void setMyClassName(String myClassName) {
				this.myClassName = myClassName;
			}

			public String getUserHeadPhoto() {
				return userHeadPhoto;
			}

			public void setUserHeadPhoto(String userHeadPhoto) {
				this.userHeadPhoto = userHeadPhoto;
			}

		}
	}
}
