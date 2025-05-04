package ru.esstu.news.aggregator.parser;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Component
public class RssParser {
    @Autowired
    private RssFeedsService rssFeedsService;
    @Autowired
    private RssItemsService rssItemsService;

    public void parseAllRssFeeds(SubscribeRuParser parser) {
//        parser.parseRssFeeds();
        List<RssFeed> feeds = rssFeedsService.findAll();
        CountDownLatch latch = new CountDownLatch(feeds.size());
        for (RssFeed feed : feeds) {
            try {
                URL url = new URL("https://" + feed.getRssHref());
                System.out.println("Parsing: "+ url);
                Runnable task = parseRssFeed(url.toString(), latch);
                new Thread(task).start();
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
                latch.countDown();
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Runnable parseRssFeed(String url, CountDownLatch latch) {
        return () -> {
            parseRssFeed(url, 0);
            latch.countDown();
            System.out.println("counter: " + latch.getCount());
        };
    }

    private void parseRssFeed(String url, int iteration) {
        String logEntry = "";
        int count = 0;
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute();
            try (XmlReader reader = new XmlReader(
                    response.bodyStream(), true, response.charset())) {
                SyndFeed feed = new SyndFeedInput().build(reader);
//                System.out.println(
//                        new String(feed
//                                .getTitle()
//                                .getBytes(StandardCharsets.UTF_8), "windows-1251"));
                for (SyndEntry entry : feed.getEntries()) {
                    count++;
                    logEntry =
                            new String(entry
                                    .toString()
                                    .getBytes(StandardCharsets.UTF_8), "windows-1251");
//                    System.out.println(
//                            new String(entry
//                            .getTitle()
//                            .getBytes(StandardCharsets.UTF_8), "windows-1251"));
                    RssItem item = new RssItem();
                    item.setTitle(entry.getTitle());
                    var description = entry.getDescription();
                    item.setDescription(description != null ? description.getValue() : null);
                    item.setAuthor(entry.getAuthor());
                    item.setUri(entry.getLink());
                    item.setFeedUrl(url);
                    item.setPublishedDate(entry.getPublishedDate());
                    item.setCategories(entry.getCategories().stream()
                            .map(SyndCategory::getName).toList());
                    logEntry += "\n" + rssItemsService.saveOrUpdate(item).toString();
                }
//                System.out.println("Done " + count);
            }
        } catch (SocketTimeoutException e) {
            if (iteration > 5) {
                System.err.println("No connection with `"+ url +"` for "+ iteration +" times!!");
            } else {
                parseRssFeed(url, iteration + 1);
            }
        } catch (IOException | FeedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println(logEntry);
            String log = e.getMessage() + " " + Arrays.toString(e.getStackTrace());
            try {
                log = new String(log.getBytes(StandardCharsets.UTF_8), "windows-1251");
            } catch (UnsupportedEncodingException ignored) {
            }
            System.err.println(log);
            throw e;
        }
    }
}
