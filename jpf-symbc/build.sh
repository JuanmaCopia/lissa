#/bin/bash

cd ../jpf-core/
ant clean; ant build

cd ../jpf-symbc/
ant clean; ant build