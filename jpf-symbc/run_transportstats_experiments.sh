#/bin/bash

SRC_FOLDER="src/examples/heapsolving/transportstats"
PACKAGE="heapsolving.transportstats"

CLASS_NAME="TransportStats"
echo "===============================  Class: $CLASS_NAME  ================================="
MINSCOPE=1
MAXSCOPE=50


METHOD="bytesRead"
echo "-------- Method: $METHOD --------"
bash run_upto.sh $CLASS_NAME $METHOD LIHYBRID $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD DRIVER $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD IFREPOK $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSA $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSAM $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSANOSB $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE

METHOD="bytesWritten"
echo "-------- Method: $METHOD --------"
bash run_upto.sh $CLASS_NAME $METHOD LIHYBRID $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD DRIVER $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD IFREPOK $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSA $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSAM $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
bash run_upto.sh $CLASS_NAME $METHOD LISSANOSB $MINSCOPE $MAXSCOPE $SRC_FOLDER $PACKAGE
