package ru.esstu.news.aggregator.api.dto;

import java.util.List;

public class SearchRequest extends OffsetLimitRequest {
    public String query;
    public List<String> categories;

    @Override
    public String toString() {
        return "SearchRequest{" +
                "query='" + query + '\'' +
                ", categories=" + categories +
                '}';
    }
}
