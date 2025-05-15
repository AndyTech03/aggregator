package ru.esstu.news.aggregator.api.dto;

import java.util.UUID;

public class UserIdNewsIdRequest {
    public UUID newsId;
    public UUID userId;

    @Override
    public String toString() {
        return "UserIdNewsIdRequest{" +
                "newsId=" + newsId +
                ", userId=" + userId +
                '}';
    }
}
