package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-12-22.
 */
public class GetEaseAccountRequestData extends BaseRequestData {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "account/robotLogin";
    }
}
