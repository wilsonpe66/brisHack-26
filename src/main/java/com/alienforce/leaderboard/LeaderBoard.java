package com.alienforce.leaderboard;

import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Builder(toBuilder = true)
public record LeaderBoard(
        List<PlayerScore> scores
) {

    static final Comparator<PlayerScore> comparator = Comparator
            .comparing(PlayerScore::score).reversed()
            .thenComparing(Comparator.comparing(PlayerScore::createTime).reversed());

    public LeaderBoard {
        scores = Objects.requireNonNullElseGet(scores, List::of);
    }

    public Optional<PlayerScore> getHighestScorer() {
        return scores
                .stream()
                .max(comparator)
                .stream()
                .findFirst();
    }

    public List<PlayerScore> getTopScorers(final int limit) {
        return scores
                .stream()
                .sorted(comparator)
                .limit(limit)
                .toList();
    }
}
