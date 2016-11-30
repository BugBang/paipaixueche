package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 获取指定城市下的分校 */
public class GetCityBranchSchoolListRequestData extends BaseRequestData {

	private static final long serialVersionUID = -5688756760973058785L;
	
	/** 城市id */
	private long cityId;
	private int pageIndex = 1;
	private int pageSize = 0;

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
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
