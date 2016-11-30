package com.session.dgjx.request;

import com.session.common.BaseRequestData;

public class OrderDetailRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;
	private String id;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	@Override
	protected String getSpecificUrlPath()
	{
		return "order/getOrderDetail";
	}
	
}
