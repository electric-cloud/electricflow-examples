#!/usr/bin/env bash

GROUPID=com.ec.complex.pipeline
PROJECTNAME=ComplexPipeline
MYJSONFILE=model.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" \
    -e "s/@@SERVICEPROJECTNAMETOKEN@@/$SERVICESPROJECTNAME/" \
    -e "s/@@GROUPID@@/$GROUPID/" input-model.json > $MYJSONFILE

./install-config.sh -c -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
efmodel.sh -A -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
