package ru.esstu.news.aggregator.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.esstu.news.aggregator.models.RssFeed;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.repos.RssItemsRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RssItemsService {
    @Autowired
    RssItemsRepo rssItemsRepo;

    /**
     * Проверяет наличие похожих записей по ненулевым полям.
     * Если записей нет — сохраняет новую.
     * Если одна — обновляет её.
     * Если >1 — выводит ошибку в лог и не сохраняет.
     */
    @Transactional
    public Optional<RssItem> saveOrUpdate(RssItem rssItem) {
        List<UUID> ids = rssItemsRepo.findMatchingIds(
                rssItem.getUri(),
                rssItem.getFeedUrl()
        );
        if (ids.isEmpty()) {
            // Нет похожих — сохраняем
            return Optional.of(rssItemsRepo.save(rssItem));
        } else if (ids.size() == 1) {
            // Одна подходящая — обновляем
            rssItem.setId(ids.get(0));
            System.out.println("Updated " + rssItem);
            return Optional.of(rssItemsRepo.save(rssItem));
        } else {
            // Несколько совпадений — логируем ошибку
            System.err.println("Error: найдено несколько записей RssItem по заданным параметрам: " + ids);
            return Optional.empty();
        }
    }
}