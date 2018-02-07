#!/usr/bin/env bash

PROJECTNAME=MultiApplicationRelease
RELEASENAME="January Release"
APPLICATIONNAME="Web Site"
MYJSONFILE=release-data.json

# Parse command line
# Only a few options in place - see the help for details and update as needed.
while getopts ":jf:P:" opt; do
  case $opt in
    j)
        JSONONLY=1
        echo "Will only transform JSON file"
        ;;
    P)
        PROJECTNAME=$OPTARG
        echo "New project name is $PROJECTNAME"
        ;;
    f)
        MYJSONFILE=$OPTARG
        echo "new parameters file is $MYJSONFILE"
        ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage:" >&2
      echo "$0 [-j] [-P <PROJECT NAME>] [-f <PARAMETERSFILE>]" >&2
      echo "  -j create JSON files only"
      echo "  -P <PROJECTNAME> specify the name of the project"
      echo "  -f <PARAMETERSFILE> use the named parameters file in JSON format as an input"
      exit 1
      ;;
  esac
done


# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" $MYJSONFILE > $MYJSONFILE.project
sed -e "s/@@RELEASENAMETOKEN@@/$RELEASENAME/" $MYJSONFILE.project> $MYJSONFILE.release
sed -e "s/@@APPLICATIONNAMETOKEN@@/$APPLICATIONNAME/" $MYJSONFILE.release> $MYJSONFILE.application
cp $MYJSONFILE.application input.json

if [ "$JSONONLY" = "1" ]
then
    echo "Tranformed JSON file only."
    exit 0
fi

ectool evalDsl --dslFile populateRCC.groovy --parametersFile input.json --timeout 300000
