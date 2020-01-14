#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

echo "Functions,Calcy, ,Humoresque, ,Manual" >> test/benchmarks.csv

executions=1000000
x=$(java -jar build/capriccio.jar -b true true $executions)
readarray -t calcy <<<$x

y=$(java -jar build/capriccio.jar -b true false $executions)
readarray -t humor <<<$y

z=$(java -cp build ManualFunctionBenchmarks $executions)
readarray -t manual <<<$z

size=${#calcy[@]}
for (( i = 0; i <= $size; i++ ))
do
  echo ${calcy[i]},${humor[i]},${manual[i]} >> test/benchmarks.csv
done
