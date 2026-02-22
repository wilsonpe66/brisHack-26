#!/usr/bin/env bash

set -e
SRC=src
OUT=out
JAR=brisHack-26.jar
echo "Cleaning..."
rm -rf "$OUT"
mkdir -p "$OUT"
echo "Compiling..."
javac -d "$OUT" $(find "$SRC" -name "*.java")
echo "Creating JAR..."
jar cfe "$JAR" Main -C "$OUT" .
echo "Done. Run with: java -jar $JAR"
