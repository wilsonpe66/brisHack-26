# BrisHack-26

Winning group repo.

## Build

```bash
./build.sh
```

Or manually:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
jar cfm brisHack-26.jar MANIFEST.MF -C out .
```

## Run

From the project root (so `assets/` is found):

```bash
java -jar brisHack-26.jar
```

## Docs

[Project doc](https://docs.google.com/document/d/1heG3mCYeEr5_7BO8B5bYu_D0lQIJZ2oiqUl7rl5NTFU/edit?usp=sharing)
