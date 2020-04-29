package com.jere.forum.entity;

import com.jere.forum.chats.model.ChatsCommentListItemBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author jere
 */
@Entity
public class ChatsCommentListItemEntity {
    @Id(autoincrement = true)
    private Long id;
    private String portrait;
    private String author;
    private String date;
    private String content;
    private Integer likeNumber;
    private Boolean isLike;
    private Long chatsTopicId;

    @Generated(hash = 1847700338)
    public ChatsCommentListItemEntity(Long id, String portrait, String author,
                                      String date, String content, Integer likeNumber, Boolean isLike,
                                      Long chatsTopicId) {
        this.id = id;
        this.portrait = portrait;
        this.author = author;
        this.date = date;
        this.content = content;
        this.likeNumber = likeNumber;
        this.isLike = isLike;
        this.chatsTopicId = chatsTopicId;
    }

    @Generated(hash = 718745021)
    public ChatsCommentListItemEntity() {
    }

    public ChatsCommentListItemBean.DataBean convertToBean() {
        ChatsCommentListItemBean.DataBean dataBean = new ChatsCommentListItemBean.DataBean();
        dataBean.setPortrait(this.portrait);
        dataBean.setAuthor(this.author);
        dataBean.setDate(this.date);
        dataBean.setContent(this.content);
        dataBean.setLikeNumber(this.likeNumber);
        dataBean.setIsLike(this.isLike);
        dataBean.setChatsTopicId(this.chatsTopicId);
        return dataBean;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortrait() {
        return this.portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeNumber() {
        return this.likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Boolean getIsLike() {
        return this.isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Long getChatsTopicId() {
        return this.chatsTopicId;
    }

    public void setChatsTopicId(Long chatsTopicId) {
        this.chatsTopicId = chatsTopicId;
    }
}
