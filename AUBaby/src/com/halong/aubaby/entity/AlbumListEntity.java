package com.halong.aubaby.entity;

import java.io.Serializable;

public class AlbumListEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1257757050791628140L;
	private Boolean result;
	private FolderList folderList;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public FolderList getFolderList() {
		return folderList;
	}

	public void setFolderList(FolderList folderList) {
		this.folderList = folderList;
	}

	public class FolderList implements Serializable {

		/**
	 * 
	 */
		private static final long serialVersionUID = -2944826182850036002L;
		private Folder[] folder;

		public Folder[] getFolder() {
			return folder;
		}

		public void setFolder(Folder[] folder) {
			this.folder = folder;
		}

		public class Folder implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4101707327708755891L;

			private int countOfPhotos;//照片数
			private String createMode;
			private String id;//相片id
			private String imgUrl;//封面照片
			private String name;//相册名

			public int getCountOfPhotos() {
				return countOfPhotos;
			}

			public void setCountOfPhotos(int countOfPhotos) {
				this.countOfPhotos = countOfPhotos;
			}

			public String getCreateMode() {
				return createMode;
			}

			public void setCreateMode(String createMode) {
				this.createMode = createMode;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getImgUrl() {
				return imgUrl;
			}

			public void setImgUrl(String imgUrl) {
				this.imgUrl = imgUrl;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

	}
}
