#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

java -jar build/capriccio.jar -b > test/benchmark-output.csv
