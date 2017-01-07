package com.session.dgjp.request;

import com.session.common.BaseRequestData;

/**
 * Created by user on 2016-12-07.
 */
public class UpImgRequestData extends BaseRequestData {

    private String file;
    private String fileName;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    protected String getSpecificUrlPath() {
        return "upload/uploadIdcard";
    }
}
