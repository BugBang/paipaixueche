package com.session.dgjp.enity;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	/** {@link Order#status} 待培训 */
	public final static String STATUS_WAITING_TRAIN = "C";
	/** {@link Order#status} 取消预约 */
	public final static String STATUS_CANCEL = "A";
	/** {@link Order#status} 待培中/待确认 */
	public final static String STATUS_TRAINING = "T";
	/** {@link Order#status} 已完成 */
	public final static String STATUS_FINISHED = "Y";
	/**{@link Order#status}}*/
	public final static String STATUS_PAYMENT = "P"; 
	
	public final static String EVAL_STATUS = "eval_status";
	
	/** {@link Order#isEval} 已评价} */
	public final static String EVALED = "Y";
	/** {@link Order#isEval} 未评价} */
	public final static String UNEVAL = "N";
	

	public final static String ID = "id";

	private String id;// 订单号，由后台生成，YYYYMMDDHHmmSS+5位随机数
	private Trainer trainer;// 教练
	private Student student;// 学员
	private Course course;// 课程
	private Car car;
	private String status;// 培训状态
	private String statusName;// 状态名
	private String nextOperate;//下一个操作码
	private String nextOperateName;//下一个操作的名称
	private Date beginTime;// 预约开始
	private Date endTime;// 预约结束
	private int orderDuration;// 预约时长，单位分钟
	private Date checkinBeginTime;// 签到开始时间
	private Date checkinEndTime;// 签到结束时间
	private int checkinDuration;// 签到时长，单位分钟
	private int fee = 0;// 本次培训实际支付费用，单位分
	private int originalFee = 0;//原价，单位分
	private int preferentialFee = 0;//优惠价或活动价，单位分
	private Date submitTime;// 提交时间
	private Date updateTime;// 更新时间
	private String remark;// 备注
	private String isEval;// 是否已评价
	private Yard yard;// 训练场地
	private int remainTime;//剩余支付时间，单位：秒

	/** {@link Order#operation} 取消订单 */
	public final static String CANCEL_ORDER = "A";
	/** {@link Order#operation} 开始签到 */
	public final static String START_SIGN = "O";
	/** {@link Order#operation} 结束签到 */
	public final static String FINISH_SIGN = "F";
	/** {@link Order#operation} 确认 */
	public final static String CONFIRM = "B";
	/** {@link Order#operation} 支付 */
	public final static String PAY = "P";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getOrderDuration() {
		return orderDuration;
	}

	public void setOrderDuration(int orderDuration) {
		this.orderDuration = orderDuration;
	}

	public Date getCheckinBeginTime() {
		return checkinBeginTime;
	}

	public void setCheckinBeginTime(Date checkinBeginTime) {
		this.checkinBeginTime = checkinBeginTime;
	}

	public Date getCheckinEndTime() {
		return checkinEndTime;
	}

	public void setCheckinEndTime(Date checkinEndTime) {
		this.checkinEndTime = checkinEndTime;
	}

	public int getCheckinDuration() {
		return checkinDuration;
	}

	public void setCheckinDuration(int checkinDuration) {
		this.checkinDuration = checkinDuration;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Car getCar()
	{
		return car;
	}

	public void setCar(Car car)
	{
		this.car = car;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getIsEval() {
		return isEval;
	}

	public void setIsEval(String isEval) {
		this.isEval = isEval;
	}

	public Yard getYard() {
		return yard;
	}

	public void setYard(Yard yard) {
		this.yard = yard;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}


	public int getOriginalFee()
	{
		return originalFee;
	}

	public void setOriginalFee(int originalFee)
	{
		this.originalFee = originalFee;
	}

	public int getPreferentialFee()
	{
		return preferentialFee;
	}

	public void setPreferentialFee(int preferentialFee)
	{
		this.preferentialFee = preferentialFee;
	}

	public int getRemainTime()
	{
		return remainTime;
	}

	public void setRemainTime(int remainTime)
	{
		this.remainTime = remainTime;
	}

	public String getNextOperate()
	{
		return nextOperate;
	}

	public void setNextOperate(String nextOperate)
	{
		this.nextOperate = nextOperate;
	}

	public String getNextOperateName()
	{
		return nextOperateName;
	}

	public void setNextOperateName(String nextOperateName)
	{
		this.nextOperateName = nextOperateName;
	}
	
}
