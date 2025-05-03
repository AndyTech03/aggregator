package ru.esstu.news.aggregator.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.esstu.news.aggregator.models.RssFeed;
import ru.esstu.news.aggregator.repos.RssFeedsRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RssFeedsService {
    @Autowired
    private RssFeedsRepo rssRepo;

    /**
     * Проверяет наличие похожих записей по ненулевым полям.
     * Если записей нет — сохраняет новую.
     * Если одна — обновляет её.
     * Если >1 — выводит ошибку в лог и не сохраняет.
     */
    @Transactional
    public Optional<RssFeed> saveOrUpdate(RssFeed rss) {
        List<UUID> ids = rssRepo.findMatchingIds(
                rss.getRssHref()
        );
        if (ids.isEmpty()) {
            // Нет похожих — сохраняем
            return Optional.of(rssRepo.save(rss));
        } else if (ids.size() == 1) {
            // Одна подходящая — обновляем
            rss.setId(ids.get(0));
            System.out.println("RssFeed updated " + rss);
            return Optional.of(rssRepo.save(rss));
        } else {
            // Несколько совпадений — логируем ошибку
            System.err.println("Error: найдено несколько записей RssFeed по заданным параметрам: " + ids);
            return Optional.empty();
        }
    }
}
