package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-12-29.
 */
public class SignPayDetailRequestData extends BaseRequestData {
    private int driverType;

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "cost/getCostList";
    }
}
