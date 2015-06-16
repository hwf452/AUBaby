package com.halong.aubaby.tab5;

import java.io.Serializable;

public class NoticeListEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5326716291654384152L;
	private int id;
	private String title;
	private String time;
	private String noticeType;
	private int isUrgency;
	private int isAdmin;
	private String isRead;
	private int noticeCanReply;
	private int noticeReplyCnt;
	private String headPhotoURL;
	private newTitle newTitle;
	private String publish_userid;
	private String publish_username;
	private String attachURL;
	private String size;
	private String attachType;
	private String originName;
	private String noticeReceivedCnt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public int getIsUrgency() {
		return isUrgency;
	}

	public void setIsUrgency(int isUrgency) {
		this.isUrgency = isUrgency;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public int getNoticeCanReply() {
		return noticeCanReply;
	}

	public void setNoticeCanReply(int noticeCanReply) {
		this.noticeCanReply = noticeCanReply;
	}

	public int getNoticeReplyCnt() {
		return noticeReplyCnt;
	}

	public void setNoticeReplyCnt(int noticeReplyCnt) {
		this.noticeReplyCnt = noticeReplyCnt;
	}

	public newTitle getNewTitle() {
		return newTitle;
	}

	public void setNewTitle(newTitle newTitle) {
		this.newTitle = newTitle;
	}

	public String getHeadPhotoURL() {
		return headPhotoURL;
	}

	public void setHeadPhotoURL(String headPhotoURL) {
		this.headPhotoURL = headPhotoURL;
	}

	public String getPublish_userid() {
		return publish_userid;
	}

	public void setPublish_userid(String publish_userid) {
		this.publish_userid = publish_userid;
	}

	public String getPublish_username() {
		return publish_username;
	}

	public void setPublish_username(String publish_username) {
		this.publish_username = publish_username;
	}

	public String getAttachURL() {
		return attachURL;
	}

	public String getSize() {
		return size;
	}

	public void setAttachURL(String attachURL) {
		this.attachURL = attachURL;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getNoticeReceivedCnt() {
		return noticeReceivedCnt;
	}

	public void setNoticeReceivedCnt(String noticeReceivedCnt) {
		this.noticeReceivedCnt = noticeReceivedCnt;
	}

	public static class newTitle implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1946490508359600023L;

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
