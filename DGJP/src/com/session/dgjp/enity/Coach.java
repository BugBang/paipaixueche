package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2017-01-11.
 */
public class Coach {
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
        private String trainerIdcard;
        private String sex;
        private double score;
        private String branchSchoolName;
        private int fee;
        private int id;
        private String headImg;
        private int age;
        private String accountType;
        private String account;
        private String trainerPhone;
        private String trainerName;
        private int lat;

        public String getTrainerIdcard() {
            return trainerIdcard;
        }

        public void setTrainerIdcard(String trainerIdcard) {
            this.trainerIdcard = trainerIdcard;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getBranchSchoolName() {
            return branchSchoolName;
        }

        public void setBranchSchoolName(String branchSchoolName) {
            this.branchSchoolName = branchSchoolName;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
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

        public String getTrainerPhone() {
            return trainerPhone;
        }

        public void setTrainerPhone(String trainerPhone) {
            this.trainerPhone = trainerPhone;
        }

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public int getLat() {
            return lat;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }
    }
}
