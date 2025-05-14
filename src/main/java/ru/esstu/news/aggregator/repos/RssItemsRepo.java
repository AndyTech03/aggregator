package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.repos.dto.RssItemFull;
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
    List<RssItemHeader> getLatest(
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

    @Query(
            value = """
                    SELECT new ru.esstu.news.aggregator.repos.dto.RssItemFull(
                    i.id,
                    i.title,
                    i.description,
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
    Optional<RssItemFull> getItemFull(
            @Param("newsId") UUID newsId
    );

    @Query(
            value = """
                    WITH source_data AS (
                        SELECT
                            title,
                            description_flat,
                            categories_flat,
                            author,
                            feed_url
                        FROM rss_items
                        WHERE id = :similarId
                    )
                    SELECT
                        i.id AS id,
                        i.title AS title
                    FROM rss_items i, source_data s
                    WHERE
                        i.id <> :similarId
                    ORDER BY (
                        similarity(i.title, s.title) * 2 +
                        COALESCE(similarity(i.description_flat, s.description_flat), 0) * 5 +
                        COALESCE(similarity(i.categories_flat, s.categories_flat), 0) * 3 +
                        COALESCE(similarity(i.author, s.author), 0) +
                        COALESCE(similarity(i.feed_url, s.feed_url), 0)
                    ) DESC
                    LIMIT :limit
                    OFFSET :offset
                    """,
            nativeQuery = true
    )
    List<RssItemHeader> getSimilar(
            @Param("similarId") UUID similarId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


}
