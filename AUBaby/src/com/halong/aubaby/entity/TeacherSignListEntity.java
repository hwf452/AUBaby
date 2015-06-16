package com.halong.aubaby.entity;

import java.io.Serializable;

public class TeacherSignListEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5521200962504967140L;

	private Boolean result;
	private String className;
	private String countOfUsers;
	private String teacherHeadPhotoURL;
	private String teacherName;
	private users[] users;

	public Boolean getResult() {
		return result;
	}

	public String getClassName() {
		return className;
	}

	public String getCountOfUsers() {
		return countOfUsers;
	}

	public String getTeacherHeadPhotoURL() {
		return teacherHeadPhotoURL;
	}

	public String getTeacherName() {
		return teacherName;
	}


	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setCountOfUsers(String countOfUsers) {
		this.countOfUsers = countOfUsers;
	}

	public void setTeacherHeadPhotoURL(String teacherHeadPhotoURL) {
		this.teacherHeadPhotoURL = teacherHeadPhotoURL;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


	public users[] getUsers() {
		return users;
	}

	public void setUsers(users[] users) {
		this.users = users;
	}


	public static class users implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7044107998448800918L;

		private String headPhotoURL;
		private String id_user;
		private String name;
		private String signInRemark;
		private String signInTime;
		private String signInUserName;
		private String signInUserSubject;
		private String signOutRemark;
		private String signOutTime;
		private String signOutUserName;
		private String signOutUserSubject;

		public String getHeadPhotoURL() {
			return headPhotoURL;
		}

		public String getId_user() {
			return id_user;
		}

		public String getName() {
			return name;
		}

		public String getSignInRemark() {
			return signInRemark;
		}

		public String getSignInTime() {
			return signInTime;
		}

		public String getSignInUserName() {
			return signInUserName;
		}

		public String getSignInUserSubject() {
			return signInUserSubject;
		}

		public String getSignOutRemark() {
			return signOutRemark;
		}

		public String getSignOutTime() {
			return signOutTime;
		}

		public String getSignOutUserName() {
			return signOutUserName;
		}

		public String getSignOutUserSubject() {
			return signOutUserSubject;
		}

		public void setHeadPhotoURL(String headPhotoURL) {
			this.headPhotoURL = headPhotoURL;
		}

		public void setId_user(String id_user) {
			this.id_user = id_user;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSignInRemark(String signInRemark) {
			this.signInRemark = signInRemark;
		}

		public void setSignInTime(String signInTime) {
			this.signInTime = signInTime;
		}

		public void setSignInUserName(String signInUserName) {
			this.signInUserName = signInUserName;
		}

		public void setSignInUserSubject(String signInUserSubject) {
			this.signInUserSubject = signInUserSubject;
		}

		public void setSignOutRemark(String signOutRemark) {
			this.signOutRemark = signOutRemark;
		}

		public void setSignOutTime(String signOutTime) {
			this.signOutTime = signOutTime;
		}

		public void setSignOutUserName(String signOutUserName) {
			this.signOutUserName = signOutUserName;
		}

		public void setSignOutUserSubject(String signOutUserSubject) {
			this.signOutUserSubject = signOutUserSubject;
		}

	}
}
