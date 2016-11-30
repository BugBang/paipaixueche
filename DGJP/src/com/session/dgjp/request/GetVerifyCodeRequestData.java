package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 获取验证码 */
public class GetVerifyCodeRequestData extends BaseRequestData {

	private static final long serialVersionUID = 1785688682399883670L;
	
	/** {@link GetVerifyCodeRequestData#authType} 登录验证码 */
	public final static String LOGIN = "L";
	/** {@link GetVerifyCodeRequestData#authType} 重置支付密码验证码 */
	public final static String RESET_PAY_PWD = "P";
	/** {@link GetVerifyCodeRequestData#authType} 注册验证码 */
	public final static String REGISTER = "R";

	/** 验证类型，L表示登录验证码，P表示重置支付密码验证码，R表示注册验证码 */
	private String authType;

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
