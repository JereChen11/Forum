package com.jere.forum.chats.model;

import com.jere.forum.entity.ChatsCommentListItemEntity;

import java.util.List;

/**
 * @author jere
 */
public class ChatsCommentListItemBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * portrait : http://jerechen.gitee.io/imageweb/portraitImage/head1.jpg
         * author : adam
         * date : 2020-4-24
         * content : My name is adam, I like make friend, let's start chatting.
         * likeNumber : 9
         * isLike : false
         * title : Hi! How are you.
         */

        private Long id;
        private String portrait;
        private String author;
        private String date;
        private String content;
        private int likeNumber;
        private boolean isLike;
        private Long chatsTopicId;

        public Long getChatsTopicId() {
            return chatsTopicId;
        }

        public void setChatsTopicId(Long chatsTopicId) {
            this.chatsTopicId = chatsTopicId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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


        public ChatsCommentListItemEntity convertToEntity() {
            ChatsCommentListItemEntity entity = new ChatsCommentListItemEntity();
            entity.setPortrait(this.portrait);
            entity.setAuthor(this.author);
            entity.setDate(this.date);
            entity.setContent(this.content);
            entity.setLikeNumber(this.likeNumber);
            entity.setIsLike(this.isLike);
            entity.setChatsTopicId(this.chatsTopicId);
            return entity;
        }
    }
}
