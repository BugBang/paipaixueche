package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-01-23.
 */
public class GetPersonalDataRequestData extends BaseRequestData {
    @Override
    protected String getSpecificUrlPath() {
        return "trainer/getTraninerPhotoOrMoney";
    }
}
