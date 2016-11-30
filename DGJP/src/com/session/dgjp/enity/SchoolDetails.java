package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-11-16.
 */
public class SchoolDetails {



    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private List<SchoolDetail> list;

        public List<SchoolDetail> getList() {
            return list;
        }

        public void setList(List<SchoolDetail> list) {
            this.list = list;
        }

        public static class SchoolDetail {
            private String photoUrl;
            private String address;
            private String timePeroid;
            private String phone;

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getTimePeroid() {
                return timePeroid;
            }

            public void setTimePeroid(String timePeroid) {
                this.timePeroid = timePeroid;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            @Override
            public String toString() {
                return "SchoolDetail{" +
                        "photoUrl='" + photoUrl + '\'' +
                        ", address='" + address + '\'' +
                        ", timePeroid='" + timePeroid + '\'' +
                        ", phone='" + phone + '\'' +
                        '}';
            }
        }
    }
}
