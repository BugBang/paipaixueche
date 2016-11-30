package com.session.dgjp.response;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.BaseResponseData;
import com.session.common.utils.GsonUtil;
import com.session.dgjp.enity.Label;

public class EvaluationResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String comment;
	private int score;
	private List<Label> labelList;
	
	public String getComment()
	{
		return comment;
	}
	public int getScore()
	{
		return score;
	}
	public List<Label> getLabelList()
	{
		return labelList;
	}
	public String getOrderId()
	{
		return orderId;
	}
	
	
}
