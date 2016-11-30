package com.session.dgjx.enity;

import java.io.Serializable;

public class Trainer implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 教练账号 */
	private String account;
	/** 姓名 */
	private String name;
	/** 手机号码 */
	private String phone;
	/** 所属分校ID */
	private long branchSchoolId;
	/** 所属分校 */
	private String branchSchoolName;
	/** 驾校名称 */
	private String driverSchoolName;
	/** 预约次数。单位次 */
	private int orderTimes;
	/** 该教练平均评分 */
	private float eval;
	/** 头像地址 */
	private String photoUrl;
	/** 最近登录时间，若为空，则表示是首次登录 */
	private String lastLoginTime;

	private BranchSchool branchSchool;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getBranchSchoolId() {
		return branchSchoolId;
	}

	public void setBranchSchoolId(long branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}

	public String getBranchSchoolName() {
		return branchSchoolName;
	}

	public void setBranchSchoolName(String branchSchoolName) {
		this.branchSchoolName = branchSchoolName;
	}

	public String getDriverSchoolName() {
		return driverSchoolName;
	}

	public void setDriverSchoolName(String driverSchoolName) {
		this.driverSchoolName = driverSchoolName;
	}

	public int getOrderTimes() {
		return orderTimes;
	}

	public void setOrderTimes(int orderTimes) {
		this.orderTimes = orderTimes;
	}

	public float getEval() {
		return eval;
	}

	public void setEval(float eval) {
		this.eval = eval;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public BranchSchool getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(BranchSchool branchSchool) {
		this.branchSchool = branchSchool;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trainer other = (Trainer) obj;
		if (account == null)
		{
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		return true;
	}
	
}
