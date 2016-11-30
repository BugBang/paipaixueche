package com.session.dgjp.response;

import java.util.List;

import com.session.common.BaseResponseData;
import com.session.dgjp.enity.PayType;

public class PayTypeListResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;

	private List<PayType> list;

	public List<PayType> getList()
	{
		return list;
	}
	
}
