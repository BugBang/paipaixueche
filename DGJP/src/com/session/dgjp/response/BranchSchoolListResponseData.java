package com.session.dgjp.response;

import java.util.List;

import com.session.common.BaseListResponseData;
import com.session.dgjp.enity.BranchSchool;

public class BranchSchoolListResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = -3762330656246542884L;
	private List<BranchSchool> list;
	
	public List<BranchSchool> getList()
	{
		return list;
	}
	
}
