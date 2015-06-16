package com.halong.aubaby.entity;

import java.io.Serializable;

public class NoticeReplyEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3935003641968773194L;

	private boolean result;
	private commentList commentList;

	public boolean isResult() {
		return result;
	}

	public commentList getCommentList() {
		return commentList;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setCommentList(commentList commentList) {
		this.commentList = commentList;
	}

	public static class commentList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7666151313628578227L;

		private comment[] comment;

		public comment[] getComment() {
			return comment;
		}

		public void setComment(comment[] comment) {
			this.comment = comment;
		}

		public static class comment implements Serializable {

			/**
		 * 
		 */
			private static final long serialVersionUID = 8235773100404363739L;
			
			private replyAttachments replyAttachments;
			private commentAttachments commentAttachments;
			private String content_comment;
			private String content_reply;
			private String headPhoto_comment;
			private String headPhoto_reply;
			private String id;
			private String isAdmin_comment;
			private String isAdmin_reply;
			private String time_comment;
			private String time_reply;
			private String userid_comment;
			private String userid_reply;
			private String username_comment;
			private String username_reply;

			public String getTime_comment() {
				return time_comment;
			}

			public String getTime_reply() {
				return time_reply;
			}

			public String getUserid_comment() {
				return userid_comment;
			}

			public String getUserid_reply() {
				return userid_reply;
			}

			public String getUsername_comment() {
				return username_comment;
			}

			public String getUsername_reply() {
				return username_reply;
			}

			public void setTime_comment(String time_comment) {
				this.time_comment = time_comment;
			}

			public void setTime_reply(String time_reply) {
				this.time_reply = time_reply;
			}

			public void setUserid_comment(String userid_comment) {
				this.userid_comment = userid_comment;
			}

			public void setUserid_reply(String userid_reply) {
				this.userid_reply = userid_reply;
			}

			public void setUsername_comment(String username_comment) {
				this.username_comment = username_comment;
			}

			public void setUsername_reply(String username_reply) {
				this.username_reply = username_reply;
			}

			public commentAttachments getCommentAttachments() {
				return commentAttachments;
			}

			public void setCommentAttachments(
					commentAttachments commentAttachments) {
				this.commentAttachments = commentAttachments;
			}

			public String getContent_comment() {
				return content_comment;
			}

			public String getContent_reply() {
				return content_reply;
			}

			public String getHeadPhoto_comment() {
				return headPhoto_comment;
			}

			public String getHeadPhoto_reply() {
				return headPhoto_reply;
			}

			public String getId() {
				return id;
			}

			public String getIsAdmin_comment() {
				return isAdmin_comment;
			}

			public String getIsAdmin_reply() {
				return isAdmin_reply;
			}

			public void setContent_comment(String content_comment) {
				this.content_comment = content_comment;
			}

			public void setContent_reply(String content_reply) {
				this.content_reply = content_reply;
			}

			public void setHeadPhoto_comment(String headPhoto_comment) {
				this.headPhoto_comment = headPhoto_comment;
			}

			public void setHeadPhoto_reply(String headPhoto_reply) {
				this.headPhoto_reply = headPhoto_reply;
			}

			public void setId(String id) {
				this.id = id;
			}

			public void setIsAdmin_comment(String isAdmin_comment) {
				this.isAdmin_comment = isAdmin_comment;
			}

			public void setIsAdmin_reply(String isAdmin_reply) {
				this.isAdmin_reply = isAdmin_reply;
			}

			public static class commentAttachments implements Serializable {

				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

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
					private static final long serialVersionUID = 8008551936712606725L;
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

			public replyAttachments getReplyAttachments() {
				return replyAttachments;
			}

			public void setReplyAttachments(replyAttachments replyAttachments) {
				this.replyAttachments = replyAttachments;
			}

			public static class replyAttachments implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 7290870328118515603L;
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
					private static final long serialVersionUID = 8008551936712606725L;
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

		}

	}
}
