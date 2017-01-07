package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * 操作订单
 */
public class OperateOrderRequestData extends BaseRequestData {

    private static final long serialVersionUID = 6181424011294766036L;

    /**
     * {@link OperateOrderRequestData#operateType} 取消订单
     */
    public final static String CANCEL_ORDER = "A";
    /**
     * {@link OperateOrderRequestData#operateType} 开始签到
     */
    public final static String START_SIGN = "O";
    /**
     * {@link OperateOrderRequestData#operateType} 结束签到
     */
    public final static String FINISH_SIGN = "F";
    /**
     * {@link OperateOrderRequestData#operateType} 确认
     */
    public final static String CONFIRM = "B";
    /**
     * {@link OperateOrderRequestData#operateType} 支付
     */
    public final static String PAYMENT = "P";

    /**
     * 订单id
     */
    private String id;
    /**
     * 操作类型，A表示取消订单，O表示开始签到，F表示结束签到，B表示确认 ，P表示支付
     */
    private String operateType;
    /**
     * 支付的费用
     */
    private double fee;
    /**
     * 支付方式
     */
    private String payType;
    private long studentCouponId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "order/operateOrder";
    }

    public long getStudentCouponId() {
        return studentCouponId;
    }

    public void setStudentCouponId(long studentCouponId) {
        this.studentCouponId = studentCouponId;
    }

}
