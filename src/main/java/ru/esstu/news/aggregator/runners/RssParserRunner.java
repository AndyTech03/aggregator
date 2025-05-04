package ru.esstu.news.aggregator.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.esstu.news.aggregator.parser.RssParser;
import ru.esstu.news.aggregator.parser.SubscribeRuParser;

@Component
@Profile("parser") // активируется только если задан профиль
public class RssParserRunner implements CommandLineRunner {

    private final SubscribeRuParser subscribeRuParser;
    private final RssParser rssParser;

    public RssParserRunner(SubscribeRuParser subscribeRuParser, RssParser rssParser) {
        this.subscribeRuParser = subscribeRuParser;
        this.rssParser = rssParser;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> RSS Parser started under profile 'parser'");

        rssParser.parseAllRssFeeds(subscribeRuParser);

        System.out.println(">>> Parsing completed. Exiting.");
        System.exit(0);
    }
}
