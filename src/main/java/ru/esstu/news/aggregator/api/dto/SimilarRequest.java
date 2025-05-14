package ru.esstu.news.aggregator.api.dto;

import java.util.UUID;

public class SimilarRequest extends OffsetLimitRequest {
    public UUID similarId;

    @Override
    public String toString() {
        return "SimilarRequest{" +
                "similarId=" + similarId +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
