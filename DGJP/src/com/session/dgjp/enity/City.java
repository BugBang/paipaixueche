package com.session.dgjp.enity;

import java.io.Serializable;

/** 城市 */
public class City implements Serializable {

	private static final long serialVersionUID = 6324723642227271822L;
	private long id;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
