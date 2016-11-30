package com.session.dgjp.response;

import java.util.List;

import com.session.common.BaseListResponseData;
import com.session.dgjp.enity.Car;

public class CarScheduleResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = 1L;
	
	private List<Car> list;
	
	public List<Car> getList()
	{
		return list;
	}
	public void setList(List<Car> list)
	{
		this.list = list;
	}
	
}
