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
import ru.esstu.news.aggregator.models.RssItem;
import ru.esstu.news.aggregator.services.RssItemsService;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Component
public class RssParser {
    @Autowired
    private RssItemsService rssItemsService;

    public void parseRssFeed(String url) {
        parseRssFeed(url, 0);
    }

    private void parseRssFeed(String url, int iteration) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute();
            try (XmlReader reader = new XmlReader(
                    response.bodyStream(), true, response.charset())) {
                SyndFeed feed = new SyndFeedInput().build(reader);
                System.out.println(feed.getTitle());
                System.out.println("***********************************");
                for (SyndEntry entry : feed.getEntries()) {
                    System.out.println(
                            new String(entry
                            .toString()
                            .getBytes(StandardCharsets.UTF_8), "windows-1251"));
                    System.out.println("***********************************");
                    RssItem item = new RssItem();
                    item.setTitle(entry.getTitle());
                    item.setDescription(entry.getDescription().getValue());
                    item.setAuthor(entry.getAuthor());
                    item.setUri(entry.getLink());
                    item.setFeedUrl(url);
                    item.setDate(entry.getPublishedDate());
                    item.setCategories(entry.getCategories().stream()
                            .map(SyndCategory::getName).toList());
                    System.out.println(rssItemsService.saveOrUpdate(item));
                }
                System.out.println("Done");
            }
        } catch (SocketTimeoutException e) {
            if (iteration > 5) {
                System.err.println("No connection with `"+ url +"` for "+ iteration +" times!!");
            } else {
                parseRssFeed(url, iteration + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }
}
