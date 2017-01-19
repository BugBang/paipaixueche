package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-01-13.
 */
public class CityAndSchoolRequestData extends BaseRequestData {


    private float lng; //经度
    private float lat; //纬度
    private String city;
    private String county;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "branchSchool/queryCountySchoolList";
    }
}
