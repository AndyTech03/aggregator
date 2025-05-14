package ru.esstu.news.aggregator.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.esstu.news.aggregator.api.dto.IdRequest;
import ru.esstu.news.aggregator.api.dto.OffsetLimitRequest;
import ru.esstu.news.aggregator.repos.dto.RssItemHeader;
import ru.esstu.news.aggregator.repos.RssItemsRepo;
import ru.esstu.news.aggregator.repos.dto.RssItemShort;
import ru.esstu.news.aggregator.services.RssItemsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rss_items")
public class RssItemsController {
    private final RssItemsService rssItemsService;
    private final RssItemsRepo rssItemsRepo;

    @Autowired
    public RssItemsController(RssItemsService rssItemsService) {
        this.rssItemsService = rssItemsService;
        this.rssItemsRepo = rssItemsService.getRepo();
    }

    @PostMapping("/latest")
    public List<RssItemHeader> findLatest(
            @RequestBody OffsetLimitRequest body
    ) {
        System.out.println("findLatest(body="+ body +");");
        long total = rssItemsRepo.count();
        if (body.offset >= total) {
            throw new ResponseStatusException(
                    HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
                    "Offset exceeds total records: " + total
            );
        }
        return rssItemsRepo.findLatest(body.limit, body.offset);
    }
    @PostMapping("/getItem")
    public Optional<RssItemShort> getItem(
            @RequestBody IdRequest body
    ) {
        System.out.println("getItem(body="+ body +");");
        return rssItemsRepo.getRssItem(body.id);
    }
}
