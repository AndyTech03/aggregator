package ru.esstu.news.aggregator.services;

import jakarta.transaction.Transactional;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.repos.RssItemsRepo;
import ru.esstu.news.aggregator.repos.dto.RssItem_IdParsedDate_Dto;

import java.util.*;

@Service
public class RssItemsService {
//    OpenAiApi openAiApi;
//    OpenAiEmbeddingModel embeddingModel;
    RssItemsRepo rssItemsRepo;

    @Autowired
    public RssItemsService(RssItemsRepo rssItemsRepo) {
//        String api_key = System.getenv("SPRING_AI_OPENAI_API_KEY");
////        System.out.println("api_key" + api_key);
//        this.openAiApi = OpenAiApi.builder()
//                .apiKey(api_key)
//                .build();
//        this.embeddingModel = new OpenAiEmbeddingModel(
//                this.openAiApi,
//                MetadataMode.EMBED,
//                OpenAiEmbeddingOptions.builder()
//                        .model("text-embedding-3-small")
//                        .user("user-6")
//                        .build(),
//                RetryUtils.DEFAULT_RETRY_TEMPLATE);
        this.rssItemsRepo = rssItemsRepo;
    }

    /**
     * Проверяет наличие похожих записей по ненулевым полям.
     * Если записей нет — сохраняет новую.
     * Если одна — обновляет её.
     * Если >1 — выводит ошибку в лог и не сохраняет.
     */
    @Transactional
    public Optional<RssItem> saveOrUpdate(RssItem rssItem) {
        List<RssItem_IdParsedDate_Dto> matchingItems = rssItemsRepo.findMatchingItems(
                rssItem.getUri()
        );
        Date now = new Date(System.currentTimeMillis());
//        List<String> keywords = new ArrayList<>(List.of(
//                rssItem.getTitle(),
//                rssItem.getDescription(),
//                rssItem.getAuthor()
//        ));
//        keywords.addAll(rssItem.getCategories());
//        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(
//                        List.of(keywords.toString())
//        );
//        System.out.println(embeddingResponse);
//        float[] embedding = embeddingResponse.getResult().getOutput();
//        rssItem.setEmbedding(embedding);
        if (matchingItems.isEmpty()) {
            // Нет похожих — сохраняем
            rssItem.setParsedDate(now);
            var item = rssItemsRepo.save(rssItem);
            System.out.println(">>> Inserted: id=" + rssItem.getId() + ", uri=" + rssItem.getUri());
            return Optional.of(item);
        } else if (matchingItems.size() == 1) {
            // Одна подходящая — обновляем
            var item = matchingItems.get(0);
            rssItem.setId(item.getId());
            rssItem.setParsedDate(item.getParsedDate());
            if (rssItem.getParsedDate() == null)
                rssItem.setParsedDate(now);

            System.out.println(">>> Updated id=" + rssItem.getId() + ", uri=" + rssItem.getUri());
            return Optional.of(rssItemsRepo.save(rssItem));
        } else {
            // Несколько совпадений — логируем ошибку
            System.err.println(">>> Error: founded more then one RssItem!: " +
                    matchingItems.stream().map(RssItem_IdParsedDate_Dto::getId));
            return Optional.empty();
        }
    }
}