package com.session.dgjx.enity;

import java.io.Serializable;
import java.util.Date;

public class BranchSchool implements Serializable
{
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;//分校名称
	private String remark;
	private char status;
	private String phone;
	private String linkman;
	private String email;
	private String driverSchoolName;//驾校名称
	private int timeProid;
	private Date createTime;
	private Date updateTime;
	private String distance;//与驾校的距离
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public char getStatus()
	{
		return status;
	}
	public void setStatus(char status)
	{
		this.status = status;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getLinkman()
	{
		return linkman;
	}
	public void setLinkman(String linkman)
	{
		this.linkman = linkman;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getDriverSchoolName()
	{
		return driverSchoolName;
	}
	public void setDriverSchoolName(String driverSchoolName)
	{
		this.driverSchoolName = driverSchoolName;
	}
	public int getTimeProid()
	{
		return timeProid;
	}
	public void setTimeProid(int timeProid)
	{
		this.timeProid = timeProid;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public String getDistance()
	{
		return distance;
	}
	public void setDistance(String distance)
	{
		this.distance = distance;
	}
	
	
	@Override
	public String toString()
	{
		return name;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		BranchSchool other = (BranchSchool) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
