#!/usr/bin/env bash

# Find the root Git workspace directory.
ROOT=$(cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)

PARAMETERSFILE=input-model.json
MYJSONFILE=model.json
PROJECTNAME=ComplexPipeline
GROUPID=com.ec.samples
BASEDIR=.

ALL=1
APPLICATIONS=0
CONFIG=0
ENVIRONMENTS=0
PROCEDURES=0
PIPELINES=0
RELEASES=0
RESOURCES=0
SERVICES=0
TEST=0
WORKFLOWS=0
JSONONLY=0


# Parse command line
# Only a few options in place - see the help for details and update as needed.
while getopts ":ActrepwasljRG:N:P:B:" opt; do
  case $opt in
    A)
        TEST=1
        CONFIG=1
        RESOURCES=1
        ENVIRONMENTS=1
        PROCEDURES=1
        WORKFLOWS=1
        APPLICATIONS=1
        SERVICES=1
        PIPELINES=1
        RELEASES=1
        ;;
    c)
        CONFIG=1
        ;;
    t)
        TEST=1
        ;;
    r)
        RESOURCES=1
        ;;
    e)
        ENVIRONMENTS=1
        ;;
    p)
        PROCEDURES=1
        ;;
    w)
        WORKFLOWS=1
        ;;
    a)
        APPLICATIONS=1
        ;;
    s)
        SERVICES=1
        ;;
    l)
        PIPELINES=1
        ;;
    j)
        JSONONLY=1
        echo "Will only transform JSON file"
        ;;
    R)
        RELEASES=1
        ;;
    N)
        PROJECTNAME=$OPTARG
        echo "New project name is $PROJECTNAME"
        ;;
    P)
        PARAMETERSFILE=$OPTARG
        echo "new parameters file is $PARAMETERSFILE"
        ;;
    G)
        GROUPID=$OPTARG
        echo "new groupId is $GROUPID"
        ;;
    B)
        BASEDIR=$OPTARG
        echo "new baseDir is $BASEDIR"
        ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage:" >&2
      echo "$0 [-A] [-c] [-t] [-r] [-e] [-p] [-w] [-a] [-s] [-p] [-R] [-N <PROJECT NAME>] [-G <GROUPID>] [-P <PARAMETERSFILE>] [-B <BASE DIR>]" >&2
      echo "  -A Do everything"
      echo "  -t run unit tests on the model.  No objects are modified"
      echo "  -c run the configuration"
      echo "  -r create resources"
      echo "  -e create environments"
      echo "  -p create procedures"
      echo "  -w create workflows"
      echo "  -a create applications"
      echo "  -s create services"
      echo "  -p create pipelines"
      echo "  -R create releases"
      echo "  -G <GROUPID> specify the groupId in the format of 'com.ec.group.id"
      echo "  -N <PROJECTNAME> specify the name of the project"
      echo "  -P <PARAMETERSFILE> use the named parameters file in JSON format as an input"
      echo "  -B <BASE DIRECTORY> use the specified directory as our base"
      echo "Root is $ROOT"
      exit 1
      ;;
  esac
done



# Replace tokens with real names
sed -e "s/@@PROJECTNAMETOKEN@@/$PROJECTNAME/" $PARAMETERSFILE > $MYJSONFILE

if [ $JSONONLY = "1" ]; then
    echo "Tranformed JSON file only."
    exit 0
fi



if [ $CONFIG = "1" ]; then
    echo "Add configurations via DSL"
    ectool evalDsl --dslFile $BASEDIR/configuration.groovy --parametersFile $MYJSONFILE

    echo "Add passwords to configurations"
    ectool modifyEmailConfig "gmail" --mailUserPassword < passwords/password-email-gmail.txt
    ectool modifyEmailConfig "test" --mailUserPassword < passwords/password-email-test.txt

    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Artifactory" --password < $BASEDIR/passwords/password-credential-artifactory.txt
    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Jenkins" --password < $BASEDIR/passwords/password-credential-jenkins.txt

    ectool modifyUser "marco" --password "marco"  --sessionPassword < $BASEDIR/passwords/password-user-admin.txt
    ectool modifyUser "seymour" --password "seymour"  --sessionPassword < $BASEDIR/passwords/password-user-admin.txt

    set +e
    echo "Create artifacts for this example."

    #NOTE: Each artifact is published separately because we need to pay attention to the name of the file.
    for artifactId in "web1" "web2" "db" "mobile" "mainframe" ; do
        echo "Creating $GROUPID:$artifactId "
        echo "Creating $GROUPID:$artifactId " > $BASEDIR/artifacts/$artifactId.txt
        ectool createArtifact "$GROUPID" "$artifactId" --description "simple text file."

        for version in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
            echo "Publishing $GROUPID:$artifactId version $version..."
            ectool --silent publishArtifactVersion \
                --version $version --artifactName $GROUPID:$artifactId \
                --fromDirectory . \
                --includePatterns $BASEDIR/artifacts/$artifactId.txt
            done
    done
    set -e
fi

# Run some essential tests to show the system works.  Good hygiene.
if [ $TEST = "1" ] ; then
    echo "######### TESTING #########"
    ectool evalDsl --dslFile test.groovy --parametersFile $MYJSONFILE
fi

# Create the resources needed in this project.
if [ $RESOURCES = "1" ] ; then
    echo "######### CREATING RESOURCES #########"
    ectool evalDsl --dslFile resources.groovy --parametersFile $MYJSONFILE
fi

# Create the environments needed in this project.
if [ $ENVIRONMENTS = "1" ] ; then
    echo "######### CREATING ENVIRONMENTS #########"
    ectool evalDsl --dslFile environments.groovy --parametersFile $MYJSONFILE
fi

# all of the helper procedures are defined here.  This includes stubs.  We'll refer to procedures by name.
if [ $PROCEDURES = "1" ] ; then
    echo "######### CREATING PROCEDURES #########"
    ectool evalDsl --dslFile procedures.groovy --parametersFile $MYJSONFILE
fi

# If you have workflows, define them here.  NOTE: Helper procedures should be already defined.
if [ $WORKFLOWS = "1" ] ; then
    echo "######### CREATING WORKFLOWS #########"
    ectool evalDsl --dslFile workflows.groovy --parametersFile $MYJSONFILE
fi

# Your application model uses existing artifacts, procedures, and environments.
if [ $APPLICATIONS = "1" ] ; then
    echo "######### CREATING APPLICATIONS #########"
    ectool evalDsl --dslFile applications.groovy --parametersFile $MYJSONFILE
fi

# Your services model uses existing procedures
if [ $SERVICES = "1" ] ; then
    echo "######### CREATING SERVICES #########"
    ectool evalDsl --dslFile services.groovy --parametersFile $MYJSONFILE
fi

# The pipelines tie most things together.  One of the last steps.
if [ $PIPELINES = "1" ] ; then
    echo "######### CREATING PIPELINES #########"
    ectool evalDsl --dslFile pipelines.groovy --parametersFile $MYJSONFILE
fi

# When we're ready, do the same for releases.
#if [ $RELEASES = "1" ] ; then
#    echo "######### TESTING #########"
#   ectool evalDsl --dslFile releases.groovy --parametersFile $MYJSONFILE
#fi
