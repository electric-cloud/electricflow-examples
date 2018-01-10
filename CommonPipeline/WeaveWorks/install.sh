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


# Parse command line
# Only a few options in place - see the help for details and update as needed.
#./install.sh -c -t -r -N WW -G com.ww.demo -P weaveworks.json
../install.sh -e -P $PROJECTNAME -G $GROUPID -f weaveworks.json
../install.sh -r -P $PROJECTNAME -G $GROUPID -f weaveworks.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-carts-service.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-catalog-service.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-orders-service.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-services.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-user-service.json
../install.sh -s -P $PROJECTNAME -G $GROUPID -f weaveworks-zipkin-service.json
