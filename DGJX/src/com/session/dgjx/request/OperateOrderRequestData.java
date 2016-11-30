package com.session.dgjx.request;

import com.session.common.BaseRequestData;

/** 操作订单 */
public class OperateOrderRequestData extends BaseRequestData {

	private static final long serialVersionUID = 6181424011294766036L;

	/** {@link OperateOrderRequestData#operateType} 取消订单 */
	public final static String CANCEL_ORDER = "A";
	/** {@link OperateOrderRequestData#operateType} 开始签到 */
	public final static String START_SIGN = "O";
	/** {@link OperateOrderRequestData#operateType} 结束签到 */
	public final static String FINISH_SIGN = "F";
	/** {@link OperateOrderRequestData#operateType} 确认 */
	public final static String CONFIRM = "B";

	/** 订单id */
	private String id;
	/** 操作类型，A表示取消订单，O表示开始签到，F表示结束签到，B表示确认 */
	private String operateType;

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

	@Override
	protected String getSpecificUrlPath() {
		return "";
	}

}
