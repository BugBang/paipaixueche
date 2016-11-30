package com.session.dgjx.enity;

import java.io.Serializable;
import java.sql.Date;

public class Yard implements Serializable
{
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;//场地名称，暂时与分校名称一致
	private char status;//状态
	private String city;//市
	private String county;//区或县
	private String town;//乡或村
	private String address;//详细地址
	private int area;//场地面积
	private Date createTime;//创建时间
	private double leftTopLongitude;//左上角经度
	private double leftTopLatitude;//左上角纬度
	private double rightBottomLongitude;//右下角经度
	private double rightBottomLatitude;//右下角纬度
	
	public double getLeftTopLongitude()
	{
		return leftTopLongitude;
	}
	public void setLeftTopLongitude(double leftTopLongitude)
	{
		this.leftTopLongitude = leftTopLongitude;
	}
	public double getLeftTopLatitude()
	{
		return leftTopLatitude;
	}
	public void setLeftTopLatitude(double leftTopLatitude)
	{
		this.leftTopLatitude = leftTopLatitude;
	}
	public double getRightBottomLongitude()
	{
		return rightBottomLongitude;
	}
	public void setRightBottomLongitude(double rightBottomLongitude)
	{
		this.rightBottomLongitude = rightBottomLongitude;
	}
	public double getRightBottomLatitude()
	{
		return rightBottomLatitude;
	}
	public void setRightBottomLatitude(double rightBottomLatitude)
	{
		this.rightBottomLatitude = rightBottomLatitude;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
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
	public char getStatus()
	{
		return status;
	}
	public void setStatus(char status)
	{
		this.status = status;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getCounty()
	{
		return county;
	}
	public void setCounty(String county)
	{
		this.county = county;
	}
	public String getTown()
	{
		return town;
	}
	public void setTown(String town)
	{
		this.town = town;
	}
	public int getArea()
	{
		return area;
	}
	public void setArea(int area)
	{
		this.area = area;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
}
