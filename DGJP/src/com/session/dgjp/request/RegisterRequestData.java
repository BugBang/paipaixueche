package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/** 注册 */
public class RegisterRequestData extends BaseRequestData {

	private static final long serialVersionUID = 7343816173169571100L;

	/** 姓名 */
	private String name;
	/** 身份证号码 */
	private String idcard;
	/** 手机号码 */
	private String phone;
	/** 验证码 */
	private String verifyCode;
	/** 分校id */
	private long branchSchoolId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public long getBranchSchoolId() {
		return branchSchoolId;
	}

	public void setBranchSchoolId(long branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
