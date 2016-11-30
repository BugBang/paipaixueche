package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-11-25.
 */
public class SignSchoolCommentRequestData extends BaseRequestData {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "branchSchool/queryEvaluationPage";
    }
}
