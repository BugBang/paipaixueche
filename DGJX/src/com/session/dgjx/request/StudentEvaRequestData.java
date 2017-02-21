package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2017-02-17.
 */
public class StudentEvaRequestData extends BaseRequestData {

    private String id;//   订单号
    private String coachEvaluate;//  教练评价

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoachEvaluate() {
        return coachEvaluate;
    }

    public void setCoachEvaluate(String coachEvaluate) {
        this.coachEvaluate = coachEvaluate;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "trainer/updateCoachEval";
    }
}
