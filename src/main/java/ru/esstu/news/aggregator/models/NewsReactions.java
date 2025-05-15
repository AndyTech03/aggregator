package ru.esstu.news.aggregator.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "news_reactions")
public class NewsReactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID newsId;
    @Enumerated(EnumType.ORDINAL)
    private NewsReactionType type;
    private Date likeDate;
    private Date unlikeDate;

    public NewsReactions() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    public NewsReactionType getType() {
        return type;
    }

    public void setType(NewsReactionType type) {
        this.type = type;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }

    public Date getUnlikeDate() {
        return unlikeDate;
    }

    public void setUnlikeDate(Date unlikeDate) {
        this.unlikeDate = unlikeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsReactions newsReactions = (NewsReactions) o;
        return Objects.equals(id, newsReactions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NewsLikes{" +
                "id=" + id +
                ", userId=" + userId +
                ", newsId=" + newsId +
                ", label=" + type +
                '}';
    }
}
