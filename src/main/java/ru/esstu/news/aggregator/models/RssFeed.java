package ru.esstu.news.aggregator.models;

import jakarta.persistence.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rss_feeds")
public class RssFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 1000)
    private String title;
    @Column(length = 1000)
    private String detail;
    private String channelHref;
    private String rssHref;
    private Boolean loaded;

    public RssFeed() {
    }

    public RssFeed(String title, String detail, String channelHref, String rssHref) {
        this.title = title;
        this.detail = detail;
        this.channelHref = channelHref.replaceAll("http://", "").replaceAll("https://", "");
        this.rssHref = rssHref.replaceAll("http://", "").replaceAll("https://", "");;
        this.loaded = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getChannelHref() {
        return channelHref;
    }

    public void setChannelHref(String channelHref) {
        this.channelHref = channelHref;
    }

    public String getRssHref() {
        return rssHref;
    }

    public void setRssHref(String rssHref) {
        this.rssHref = rssHref;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public Boolean getLoaded() {
        return loaded;
    }

    @Override
    public String toString() {
        String string = "RssFeed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", channelHref='" + channelHref + '\'' +
                ", rssHref='" + rssHref + '\'' +
                ", loaded=" + loaded +
                '}';
        try {
            string = new String(string.getBytes(StandardCharsets.UTF_8), "Windows-1251");
        } catch (UnsupportedEncodingException ignored) {
        }
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssFeed rssFeeds = (RssFeed) o;
        return Objects.equals(id, rssFeeds.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
