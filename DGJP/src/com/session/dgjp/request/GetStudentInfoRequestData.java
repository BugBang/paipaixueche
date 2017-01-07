package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-12-07.
 */
public class GetStudentInfoRequestData extends BaseRequestData {
    @Override
    protected String getSpecificUrlPath() {
        return "registration/getRegInfo";
    }
}
