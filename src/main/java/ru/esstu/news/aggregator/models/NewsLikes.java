package ru.esstu.news.aggregator.models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "news_likes")
public class NewsLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID newsId;
    @Enumerated(EnumType.ORDINAL)
    private NewsLikeLabel label;

    public NewsLikes() {
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

    public NewsLikeLabel getLabel() {
        return label;
    }

    public void setLabel(NewsLikeLabel label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsLikes newsLikes = (NewsLikes) o;
        return Objects.equals(id, newsLikes.id);
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
                ", label=" + label +
                '}';
    }
}
