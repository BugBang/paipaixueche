package com.session.dgjx.enity;

import java.util.List;

/**
 * Created by user on 2017-02-15.
 */
public class HomePager {

    private List<ListBean> list;
    private List<OlistBean> olist;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<OlistBean> getOlist() {
        return olist;
    }

    public void setOlist(List<OlistBean> olist) {
        this.olist = olist;
    }

    public static class ListBean {
        private String phone;
        private String status;
        private String beginTime;
        private String orderDuration;
        private String endTime;
        private String course;
        private int fee;
        private String id;
        private String address;
        private String studentName;
        private String photoUrl;
        private int totalMoney;
        private int placeState;
        private String studentAccount;

        public int getPlaceState() {
            return placeState;
        }

        public void setPlaceState(int placeState) {
            this.placeState = placeState;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getOrderDuration() {
            return orderDuration;
        }

        public void setOrderDuration(String orderDuration) {
            this.orderDuration = orderDuration;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public int getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(int totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getStudentAccount() {
            return studentAccount;
        }

        public void setStudentAccount(String studentAccount) {
            this.studentAccount = studentAccount;
        }
    }

    public static class OlistBean {
        private String phone;
        private String trainerAccount;
        private int fee;
        private String id;
        private int totalMoney;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTrainerAccount() {
            return trainerAccount;
        }

        public void setTrainerAccount(String trainerAccount) {
            this.trainerAccount = trainerAccount;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(int totalMoney) {
            this.totalMoney = totalMoney;
        }
    }
}
