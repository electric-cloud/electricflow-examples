#!/usr/bin/env bash

GROUPID=com.ec.multiapp
PROJECTNAME=MultiApplicationRelease
MYJSONFILE=model.json
MYRELJSONFILE=release-model.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" input-model.json > $MYJSONFILE.project
sed -e "s/@@GROUPID@@/$GROUPID/" $MYJSONFILE.project > $MYJSONFILE

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" input-release.json > $MYRELJSONFILE.project
sed -e "s/@@GROUPID@@/$GROUPID/" $MYRELJSONFILE.project > $MYRELJSONFILE.groupid
cp $MYRELJSONFILE.groupid $MYRELJSONFILE

./install-config.sh -c -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
../install.sh -A -f MultiAppRelease/$MYJSONFILE -P $PROJECTNAME -G $GROUPID
../install.sh -R -f $MYRELJSONFILE -P $PROJECTNAME -G $GROUPID
