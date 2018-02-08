#!/usr/bin/env bash

PROJECTNAME=CommonPipeline
GROUPID=com.ec.samples
MYJSONFILE=model.json
CONFIG=0

# Parse command line
# Only a few options in place - see the help for details and update as needed.
while getopts ":crP:f:G:" opt; do
  case $opt in
    c)
        CONFIG=1
        ;;
    P)
        PROJECTNAME=$OPTARG
        echo "New project name is $PROJECTNAME"
        ;;
    f)
        PARAMETERSFILE=$OPTARG
        echo "new parameters file is $PARAMETERSFILE"
        ;;
    G)
        GROUPID=$OPTARG
        echo "new groupId is $GROUPID"
        ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage:" >&2
      echo "$0 [-c] [-P <PROJECT NAME>] [-G <GROUPID>] [-f <PARAMETERSFILE>]" >&2
      echo "  -A Do everything"
      echo "  -c run the configuration"
      echo "  -G <GROUPID> specify the groupId in the format of 'com.ec.group.id"
      echo "  -P <PROJECTNAME> specify the name of the project"
      echo "  -f <PARAMETERSFILE> use the named parameters file in JSON format as an input"
      exit 1
      ;;
  esac
done

# This is an example of a script to setup a configuration with the following items:
# - email configurations
# - Credentials
# - Users
# - Artifacts

if [ $CONFIG = "1" ] ; then
    echo "Add configurations via DSL"
    efmodel.sh -c -f $MYJSONFILE -P $PROJECTNAME -G $GROUPID

    echo "Add passwords to configurations"
    ectool modifyEmailConfig "gmail" --mailUserPassword < passwords/password-email-gmail.txt
    ectool modifyEmailConfig "test" --mailUserPassword < passwords/password-email-test.txt

    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Artifactory" --password < passwords/password-credential-artifactory.txt
    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Jenkins" --password < passwords/password-credential-jenkins.txt

    ectool modifyUser "marco" --password "marco"  --sessionPassword < passwords/password-user-admin.txt
    ectool modifyUser "seymour" --password "seymour"  --sessionPassword < passwords/password-user-admin.txt

    set +e
    echo "Create artifacts for this example."

    #NOTE: Each artifact is published separately because we need to pay attention to the name of the file.
    for artifactId in "web1" "web2" "db" "mobile" "mainframe" ; do
        echo "Creating $GROUPID:$artifactId "
        echo "Creating $GROUPID:$artifactId " > artifacts/$artifactId.txt
        ectool createArtifact "$GROUPID" "$artifactId" --description "simple text file."

        for version in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
            echo "Publishing $GROUPID:$artifactId version $version..."
            ectool --silent publishArtifactVersion \
                --version $version --artifactName $GROUPID:$artifactId \
                --fromDirectory . \
                --includePatterns artifacts/$artifactId.txt
            done
    done
    set -e
fi

