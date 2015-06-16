package com.halong.aubaby.entity;

import java.io.Serializable;

public class WDiaryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8934115848963036851L;

	private Boolean result;//数据请求是否成功
	private Pic[] pic;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Pic[] getPic() {
		return pic;
	}

	public void setPic(Pic[] pic) {
		this.pic = pic;
	}

	public static class Pic implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9118063055754373030L;
		private String diaryid;//日志ID
		private String picid;//图片ID
		private String publishTime;//发表时间
		private String type;//日记携带图片类型
		private String url;//图片链接

		public String getDiaryid() {
			return diaryid;
		}

		public void setDiaryid(String diaryid) {
			this.diaryid = diaryid;
		}

		public String getPicid() {
			return picid;
		}

		public void setPicid(String picid) {
			this.picid = picid;
		}

		public String getPublishTime() {
			return publishTime;
		}

		public void setPublishTime(String publishTime) {
			this.publishTime = publishTime;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
