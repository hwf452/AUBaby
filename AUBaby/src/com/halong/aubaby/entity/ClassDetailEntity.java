package com.halong.aubaby.entity;

import java.io.Serializable;


public class ClassDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3945375758504237205L;
	private Boolean result;
	private ObjectInfo objinfo;
	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public ObjectInfo getObjinfo() {
		return objinfo;
	}

	public void setObjinfo(ObjectInfo objinfo) {
		this.objinfo = objinfo;
	}

	public static class ObjectInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5662105209639671410L;
		private String schoolName;//学校名称
		private String id;//群id
		private String img;//群头像
		private String isClassAdmin;//当前用户是否班级管理员
		private String isofficalClass;//是否官方群
		private String name;//群名称
		private String qianm;//群签名
		private String sqjrfs;//本人加入方式（F:正式）
		private String time;//加入时间
		private User[] user;

		private Admin[] admin;
		public String getSchoolName() {
			return schoolName;
		}

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
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

		public String getIsClassAdmin() {
			return isClassAdmin;
		}

		public void setIsClassAdmin(String isClassAdmin) {
			this.isClassAdmin = isClassAdmin;
		}

		public String getIsofficalClass() {
			return isofficalClass;
		}

		public void setIsofficalClass(String isofficalClass) {
			this.isofficalClass = isofficalClass;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getQianm() {
			return qianm;
		}

		public void setQianm(String qianm) {
			this.qianm = qianm;
		}

		public String getSqjrfs() {
			return sqjrfs;
		}

		public void setSqjrfs(String sqjrfs) {
			this.sqjrfs = sqjrfs;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public User[] getUser() {
			return user;
		}

		public void setUser(User[] user) {
			this.user = user;
		}

		public Admin[] getAdmin() {
			return admin;
		}

		public void setAdmin(Admin[] admin) {
			this.admin = admin;
		}

		public static class User implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 102684117715164758L;

			private String code;//成员id
			private String img;//成员头像
			private String isSelf;//当前成员是否本人
			private String mobile;//手机号码
			private String name;//成员名称
			private String status;//是否通过审核
			private String isAdmin;//是否是老师
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

			public String getIsSelf() {
				return isSelf;
			}

			public void setIsSelf(String isSelf) {
				this.isSelf = isSelf;
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

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}

		}
		public static class Admin implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 6228950682804663286L;
			private String code;//成员id
			private String img;//成员头像
			private String isSelf;//当前成员是否本人
			private String mobile;//手机号码
			private String name;//成员名称
			private String status;//是否通过审核
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

			public String getIsSelf() {
				return isSelf;
			}

			public void setIsSelf(String isSelf) {
				this.isSelf = isSelf;
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

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

		}
	}

}
