package ru.esstu.news.aggregator.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esstu.news.aggregator.models.RssFeed;
import ru.esstu.news.aggregator.services.RssFeedsService;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class SubscribeRuParser {
    @Autowired
    private RssFeedsService rssService;


    public void parseRssFeeds()
            throws IOException {

        String url = "https://subscribe.ru/catalog/media?rss";
        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .ignoreContentType(true)
                .execute();
        Document doc = Jsoup.parse(
                response.bodyStream(),
                response.charset(),
                url
        );
        System.out.println("Content-Type header: " + response.header("Content-Type"));
        System.out.println("Jsoup.detected charset: " + response.charset());
        System.out.println("doc got");
        Elements rssList = doc.select(".entry.fullentry.rss");
        rssList.forEach(rss -> {
            try {
                System.out.println(new String(rss.text().getBytes(StandardCharsets.UTF_8), "Windows-1251"));
            } catch (UnsupportedEncodingException ignored) {
            }
            String title = null;
            String detail = null;
            String channelHref = null;
            String rssHref = null;

            Element titleH2 = rss.select("h2 a").first();
            if (titleH2 == null) {
                System.out.println("Title not found!");
            } else {
                title = titleH2.text().trim();
            }

            Element detailP = rss.select(".content p").first();
            if (detailP == null) {
                System.out.println("Detail not found!");
            } else {
                detail = detailP.text().trim();
            }

            Elements links = rss.select(".content .lightblue a");
            int count = links.size();
            if (count == 0) {
                System.out.println("Links count == 0!");
            } else if (count > 2) {
                System.out.println("Links count > 2!");
            } else {
                Element channelHrefA = links.first();
                if (channelHrefA == null) {
                    System.out.println("ChannelHref not found!");
                } else {
                    channelHref = channelHrefA.text();
                }
                if (count == 2) {
                    Element rssLink = links.last();
                    if (rssLink == null) {
                        System.out.println("RssHref not found!");
                    } else {
                        rssHref = rssLink.text();
                    }
                }
            }

            var item = new RssFeed(title, detail, channelHref, rssHref);
            System.out.println(item);
            var rssFeed = rssService.saveOrUpdate(item);
            System.out.println(rssFeed);
        });
    }
}
