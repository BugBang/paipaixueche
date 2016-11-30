package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 重置支付密码 */
public class ResetPayPasswordRequestData extends BaseRequestData {

	private static final long serialVersionUID = -616427548456238562L;
	
	/** {@link ResetPayPasswordRequestData#modifyType} 修改支付密码（需要输入旧支付密码） */
	public final static String MODIFY = "R";
	/** {@link ResetPayPasswordRequestData#modifyType} 重置支付密码（需要输入验证码，身份证号码） */
	public final static String RESET = "L";

	/** 修改类型，R为修改支付密码（需要输入旧支付密码），L为重置支付密码（需要输入验证码，身份证号码） */
	private String modifyType;
	/** 旧支付密码 */
	private String oldPayPassword;
	/** 新支付密码 */
	private String newPayPassword;
	/** 验证码 */
	private String verifyCode;
	/** 身份证号码 */
	private String idcard;

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getOldPayPassword() {
		return oldPayPassword;
	}

	public void setOldPayPassword(String oldPayPassword) {
		this.oldPayPassword = oldPayPassword;
	}

	public String getNewPayPassword() {
		return newPayPassword;
	}

	public void setNewPayPassword(String newPayPassword) {
		this.newPayPassword = newPayPassword;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
