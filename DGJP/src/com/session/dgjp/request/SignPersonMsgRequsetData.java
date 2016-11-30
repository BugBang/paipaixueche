package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-11-17.
 */
public class SignPersonMsgRequsetData extends BaseRequestData {
    private static final long serialVersionUID = -7214524017639946102L;

    private String name;
    private String population;
    private String idcard;
    private String qq;
    private String email;
    private String phone;
    private String address;
    private String frontImg;
    private String oppositeImg;
    private String city;
    private String category;
    private Long branchSchoolId;
    private String classType;
    private String payWay;
    private Double payMoney;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFrontImg() {
        return frontImg;
    }

    public void setFrontImg(String frontImg) {
        this.frontImg = frontImg;
    }

    public String getOppositeImg() {
        return oppositeImg;
    }

    public void setOppositeImg(String oppositeImg) {
        this.oppositeImg = oppositeImg;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getBranchSchoolId() {
        return branchSchoolId;
    }

    public void setBranchSchoolId(Long branchSchoolId) {
        this.branchSchoolId = branchSchoolId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "registration/save";
    }
}
