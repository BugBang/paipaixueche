package com.session.dgjx;

import android.os.Environment;

import com.session.dgjx.personal.PersonalCenterActivity;

/** 常量配置 */
public final class Constants {

	/** 通信协议版本 */
	public final static String VERSION_PROTOCOL = "1.0";
	/** 图片拼接地址 */
	public final static String IMG_URL = "http://www.paipaixueche.com/files";
    /** 生产环境IP地址 */
	public final static String RELEASE_URL = "https://120.76.168.132:8443/";
	/** 测试环境IP地址 */
//	public final static String TEST_URL = "https://183.62.251.19:8443/";
	public final static String TEST_URL = "https://10.0.0.35:8443/";
//	public final static String TEST_URL = "https://10.0.0.31:8443/";
	/** 后台接口主机IP地址 */
	public final static String URL_IP = RELEASE_URL;
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
    /** 获取教练个人信息 */
    public final static String URL_GET_ACCOUNT_DATA = URL + "trainer/getTraninerPhotoOrMoney.flow";
    /** 获取教练收入列表信息*/
    public final static String URL_GET_COACH_INCOME_DATA = URL + "trainer/getTrainerSrListPage.flow";
    /** 获取首页信息*/
    public final static String URL_GET_STUDENT_ORDER_LIST_AND_COMEIN_DATA = URL + "trainer/queryCoachOrderPageInfo.flow";
    /** 提交教练评价*/
    public final static String URL_POST_COACH_EVAL = URL + "trainer/updateCoachEval.flow";
	/** 日志 */
	public final static String LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/dgjx";

	private Constants() {
		// 常量配置类，不需要实例
	}

    public static final class ConValue{

        /**
         * Tab选项卡的图标
         */
        public static int mImageViewArray[] = {
                R.drawable.bottom_home_img_bg,
                R.drawable.bottom_order_img_bg,
                R.drawable.bottom_me_img_bg
        };

        /**
         * Tab选项卡的文字
         */
        public static String mTextViewArray[] = {"首页", "预约", "我的"};

        /**
         * 每一个Tab界面
         */
        public static Class<?> mTabClassArray[] = {
                HomePagerActivity.class,
                HomeActivity.class,
                PersonalCenterActivity.class
        };
    }

}
