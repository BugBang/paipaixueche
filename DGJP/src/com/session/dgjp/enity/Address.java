package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2017-02-08.
 */
public class Address {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private String phone;
        private int defAddress;
        private int placeState;
        private String carAddress;
        private String place;
        private String stuAccount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


        public int getPlaceState() {
            return placeState;
        }

        public void setPlaceState(int placeState) {
            this.placeState = placeState;
        }

        public String getCarAddress() {
            return carAddress;
        }

        public void setCarAddress(String carAddress) {
            this.carAddress = carAddress;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public int getDefAddress() {
            return defAddress;
        }

        public void setDefAddress(int defAddress) {
            this.defAddress = defAddress;
        }

        public String getStuAccount() {
            return stuAccount;
        }

        public void setStuAccount(String stuAccount) {
            this.stuAccount = stuAccount;
        }
    }
}
