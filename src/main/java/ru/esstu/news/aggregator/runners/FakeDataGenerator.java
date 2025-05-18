package ru.esstu.news.aggregator.runners;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.esstu.news.aggregator.models.*;
import ru.esstu.news.aggregator.repos.NewsReactionsRepo;
import ru.esstu.news.aggregator.repos.NewsUsersRepo;
import ru.esstu.news.aggregator.repos.NewsViewsRepo;
import ru.esstu.news.aggregator.repos.RssItemsRepo;
import ru.esstu.news.aggregator.utils.ProgressPrinter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Profile("faker") // активируется только если задан профиль
public class FakeDataGenerator implements CommandLineRunner {
    private final NewsUsersRepo usersRepo;
    private final RssItemsRepo itemsRepo;
    private final NewsViewsRepo viewsRepo;
    private final NewsReactionsRepo reactionsRepo;
    private final Faker faker = new Faker();
    private final Random rand = new Random();

    @Autowired
    public FakeDataGenerator(NewsUsersRepo usersRepo, RssItemsRepo itemsRepo, NewsViewsRepo viewsRepo, NewsReactionsRepo reactionsRepo) {
        this.usersRepo = usersRepo;
        this.itemsRepo = itemsRepo;
        this.viewsRepo = viewsRepo;
        this.reactionsRepo = reactionsRepo;
    }

    @Override
    public void run(String... args) {
        System.out.println(">>> Faker Generator started under profile 'faker'");

//        generateUsers(1000);
        generateInteractions();

        System.out.println(">>> Generating completed. Exiting.");
        System.exit(0);
    }
    @Transactional
    private void generateUsers(int count) {
        System.out.println(">>> generateUsers");
        for (int i = 0; i < count; i++) {
            NewsUsers u = new NewsUsers();
            u.setUsername(faker.name().username());
            u.setPassword(faker.internet().password());
            u.setAge(rand.nextInt(60) + 18);
            u.setSex(rand.nextBoolean());
            usersRepo.save(u);
        }
    }
    private void generateInteractions() {
        System.out.println(">>> generateInteractions");
        List<NewsUsers> users = usersRepo.findAll();
        List<RssItem> items = itemsRepo.findAll();

        // collect distinct metadata
        List<String> allCats = items.stream()
                .flatMap(i -> i.getCategories().stream())
                .distinct().collect(Collectors.toList());
        List<String> allAuthors = items.stream()
                .map(RssItem::getAuthor)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        List<String> allChannels = items.stream()
                .map(RssItem::getFeedUrl)
                .distinct().collect(Collectors.toList());

        System.out.println("allCats count=" + allCats.size());
        System.out.println("allAuthors count=" + allAuthors.size());
        System.out.println("allChannels count=" + allChannels.size());
        System.out.println("items count=" + items.size());
        // build groups
        List<String> catGroups = sampleRandom(allCats, 48);
        List<String> authorGroups = sampleRandom(allAuthors, 48);
        List<String> channelGroups = sampleRandom(allChannels, 48);
        List<RssItem> randomItems = sampleRandom(items, 48);

        // init threads
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // for each category group
        List<CountDownLatch> categoryLatchList = new ArrayList<>();
        List<Integer> categoryLatchCounts = new ArrayList<>();
        for (String category : catGroups) {
            var categoryLow = category.toLowerCase();
            var usersSet = randomUsers(users, 48);
            var latch = new CountDownLatch(usersSet.size());
            categoryLatchList.add(latch);
            categoryLatchCounts.add(usersSet.size());
            List<RssItem> filtered = items.stream()
                    .filter(i -> i.getCategories().stream()
                            .anyMatch(c ->
                                    c.toLowerCase().contains(categoryLow) ||
                                    categoryLow.contains(c.toLowerCase())
                            )
                    )
                    .collect(Collectors.toList());

            executor.submit(() -> assignViewsAndReactions(latch, usersSet, filtered, 100));

        }

        // authors
        List<CountDownLatch> authorsLatchList = new ArrayList<>();
        List<Integer> authorsLatchCounts = new ArrayList<>();
        for (String author : authorGroups) {
            var usersSet = randomUsers(users, 48);
            var latch = new CountDownLatch(usersSet.size());
            authorsLatchList.add(latch);
            authorsLatchCounts.add(usersSet.size());
            List<RssItem> filtered = items.stream()
                    .filter(i -> author.equals(i.getAuthor()))
                    .collect(Collectors.toList());

            executor.submit(() -> assignViewsAndReactions(latch, usersSet, filtered, 100));
        }

        // channels
        List<CountDownLatch> channelsLatchList = new ArrayList<>();
        List<Integer> channelsLatchCounts = new ArrayList<>();
        for (String channel : channelGroups) {
            var usersSet = randomUsers(users, 48);
            var latch = new CountDownLatch(usersSet.size());
            channelsLatchList.add(latch);
            channelsLatchCounts.add(usersSet.size());
            List<RssItem> filtered = items.stream()
                    .filter(i -> channel.equals(i.getFeedUrl()))
                    .collect(Collectors.toList());

            executor.submit(() -> assignViewsAndReactions(latch, usersSet, filtered, 120));
        }

        // randoms
        List<CountDownLatch> randomsLatchList = new ArrayList<>();
        List<Integer> randomsLatchCounts = new ArrayList<>();
        {
            var usersSet = randomUsers(users, 120);
            var latch = new CountDownLatch(usersSet.size());
            randomsLatchList.add(latch);
            randomsLatchCounts.add(usersSet.size());
            executor.submit(() -> assignViewsAndReactions(latch, usersSet, randomItems, 50));
        }

        System.out.println(
                List.of(categoryLatchCounts, authorsLatchCounts, channelsLatchCounts, randomsLatchCounts)
        );
        ProgressPrinter progress = new ProgressPrinter(
                List.of("Categories", "Authors", "Channels", "Random"),
                List.of(categoryLatchList, authorsLatchList, channelsLatchList, randomsLatchList),
                List.of(categoryLatchCounts, authorsLatchCounts, channelsLatchCounts, randomsLatchCounts)
        );
        try {
            progress.printMultiProgress();
        } catch (InterruptedException e) {
            System.err.println(e + "\n" + String.join("\n",
                    Arrays.stream(e.getStackTrace())
                            .map(StackTraceElement::toString)
                            .toList()));
        }
        executor.shutdown();
    }

