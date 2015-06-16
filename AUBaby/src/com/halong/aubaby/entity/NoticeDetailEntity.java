package com.halong.aubaby.entity;

import java.io.Serializable;

public class NoticeDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349573858529190170L;
	private boolean result;
	private attachments attachments;

	private noticeEntity noticeEntity;
	private receiverDetail receiverDetail;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public noticeEntity getNoticeEntity() {
		return noticeEntity;
	}

	public void setNoticeEntity(noticeEntity noticeEntity) {
		this.noticeEntity = noticeEntity;
	}

	public attachments getAttachments() {
		return attachments;
	}

	public void setAttachments(attachments attachments) {
		this.attachments = attachments;
	}

	public receiverDetail getReceiverDetail() {
		return receiverDetail;
	}

	public void setReceiverDetail(receiverDetail receiverDetail) {
		this.receiverDetail = receiverDetail;
	}

	public static class attachments implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3457628721648544729L;
		private attachment[] attachment;

		public attachment[] getAttachment() {
			return attachment;
		}

		public void setAttachment(attachment[] attachment) {
			this.attachment = attachment;
		}

		public static class attachment implements Serializable {

			/**
		 * 
		 */
			private static final long serialVersionUID = -5761192107859965087L;

			private String originName;

			private String size;
			private String type;
			private String url;

			public String getOriginName() {
				return originName;
			}

			public String getSize() {
				return size;
			}

			public String getType() {
				return type;
			}

			public String getUrl() {
				return url;
			}

			public void setOriginName(String originName) {
				this.originName = originName;
			}

			public void setSize(String size) {
				this.size = size;
			}

			public void setType(String type) {
				this.type = type;
			}

			public void setUrl(String url) {
				this.url = url;
			}
		}
	}

	public static class noticeEntity implements Serializable {

		/**
	 * 
	 */
		private static final long serialVersionUID = -5055200518204070883L;
		private String classid;
		private String content;
		private String countOfComment;
		private String countOfNewComment;
		private String headPhotoURL;
		private String id;
		private String isAdmin;
		private String myClassName;
		private String noticeType;
		private String onoff_comment;
		private String onoff_emergency;
		private String onoff_mutilSelect;
		private String onoff_needSign;
		private String onoff_vedio;
		private String onoff_voice;
		private String onoff_vote;
		private String ownername;
		private String publishTime;
		private String title;
		private String useClassName;
		private String userGroupName;
		private String userid;

		public String getClassid() {
			return classid;
		}

		public void setClassid(String classid) {
			this.classid = classid;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCountOfComment() {
			return countOfComment;
		}

		public void setCountOfComment(String countOfComment) {
			this.countOfComment = countOfComment;
		}

		public String getCountOfNewComment() {
			return countOfNewComment;
		}

		public void setCountOfNewComment(String countOfNewComment) {
			this.countOfNewComment = countOfNewComment;
		}

		public String getHeadPhotoURL() {
			return headPhotoURL;
		}

		public void setHeadPhotoURL(String headPhotoURL) {
			this.headPhotoURL = headPhotoURL;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(String isAdmin) {
			this.isAdmin = isAdmin;
		}

		public String getMyClassName() {
			return myClassName;
		}

		public void setMyClassName(String myClassName) {
			this.myClassName = myClassName;
		}

		public String getNoticeType() {
			return noticeType;
		}

		public void setNoticeType(String noticeType) {
			this.noticeType = noticeType;
		}

		public String getOnoff_comment() {
			return onoff_comment;
		}

		public void setOnoff_comment(String onoff_comment) {
			this.onoff_comment = onoff_comment;
		}

		public String getOnoff_emergency() {
			return onoff_emergency;
		}

		public void setOnoff_emergency(String onoff_emergency) {
			this.onoff_emergency = onoff_emergency;
		}

		public String getOnoff_mutilSelect() {
			return onoff_mutilSelect;
		}

		public void setOnoff_mutilSelect(String onoff_mutilSelect) {
			this.onoff_mutilSelect = onoff_mutilSelect;
		}

		public String getOnoff_needSign() {
			return onoff_needSign;
		}

		public void setOnoff_needSign(String onoff_needSign) {
			this.onoff_needSign = onoff_needSign;
		}

		public String getOnoff_vedio() {
			return onoff_vedio;
		}

		public void setOnoff_vedio(String onoff_vedio) {
			this.onoff_vedio = onoff_vedio;
		}

		public String getOnoff_voice() {
			return onoff_voice;
		}

		public void setOnoff_voice(String onoff_voice) {
			this.onoff_voice = onoff_voice;
		}

		public String getOnoff_vote() {
			return onoff_vote;
		}

		public void setOnoff_vote(String onoff_vote) {
			this.onoff_vote = onoff_vote;
		}

		public String getOwnername() {
			return ownername;
		}

		public void setOwnername(String ownername) {
			this.ownername = ownername;
		}

		public String getPublishTime() {
			return publishTime;
		}

		public void setPublishTime(String publishTime) {
			this.publishTime = publishTime;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUseClassName() {
			return useClassName;
		}

		public void setUseClassName(String useClassName) {
			this.useClassName = useClassName;
		}

		public String getUserGroupName() {
			return userGroupName;
		}

		public void setUserGroupName(String userGroupName) {
			this.userGroupName = userGroupName;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

	}

	public static class receiverDetail implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7724806690090153092L;

		private String receveOverview;
		private String listA;
		private String listB;
		private String listC;
		public String getListA() {
			return listA;
		}

		public void setListA(String listA) {
			this.listA = listA;
		}

		public String getListB() {
			return listB;
		}

		public void setListB(String listB) {
			this.listB = listB;
		}

		public String getListC() {
			return listC;
		}

		public void setListC(String listC) {
			this.listC = listC;
		}

	

		public String getReceveOverview() {
			return receveOverview;
		}

		public void setReceveOverview(String receveOverview) {
			this.receveOverview = receveOverview;
		}
	}
}
