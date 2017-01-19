package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2017-01-17.
 */
public class CoachDetail {

    private List<TeachingSchedule> list;

    private int endFlag;

    public int getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(int endFlag) {
        this.endFlag = endFlag;
    }
    public List<TeachingSchedule> getList() {
        return list;
    }

    public void setList(List<TeachingSchedule> list) {
        this.list = list;
    }

}
