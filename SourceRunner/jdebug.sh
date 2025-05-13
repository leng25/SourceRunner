#!/bin/bash

echo "Connecting to JDB on port 5005..."

while true; do
    read -p "jdb> " cmd
    case "$cmd" in
        r) echo "run" ;;
        n) echo "next" ;;
        s) echo "step" ;;
        l) echo "list" ;;
        p*) echo "${cmd/p/print }" ;;
        q) echo "exit"; break ;;
        *) echo "$cmd" ;;
    esac
done | jdb -sourcepath src/main/java -attach 5005
