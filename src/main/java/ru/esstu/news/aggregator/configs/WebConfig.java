package ru.esstu.news.aggregator.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:^(?!api|static|favicon\\.ico|error|index\\.html).*}/**")
                .setViewName("forward:/index.html");
    }
}
