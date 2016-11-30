package com.session.common;


public class BaseListResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;
	protected final static String LIST = "list";
	protected final static String ENDFLAG = "endFlag";
	
	private int endFlag;
	public int getEndFlag()
	{
		return endFlag;
	}
	public void setEndFlag(int endFlag)
	{
		this.endFlag = endFlag;
	}
}	
