package com.jere.forum.entity;


import com.jere.forum.chats.model.ChatsListItemBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * @author jere
 */
@Entity
public class ChatsListItemEntity {
    @Id(autoincrement = true)
    private Long id;

    private String portrait;
    private String author;
    private String date;
    private String title;
    private String content;
    private Integer likeNumber;
    private Boolean isLike;
    private Integer viewNumber;
    private Boolean isMyTopic;
    @ToMany(referencedJoinProperty = "chatsTopicId")
    private List<ChatsCommentListItemEntity> chatComments;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1673332543)
    private transient ChatsListItemEntityDao myDao;

    @Generated(hash = 850518983)
    public ChatsListItemEntity(Long id, String portrait, String author, String date,
                               String title, String content, Integer likeNumber, Boolean isLike,
                               Integer viewNumber, Boolean isMyTopic) {
        this.id = id;
        this.portrait = portrait;
        this.author = author;
        this.date = date;
        this.title = title;
        this.content = content;
        this.likeNumber = likeNumber;
        this.isLike = isLike;
        this.viewNumber = viewNumber;
        this.isMyTopic = isMyTopic;
    }

    @Generated(hash = 1717039877)
    public ChatsListItemEntity() {
    }

    public ChatsListItemBean.DataBean convertToBean() {
        ChatsListItemBean.DataBean bean = new ChatsListItemBean.DataBean();
        bean.setId(this.id);
        bean.setPortrait(this.portrait);
        bean.setAuthor(this.author);
        bean.setDate(this.date);
        bean.setTitle(this.title);
        bean.setContent(this.content);
        if (likeNumber == null) {
            bean.setLikeNumber(0);
        } else {
            bean.setLikeNumber(likeNumber);
        }
        if (viewNumber == null) {
            bean.setViewNumber(0);
        } else {
            bean.setViewNumber(this.viewNumber);
        }
        bean.setIsLike(this.isLike);
        bean.setIsMyTopic(this.isMyTopic);
        return bean;
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getViewNumber() {
        return this.viewNumber;
    }

    public void setViewNumber(Integer viewNumber) {
        this.viewNumber = viewNumber;
    }

    public Boolean getIsMyTopic() {
        return this.isMyTopic;
    }

    public void setIsMyTopic(Boolean isMyTopic) {
        this.isMyTopic = isMyTopic;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 446060256)
    public List<ChatsCommentListItemEntity> getChatComments() {
        if (chatComments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChatsCommentListItemEntityDao targetDao = daoSession
                    .getChatsCommentListItemEntityDao();
            List<ChatsCommentListItemEntity> chatCommentsNew = targetDao
                    ._queryChatsListItemEntity_ChatComments(id);
            synchronized (this) {
                if (chatComments == null) {
                    chatComments = chatCommentsNew;
                }
            }
        }
        return chatComments;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1050010668)
    public synchronized void resetChatComments() {
        chatComments = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1536010696)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChatsListItemEntityDao() : null;
    }
}
