package com.session.dgjp.request;

import com.session.common.BaseRequestData;

public class PayTypeListRequestData extends BaseRequestData
{

	private static final long serialVersionUID = 1L;

	@Override
	protected String getSpecificUrlPath()
	{
		return "order/queryPayTypeList";
	}

}
