package com.session.dgjp.response;

import java.util.List;

import com.session.common.BaseListResponseData;
import com.session.dgjp.enity.Coupon;

public class CouponListResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = -3762330656246542884L;
	private List<Coupon> list;
	
	public List<Coupon> getList()
	{
		return list;
	}
	
}
