package com.session.common;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.session.common.utils.AppUtil;
import com.session.common.utils.CryptoUtil;
import com.session.common.utils.DateUtil;
import com.session.common.utils.HttpUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.MD5Util;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.request.LoginRequestData;
import com.session.dgjp.response.InitResponseData;

/**
 * 后台接口请求父类
 *
 * @author YJ Liang
 *         2016  下午3:47:42
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient int requestTimes = 2;//默认请求两次

    private transient Gson gson;

    private String sessionId;//获取token接口返回的sessionId

    private String authString;//md5校验码，MD5(data+timeStamp+key);

    private String timeStamp = BaseRequestTask.getTimeStamp();

    private String data;//业务参数加密后的字符串

    private String channel;//渠道标识
    private transient static String mChannel;

    private String appVersion;//app版本号
    private transient static String mAppVersion;
    private String protocolVersion = Constants.VERSION_PROTOCOL;//通讯协议版本号

    protected transient BaseRequestData requestData;

    public BaseRequest() {
        AppInstance app = AppInstance.getInstance();
        if (mAppVersion == null) {
            mAppVersion = AppUtil.getVersionName(app);
        }
        appVersion = mAppVersion;
        sessionId = BaseRequestTask.mSessionId;
        if (TextUtil.isEmpty(mChannel)) {
            // mChannel = AppUtil.getChannel(app);
            mChannel = "ANDROID";
        }
        channel = mChannel;
    }

    public BaseRequest(BaseRequestData requestData) {
        this();
        this.requestData = requestData;
    }

    /**
     * 子类需要根据各自的情况重写该方法
     *
     * @author YJ Liang
     * 2015-12-31 下午4:46:56
     */
    protected void createGson() {
        gson = new GsonBuilder().setDateFormat(DateUtil.NETWORK_TIME_FORMAT).create();
    }

    public void setRequestData(BaseRequestData requestData) {
        this.requestData = requestData;
    }

    /**
     * 如果需要对结果进行自定义组装，可以重写该方法
     *
     * @param context
     * @return
     */
    public <T extends BaseResponseData> BaseResponse<T> sendRequest(Class<T> clazz) throws Exception {
        if (gson == null) {
            createGson();
        }
        if (TextUtil.isEmpty(sessionId)) {
            //sessionId为空，也要重新获取sessionId
            init();
        }
        getData();
        getAuthString();
        String result = HttpUtil.post(requestData.getUrl(), BaseRequestTask.getParams(this));
        BaseResponse<T> baseResponse = gson.fromJson(result, new TypeToken<BaseResponse<T>>() {
        }.getType());
        if (BaseResponse.CODE_SESSION_FAIL == baseResponse.getCode()) {
            requestTimes--;
            if (requestTimes > 0) {
                //session失效，重新获取token和sessionId
                init();
                return sendRequest(clazz);
            }
        }
        String data = baseResponse.getData();
        if (!TextUtil.isEmpty(data)) {
            data = CryptoUtil.decrypt(CryptoUtil.getToken(), data);
            try {
                //如果重写initData方法，就通过initData将json映射到responseData的成员变量中
                Method method = clazz.getDeclaredMethod("parseData", String.class);
                if (method != null) {
                    T responseData = clazz.newInstance();
                    responseData.parseData(data);
                    baseResponse.setResponseData(responseData);
                } else {
                    baseResponse.setResponseData(gson.fromJson(data, clazz));
                }
            } catch (NoSuchMethodException e) {
                baseResponse.setResponseData(gson.fromJson(data, clazz));
            }
        }
        return baseResponse;
    }

    public String getAuthString() {
        authString = MD5Util.encode(data + timeStamp + BaseRequestTask.KEY);
        return authString;
    }

    public String getUrl() {
        return requestData != null ? requestData.getUrl() : "";
    }

    public String getData() {
        if (requestData == null) {
            data = "";
        } else {
            if (gson == null) {
                createGson();
            }
            String json = gson.toJson(requestData);
            data = CryptoUtil.encrypt(CryptoUtil.getToken(), json);
        }
        return data;
    }

    /**
     * 重新初始化token和sessionId
     *
     * @author YJ Liang
     * 2016  上午11:32:47
     */
    private void init() {
        List<NameValuePair> sessionParams = new ArrayList<NameValuePair>();
        String randomStr = InitRequestTask.getRandomString();
        sessionParams.add(new BasicNameValuePair(InitRequestTask.KEY_RAND, randomStr));
        sessionParams.add(new BasicNameValuePair(BaseRequestTask.KEY_TIME_STAMP, timeStamp));
        sessionParams.add(new BasicNameValuePair(BaseRequestTask.KEY_AUTH_STRING, MD5Util.encode(timeStamp + BaseRequestTask.KEY)));
        sessionParams.add(new BasicNameValuePair(BaseRequestTask.KEY_CHANNEL, channel));
        sessionParams.add(new BasicNameValuePair(BaseRequestTask.KEY_VERSION_APP, appVersion));
        sessionParams.add(new BasicNameValuePair(BaseRequestTask.KEY_VERSION_PRT, protocolVersion));
        String initResult = HttpUtil.post(Constants.URL_INIT, sessionParams);
        BaseResponse<InitResponseData> initResponse = gson.fromJson(initResult, new TypeToken<BaseResponse<InitResponseData>>() {
        }.getType());
        if (BaseResponse.CODE_SUCCESS == initResponse.getCode()) {
            InitResponseData initResponseData = gson.fromJson(CryptoUtil.decrypt(randomStr, initResponse.getData()), InitResponseData.class);
            synchronized (InitResponseData.class) {
                sessionId = initResponseData.getSessionId();
                BaseRequestTask.mSessionId = sessionId;
                CryptoUtil.setToken(initResponseData.getToken());
            }
            //登陆
            LoginRequestData loginRequestData = new LoginRequestData();
            loginRequestData.setAccount(SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT, null));
            loginRequestData.setPassword(SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_PASSWORD, null));
            String loginData = CryptoUtil.encrypt(CryptoUtil.getToken(), gson.toJson(loginRequestData));
            List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
            loginParams = new ArrayList<NameValuePair>();
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_CHANNEL, channel));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_VERSION_APP, appVersion));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_VERSION_PRT, protocolVersion));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_TIME_STAMP, timeStamp));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_DATA, loginData));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_SESSIONID, sessionId));
            loginParams.add(new BasicNameValuePair(BaseRequestTask.KEY_AUTH_STRING, MD5Util.encode(loginData + timeStamp + BaseRequestTask.KEY)));
            HttpUtil.post(Constants.URL_LOGIN, loginParams);
        }
    }

}
