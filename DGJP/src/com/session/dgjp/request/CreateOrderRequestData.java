package com.session.dgjp.request;

import com.session.common.BaseRequestData;

public class CreateOrderRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getSpecificUrlPath()
	{
		return "order/createOrder";
	}
	private String studentAccount;
	private String trainerAccount;
	private long branchSchoolId;
	private String teachingTime;
	private String timeSlot;
	private String course;
	private String carno;
	private int preferentialFee;
	private int originalFee;

	public void setTrainerAccount(String trainerAccount)
	{
		this.trainerAccount = trainerAccount;
	}
	public void setBranchSchoolId(long branchSchoolId)
	{
		this.branchSchoolId = branchSchoolId;
	}
	public void setTeachingTime(String teachingTime)
	{
		this.teachingTime = teachingTime;
	}
	public void setTimeSlot(String timeSlot)
	{
		this.timeSlot = timeSlot;
	}
	public void setCourse(String course)
	{
		this.course = course;
	}
	public void setCarno(String carno)
	{
		this.carno = carno;
	}
	public void setStudentAccount(String studentAccount)
	{
		this.studentAccount = studentAccount;
	}
	public void setPreferentialFee(int preferentialFee)
	{
		this.preferentialFee = preferentialFee;
	}
	public void setOriginalFee(int originalFee)
	{
		this.originalFee = originalFee;
	}
	
}
