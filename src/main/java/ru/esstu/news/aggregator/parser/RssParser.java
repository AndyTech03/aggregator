package ru.esstu.news.aggregator.parser;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esstu.news.aggregator.models.RssFeed;
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.services.RssFeedsService;
import ru.esstu.news.aggregator.services.RssItemsService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static ru.esstu.news.aggregator.utils.ConsoleEncodingFix.fixStringEncoding;


@Component
public class RssParser {
    private final RssFeedsService rssFeedsService;
    private final RssItemsService rssItemsService;

    @Autowired
    public RssParser(RssFeedsService rssFeedsService, RssItemsService rssItemsService) {
        this.rssFeedsService = rssFeedsService;
        this.rssItemsService = rssItemsService;
    }

    public void parseAllRssFeeds(SubscribeRuParser parser) {
        if (true)
            parser.parseRssFeeds();
        List<RssFeed> feeds = rssFeedsService.findAll();
        CountDownLatch latch = new CountDownLatch(feeds.size()*2);
        for (RssFeed feed : feeds) {
            String url = "https://" + feed.getRssHref();
            Runnable task = parseRssFeed(url, latch);
            new Thread(task).start();

            url = "http://" + feed.getRssHref();
            task = parseRssFeed(url, latch);
            new Thread(task).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Runnable parseRssFeed(String url, CountDownLatch latch) {
        return () -> {
            try {
                int attemptsCount = 5;
                boolean failure = true;
                for (int i = 0; i < attemptsCount; i ++) {
                    boolean finished = parseRssFeed(url);
                    if (finished) {
                        failure = false;
                        String log = fixStringEncoding(
                                ">>> "+ url +" successfully finished. " +
                                        "Awaiting "+ (latch.getCount() - 1) +" tasks."
                        );
                        System.out.println(log);
                        break;
                    }
                }
                if (failure) {
                    String log = fixStringEncoding(
                            ">>> "+ url +" no connection for "+ attemptsCount +" times! " +
                                    "Awaiting "+ (latch.getCount() - 1) +" tasks."
                    );
                    System.err.println(log);
                }
            } catch (Exception e) {
                String log = fixStringEncoding(
                        ">>> "+ url +" finished with errors! "+
                            "Awaiting "+ (latch.getCount() - 1) +" tasks.\n"+
                        "\t"+ e +"\n"+
//                        "\t"+ e.getMessage() +"\n"+
                        "\t"+ String.join(
                                "\n\t",
                                Arrays.stream(e.getStackTrace())
                                        .map(StackTraceElement::toString)
                                        .toList())
                );
                System.err.println(log);
            }
            latch.countDown();
        };
    }

    private boolean parseRssFeed(String url)
            throws IOException, FeedException {
        String logEntry = "";
        try {
            Connection.Response response = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .ignoreContentType(true)
                        .execute();
            try (XmlReader reader = new XmlReader(
                    response.bodyStream(), true, response.charset())) {
                SyndFeed feed = new SyndFeedInput().build(reader);
                for (SyndEntry entry : feed.getEntries()) {
                    try {
                        logEntry = entry.toString();
                        RssItem item = new RssItem();
                        var description = entry.getDescription();
                        item.setTitle(entry.getTitle());
                        item.setDescription(description != null ? description.getValue() : null);
                        item.setAuthor(entry.getAuthor());
                        item.setUri(entry.getLink());
                        item.setFeedUrl(url);
                        item.setPublishedDate(entry.getPublishedDate());
                        item.setCategories(entry.getCategories().stream().map(SyndCategory::getName).toList());
                        logEntry += "\n"+ rssItemsService.saveOrUpdate(item).toString();
                    } catch (Exception e) {
                        System.err.println(new String(logEntry
                                .getBytes(StandardCharsets.UTF_8), "windows-1251"));
                        throw e;
                    }
                }
                return true;
            }
        } catch (SocketTimeoutException ignored) {
        } catch (ParsingFeedException e) {
            ParsingFeedException err = e;
            if (err.getMessage().contains("DOCTYPE is disallowed when the feature")) {
                err = new ParsingFeedException("XML document expected, DOCTYPE received!");
            }
            throw err;
        }
        return false;
    }
}
