package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-11-22.
 */
public class SignSchoolListRequsetData extends BaseRequestData {

    private double longitude;
    private double latitude;
    private String allType;//综合
    private String distanceType;//距离-排序
    private String costType;//学费-排序
    private String credit;//信用-排序
    private String enrollNum;//报名人数-排序

    public String getEnrollNum() {
        return enrollNum;
    }

    public void setEnrollNum(String enrollNum) {
        this.enrollNum = enrollNum;
    }

    public String getAllType() {
        return allType;
    }

    public void setAllType(String allType) {
        this.allType = allType;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "branchSchool/queryBranchSchoolPage";
    }
}
