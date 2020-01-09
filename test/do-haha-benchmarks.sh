#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

echo "Functions,Without warm up,Functions,With warm up" >> test/benchmarks.csv
y=$(java -jar build/capriccio.jar -b true)
readarray -t with <<<$y

size=${#with[@]}
for (( i = 0; i <= $size; i++ ))
do
   IFS=',' read -ra funcName <<< ${with[i]}
   x=$(java -jar build/capriccio.jar -b false ${funcName[0]})
   printf $x,${with[i]}"\n" >> test/benchmarks.csv
done
