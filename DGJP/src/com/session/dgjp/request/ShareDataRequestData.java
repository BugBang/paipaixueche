package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-12-26.
 */
public class ShareDataRequestData extends BaseRequestData {
    @Override
    protected String getSpecificUrlPath() {
        return "util/getShare";
    }
}
