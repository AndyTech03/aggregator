package ru.esstu.news.aggregator.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.esstu.news.aggregator.api.dto.DatePagingRequest;
import ru.esstu.news.aggregator.repos.RssItemsRepo;
import ru.esstu.news.aggregator.repos.dto.RssItemHeader;

import java.util.List;

@RestController
@RequestMapping("/api/trends")
public class TrendsController {
    RssItemsRepo rssItemsRepo;

    @Autowired

    public TrendsController(RssItemsRepo rssItemsRepo) {
        this.rssItemsRepo = rssItemsRepo;
    }

    @PostMapping("/getTopNews")
    public List<RssItemHeader> getTopNews(
            @RequestBody DatePagingRequest request
    ) {
        return rssItemsRepo.getTopNews(request.offset, request.limit, request.date);
    }
}
