package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/** 登录 */
public class LoginRequestData extends BaseRequestData {

	private static final long serialVersionUID = 1363596127741931234L;
	
	/** 验证码 */
	private String password;
	
	/** 验证类型，S表示短信验证码登录 */
	private final String loginType = "S";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginType() {
		return loginType;
	}
	
	@Override
	protected String getSpecificUrlPath() {
		return "account/login";
	}

}
