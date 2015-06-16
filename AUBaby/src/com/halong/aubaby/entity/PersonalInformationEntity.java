package com.halong.aubaby.entity;

import java.io.Serializable;

public class PersonalInformationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 798981490789155511L;

	private boolean result;
	private banjilist banjilist;
	private user user;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public banjilist getBanjilist() {
		return banjilist;
	}

	public void setBanjilist(banjilist banjilist) {
		this.banjilist = banjilist;
	}

	public user getUser() {
		return user;
	}

	public void setUser(user user) {
		this.user = user;
	}

	public static class user implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7327822442328919301L;
		private String QQ;
		private String mailbox;
		private String mobile;
		private String name;
		private String stats;
		private String userid;

		public String getQQ() {
			return QQ;
		}

		public void setQQ(String qQ) {
			QQ = qQ;
		}

		public String getMailbox() {
			return mailbox;
		}

		public void setMailbox(String mailbox) {
			this.mailbox = mailbox;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStats() {
			return stats;
		}

		public void setStats(String stats) {
			this.stats = stats;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}
	}

	public static class banjilist implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5271145392742570307L;
		private banji[] banji;

		public banji[] getBanji() {
			return banji;
		}

		public void setBanji(banji[] banji) {
			this.banji = banji;
		}

		public static class banji implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8734598045487735791L;
			private String accessClass;
			private String classid;
			private String classname;
			private String fabiao;
			private String isAdmin;
			private String managerClass;
			private String myClassName;
			private String myClassRelation;
			private String nickName;
			private String pinglun;
			private String point;
			private String schoolName;
			private String type;
			private String userHeadPhoto;
			private String userid;
			private String zan;

			public String getAccessClass() {
				return accessClass;
			}

			public void setAccessClass(String accessClass) {
				this.accessClass = accessClass;
			}

			public String getClassid() {
				return classid;
			}

			public void setClassid(String classid) {
				this.classid = classid;
			}

			public String getClassname() {
				return classname;
			}

			public void setClassname(String classname) {
				this.classname = classname;
			}

			public String getFabiao() {
				return fabiao;
			}

			public void setFabiao(String fabiao) {
				this.fabiao = fabiao;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}

			public String getManagerClass() {
				return managerClass;
			}

			public void setManagerClass(String managerClass) {
				this.managerClass = managerClass;
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

			public String getNickName() {
				return nickName;
			}

			public void setNickName(String nickName) {
				this.nickName = nickName;
			}

			public String getPinglun() {
				return pinglun;
			}

			public void setPinglun(String pinglun) {
				this.pinglun = pinglun;
			}

			public String getPoint() {
				return point;
			}

			public void setPoint(String point) {
				this.point = point;
			}

			public String getSchoolName() {
				return schoolName;
			}

			public void setSchoolName(String schoolName) {
				this.schoolName = schoolName;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getUserHeadPhoto() {
				return userHeadPhoto;
			}

			public void setUserHeadPhoto(String userHeadPhoto) {
				this.userHeadPhoto = userHeadPhoto;
			}

			public String getUserid() {
				return userid;
			}

			public void setUserid(String userid) {
				this.userid = userid;
			}

			public String getZan() {
				return zan;
			}

			public void setZan(String zan) {
				this.zan = zan;
			}

		}

	}
}
