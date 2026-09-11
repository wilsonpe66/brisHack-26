#!/bin/sh

exec java -Djava.library.path=/usr/lib/jni \
    -jar /usr/share/alien-force/alien-force.jar "$@"
