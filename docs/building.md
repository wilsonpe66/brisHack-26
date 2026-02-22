# Building & Running

## Prerequisites

- **Java JDK** (any version that supports `javax.swing` and `javax.sound.sampled` — JDK 8+).

## Build

From the project root:

```bash
./build.sh
```

This script:

1. **Cleans** the `out/` directory.
2. **Compiles** all `.java` files from `src/` into `out/`.
3. **Packages** them into `brisHack-26.jar` with the entry point set to `Main` (via `jar cfe`).

## Run

```bash
java -jar brisHack-26.jar
```

> **Important:** Run from the **project root** directory. The game loads assets using relative paths (e.g. `assets/images/spaceship.png`), so the working directory must be the repo root.

## Project Structure for Compilation

There are no external dependencies — the project uses only the Java standard library (`java.awt`, `javax.swing`, `javax.sound.sampled`).

All source files are in three directories:

```
src/entities/   → 6 files
src/game/       → 9 files
src/utils/      → 1 file
```

The build script finds and compiles them all with a single `javac` invocation.
