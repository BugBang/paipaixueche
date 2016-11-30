package com.session.common.utils;

import java.io.Serializable;

/** 版本信息 */
public final class VersionInfo implements Serializable {

	private static final long serialVersionUID = 3267315131690112507L;
	private String version;
	private int code;
	private int type;
	private String description;
	private String url;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
