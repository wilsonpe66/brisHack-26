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
│   ├── asteroid/    # four asteroid sprites
│   ├── boss-alien/  # five boss sprites
│   ├── missile/     # six projectile sprites
│   ├── planet/      # three 500x500 RGBA gameplay-background sprites
│   ├── spaceship/   # six player-ship sprites
│   ├── shipGreen_manned.png
│   └── spacebackground.png
└── sounds/          # music loops and sound effects
```

Maven copies every file below `src/resource/com/alienforce/assets`. The three planet PNGs are mapped by `ImageKey.PLANET_1` through `PLANET_3`, loaded from the classpath by `Planet`, and included in the shaded JAR.

## Ubuntu package

Run the packaging script from the repository root:

```bash
./build.sh
```

The script builds the shaded JAR and then creates `target/alien-force_1.0.0_all.deb`. Install it with APT so Ubuntu resolves the Java runtime dependency:

```bash
sudo apt install ./target/alien-force_1.0.0_all.deb
```

The package requires Ubuntu's full `openjdk-26-jre` package because the game uses Swing, AWT, and audio. That Ubuntu package supplies the Java runtime and declares its required native GUI, font, printing, compression, and ALSA libraries. The package also depends on `libjinput-jni` and configures Java to find it in `/usr/lib/jni`, enabling JInput controller support.

The package installs the application in the standard system locations:

| Path | Purpose |
|---|---|
| `/usr/share/alien-force/alien-force.jar` | Application and bundled Java libraries |
| `/usr/bin/alien-force` | Command-line launcher |
| `/usr/share/applications/alien-force.desktop` | Desktop application launcher |
| `/usr/share/icons/hicolor/256x256/apps/alien-force.png` | Desktop application icon |

After installation, launch **Alien Force** from the desktop application menu or run `alien-force` in a terminal.
