package com.session.dgjp.request;

import com.session.common.BaseListRequestData;

/**
 * 获取分校列表的接口
 *
 * @author YJ Liang
 *         2016  上午10:43:41
 */
public class BranchSchoolListRequestData extends BaseListRequestData {

    private static final long serialVersionUID = 1L;

    private Double longitude;//用户的经度
    private Double latitude;//用户的纬度

    public BranchSchoolListRequestData() {
        super();
        this.pageSize = 0;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "branchSchool/queryBranchSchoolList";
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
