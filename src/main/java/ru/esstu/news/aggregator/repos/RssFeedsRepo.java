package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.models.RssFeed;

import java.util.List;
import java.util.UUID;

public interface RssFeedsRepo extends JpaRepository<RssFeed, UUID> {
    @Query("SELECT f.id FROM RssFeed f"
            + " WHERE (:rssHref IS NOT NULL AND f.rssHref = :rssHref)")
    List<UUID> findMatchingIds(
            @Param("rssHref") String rssHref
    );
}
