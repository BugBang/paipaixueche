package com.session.dgjp.request;

import com.session.common.BaseRequestData;

public class CouponListRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;
	
	private String isUseable;
	private long branchSchoolId;
	private int preferentialFee = 0;//默认查询全部
	
	private int pageIndex = 1;
	private int pageSize = 0;
	
	@Override
	protected String getSpecificUrlPath()
	{
		return "coupon/queryStudentUsableCouponList";
	}

	public String getIsUseable()
	{
		return isUseable;
	}

	public void setIsUseable(String isUseable)
	{
		this.isUseable = isUseable;
	}

	public long getBranchSchoolId()
	{
		return branchSchoolId;
	}

	public void setBranchSchoolId(long branchSchoolId)
	{
		this.branchSchoolId = branchSchoolId;
	}

	public int getPreferentialFee()
	{
		return preferentialFee;
	}

	public void setPreferentialFee(int preferentialFee)
	{
		this.preferentialFee = preferentialFee;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
