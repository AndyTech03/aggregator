package ru.esstu.news.aggregator.repos.dto;

import java.util.UUID;

public interface RssItemHeader {
    UUID getId();
    String getTitle();
}
