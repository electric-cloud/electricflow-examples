#!/usr/bin/env bash

GROUPID=com.ec.complex.pipeline
PROJECTNAME=ComplexPipeline
MYJSONFILE=model.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" input-model.json > $MYJSONFILE.project
sed -e "s/@@GROUPID@@/$GROUPID/" $MYJSONFILE.project > $MYJSONFILE.groupid
cp $MYJSONFILE.groupid $MYJSONFILE

./install-config.sh -c -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
../install.sh -A -f ComplexPipeline/$MYJSONFILE -P $PROJECTNAME -G $GROUPID
