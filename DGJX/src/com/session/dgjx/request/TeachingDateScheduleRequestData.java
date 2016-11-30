package com.session.dgjx.request;

import com.session.common.BaseRequestData;

public class TeachingDateScheduleRequestData extends BaseRequestData
{
	private static final long serialVersionUID = 1L;
	
	private String course;
	@Override
	protected String getSpecificUrlPath()
	{
		return "trainer/getTrainerTeachingScheduleCourse";
	}

	public String getCourse()
	{
		return course;
	}
	public void setCourse(String course)
	{
		this.course = course;
	}

}
