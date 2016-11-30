package com.session.dgjp.request;

import com.session.common.BaseListRequestData;

public class CarScheduleRequestData extends BaseListRequestData
{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getSpecificUrlPath()
	{
		return "trainer/getCarSchedule";
	}
	
	private String trainerAccount;
	private long branchSchoolId;
	private String teachingTime;
	private String timeSlot;
	public String getTrainerAccount()
	{
		return trainerAccount;
	}
	public void setTrainerAccount(String trainerAccount)
	{
		this.trainerAccount = trainerAccount;
	}
	public long getBranchSchoolId()
	{
		return branchSchoolId;
	}
	public void setBranchSchoolId(long branchSchoolId)
	{
		this.branchSchoolId = branchSchoolId;
	}
	public String getTeachingTime()
	{
		return teachingTime;
	}
	public void setTeachingTime(String teachingTime)
	{
		this.teachingTime = teachingTime;
	}
	public String getTimeSlot()
	{
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot)
	{
		this.timeSlot = timeSlot;
	}
	
}
