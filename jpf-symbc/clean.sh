#/bin/bash

# Remove only FILES (option -type f)
# find . -type d -name "output" -exec rm -f {} \;
find . -type d -name "output" -exec rm -rv {} \;  > /dev/null 2>&1

# Remove only DIRS (option -type d)
#find /dir/to/search/ -type d -name "FILE-TO-FIND-Regex" -exec rm -f {} \;