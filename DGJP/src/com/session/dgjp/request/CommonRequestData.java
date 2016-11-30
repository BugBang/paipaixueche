package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 检查用户是否设置支付密码、退出登录、获取个人信息 */
public class CommonRequestData extends BaseRequestData {

	private static final long serialVersionUID = -1136718052081994187L;

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
