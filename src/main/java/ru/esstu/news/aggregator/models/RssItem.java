package ru.esstu.news.aggregator.models;

import jakarta.persistence.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Entity
@Table(
        name = "rss_items"
)
public class RssItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 1000)
    private String title;
    @Column(length = 50000)
    private String description;
    private String author;
    @Column(length = 1000)
    private String uri;
    @Column(length = 1000)
    private String feedUrl;
    private Date publishedDate;
    private Date parsedDate;
    private List<String> categories;
    @Column(length = 2000)
    private String categoriesFlat;
    @Column(length = 2000)
    private String descriptionFlat;

    public RssItem() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionFlat = description != null && description.length() > 2000 ? description.substring(0, 2000) : description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(Date parsedDate) {
        this.parsedDate = parsedDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        this.categoriesFlat = categories.toString();
    }

    @Override
    public String toString() {
        String string = "RssItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", uri='" + uri + '\'' +
                ", feedUrl='" + feedUrl + '\'' +
                ", publishedDate=" + publishedDate +
                ", parsedDate=" + parsedDate +
                ", categories=" + categories +
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
        RssItem rssItem = (RssItem) o;
        return Objects.equals(id, rssItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
