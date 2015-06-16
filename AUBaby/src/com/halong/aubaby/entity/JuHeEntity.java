package com.halong.aubaby.entity;

import java.io.Serializable;

public class JuHeEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6547472534875998758L;
	private Boolean result;
	private String date;
	private listJiaXiaoTong listJiaXiaoTong;
	private listSign listSign;
	private listXiTongXinxi listXiTongXinxi;

	public Boolean getResult() {
		return result;
	}

	public String getDate() {
		return date;
	}

	public listJiaXiaoTong getListJiaXiaoTong() {
		return listJiaXiaoTong;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setListJiaXiaoTong(listJiaXiaoTong listJiaXiaoTong) {
		this.listJiaXiaoTong = listJiaXiaoTong;
	}

	public listSign getListSign() {
		return listSign;
	}

	public void setListSign(listSign listSign) {
		this.listSign = listSign;
	}

	public listXiTongXinxi getListXiTongXinxi() {
		return listXiTongXinxi;
	}

	public void setListXiTongXinxi(listXiTongXinxi listXiTongXinxi) {
		this.listXiTongXinxi = listXiTongXinxi;
	}

	public class listJiaXiaoTong implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8140400656316446019L;

		private info[] info;

		public info[] getInfo() {
			return info;
		}

		public void setInfo(info[] info) {
			this.info = info;
		}

		public class info implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6000143664438238981L;
			private String attachType;
			private String attachURL;
			private String className;
			private String classid;
			private String headPhotoURL;
			private int id;
			private String isAdmin;
			private String isRead;
			private String isReply;
			private String isUrgency;
			private int noticeCanReply;
			private String noticeReplyCnt;
			private String noticeType;
			private String originName;
			private String ownerName;
			private String publish_userid;
			private String publish_username;
			private String size;
			private String title;
			private String time;
			private String useClassName;

			public String getAttachType() {
				return attachType;
			}

			public String getAttachURL() {
				return attachURL;
			}

			public String getClassName() {
				return className;
			}

			public String getClassid() {
				return classid;
			}

			public String getHeadPhotoURL() {
				return headPhotoURL;
			}

			public int getId() {
				return id;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public String getIsRead() {
				return isRead;
			}

			public String getIsReply() {
				return isReply;
			}

			public String getIsUrgency() {
				return isUrgency;
			}

			public int getNoticeCanReply() {
				return noticeCanReply;
			}

			public String getNoticeReplyCnt() {
				return noticeReplyCnt;
			}

			public String getNoticeType() {
				return noticeType;
			}

			public String getOriginName() {
				return originName;
			}

			public String getOwnerName() {
				return ownerName;
			}

			public String getPublish_userid() {
				return publish_userid;
			}

			public String getPublish_username() {
				return publish_username;
			}

			public String getSize() {
				return size;
			}

			public String getTitle() {
				return title;
			}

			public String getTime() {
				return time;
			}

			public String getUseClassName() {
				return useClassName;
			}

			public void setAttachType(String attachType) {
				this.attachType = attachType;
			}

			public void setAttachURL(String attachURL) {
				this.attachURL = attachURL;
			}

			public void setClassName(String className) {
				this.className = className;
			}

			public void setClassid(String classid) {
				this.classid = classid;
			}

			public void setHeadPhotoURL(String headPhotoURL) {
				this.headPhotoURL = headPhotoURL;
			}

			public void setId(int id) {
				this.id = id;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}

			public void setIsRead(String isRead) {
				this.isRead = isRead;
			}

			public void setIsReply(String isReply) {
				this.isReply = isReply;
			}

			public void setIsUrgency(String isUrgency) {
				this.isUrgency = isUrgency;
			}

			public void setNoticeCanReply(int noticeCanReply) {
				this.noticeCanReply = noticeCanReply;
			}

			public void setNoticeReplyCnt(String noticeReplyCnt) {
				this.noticeReplyCnt = noticeReplyCnt;
			}

			public void setNoticeType(String noticeType) {
				this.noticeType = noticeType;
			}

			public void setOriginName(String originName) {
				this.originName = originName;
			}

			public void setOwnerName(String ownerName) {
				this.ownerName = ownerName;
			}

			public void setPublish_userid(String publish_userid) {
				this.publish_userid = publish_userid;
			}

			public void setPublish_username(String publish_username) {
				this.publish_username = publish_username;
			}

			public void setSize(String size) {
				this.size = size;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public void setTime(String time) {
				this.time = time;
			}

			public void setUseClassName(String useClassName) {
				this.useClassName = useClassName;
			}

		}
	}

	public class listSign implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4478808192963755536L;
		private list[] list;

		public list[] getList() {
			return list;
		}

		public void setList(list[] list) {
			this.list = list;
		}

		public class list implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6978647476295369342L;
			private String Userid;
			private String SignTime;
			private String SignDate;
			private String Remark;
			private String Operatorid;
			private String OperatorSubject;
			private String OperatorName;
			private String MyName;
			private String Id_sw;
			private String HeadPhotoURL;

			public String getUserid() {
				return Userid;
			}

			public String getSignTime() {
				return SignTime;
			}

			public String getSignDate() {
				return SignDate;
			}

			public String getRemark() {
				return Remark;
			}

			public String getOperatorid() {
				return Operatorid;
			}

			public String getOperatorSubject() {
				return OperatorSubject;
			}

			public String getOperatorName() {
				return OperatorName;
			}

			public String getMyName() {
				return MyName;
			}

			public String getId_sw() {
				return Id_sw;
			}

			public String getHeadPhotoURL() {
				return HeadPhotoURL;
			}

			public void setUserid(String userid) {
				Userid = userid;
			}

			public void setSignTime(String signTime) {
				SignTime = signTime;
			}

			public void setSignDate(String signDate) {
				SignDate = signDate;
			}

			public void setRemark(String remark) {
				Remark = remark;
			}

			public void setOperatorid(String operatorid) {
				Operatorid = operatorid;
			}

			public void setOperatorSubject(String operatorSubject) {
				OperatorSubject = operatorSubject;
			}

			public void setOperatorName(String operatorName) {
				OperatorName = operatorName;
			}

			public void setMyName(String myName) {
				MyName = myName;
			}

			public void setId_sw(String id_sw) {
				Id_sw = id_sw;
			}

			public void setHeadPhotoURL(String headPhotoURL) {
				HeadPhotoURL = headPhotoURL;
			}

		}

	}

	public class listXiTongXinxi implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3684472256634144963L;

		private info[] info;

		public info[] getInfo() {
			return info;
		}

		public void setInfo(info[] info) {
			this.info = info;
		}

		public class info implements Serializable {

			/**
		 * 
		 */
			private static final long serialVersionUID = 6174509609154933930L;
			private String useClassName;
			private String title;
			private String time;
			private String size;
			private String publish_username;
			private String ownerName;
			private String publish_userid;
			private String originName;
			private String noticeType;
			private String noticeReplyCnt;
			private String noticeCanReply;
			private String isUrgency;
			private String isReply;
			private String isRead;
			private String isAdmin;
			private String id;
			private String headPhotoURL;
			private String classid;
			private String className;
			private String attachURL;
			private String attachType;
			private newTitle newTitle;

			public String getUseClassName() {
				return useClassName;
			}

			public String getTitle() {
				return title;
			}

			public String getTime() {
				return time;
			}

			public String getSize() {
				return size;
			}

			public String getPublish_username() {
				return publish_username;
			}

			public String getOwnerName() {
				return ownerName;
			}

			public String getPublish_userid() {
				return publish_userid;
			}

			public String getOriginName() {
				return originName;
			}

			public String getNoticeType() {
				return noticeType;
			}

			public String getNoticeReplyCnt() {
				return noticeReplyCnt;
			}

			public String getNoticeCanReply() {
				return noticeCanReply;
			}

			public String getIsUrgency() {
				return isUrgency;
			}

			public String getIsReply() {
				return isReply;
			}

			public String getIsRead() {
				return isRead;
			}

			public String getIsAdmin() {
				return isAdmin;
			}

			public String getId() {
				return id;
			}

			public String getHeadPhotoURL() {
				return headPhotoURL;
			}

			public String getClassid() {
				return classid;
			}

			public String getClassName() {
				return className;
			}

			public String getAttachURL() {
				return attachURL;
			}

			public String getAttachType() {
				return attachType;
			}

			public void setUseClassName(String useClassName) {
				this.useClassName = useClassName;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public void setTime(String time) {
				this.time = time;
			}

			public void setSize(String size) {
				this.size = size;
			}

			public void setPublish_username(String publish_username) {
				this.publish_username = publish_username;
			}

			public void setOwnerName(String ownerName) {
				this.ownerName = ownerName;
			}

			public void setPublish_userid(String publish_userid) {
				this.publish_userid = publish_userid;
			}

			public void setOriginName(String originName) {
				this.originName = originName;
			}

			public void setNoticeType(String noticeType) {
				this.noticeType = noticeType;
			}

			public void setNoticeReplyCnt(String noticeReplyCnt) {
				this.noticeReplyCnt = noticeReplyCnt;
			}

			public void setNoticeCanReply(String noticeCanReply) {
				this.noticeCanReply = noticeCanReply;
			}

			public void setIsUrgency(String isUrgency) {
				this.isUrgency = isUrgency;
			}

			public void setIsReply(String isReply) {
				this.isReply = isReply;
			}

			public void setIsRead(String isRead) {
				this.isRead = isRead;
			}

			public void setIsAdmin(String isAdmin) {
				this.isAdmin = isAdmin;
			}

			public void setId(String id) {
				this.id = id;
			}

			public void setHeadPhotoURL(String headPhotoURL) {
				this.headPhotoURL = headPhotoURL;
			}

			public void setClassid(String classid) {
				this.classid = classid;
			}

			public void setClassName(String className) {
				this.className = className;
			}

			public void setAttachURL(String attachURL) {
				this.attachURL = attachURL;
			}

			public void setAttachType(String attachType) {
				this.attachType = attachType;
			}

			public newTitle getNewTitle() {
				return newTitle;
			}

			public void setNewTitle(newTitle newTitle) {
				this.newTitle = newTitle;
			}

			public class newTitle implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 3650992105753475207L;

				public String getA() {
					return A;
				}

				public String getH() {
					return H;
				}

				public String getID() {
					return ID;
				}

				public String getJUMP() {
					return JUMP;
				}

				public String getU() {
					return U;
				}

				public String getContent() {
					return content;
				}

				public void setA(String a) {
					A = a;
				}

				public void setH(String h) {
					H = h;
				}

				public void setID(String iD) {
					ID = iD;
				}

				public void setJUMP(String jUMP) {
					JUMP = jUMP;
				}

				public void setU(String u) {
					U = u;
				}

				public void setContent(String content) {
					this.content = content;
				}

				private String A;
				private String H;
				private String ID;
				private String JUMP;
				private String U;
				private String content;
			}
		}

	}
}
