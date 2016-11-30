package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-11-25.
 */
public class SignAllComment {

    private List<SignAllCommentModel> list;

    public List<SignAllCommentModel> getList() {
        return list;
    }

    public void setList(List<SignAllCommentModel> list) {
        this.list = list;
    }

    public static class SignAllCommentModel {
        private int score;
        private String orderId;
        private String name;
        private String comment;
        private int id;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
