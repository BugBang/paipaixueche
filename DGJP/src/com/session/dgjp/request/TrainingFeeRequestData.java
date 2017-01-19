package com.session.dgjp.request;

import com.session.common.BaseRequestData;

public class TrainingFeeRequestData extends BaseRequestData {
    private static final long serialVersionUID = 1L;

    private String trainerAccount;
    private String studentAccount;
    private int duration = 0;
    private String carno;
    private String teachingTime;
    private String course;
    private String timeSlot;

    public String getTrainerAccount() {
        return trainerAccount;
    }

    public void setTrainerAccount(String trainerAccount) {
        this.trainerAccount = trainerAccount;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getTeachingTime() {
        return teachingTime;
    }

    public void setTeachingTime(String teachingTime) {
        this.teachingTime = teachingTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "order/getTrainingFee";
    }

}
