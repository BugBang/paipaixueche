package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-12-01.
 */
public class HomePager {



    private List<RotationListBean> rotationList;
    private List<AdvertisementListBean> advertisementList;

    public List<RotationListBean> getRotationList() {
        return rotationList;
    }

    public void setRotationList(List<RotationListBean> rotationList) {
        this.rotationList = rotationList;
    }

    public List<AdvertisementListBean> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<AdvertisementListBean> advertisementList) {
        this.advertisementList = advertisementList;
    }

    public static class RotationListBean {
        private String photoUrl;
        private String name;
        private String fileUrl;
        private int id;
        private String type;
        private String status;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class AdvertisementListBean {
        private String photoUrl;
        private String name;
        private String fileUrl;
        private int id;
        private String type;
        private String status;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
