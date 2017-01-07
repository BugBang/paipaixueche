package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-12-07.
 */
public class SignStudent {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String qq;
        private String address;
        private String city;
        private String payWay;
        private int branchSchoolId;
        private String population;
        private int classId;
        private int payMoney;
        private String oppositeImg;
        private String phone;
        private String frontImg;
        private String idcard;
        private String name;
        private int id;
        private String category;
        private String email;
        private String cityname;
        private String dsname;
        private String classname;

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getDsname() {
            return dsname;
        }

        public void setDsname(String dsname) {
            this.dsname = dsname;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPayWay() {
            return payWay;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }

        public int getBranchSchoolId() {
            return branchSchoolId;
        }

        public void setBranchSchoolId(int branchSchoolId) {
            this.branchSchoolId = branchSchoolId;
        }

        public String getPopulation() {
            return population;
        }

        public void setPopulation(String population) {
            this.population = population;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public int getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(int payMoney) {
            this.payMoney = payMoney;
        }

        public String getOppositeImg() {
            return oppositeImg;
        }

        public void setOppositeImg(String oppositeImg) {
            this.oppositeImg = oppositeImg;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFrontImg() {
            return frontImg;
        }

        public void setFrontImg(String frontImg) {
            this.frontImg = frontImg;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
