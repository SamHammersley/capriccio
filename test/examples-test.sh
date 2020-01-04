#!/bin/bash

parent_path=$( cd "$(dirname "${BASH_SOURCE[1]}")" ; pwd -P )

cd "$parent_path"

x=$(java -jar build/capriccio.jar examples)
readarray -t output <<<$x

for row in "${output[@]}";do

  success="${row:${#row}-1}"

  if [ $success -eq "0" ]; then
    echo "Failed test: $row"
    exit 1
  fi

done

echo "All test passed!"
exit 0
