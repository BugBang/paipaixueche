package com.session.common;

import java.io.Serializable;

public class BaseResponse<T extends BaseResponseData> implements Serializable {
    private static final long serialVersionUID = 8904947380850176337L;

    public final static int CODE_SUCCESS = 0;
    /**
     * session失效 4
     */
    public final static int CODE_SESSION_FAIL = 4;
    /**
     * 登陆失败
     */
    public final static int CODE_LOGIN_FAIL = 7;
    protected int code;
    protected String msg;
    protected String data;
    protected T responseData;

    public BaseResponse() {
        super();
    }

    public BaseResponse(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

    /**
     * 判断返回结果是否需要重新登陆
     *
     * @return
     * @author YJ Liang
     * 2016  下午3:54:44
     */
    public boolean toLogin() {
        return CODE_LOGIN_FAIL == code || CODE_SESSION_FAIL == code;
    }

}
