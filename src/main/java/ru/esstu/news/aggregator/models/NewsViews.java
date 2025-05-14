package ru.esstu.news.aggregator.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "news_views")
public class NewsViews {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID newsId;
}
