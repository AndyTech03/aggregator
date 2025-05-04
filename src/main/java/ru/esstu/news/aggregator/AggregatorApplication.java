package ru.esstu.news.aggregator;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.esstu.news.aggregator.parser.RssParser;
import ru.esstu.news.aggregator.parser.SubscribeRuParser;

@RestController
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class AggregatorApplication extends SpringBootServletInitializer implements ApplicationContextAware {
    public static void main(String[] args) {
        SpringApplication.run(AggregatorApplication.class, args);
    }
    private ApplicationContext context;
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AggregatorApplication.class);
    }
    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok()
                .header("content-type", "application/json")
                .body("{\"message\": \"Hello!\"}");
    }
    @Configuration
    public static class MyConfiguration implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedMethods("*");
        }

    }
    @Bean
    @Profile("api")
    public CommandLineRunner apiRunnerBean() {
        return (args) -> {
            System.out.println("API Started...");

            System.out.println("args");
            for (String arg : args) {
                System.out.println(arg);
            }
//			DbController.connectToDataBase(readProperties());
        };
    }

    @Bean
    @Profile("parser")
    public CommandLineRunner parserRunnerBean(SubscribeRuParser subscribeRuParser, RssParser rssParser) {
        return (args) -> {
            System.err.println("Parser Started...");

            System.out.println("args");
            for (String arg : args) {
                System.out.println(arg);
            }
//            subscribeRuParser.parseRssFeeds();
            rssParser.parseRssFeed("https://aif.ru/rss/all.php");
            SpringApplication.exit(context);
        };
    }
    @PreDestroy
    public void onDestroy()
            throws Exception {
//        DbController.close();
        System.out.println("Stopped.");
    }
}
