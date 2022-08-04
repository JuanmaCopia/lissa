#!/bin/bash

# Constants
TIMEOUT=7210 # 2 hours and 10 seconds

# Time between timeout checks
INTERVAL=60 # 1 minutes

# Arguments
CLASS_NAME=$1
METHOD=$2
TECHNIQUE=$3
MINSCOPE=$4
MAXSCOPE=$5
SRC_FOLDER=$6
PACKAGE=$7

OUTDIR="output/stdout"
mkdir output 2>/dev/null
mkdir $OUTDIR 2>/dev/null

echo ""
echo "---- $TECHNIQUE ----"

retn_code=0

for ((i = $MINSCOPE; i <= $MAXSCOPE; i++)); do
    if [ $retn_code -eq 0 ]; then
        echo "Running $CLASS_NAME.$METHOD with $TECHNIQUE for scope $i"
        bash run_script.sh $CLASS_NAME $TECHNIQUE $i $METHOD $TIMEOUT $INTERVAL $SRC_FOLDER $PACKAGE > "${OUTDIR}/${CLASS_NAME}_${METHOD}_${i}-${TECHNIQUE}"
        retn_code=$?
    else
        echo "Stopping execution due timeout"
        exit 0
    fi
done