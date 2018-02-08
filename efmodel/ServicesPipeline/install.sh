#!/usr/bin/env bash

GROUPID=com.ec.services
PROJECTNAME=ServiceRelease
SERVICESPROJECTNAME=WW
MYJSONFILE=model.json
MYRELJSONFILE=release-model.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" \
    -e "s/@@SERVICEPROJECTNAMETOKEN@@/$SERVICESPROJECTNAME/" \
    -e "s/@@GROUPID@@/$GROUPID/" input-model.json > $MYJSONFILE

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" \
    -e "s/@@SERVICEPROJECTNAMETOKEN@@/$SERVICESPROJECTNAME/" \
    -e "s/@@GROUPID@@/$GROUPID/" input-release.json > $MYRELJSONFILE

./install-config.sh -c -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
efmodel.sh -A -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID
efmodel.sh -R -f $MYRELJSONFILE -P $PROJECTNAME -G $GROUPID
