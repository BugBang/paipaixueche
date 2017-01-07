package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-11-22.
 */
public class SignSchoolList {

    private String endFlag;
    private List<SignSchoolListModel> list;

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public List<SignSchoolListModel> getList() {
        return list;
    }

    public void setList(List<SignSchoolListModel> list) {
        this.list = list;
    }

    public static class SignSchoolListModel {
        private String address;
        private int countNum;
        private String smallPhotoUrl;
        private double score;
        private String name;
        private double interval;
        private int id;
        private double expenses;
        private String distance;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCountNum() {
            return countNum;
        }

        public void setCountNum(int countNum) {
            this.countNum = countNum;
        }

        public String getSmallPhotoUrl() {
            return smallPhotoUrl;
        }

        public void setSmallPhotoUrl(String smallPhotoUrl) {
            this.smallPhotoUrl = smallPhotoUrl;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getInterval() {
            return interval;
        }

        public void setInterval(double interval) {
            this.interval = interval;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getExpenses() {
            return expenses;
        }

        public void setExpenses(double expenses) {
            this.expenses = expenses;
        }

        @Override
        public String toString() {
            return "SignSchoolListModel{" +
                    "address='" + address + '\'' +
                    ", countNum=" + countNum +
                    ", smallPhotoUrl='" + smallPhotoUrl + '\'' +
                    ", score=" + score +
                    ", name='" + name + '\'' +
                    ", interval=" + interval +
                    ", id=" + id +
                    ", expenses=" + expenses +
                    ", distance='" + distance + '\'' +
                    '}';
        }
    }
}
