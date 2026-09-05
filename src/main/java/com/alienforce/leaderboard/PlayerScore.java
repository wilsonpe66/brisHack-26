package com.alienforce.leaderboard;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record PlayerScore(String name, int level, int score, LocalDateTime createTime) {

    public PlayerScore {
        name = Objects.requireNonNullElse(name, "<UNKNOWN>");
        createTime = Objects.requireNonNullElseGet(createTime, LocalDateTime::now);
    }
}
