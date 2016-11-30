package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/** 版本检测 */
public class CheckVersionRequestData extends BaseRequestData {

	private static final long serialVersionUID = 6288667290146333409L;

	/** type类型，VSA表示android教练版 */
	private final String type = "VTA";

	public String getType() {
		return type;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
