#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

echo "Functions,Times" >> test/calcy-benchmarks.csv
files=$(ls -1 examples/benchmarks)
readarray -t fileArray <<<$files

size=${#fileArray[@]}
executions=1000
for (( i = 0; i < $size; i++ ))
do
  averageTime=0

  for (( j = 0; j < $executions; j++ ))
  do
    start=$(date +%s%3N)
    t=$(java -jar build/capriccio.jar examples/benchmarks/${fileArray[i]})
    end=$(date +%s%3N)
    delta=$(( end - start ))
    averageTime=$(( ((averageTime * j) + delta) / (j + 1) ))
  done

  echo ${fileArray[i]} "," $averageTime >> test/calcy-benchmarks.csv
done
