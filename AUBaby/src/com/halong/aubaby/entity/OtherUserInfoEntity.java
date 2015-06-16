package com.halong.aubaby.entity;

import java.io.Serializable;

public class OtherUserInfoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9119763602849823655L;
	private Boolean result;
	private BanJiList banjilist;
	private User user;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public BanJiList getBanjilist() {
		return banjilist;
	}

	public void setBanjilist(BanJiList banjilist) {
		this.banjilist = banjilist;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static class BanJiList implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1338324259277583986L;
		private BanJi[] banji;

		public BanJi[] getBanJi() {
			return banji;
		}

		public void setBanJi(BanJi[] banji) {
			this.banji = banji;
		}

		public static class BanJi implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1201954651172004584L;
			private String code;//群id
			private String headPhoto;//群头像
			private String isAdmin;//是否群管理员
			private String name;//群名称
			private String type;//群类型
			private String myClassName;//班级内称呼
			private String myClassRelation;//关系
			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getHeadPhoto() {
				return headPhoto;
			}

			public void setHeadPhoto(String headPhoto) {
				this.headPhoto = headPhoto;
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

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getMyClassName() {
				return myClassName;
			}

			public void setMyClassName(String myClassName) {
				this.myClassName = myClassName;
			}

			public String getMyClassRelation() {
				return myClassRelation;
			}

			public void setMyClassRelation(String myClassRelation) {
				this.myClassRelation = myClassRelation;
			}

		
		}
	}

	public static class User implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2906156747361257953L;
		private String code;//用户id
		private String fabiao;//发表数
		private String img;//用户头像
		private String liulan;//浏览数
		private String name;//用户名字
		private String pinglun;//评论数
		private String qianming;//签名
		private String zan;//赞
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getFabiao() {
			return fabiao;
		}
		public void setFabiao(String fabiao) {
			this.fabiao = fabiao;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getLiulan() {
			return liulan;
		}
		public void setLiulan(String liulan) {
			this.liulan = liulan;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPinglun() {
			return pinglun;
		}
		public void setPinglun(String pinglun) {
			this.pinglun = pinglun;
		}
		public String getQianming() {
			return qianming;
		}
		public void setQianming(String qianming) {
			this.qianming = qianming;
		}
		public String getZan() {
			return zan;
		}
		public void setZan(String zan) {
			this.zan = zan;
		}
	}

}
