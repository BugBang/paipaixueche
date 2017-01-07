package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-12-26.
 */
public class ShareData {


    private List<ShareBean> share;

    public List<ShareBean> getShare() {
        return share;
    }

    public void setShare(List<ShareBean> share) {
        this.share = share;
    }

    public static class ShareBean {
        private String message;
        private String title;
        private String image;
        private String url;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
