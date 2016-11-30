package com.session.dgjp.response;

import com.session.common.BaseResponseData;

public class InitResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private String token;
	
	public String getSessionId()
	{
		return sessionId;
	}
	public String getToken()
	{
		return token;
	}
}
