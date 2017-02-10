package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-09.
 */
public class DeleteStudentAddressRequestData extends BaseRequestData {
    private int id;
    private String stuAccount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "student/delAddress";
    }
}
