package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 查询订单支付状态 */
public class GetOrderPayStatusRequestData extends BaseRequestData {

	private static final long serialVersionUID = -3723233409417866112L;

	/** 订单id */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "order/getOrderPayStatus";
	}

}
