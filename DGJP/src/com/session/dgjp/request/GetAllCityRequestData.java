package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 获取所有城市 */
public class GetAllCityRequestData extends BaseRequestData {

	private static final long serialVersionUID = 5932121751388817057L;
	
	private int pageIndex = 1;
	private int pageSize = 0;

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
