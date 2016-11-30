package com.session.dgjp.response;

import java.util.List;

import com.session.common.BaseListResponseData;
import com.session.dgjp.enity.Label;

public class LabelListResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = 1L;
	private List<Label> list;
	
	public List<Label> getList()
	{
		return list;
	}

}
