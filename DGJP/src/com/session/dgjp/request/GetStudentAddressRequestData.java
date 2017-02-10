package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-08.
 */
public class GetStudentAddressRequestData extends BaseRequestData {

    private String stuAccount;

    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "student/queryAddressList";
    }
}
