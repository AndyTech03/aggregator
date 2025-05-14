package ru.esstu.news.aggregator.repos.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record RssItemFull(
        UUID id,
        String title,
        String description,
        String author,
        List<String> categories,
        String feedUrl,
        Date date,
        String uri,
        long likesCount,
        long viewsCount
) {
}
