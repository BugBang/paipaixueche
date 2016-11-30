package com.session.dgjp.response;

import com.session.common.BaseResponseData;

public class TrainingFeeResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;
	
	private int fee = 0;
	
	private int originalFee = 0;

	public int getFee()
	{
		return fee;
	}

	public int getOriginalFee()
	{
		return originalFee;
	}

	public void setOriginalFee(int originalFee)
	{
		this.originalFee = originalFee;
	}
	
	
}
