package com.session.dgjp.response;

import com.session.common.BaseResponseData;

public class CreateOrderResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 4813274647330079351L;
	
	private String orderId;
	private int remainTime = 0;

	public String getOrderId()
	{
		return orderId;
	}

	public int getRemainTime()
	{
		return remainTime;
	}
	
}
