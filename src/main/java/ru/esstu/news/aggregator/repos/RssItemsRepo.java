package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.repos.dto.RssItemFull;
import ru.esstu.news.aggregator.repos.dto.RssItemHeader;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.repos.dto.RssItemShort;
import ru.esstu.news.aggregator.repos.dto.RssItem_IdParsedDate_Dto;

import java.util.Date;
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
                    (SELECT COUNT(r)
                        FROM NewsReactions r
                        WHERE
                            r.newsId = i.id AND
                            r.type = ru.esstu.news.aggregator.models.NewsReactionType.LIKE),
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
                    (SELECT COUNT(r)
                        FROM NewsReactions r
                        WHERE
                            r.newsId = i.id AND
                            r.type = ru.esstu.news.aggregator.models.NewsReactionType.LIKE),
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

    @Query(value = """
            WITH recent AS (
                SELECT
                    nv.news_id,
                    COUNT(*) FILTER (WHERE
                        nv.date >= :date AND
                        nv.date <= (:date)::timestamp + interval '24 hours'
                        ) AS views,
                    COUNT(*) FILTER (WHERE
                        nr.type = 0 AND
                        nr.like_date >= :date AND
                        nr.like_date <= (:date)::timestamp + interval '24 hours'
                        ) AS likes,
                    COUNT(*) FILTER (WHERE
                        nr.type = 1 AND
                        nr.unlike_date >= :date AND
                        nr.unlike_date <= (:date)::timestamp + interval '24 hours'
                    ) AS dislikes
                FROM news_views nv
                LEFT JOIN news_reactions nr
                    ON nv.news_id = nr.news_id
                GROUP BY nv.news_id
             )
             SELECT
                r.news_id as id,
                ni.title
             FROM recent r
                left join rss_items ni
                on ni.id = r.news_id
             ORDER BY (1.0 * r.views + 3.0 * r.likes - 2.0 * r.dislikes) DESC
             OFFSET :offset
             LIMIT :limit;
            """, nativeQuery = true)
    List<RssItemHeader> getTopNews(
            int offset, int limit, Date date
    );
}
