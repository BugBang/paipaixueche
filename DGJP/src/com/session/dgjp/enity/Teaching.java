package com.session.dgjp.enity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 教学时段
 *
 * @author YJ Liang
 *         2016  上午11:44:00
 */
public class Teaching implements Comparable<Teaching>, Serializable, Parcelable {

    private static final long serialVersionUID = 1L;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm");
    private Date teachingTime;
    private List<Integer> timeSlot;

    public Teaching() {
        super();
    }

    public Teaching(Date teachingTime, List<Integer> timeSlot) {
        super();
        this.teachingTime = teachingTime;
        this.timeSlot = timeSlot;
    }

    public Date getTeachingTime() {
        return teachingTime;
    }

    public void setTeachingTime(Date teachingTime) {
        this.teachingTime = teachingTime;
    }

    public List<Integer> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<Integer> timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teachingTime == null) ? 0 : teachingTime.hashCode());
        result = prime * result + ((timeSlot == null) ? 0 : timeSlot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teaching other = (Teaching) obj;
        if (teachingTime == null) {
            if (other.teachingTime != null)
                return false;
        } else if (!teachingTime.equals(other.teachingTime))
            return false;
        if (timeSlot == null) {
            if (other.timeSlot != null)
                return false;
        } else if (!timeSlot.equals(other.timeSlot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Period [teachingTime=" + teachingTime + ", timeSlot=" + timeSlot + "]";
    }

    @Override
    public int compareTo(Teaching another) {
        /*if(teachingTime.equals(another.getTeachingTime()))
		{
			
		}else {
		}*/
        return teachingTime.compareTo(another.getTeachingTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(teachingTime);
        dest.writeList(timeSlot);
    }

    public static final Parcelable.Creator<Teaching> CREATOR = new Parcelable.Creator<Teaching>() {
        public Teaching createFromParcel(Parcel in) {
            Teaching teaching = new Teaching();
            teaching.setTeachingTime((Date) in.readSerializable());
            teaching.setTimeSlot((List<Integer>) in.readArrayList(Integer.class.getClassLoader()));
            return teaching;
        }

        public Teaching[] newArray(int size) {
            return new Teaching[size];
        }
    };


}
