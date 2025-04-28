package ru.esstu.news.aggregator;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class AggregatorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AggregatorApplication.class, args);
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
    @PreDestroy
    public void onDestroy()
            throws Exception {
//        DbController.close();
        System.out.println("Stopped.");
    }
}
