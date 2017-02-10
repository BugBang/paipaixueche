package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-08.
 */
public class AddStudentAddressRequestData extends BaseRequestData {
    private String place;//地点
    private String carAddress;//上车地址
    private String phone;// 电话
    private String stuAccount;//学员帐号
    private int id;//序号

    public String getPlace() {
        return place;
    }

    public String getCarAddress() {
        return carAddress;
    }

    public void setCarAddress(String carAddress) {
        this.carAddress = carAddress;
    }

    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "student/updateAddressInfo";
    }
}
