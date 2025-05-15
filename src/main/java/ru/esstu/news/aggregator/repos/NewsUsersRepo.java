package ru.esstu.news.aggregator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.esstu.news.aggregator.models.NewsUsers;

import java.util.UUID;

public interface NewsUsersRepo extends JpaRepository<NewsUsers, UUID> {
}
