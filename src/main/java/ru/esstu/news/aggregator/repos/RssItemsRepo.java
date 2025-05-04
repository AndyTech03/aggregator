package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.models.RssItem;

import java.util.List;
import java.util.UUID;

public interface RssItemsRepo extends JpaRepository<RssItem, UUID> {
    @Query("SELECT i.id FROM RssItem i"
            + " WHERE i.uri = :uri AND i.feedUrl = :feedUrl")
    List<UUID> findMatchingIds(
            @Param("uri") String uri,
            @Param("feedUrl") String feedUrl
    );
}
