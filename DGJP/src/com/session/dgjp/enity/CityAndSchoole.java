package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2017-01-13.
 */
public class CityAndSchoole {

    private BranchlistBean branchlist;
    private CountlistBean countlist;

    public BranchlistBean getBranchlist() {
        return branchlist;
    }

    public void setBranchlist(BranchlistBean branchlist) {
        this.branchlist = branchlist;
    }

    public CountlistBean getCountlist() {
        return countlist;
    }

    public void setCountlist(CountlistBean countlist) {
        this.countlist = countlist;
    }

    public static class BranchlistBean {
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
            private float score;
            private String branchSchoolName;
            private String city;
            private int id;
            private double distance;
            private String headImg;
            private String trainerTotal;
            private String county;
            private String smallPhotoUrl;

            public String getTrainerIdcard() {
                return trainerIdcard;
            }

            public void setTrainerIdcard(String trainerIdcard) {
                this.trainerIdcard = trainerIdcard;
            }

            public float getScore() {
                return score;
            }

            public void setScore(float score) {
                this.score = score;
            }

            public String getBranchSchoolName() {
                return branchSchoolName;
            }

            public void setBranchSchoolName(String branchSchoolName) {
                this.branchSchoolName = branchSchoolName;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public String getTrainerTotal() {
                return trainerTotal;
            }

            public void setTrainerTotal(String trainerTotal) {
                this.trainerTotal = trainerTotal;
            }

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }

            public String getSmallPhotoUrl() {
                return smallPhotoUrl;
            }

            public void setSmallPhotoUrl(String smallPhotoUrl) {
                this.smallPhotoUrl = smallPhotoUrl;
            }
        }
    }

    public static class CountlistBean {
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
            private String county;

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }
        }
    }
}
