package com.session.dgjx.enity;

import java.io.Serializable;

public class Car implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**车辆id*/
	private long carId;
	/**车牌号*/
	private String carno;
	/**车辆类型，如小轿车、中型车、大型车*/
	private String carType;
	/**车型，入捷达、大众*/
	private String name;
	/**档位类型，如自动挡、手动挡*/
	private String gearType;
	/**学车费，单位 分/小时*/
	private int fee;
	
	
	public long getCarId()
	{
		return carId;
	}
	public void setCarId(long carId)
	{
		this.carId = carId;
	}
	public String getCarno()
	{
		return carno;
	}
	public void setCarno(String carno)
	{
		this.carno = carno;
	}
	public String getCarType()
	{
		return carType;
	}
	public void setCarType(String carType)
	{
		this.carType = carType;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getGearType()
	{
		return gearType;
	}
	public void setGearType(String gearType)
	{
		this.gearType = gearType;
	}
	public int getFee()
	{
		return fee;
	}
	public void setFee(int fee)
	{
		this.fee = fee;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (carId ^ (carId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((carType == null) ? 0 : carType.hashCode());
		result = prime * result + ((carno == null) ? 0 : carno.hashCode());
		result = prime * result + ((gearType == null) ? 0 : gearType.hashCode());
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
		Car other = (Car) obj;
		if (carId != other.carId)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (carType == null)
		{
			if (other.carType != null)
				return false;
		} else if (!carType.equals(other.carType))
			return false;
		if (carno == null)
		{
			if (other.carno != null)
				return false;
		} else if (!carno.equals(other.carno))
			return false;
		if (gearType == null)
		{
			if (other.gearType != null)
				return false;
		} else if (!gearType.equals(other.gearType))
			return false;
		return true;
	}
	
	public String getEllipsisName()
	{
		if(name != null && name.length()>4)
		{
			String ellipsisName = name.substring(0, 4);
			ellipsisName +="...";
			return ellipsisName;
		}else {
			return name;
		}
	}
	
}
