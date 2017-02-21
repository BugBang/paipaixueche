package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-20.
 */
public class CoachComeInRequestData extends BaseRequestData {
    @Override
    protected String getSpecificUrlPath() {
        return "trainer/getTrainerSrListPage";
    }
}
