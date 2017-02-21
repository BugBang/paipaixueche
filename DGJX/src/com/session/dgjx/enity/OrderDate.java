package com.session.dgjx.enity;

import com.session.common.utils.DateUtil;

import java.util.Date;

public class OrderDate {
    private Date date;

    public OrderDate(Date date) {
        super();
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date != null ? DateUtil.LOCAL_SIMPLE_SDF.format(date) : "全部培训计划";
    }

}