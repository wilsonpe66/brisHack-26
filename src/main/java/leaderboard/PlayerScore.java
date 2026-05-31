package leaderboard;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;

@Builder(toBuilder = true)
public record PlayerScore(String name, int score, LocalDateTime createTime) {

    public PlayerScore {
        name = Objects.requireNonNullElse(name, "<UNKNOWN>");
        createTime = Objects.requireNonNullElseGet(createTime, LocalDateTime::now);
    }
}
