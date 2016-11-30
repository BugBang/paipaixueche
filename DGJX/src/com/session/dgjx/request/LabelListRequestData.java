package com.session.dgjx.request;

import com.session.common.BaseRequestData;

public class LabelListRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;
	
	private String score;
	
	@Override
	protected String getSpecificUrlPath()
	{
		return "order/queryLabelList";
	}

	public String getScore()
	{
		return score;
	}

	public void setScore(String score)
	{
		this.score = score;
	}
	
}
