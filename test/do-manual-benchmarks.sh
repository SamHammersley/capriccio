#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..

echo "Functions,Without warm up,Functions,With warm up" >> test/benchmarks.csv
x=$(java -jar build/capriccio.jar -b false)
y=$(java -jar build/capriccio.jar -b true)

readarray -t without <<<$x
readarray -t with <<<$y

if [ ${#without[@]} -ne ${#with[@]} ]; then
  echo "Outputs are of different sizes"
  exit 1
fi

size=${#without[@]}
for (( i = 0; i <= $size; i++ ))
do
  echo ${without[i]},${with[i]} >> test/benchmarks.csv
done
