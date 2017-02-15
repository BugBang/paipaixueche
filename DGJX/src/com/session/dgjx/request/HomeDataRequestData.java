package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-14.
 */
public class HomeDataRequestData extends BaseRequestData {

    private String trainerAccount;
    private String beginTime;

    public String getTrainerAccount() {
        return trainerAccount;
    }

    public void setTrainerAccount(String trainerAccount) {
        this.trainerAccount = trainerAccount;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "trainer/queryCoachOrderPageInfo";
    }
}
