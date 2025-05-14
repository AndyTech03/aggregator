package ru.esstu.news.aggregator.api.dto;

import org.springframework.beans.factory.annotation.Value;

public class OffsetLimitRequest {
    @Value("0")
    public int offset;
    @Value("10")
    public int limit;

    @Override
    public String toString() {
        return "OffsetLimitRequest{" +
                "offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
