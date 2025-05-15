package ru.esstu.news.aggregator.api.dto;

import java.util.Date;

public class DatePagingRequest extends OffsetLimitRequest {
    public Date date;

    @Override
    public String toString() {
        return "DatePagingRequest{" +
                "date=" + date +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
