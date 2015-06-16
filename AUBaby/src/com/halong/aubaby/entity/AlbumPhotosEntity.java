package com.halong.aubaby.entity;

import java.io.Serializable;

public class AlbumPhotosEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387648953290267693L;

	private Boolean result;
	private itemList itemList;

	public boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public itemList getItemList() {
		return itemList;
	}

	public void setItemList(itemList itemList) {
		this.itemList = itemList;
	}

	public class itemList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2781439186487353989L;
		private item[] item;

		public class item implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2702341630501921406L;

			private String diaryid;//日记id
			private String id;//收藏id
			private String picid;//图片id
			private String publishDate;//发表时间
			private String url;//图片链接

			public String getDiaryid() {
				return diaryid;
			}

			public void setDiaryid(String diaryid) {
				this.diaryid = diaryid;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getPicid() {
				return picid;
			}

			public void setPicid(String picid) {
				this.picid = picid;
			}

			public String getPublishDate() {
				return publishDate;
			}

			public void setPublishDate(String publishDate) {
				this.publishDate = publishDate;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}
		}

		public item[] getItem() {
			return item;
		}

		public void setItem(item[] item) {
			this.item = item;
		}
	}

}
