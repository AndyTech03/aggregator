package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.repos.dto.RssItem_IdParsedDate_Dto;

import java.util.List;
import java.util.UUID;

public interface RssItemsRepo extends JpaRepository<RssItem, UUID> {
    @Query("SELECT i.id as id, i.parsedDate as parsedDate FROM RssItem i"
            + " WHERE i.uri = :uri AND i.feedUrl = :feedUrl")
    List<RssItem_IdParsedDate_Dto> findMatchingItems(
            @Param("uri") String uri,
            @Param("feedUrl") String feedUrl
    );
}
