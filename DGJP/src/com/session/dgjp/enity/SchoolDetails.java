package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-11-16.
 */
public class SchoolDetails {

    private List<EListBean> eList;   //评论列表
    private List<ScListBean> scList; //班别列表
    private List<BsListBean> bsList; //驾校信息

    public List<EListBean> getEList() {
        return eList;
    }

    public void setEList(List<EListBean> eList) {
        this.eList = eList;
    }

    public List<ScListBean> getScList() {
        return scList;
    }

    public void setScList(List<ScListBean> scList) {
        this.scList = scList;
    }

    public List<BsListBean> getBsList() {
        return bsList;
    }

    public void setBsList(List<BsListBean> bsList) {
        this.bsList = bsList;
    }

    public static class EListBean {
        private String eName;
        private int eScore;
        private int evaId;
        private String eComment;

        public String getEName() {
            return eName;
        }

        public void setEName(String eName) {
            this.eName = eName;
        }

        public int getEScore() {
            return eScore;
        }

        public void setEScore(int eScore) {
            this.eScore = eScore;
        }

        public int getEvaId() {
            return evaId;
        }

        public void setEvaId(int evaId) {
            this.evaId = evaId;
        }

        public String getEComment() {
            return eComment;
        }

        public void setEComment(String eComment) {
            this.eComment = eComment;
        }
    }

    public static class ScListBean {
        private double classCost;
        private String classCountNum;
        private int classId;
        private String classTitle;
        private String photo;
        private String className;

        public double getClassCost() {
            return classCost;
        }

        public void setClassCost(double classCost) {
            this.classCost = classCost;
        }

        public String getClassCountNum() {
            return classCountNum;
        }

        public void setClassCountNum(String classCountNum) {
            this.classCountNum = classCountNum;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public String getClassTitle() {
            return classTitle;
        }

        public void setClassTitle(String classTitle) {
            this.classTitle = classTitle;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    public static class BsListBean {
        private String address;
        private String smallPhotoUrl;
        private String timePeroid;
        private String photoUrl;
        private String phone;
        private int id;
        private int longitude;
        private List<?> schoolClassVo;
        private List<?> commentVo;
        private String panoramicUrl;//全景图网址

        public String getPanoramicUrl() {
            return panoramicUrl;
        }

        public void setPanoramicUrl(String panoramicUrl) {
            this.panoramicUrl = panoramicUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSmallPhotoUrl() {
            return smallPhotoUrl;
        }

        public void setSmallPhotoUrl(String smallPhotoUrl) {
            this.smallPhotoUrl = smallPhotoUrl;
        }

        public String getTimePeroid() {
            return timePeroid;
        }

        public void setTimePeroid(String timePeroid) {
            this.timePeroid = timePeroid;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public List<?> getSchoolClassVo() {
            return schoolClassVo;
        }

        public void setSchoolClassVo(List<?> schoolClassVo) {
            this.schoolClassVo = schoolClassVo;
        }

        public List<?> getCommentVo() {
            return commentVo;
        }

        public void setCommentVo(List<?> commentVo) {
            this.commentVo = commentVo;
        }
    }
}
