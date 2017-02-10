package com.session.dgjp;

import android.os.Environment;

/** 常量配置 */
public final class Constants {

	/** 通信协议版本 */
	public final static String VERSION_PROTOCOL = "1.0";
//-------------------------------------------------------------
	/** 生产环境IP地址 */
	public final static String RELEASE_URL = "https://papaxueche.cn:8443/";
//	public final static String RELEASE_URL = "https://120.76.168.132:8443/";
	/** 测试环境IP地址 */
//    public final static String TEST_URL ="https://www.papaxueche.cn:8443/";
    public final static String TEST_URL ="https://10.0.0.35:8443/";
    /** 后台接口主机IP地址 */
    public final static String URL_IP = TEST_URL;
    /** 后台接口地址 */
    public final static String URL = URL_IP + "DGFDS/";
//------------------------------------------------------------
    /** 生产环境图片拼接地址 */
    public final static String RELEASE_IMG_URL = "http://120.76.184.175/files";
    /** 测试环境图片拼接地址 */
    public final static String TEST_IMG_URL = "";
    /** 图片拼接IP地址 */
    public final static String URL_IMG_IP = RELEASE_IMG_URL;
//-------------------------------------------------------------
	/** 初始化接口地址 */
	public final static String URL_INIT = URL + "token.flow";
	/** 注册接口地址 */
	public final static String URL_REGISTER = URL + "account/register.flow";
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
    /** 提交报名用户信息 */
    public final static String URL_SIGN_PERSON_MAS = URL + "registration/save.flow";
	/** 设置支付密码接口地址 */
	public final static String URL_SET_PAY_PASSWORD = URL + "account/setPayPassword.flow";
	/** 检查用户是否设置支付密码接口地址 */
	public final static String URL_CHECK_PAY_PASSWORD = URL + "account/checkPayPassword.flow";
	/** 获取用户余额接口地址 */
	public final static String URL_GET_ACCOUNT_BALANCE = URL + "account/getAccountBalance.flow";
	/** 操作订单接口地址 */
	public final static String URL_OPERATE_ORDER = URL + "order/operateOrder.flow";
	/** 查询订单支付状态 */
	public final static String URL_GET_ORDER_PAY_STATUS = URL + "order/getOrderPayStatus.flow";
	/** 获取所有分校接口地址 */
	public final static String URL_QUERY_BRANCH_SCHOOL_LIST = URL + "branchSchool/queryBranchSchoolList.flow";
	/** 查询账单接口地址 */
	public final static String URL_QUERY_BILL_LOG_LIST = URL + "billLog/queryBillLogList.flow";
	/** 获取学员优惠券列表 */
	public final static String URL_QUERY_STUDENT_USABLE_COUPON_LIST = URL + "coupon/queryStudentUsableCouponList.flow";
	/** 获取所有城市 */
	public final static String URL_GET_ALL_CITY = URL + "util/getAllCity.flow";
	/** 获取指定城市下的分校 */
	public final static String URL_GET_CITY_BRANCH_SCHOOL_LIST = URL + "util/getCityBranchSchoolList.flow";
	/** 日志 */
	public final static String LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/dgjp";
	/** 微信支付的APPID */
	public final static String APPID = "wx129628f8a9b9b0d0";

    /** 分校列表 */
    public final static String URL_GET_SCHOOL_LIST = URL + "branchSchool/queryBranchSchoolPage.flow";
    /** 学校详情 */
    public final static String URL_GET_SCHOOL_DETAILS = URL + "branchSchool/getTBranchSchoolDetails.flow";
    /**分校评论分页*/
    public final static String URL_GET_SCHOOL_COMMENT = URL + "branchSchool/queryEvaluationPage.flow";
    /**首页*/
    public final static String URL_GET_HOME_DATA = URL + "branchSchool/getBanner.flow";
    /**首页图片*/
    public final static String URL_GET_HOME_IMG = URL + "banner/getBannerList.flow";
    /**上传身份证照片*/
    public final static String URL_UP_LOAD_IDCARD = URL + "upload/uploadIdcard.flow";
    /**获取用户信息*/
    public final static String URL_GET_STUDENT_INFO = URL + "registration/getRegInfo.flow";
    /**更新报名信息*/
    public final static String URL_UPDATA_STUDENT_INFO = URL + "registration/updateRegInfo.flow";
    /**获取环信聊天的账号密码*/
    public final static String URL_GET_EASE_USER = URL + "account/robotLogin.flow";
    /**获取分享参数*/
    public final static String URL_GET_SHARE_DATA = URL + "util/getShare.flow";
    /**获取报名付款明细*/
    public final static String URL_GET_SIGN_PAY_DETAIL = URL + "cost/getCostList.flow";
    /**获取底部教练列表*/
    public final static String URL_GET_COACH_LIST = URL + "branchSchool/queryTrainer.flow";
    /**获取城市及其对应驾校*/
    public final static String URL_GET_CITY_AND_SCHOOL = URL + "branchSchool/queryCountySchoolList.flow";
    /**获取教练详细信息*/
    public final static String URL_GET_COACH_DETAIL = URL + "trainer/getTrainerTeachingSchedule.flow";
    /**获取学员上车地址*/
    public final static String URL_GET_STUDENT_ADDRESS = URL + "student/queryAddressList.flow";
    /**添加学员上车地址*/
    public final static String URL_ADD_STUDENT_ADDRESS = URL + "student/updateAddressInfo.flow";
    /**删除学员上车地址*/
    public final static String URL_DELETE_STUDENT_ADDRESS = URL + "student/delAddress.flow";
    /**修改默认选中上车地址*/
    public final static String URL_EDITOR_DEFAULT_STUDENT_ADDRESS = URL + "student/updateMoRenPlace.flow";
    /**修改默认上车地址*/
    public final static String URL_EDITOR_DEFAULT_PLACE_STUDENT_ADDRESS = URL + "student/updateMoRenAddress.flow";

	private Constants() {
		// 常量配置类，不需要实例
	}
	
}