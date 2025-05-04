package ru.esstu.news.aggregator.repos.dto;

import java.util.Date;
import java.util.UUID;

public interface RssItem_IdParsedDate_Dto {
    UUID getId();
    Date getParsedDate();
}
