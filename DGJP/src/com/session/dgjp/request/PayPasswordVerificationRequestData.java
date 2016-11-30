package com.session.dgjp.request;

import com.session.common.BaseRequestData;
import com.session.common.BaseRequestTask;
import com.session.common.utils.MD5Util;

public class PayPasswordVerificationRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getSpecificUrlPath()
	{
		return "account/verifyPayPassword";
	}
	
	private String payPassword;

	public void setPayPassword(String payPassword)
	{
		this.payPassword = MD5Util.encode(payPassword+BaseRequestTask.KEY);
	}

}
