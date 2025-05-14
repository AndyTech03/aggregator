package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.repos.dto.RssItemHeader;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.repos.dto.RssItemShort;
import ru.esstu.news.aggregator.repos.dto.RssItem_IdParsedDate_Dto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RssItemsRepo extends JpaRepository<RssItem, UUID> {
    @Query("SELECT i.id as id, i.parsedDate as parsedDate FROM RssItem i"
            + " WHERE i.uri = :uri")
    List<RssItem_IdParsedDate_Dto> findMatchingItems(
            @Param("uri") String uri
    );

    @Query(
            value = "SELECT id, title " +
                    "FROM rss_items " +
                    "ORDER BY coalesce(published_date, parsed_date) DESC " +
                    "LIMIT :limit " +
                    "OFFSET :offset",
            nativeQuery = true
    )
    List<RssItemHeader> findLatest(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(
            value = """
                    SELECT new ru.esstu.news.aggregator.repos.dto.RssItemShort(
                    i.id,
                    i.title,
                    i.author,
                    i.categories,
                    i.feedUrl,
                    coalesce(i.publishedDate, i.parsedDate),
                    i.uri,
                    (SELECT COUNT(l)
                        FROM NewsLikes l
                        WHERE
                            l.newsId = i.id AND
                            l.label = ru.esstu.news.aggregator.models.NewsLikeLabel.LIKE),
                    (SELECT COUNT(v)
                        FROM NewsViews v
                        WHERE v.newsId = i.id)
                    )
                    from RssItem i
                    where i.id = :newsId
                    """
    )
    Optional<RssItemShort> getRssItem(
            @Param("newsId") UUID newsId
    );
}
