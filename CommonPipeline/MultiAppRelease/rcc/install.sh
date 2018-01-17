#!/usr/bin/env bash

PROJECTNAME=MultiApplicationRelease
RELEASENAME="January Release"
APPLICATIONNAME="Web Site"
MYJSONFILE=release-data.json

# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" input-release-data.json > $MYJSONFILE.project
sed -e "s/@@RELEASENAMETOKEN@@/$RELEASENAME/" $MYJSONFILE.project> $MYJSONFILE.release
sed -e "s/@@APPLICATIONNAMETOKEN@@/$APPLICATIONNAME/" $MYJSONFILE.release> $MYJSONFILE.application
cp $MYJSONFILE.application $MYJSONFILE

ectool evalDsl --dslFile releaseDashboardContent.groovy --parametersFile $MYJSONFILE --timeout 300000
