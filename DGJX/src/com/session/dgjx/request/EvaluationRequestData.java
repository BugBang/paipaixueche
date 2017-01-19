package com.session.dgjx.request;

import com.session.common.BaseRequestData;

public class EvaluationRequestData extends BaseRequestData {
    private static final long serialVersionUID = 1L;

    private String orderId;

    @Override
    protected String getSpecificUrlPath() {
        return "order/getEvaluation";
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
