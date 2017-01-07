package com.session.dgjx;

import android.os.Environment;

/** 常量配置 */
public final class Constants {

	/** 通信协议版本 */
	public final static String VERSION_PROTOCOL = "1.0";
	/** 生产环境IP地址 */
	public final static String RELEASE_URL = "https://120.76.168.132:8443/";
	/** 测试环境IP地址 */
//	public final static String TEST_URL = "https://183.62.251.19:8443/";
	public final static String TEST_URL = "https://10.0.0.101:8443/";
	/** 后台接口主机IP地址 */
	public final static String URL_IP = TEST_URL;
	/** 后台接口地址 */
	public final static String URL = URL_IP + "DGFDS/";
	/** 初始化接口地址 */
	public final static String URL_INIT = URL + "token.flow";
	/** 登录接口地址 */
	public final static String URL_LOGIN = URL + "account/login.flow";
	/** 退出登录接口地址 */
	public final static String URL_LOGOUT = URL + "account/logout.flow";
	/** 版本检测接口地址 */
	public final static String URL_CHECK_VERSION = URL + "account/checkVersion.flow";
	/** 获取验证码接口地址 */
	public final static String URL_GET_VERIFY_CODE = URL + "account/getVerifyCode.flow";
	/** 获取个人信息接口地址 */
	public final static String URL_GET_ACCOUNT_INFO = URL + "account/getAccountInfo.flow";
	/** 重置支付密码接口地址 */
	public final static String URL_RESET_PAY_PASSWORD = URL + "account/resetPayPassword.flow";
	/** 设置支付密码接口地址 */
	public final static String URL_SET_PAY_PASSWORD = URL + "account/setPayPassword.flow";
	/** 检查用户是否设置支付密码接口地址 */
	public final static String URL_CHECK_PAY_PASSWORD = URL + "account/checkPayPassword.flow";
	/** 操作订单接口地址 */
	public final static String URL_OPERATE_ORDER = URL + "order/operateOrder.flow";
	/** 日志 */
	public final static String LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/dgjx";

	private Constants() {
		// 常量配置类，不需要实例
	}

}
