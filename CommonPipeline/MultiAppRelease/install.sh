#!/usr/bin/env bash

GROUPID=com.ec.multiapp
PROJECTNAME=MultiApplicationRelease
MYJSONFILE=model.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" input-model.json > $MYJSONFILE.project
sed -e "s/@@GROUPID@@/$GROUPID/" $MYJSONFILE.project > $MYJSONFILE

./install-config.sh -c -P $MYJSONFILE -N $PROJECTNAME -G $GROUPID
cd ..
./install.sh -A -P MultiAppRelease/$MYJSONFILE -N $PROJECTNAME -G $GROUPID
