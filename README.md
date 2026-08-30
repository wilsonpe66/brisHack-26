# Alien Force

Alien Force is a Java Swing arcade game inspired by Asteroids. Pilot a ship through 15 increasingly difficult levels of asteroids, aliens, and boss aliens, then compare your score and reached level on a persistent local leaderboard.

## Requirements

- JDK 26
- Maven 3

## Build

```bash
./build.sh
```

The script runs the Maven build and copies `target/alien-force.jar` into `deb/`.

To build without copying the JAR:

```bash
mvn clean package
```

## Run

```bash
java -jar target/alien-force.jar
```

## Controls

| Action | Keyboard |
|---|---|
| Thrust | `W` or `Up` |
| Rotate | `A`/`D` or `Left`/`Right` |
| Fire | `Space` or `Z` |
| Fire without cooldown | `X` |
| Pause | `Enter` |
| Mute | `M` |
| Toggle fullscreen | `F11` |
| Exit fullscreen | `Escape` |

The first detected JInput-compatible gamepad is also supported. See [Input & Controls](docs/input.md) for the controller mappings.

Destroyed objects leave short expanding explosion effects, and player progress unlocks stronger ship sprites and projectile patterns as the score increases.

## Documentation

See the [documentation index](docs/README.md) for architecture, gameplay, UI, audio, and tuning details.
