package com.session.dgjp.response;

import com.session.common.BaseResponseData;

public class OperateOrderResponseData extends BaseResponseData
{

	private static final long serialVersionUID = 1L;
	
	private String onlinePayId;//预支付订单号（在线支付订单号）
	private String desc;//商品描述/商品名称，如手机端微信支付时需要的xml串，手机端可直接使用
	
	public String getOnlinePayId()
	{
		return onlinePayId;
	}
	public String getDesc()
	{
		return desc;
	}
	
}
