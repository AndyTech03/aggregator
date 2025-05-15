package ru.esstu.news.aggregator.api.dto;

import ru.esstu.news.aggregator.models.NewsReactionType;

import java.util.UUID;

public class ReactionRequest extends UserIdNewsIdRequest {
    public NewsReactionType type;

    @Override
    public String toString() {
        return "ReactionRequest{" +
                "userId=" + userId +
                ", newsId=" + newsId +
                ", type=" + type +
                '}';
    }
}
