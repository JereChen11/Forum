package com.jere.forum.chats.model;

import com.jere.forum.entity.ChatsListItemEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author jere
 */
public class ChatsListItemBean implements Serializable {
    private ArrayList<DataBean> data;

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * portrait : http://jerechen.gitee.io/imageweb/portraitImage/head1.jpg
         * author : adam
         * date : 2020-4-24
         * title : Anyone here? Let's have a chat~
         * content : My name is adam, I like make friend, let's start chatting.
         * likeNumber : 9
         * isLike : false
         * viewNumber : 13
         */
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        private String portrait;
        private String author;
        private String date;
        private String title;
        private String content;
        private int likeNumber;
        private boolean isLike;
        private int viewNumber;
        private boolean isMyTopic;

        public boolean isMyTopic() {
            return isMyTopic;
        }

        public void setIsMyTopic(boolean myTopic) {
            isMyTopic = myTopic;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLikeNumber() {
            return likeNumber;
        }

        public void setLikeNumber(int likeNumber) {
            this.likeNumber = likeNumber;
        }

        public boolean isIsLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }

        public int getViewNumber() {
            return viewNumber;
        }

        public void setViewNumber(int viewNumber) {
            this.viewNumber = viewNumber;
        }

        public ChatsListItemEntity convertToEntity() {
            ChatsListItemEntity entity = new ChatsListItemEntity();
            entity.setId(this.id);
            entity.setPortrait(this.portrait);
            entity.setAuthor(this.author);
            entity.setDate(this.date);
            entity.setTitle(this.title);
            entity.setContent(this.content);
            entity.setIsLike(this.isLike);
            entity.setLikeNumber(this.likeNumber);
            entity.setViewNumber(this.viewNumber);
            entity.setIsMyTopic(this.isMyTopic);
            return entity;
        }
    }
}
