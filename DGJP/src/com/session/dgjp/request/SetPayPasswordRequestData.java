package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 设置支付密码 */
public class SetPayPasswordRequestData extends BaseRequestData {

	private static final long serialVersionUID = -4765871357984952112L;

	/** 支付密码 */
	private String payPassword;

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
