package com.session.dgjp.request;


import com.session.common.BaseListRequestData;

public class OrderListRequestData extends BaseListRequestData {
    private static final long serialVersionUID = 5570549071615305666L;

    /**
     * @see {@link OrderListRequestData#isFinish} 已完成}
     */
    public final static String FINISHED = "Y";
    /**
     * @see {@link OrderListRequestData#isFinish} 未完成}
     */
    public final static String UNFINISH = "N";
    /**
     * @see {@link OrderListRequestData#isFinish} 所有清单}
     */
    public final static String ALL = "A";

    private String course;
    private String isFinish;
    private String beginTime;
    private String endTime;

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "order/queryOrderList";
    }

}
