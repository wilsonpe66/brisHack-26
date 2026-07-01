package com.alienforce.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;

@Builder(toBuilder = true)
public record LeaderBoard(
    List<PlayerScore> scores
) {

    private static final Comparator<PlayerScore> comparator = Comparator
        .comparing(PlayerScore::score).reversed()
        .thenComparing(Comparator.comparing(PlayerScore::createTime).reversed());

    public LeaderBoard {
        scores = Objects.requireNonNullElseGet(scores, ArrayList::new);
    }

    public Optional<PlayerScore> getHighestScorer() {
        return scores
            .stream()
            .max(
                Comparator.comparing(PlayerScore::score)
                    .thenComparing(
                        Comparator.comparing(PlayerScore::createTime)
                            .reversed()
                    )
            )
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
