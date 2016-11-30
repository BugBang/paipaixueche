package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 获取所有分校 */
public class QueryBranchSchoolListRequestData extends BaseRequestData {

	private static final long serialVersionUID = -2626386305996696315L;
	/** 经度 */
	private double longitude;
	/** 纬度 */
	private double latitude;

	private int pageIndex;
	private int pageSize;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
