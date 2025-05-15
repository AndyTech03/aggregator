package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.esstu.news.aggregator.models.NewsReactions;

import java.util.Optional;
import java.util.UUID;

public interface NewsReactionsRepo extends JpaRepository<NewsReactions, UUID> {
    Optional<NewsReactions> findByNewsIdAndUserId(UUID newsId, UUID userId);
}
