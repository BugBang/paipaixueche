package com.session.dgjp.request;

import com.session.common.BaseListRequestData;

public class TrainerListRequestData extends BaseListRequestData {
    private static final long serialVersionUID = 1L;
    private long branchSchoolId;
    private String name;

    public TrainerListRequestData() {
        super();
        this.pageSize = 0;
    }

    public long getBranchSchoolId() {
        return branchSchoolId;
    }

    public void setBranchSchoolId(long branchSchoolId) {
        this.branchSchoolId = branchSchoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "trainer/queryTrainerList";
    }

}
