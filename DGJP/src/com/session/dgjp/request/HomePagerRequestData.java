package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-11-29.
 */
public class HomePagerRequestData extends BaseRequestData {
//    @Override
//    protected String getSpecificUrlPath() {
//        return "branchSchool/getBanner";
//    }
    @Override
    protected String getSpecificUrlPath() {
        return "banner/getBannerList";
    }
}
