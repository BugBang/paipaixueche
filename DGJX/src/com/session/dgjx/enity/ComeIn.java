package com.session.dgjx.enity;

import java.util.List;

/**
 * Created by user on 2017-02-20.
 */
public class ComeIn {
    private String endFlag;
    private List<ListBean> list;

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String createtime;
        private String course;
        private String phone;
        private String price;
        private String accountType;
        private String account;
        private String stname;
        private String photoUrl;
        private String trname;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getStname() {
            return stname;
        }

        public void setStname(String stname) {
            this.stname = stname;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getTrname() {
            return trname;
        }

        public void setTrname(String trname) {
            this.trname = trname;
        }
    }
}
