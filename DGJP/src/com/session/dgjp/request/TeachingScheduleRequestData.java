package com.session.dgjp.request;

import com.session.common.BaseRequestData;

public class TeachingScheduleRequestData extends BaseRequestData {
    private static final long serialVersionUID = 1L;

    private String trainerAccount;

    @Override
    protected String getSpecificUrlPath() {
        return "trainer/getTrainerTeachingSchedule";
    }

    public String getTrainerAccount() {
        return trainerAccount;
    }

    public void setTrainerAccount(String trainerAccount) {
        this.trainerAccount = trainerAccount;
    }


}
