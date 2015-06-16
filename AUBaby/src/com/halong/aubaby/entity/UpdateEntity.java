package com.halong.aubaby.entity;

import java.io.Serializable;

public class UpdateEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7795761437231624471L;
	private boolean result;
	private String appname;
	private String apkname;
	private String verName;
	private int verCode;
	private String updateurl;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApkname() {
		return apkname;
	}

	public void setApkname(String apkname) {
		this.apkname = apkname;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public int getVerCode() {
		return verCode;
	}

	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}

	public String getUpdateurl() {
		return updateurl;
	}

	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
