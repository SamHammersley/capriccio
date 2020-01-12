#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

echo "Functions,Times" >> test/warmup-humoresque-benchmarks.csv
files=$(ls -1 examples/benchmarks)
readarray -t fileArray <<<$files

size=${#fileArray[@]}
executions=1000
for (( i = 0; i < $size; i++ ))
do
  fileName=${fileArray[i]}
  for (( j = 0; j < $executions; j++ ))
  do
    x=$(java -jar build/capriccio.jar -b true 1 ${fileName%%.*})
  done

  echo $x >> test/warmup-humoresque-benchmarks.csv
done

