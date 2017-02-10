package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-09.
 */
public class EditorDefaultAddressRequestData extends BaseRequestData {
    private String stuAccount;
    private int id;

    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "student/updateMoRenPlace";
    }
}
