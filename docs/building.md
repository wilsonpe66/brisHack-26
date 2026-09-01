# Building and Running

## Requirements

- JDK 26, matching the Maven compiler source and target
- Maven 3

The project depends on Lombok, JInput, Jackson Databind, and Jackson's Java Time module. Maven downloads these dependencies during the build.

## Build

From the repository root:

```bash
./build.sh
```

`build.sh` runs Maven's default goal (`clean package`) and copies the shaded executable JAR from `target/alien-force.jar` to `deb/alien-force.jar`.

To create only the Maven artifacts:

```bash
mvn clean package
```

The Maven Shade plugin sets `com.alienforce.Main` as the entry point and bundles runtime dependencies. Images and sounds from `src/resource/com/alienforce/assets` are copied onto the classpath under `com/alienforce/assets`.

## Run

```bash
java -jar target/alien-force.jar
```

Assets are loaded from the JAR classpath, so the game does not rely on a particular working directory.

## Source layout

```text
src/main/java/com/alienforce/
├── assets/       # classpath image/audio loading and playback
├── entities/     # player, enemies, obstacles, bullets, collision contracts
├── game/         # JFrame, panels, world state, and spawners
├── input/        # keyboard and JInput gamepad handling
├── leaderboard/  # score model and JSON persistence
├── motion/       # position and velocity value types
└── utils/        # constants, level tuning, color transitions, settings, fonts

src/resource/com/alienforce/assets/
├── images/
└── sounds/
```
