package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.esstu.news.aggregator.models.NewsLikeLabel;
import ru.esstu.news.aggregator.models.NewsLikes;

import java.util.UUID;

public interface NewsLikesRepo extends JpaRepository<NewsLikes, UUID> {
    @Query(
            value = """
                    select
                        count(*)
                    from NewsLikes
                    where
                        newsId = :newsId and
                        label = :likeLabel
                    """
    )
    int getNewsLikesCount(
            @Param("newsId") UUID newsId,
            @Param("likeLabel") NewsLikeLabel likeLabel
    );
}
