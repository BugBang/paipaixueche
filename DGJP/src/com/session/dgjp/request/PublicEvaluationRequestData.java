package com.session.dgjp.request;

import java.util.List;

import com.session.common.BaseRequestData;

public class PublicEvaluationRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getSpecificUrlPath()
	{
		return "order/publicEvaluation";
	}
	
	private int score;//评价分数
	private String comment;//评价和建议
	private String labelIds;//评价标签id,用逗号分隔
	private String orderId;//订单id

	public float getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public String getLabelIds()
	{
		return labelIds;
	}
	public void setLabelIds(String ids)
	{
		this.labelIds = ids;
	}
	public String getOrderId()
	{
		return orderId;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

}
