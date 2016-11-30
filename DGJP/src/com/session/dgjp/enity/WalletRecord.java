package com.session.dgjp.enity;

import java.io.Serializable;

/** 钱包记录 */
public class WalletRecord implements Serializable {

	private static final long serialVersionUID = -4279586679084403120L;
	private long id;
	private String typeName;
	private String logTime;
	private double money;
	private String yearMonth;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

}
