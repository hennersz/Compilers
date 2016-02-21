#! /bin/bash

java -cp bin/:lib/java-cup-$1-runtime.jar $2 $3  >> result.csv 2>&1

exit 0
