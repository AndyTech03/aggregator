package ru.esstu.news.aggregator.api.dto;

import java.util.UUID;

public class IdRequest {
    public UUID id;

    @Override
    public String toString() {
        return "IdRequest{" +
                "id=" + id +
                '}';
    }
}
