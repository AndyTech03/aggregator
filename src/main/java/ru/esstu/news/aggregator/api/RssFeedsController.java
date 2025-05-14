package ru.esstu.news.aggregator.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.esstu.news.aggregator.models.RssFeed;
import ru.esstu.news.aggregator.services.RssFeedsService;

import java.util.List;

@RestController
public class RssFeedsController {
    private final RssFeedsService rssFeedsService;

    @Autowired
    public RssFeedsController(RssFeedsService rssFeedsService) {
        this.rssFeedsService = rssFeedsService;
    }

    @PostMapping("/api/rssFeeds")
    public List<RssFeed> getRssFeeds() {
        return rssFeedsService.findAll();
    }
}
