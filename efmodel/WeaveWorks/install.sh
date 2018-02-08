#!/usr/bin/env bash

PARAMETERSFILE=NULL
PROJECTNAME=WW
GROUPID=com.ww.demo

while getopts ":G:N:P:" opt; do
  case $opt in
    G)
        GROUPID=$OPTARG
        echo "new groupId is $GROUPID"
        ;;
    N)
        PROJECTNAME=$OPTARG
        echo "New project name is $PROJECTNAME"
        ;;
    P)
        PARAMETERSFILE=$OPTARG
        echo "new parameters file is $PARAMETERSFILE"
        ;;
  esac
done

for json in "weaveworks" "weaveworks-carts-service" "weaveworks-catalog-service" "weaveworks-orders-service" "weaveworks-services" "weaveworks-user-service" "weaveworks-zipkin-service"; do
    echo "transforming $json"

    # Replace tokens with real names
    sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" \
        -e "s/@@SERVICEPROJECTNAMETOKEN@@/$SERVICESPROJECTNAME/" \
        -e "s/@@GROUPID@@/$GROUPID/" $json.json > new-$json.json

done
# Parse command line
# Only a few options in place - see the help for details and update as needed.
#./install.sh -c -t -r -N WW -G com.ww.demo -P weaveworks.json
efmodel.sh -e -P $PROJECTNAME -G $GROUPID -f new-weaveworks.json
efmodel.sh -r -P $PROJECTNAME -G $GROUPID -f new-weaveworks.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-carts-service.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-catalog-service.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-orders-service.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-services.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-user-service.json
efmodel.sh -s -P $PROJECTNAME -G $GROUPID -f new-weaveworks-zipkin-service.json
