package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-01-11.
 */
public class CoachListRequestData extends BaseRequestData {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "branchSchool/queryTrainer";
    }
}