    @Transactional
    private void assignViewsAndReactions(CountDownLatch latch, List<NewsUsers> users, List<RssItem> items, int maxPerUser) {
        if (items.isEmpty()) return;
        for (NewsUsers u : users) {
            int limit = Math.min(maxPerUser, items.size());
            int count = rand.nextInt(limit);
            List<RssItem> set = new ArrayList<>(items);
            for (int i = 0; i < count; i++) {
                if (set.isEmpty()) break;
                RssItem item = set.get(rand.nextInt(set.size()));
                set.remove(item);
                // skip if view exists
                if (viewsRepo.existsByUserIdAndNewsId(u.getId(), item.getId())) {
                    i--;
                    continue;
                }
                Date start = Optional.ofNullable(item.getPublishedDate()).orElse(item.getParsedDate());
                Instant startI = start.toInstant();
                Instant endI = Instant.now();
                Date viewDate = Date.from(Instant.ofEpochMilli(
                        startI.toEpochMilli() + (long) (rand.nextDouble() * (endI.toEpochMilli() - startI.toEpochMilli()))
                ));
                NewsViews v = new NewsViews();
                v.setUserId(u.getId());
                v.setNewsId(item.getId());
                v.setDate(viewDate);
                viewsRepo.save(v);

                // reactions
                if (rand.nextDouble() < 0.3) {
                    NewsReactions r = new NewsReactions();
                    r.setUserId(u.getId());
                    r.setNewsId(item.getId());
                    NewsReactionType type = NewsReactionType.values()[rand.nextInt(2)];
                    r.setType(type);
                    Date reactionTime = Date.from(
                            viewDate.toInstant().plusMillis((long) (rand.nextDouble() * (Instant.now().toEpochMilli() - viewDate.toInstant().toEpochMilli())))
                    );
                    if (type == NewsReactionType.LIKE) r.setLikeDate(reactionTime);
                    else r.setUnlikeDate(reactionTime);
                    reactionsRepo.save(r);
                }
            }
            latch.countDown();
        }
    }

    private <T> List<T> randomUsers(List<T> list, int n) {
        Collections.shuffle(list);
        return list.stream().limit(n).collect(Collectors.toList());
    }

    private <T> List<T> sampleRandom(List<T> list, int n) {
        Collections.shuffle(list);
        return list.stream().limit(n).collect(Collectors.toList());
    }
}
