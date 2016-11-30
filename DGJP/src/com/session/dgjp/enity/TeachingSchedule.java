package com.session.dgjp.enity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TeachingSchedule implements Parcelable
{
	private String course;
	private List<Teaching> teachingList;

	public TeachingSchedule()
	{
		super();
	}

	public TeachingSchedule(String course, List<Teaching> teachingList)
	{
		super();
		this.course = course;
		this.teachingList = teachingList;
	}

	public String getCourse()
	{
		return course;
	}

	public void setCourse(String course)
	{
		this.course = course;
	}

	public List<Teaching> getTeachingList()
	{
		return teachingList;
	}

	public void setTeaching(List<Teaching> teachingList)
	{
		this.teachingList = teachingList;
	}

	public static final Parcelable.Creator<TeachingSchedule> CREATOR = new Parcelable.Creator<TeachingSchedule>()
	{
		public TeachingSchedule createFromParcel(Parcel in)
		{
			TeachingSchedule teachingSchedule = new TeachingSchedule();
			teachingSchedule.setCourse(in.readString());
			List<Teaching> teachings = new ArrayList<Teaching>();
			in.readTypedList(teachings,Teaching.CREATOR);
			teachingSchedule.setTeaching(teachings);
			return teachingSchedule;
		}

		public TeachingSchedule[] newArray(int size)
		{
			return new TeachingSchedule[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(course);
		dest.writeTypedList(teachingList);
	}

}
