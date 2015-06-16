package com.halong.aubaby.entity;

import java.io.Serializable;

public class UserSignListEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6699539084382751446L;
	private Boolean result;
	private String className;
	private String headPhotoURL;
	private list[] list;

	public Boolean getResult() {
		return result;
	}

	public String getClassName() {
		return className;
	}

	public String getHeadPhotoURL() {
		return headPhotoURL;
	}

	public list[] getList() {
		return list;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setHeadPhotoURL(String headPhotoURL) {
		this.headPhotoURL = headPhotoURL;
	}

	public void setList(list[] list) {
		this.list = list;
	}

	public class list implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7986008663043406698L;

		private String key;

		private value[] value;

		public String getKey() {
			return key;
		}

		public value[] getValue() {
			return value;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setValue(value[] value) {
			this.value = value;
		}

		public class value implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -395744967376998219L;

			private String ClassName;
			private String HeadPhotoURL;
			private String Id_sw;
			private String MyName;
			private String OperatorName;
			private String OperatorSubject;
			private String Operatorid;
			private String Remark;
			private String SignDate;
			private String SignTime;

			public String getClassName() {
				return ClassName;
			}

			public String getHeadPhotoURL() {
				return HeadPhotoURL;
			}

			public String getId_sw() {
				return Id_sw;
			}

			public String getMyName() {
				return MyName;
			}

			public String getOperatorName() {
				return OperatorName;
			}

			public String getOperatorSubject() {
				return OperatorSubject;
			}

			public String getOperatorid() {
				return Operatorid;
			}

			public String getRemark() {
				return Remark;
			}

			public String getSignDate() {
				return SignDate;
			}

			public String getSignTime() {
				return SignTime;
			}

			public String getUserid() {
				return Userid;
			}

			public void setClassName(String className) {
				ClassName = className;
			}

			public void setHeadPhotoURL(String headPhotoURL) {
				HeadPhotoURL = headPhotoURL;
			}

			public void setId_sw(String id_sw) {
				Id_sw = id_sw;
			}

			public void setMyName(String myName) {
				MyName = myName;
			}

			public void setOperatorName(String operatorName) {
				OperatorName = operatorName;
			}

			public void setOperatorSubject(String operatorSubject) {
				OperatorSubject = operatorSubject;
			}

			public void setOperatorid(String operatorid) {
				Operatorid = operatorid;
			}

			public void setRemark(String remark) {
				Remark = remark;
			}

			public void setSignDate(String signDate) {
				SignDate = signDate;
			}

			public void setSignTime(String signTime) {
				SignTime = signTime;
			}

			public void setUserid(String userid) {
				Userid = userid;
			}

			private String Userid;

		}
	}
}
