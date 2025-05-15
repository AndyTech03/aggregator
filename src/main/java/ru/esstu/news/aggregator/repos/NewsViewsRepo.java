package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.esstu.news.aggregator.models.NewsViews;

import java.util.UUID;

public interface NewsViewsRepo extends JpaRepository<NewsViews, UUID> {
    boolean existsByUserIdAndNewsId(UUID userId, UUID newsId);
}
