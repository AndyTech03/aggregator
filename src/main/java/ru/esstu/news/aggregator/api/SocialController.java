package ru.esstu.news.aggregator.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.esstu.news.aggregator.api.dto.ReactionRequest;
import ru.esstu.news.aggregator.api.dto.UserIdNewsIdRequest;
import ru.esstu.news.aggregator.models.NewsReactionType;
import ru.esstu.news.aggregator.models.NewsReactions;
import ru.esstu.news.aggregator.repos.NewsReactionsRepo;
import ru.esstu.news.aggregator.repos.RssItemsRepo;
import ru.esstu.news.aggregator.services.RssItemsService;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/social")
public class SocialController {
    private final RssItemsService rssItemsService;
    private final RssItemsRepo rssItemsRepo;
    private final NewsReactionsRepo reactionsRepo;

    @Autowired
    public SocialController(RssItemsService rssItemsService, NewsReactionsRepo reactionsRepo) {
        this.rssItemsService = rssItemsService;
        this.rssItemsRepo = rssItemsService.getRepo();
        this.reactionsRepo = reactionsRepo;
    }

    @PostMapping("/setReaction")
    public void setReaction(
            @RequestBody ReactionRequest body
    ) {
        var now = new Date();
        var existing = reactionsRepo.findByNewsIdAndUserId(body.newsId, body.userId);
        existing.ifPresentOrElse(r -> {
            if (r.getType() == body.type) return;
            if (body.type == NewsReactionType.LIKE)
                r.setLikeDate(now);
            if (r.getType() == NewsReactionType.LIKE /*&& body.type != NewsReactionType.LIKE*/)
                r.setUnlikeDate(now);
            r.setType(body.type);
            reactionsRepo.save(r);
        }, () -> {
            var r = new NewsReactions();
            r.setNewsId(body.newsId);
            r.setUserId(body.userId);
            r.setType(body.type);
            if (body.type == NewsReactionType.LIKE)
                r.setLikeDate(now);
            else
                r.setUnlikeDate(now);
            reactionsRepo.save(r);
        });
    }
    @PostMapping("/getReaction")
    public Optional<NewsReactionType> getReaction(
            @RequestBody UserIdNewsIdRequest body
    ) {
        return reactionsRepo.findByNewsIdAndUserId(body.newsId, body.userId).map(NewsReactions::getType);
    }
}
