#/bin/bash

# Arguments
PATH_CONFIG_FILE=$1

export LD_LIBRARY_PATH="lib"

CP="build/main:build/examples:build/peers:build/tests:build/annotations:build/classes:build/:lib/*:../jpf-core/build/main:../jpf-core/build/examples:../jpf-core/build/peers:../jpf-core/build/tests:../jpf-core/build/annotations:../jpf-core/build/classes:../jpf-core/build:../jpf-core/lib/*:../jpf-symbc/build/main:../jpf-symbc/build/examples:../jpf-symbc/build/peers:../jpf-symbc/build/tests:../jpf-symbc/build/annotations:../jpf-symbc/build/classes:../jpf-symbc/build:../jpf-symbc/lib/*"

java -Dfile.encoding=UTF-8 -Xms4096m -Xmx4096m -Xss100m -ea -cp $CP gov.nasa.jpf.tool.RunJPF $PATH_CONFIG_FILE
