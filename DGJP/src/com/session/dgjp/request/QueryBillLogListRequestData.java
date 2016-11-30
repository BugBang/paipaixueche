package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 查询账单 */
public class QueryBillLogListRequestData extends BaseRequestData {

	private static final long serialVersionUID = -4575491328311465734L;
	
	/** 学员账号 */
	private String studentAccount;
	/** 开始时间 */
	private String beginTime = "2016-01-01";
	/** 结束时间 */
	private String endTime = "";
	/** 第几页 */
	private int pageIndex;
	/** 每页多少个 */
	private int pageSize = 10;

	public String getStudentAccount() {
		return studentAccount;
	}

	public void setStudentAccount(String studentAccount) {
		this.studentAccount = studentAccount;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
