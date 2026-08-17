package com.alienforce.leaderboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists the player name and the ten most recent game scores as JSON.
 */
public final class LeaderboardStore {

    private static final int MAX_RECORDS = 10;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final Path file;
    private final List<String> playerNames;
    private LeaderBoard leaderBoard;
    private String playerName;

    private LeaderboardStore(
            final Path file,
            final String playerName,
            final List<String> playerNames,
            final LeaderBoard leaderBoard
    ) {
        this.file = file;
        this.playerName = playerName;
        this.playerNames = playerNames;
        this.leaderBoard = leaderBoard;
    }

    public static LeaderboardStore load() {
        final Path file = Path.of(
                System.getProperty("user.home", "."),
                ".alien-force",
                ".alien-force-leaderboard.json"
        );
        if (!Files.exists(file)) {
            return new LeaderboardStore(file, null, new ArrayList<>(), LeaderBoard.builder().build());
        }

        try {
            final StoredLeaderboard stored = OBJECT_MAPPER.readValue(file.toFile(), StoredLeaderboard.class);
            final List<String> playerNames = normalizeNames(stored.playerNames());
            final String playerName = stored.playerName();
            if (isValidName(playerName) && !containsName(playerNames, playerName)) {
                playerNames.add(playerName.trim());
            }
            final List<PlayerScore> scores = retainRecentScores(stored.scores());
            final String selectedName = findName(playerNames, playerName);
            return new LeaderboardStore(file, selectedName, playerNames, new LeaderBoard(scores));
        } catch (IOException | RuntimeException ignored) {
            return new LeaderboardStore(file, null, new ArrayList<>(), LeaderBoard.builder().build());
        }
    }

    private static List<PlayerScore> retainRecentScores(final List<PlayerScore> scores) {
        return scores
                .stream()
                .sorted(LeaderBoard.comparator)
                .limit(MAX_RECORDS)
                .toList();
    }

    private static List<String> normalizeNames(final List<String> names) {
        final List<String> normalized = new ArrayList<>();
        if (names != null) {
            names.stream()
                    .filter(LeaderboardStore::isValidName)
                    .map(String::trim)
                    .filter(name -> !containsName(normalized, name))
                    .forEach(normalized::add);
        }
        return normalized;
    }

    private static boolean isValidName(final String name) {
        return name != null && !name.isBlank() && name.trim().length() <= 50;
    }

    private static boolean containsName(final List<String> names, final String candidate) {
        return candidate != null && names.stream().anyMatch(name -> name.equalsIgnoreCase(candidate.trim()));
    }

    private static String findName(final List<String> names, final String candidate) {
        if (candidate == null) {
            return null;
        }
        return names
                .stream()
                .filter(name -> name.equalsIgnoreCase(candidate.trim()))
                .findFirst()
                .orElse(null);
    }

    public LeaderBoard leaderBoard() {
        return leaderBoard;
    }

    public String playerName() {
        return playerName;
    }

    public List<String> playerNames() {
        return List.copyOf(playerNames);
    }

    public boolean addPlayerName(final String name) {
        if (!isValidName(name) || containsName(playerNames, name)) {
            return false;
        }
        playerNames.add(name.trim());
        save();
        return true;
    }

    public boolean selectPlayerName(final String name) {
        final String storedName = findName(playerNames, name);
        if (storedName == null) {
            return false;
        }
        playerName = storedName;
        save();
        return true;
    }

    public void record(final String name, final int score) {
        List<PlayerScore> scores = new ArrayList<>(leaderBoard.scores());
        scores.add(
                PlayerScore
                        .builder()
                        .name(name)
                        .score(score)
                        .build()
        );
        leaderBoard = leaderBoard
                .toBuilder()
                .scores(retainRecentScores(scores))
                .build();

        save();
    }

    private void save() {
        final StoredLeaderboard stored = new StoredLeaderboard(playerName, playerNames, leaderBoard.scores());
        try {
            Files.createDirectories(file.getParent());
            OBJECT_MAPPER.writeValue(file.toFile(), stored);
        } catch (IOException ignored) {
            // A score should never prevent the game-over screen from opening.
        }
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private record StoredLeaderboard(String playerName, List<String> playerNames, List<PlayerScore> scores) {
    }
}
