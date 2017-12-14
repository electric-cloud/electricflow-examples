#!/usr/bin/env bash

set -e
PARAMETERSFILE=complexPipeline.json
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

PROJECTNAME=ComplexPipeline
GROUPID=com.ec.samples

# Parse command line
# Only a few options in place - see the help for details and update as needed.
while getopts ":actrepwaspRN:P:" opt; do
  case $opt in
    a)
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
    p)
        PIPELINES=1
        ;;
    R)
        RELEASES=1
        ;;
    N)
        PROJECTNAME=$OPTARG
        ;;
    P)
        PARAMETERSFILE=$OPTARG
        ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage:" >&2
      echo "$0 [-a] [-c] [-t] [-r] [-e] [-p] [-w] [-a] [-s] [-p] [-R] [-P <PARAMETERSFILE>]" >&2
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
      echo "  -P <PARAMETERSFILE> use the named parameters file in JSON format"
      exit 1
      ;;
  esac
done

if [ $CONFIG = "1" ]; then
    echo "Add configurations via DSL"
    ectool evalDsl --dslFile configuration.groovy --parametersFile configuration.json

    echo "Add passwords to configurations"
    ectool modifyEmailConfig "gmail" --mailUserPassword < passwords/password-email-gmail.txt
    ectool modifyEmailConfig "test" --mailUserPassword < passwords/password-email-test.txt

    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Artifactory" --password < passwords/password-credential-artifactory.txt
    ectool modifyCredential --projectName "$PROJECTNAME" --credentialName "Jenkins" --password < passwords/password-credential-jenkins.txt

    ectool modifyUser "marco" --password "marco"  --sessionPassword < passwords/password-user-admin.txt
    ectool modifyUser "seymour" --password "seymour"  --sessionPassword < passwords/password-user-admin.txt

    ectool createCredential --projectName "$PROJECTNAME" \
    --credentialName "artifactory" --userName "admin" --password "artifactorypassword"

    echo "Create artifacts for this example."

    #NOTE: Each artifact is published separately because we need to pay attention to the name of the file.
    for artifactId in "web1" "web2" "db" "mobile" "mainframe" ; do
        echo "Creating $GROUPID:$artifactId "
        echo "Creating $GROUPID:$artifactId " > artifacts/$artifactId.txt
        ectool createArtifact "$GROUPID" "$artifactId" --description "simple text file."

        for version in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
            echo "Publishing com.ec.samples:$artifactId version $version..."
            ectool --silent publishArtifactVersion \
                --version $version --artifactName $GROUPID:$artifactId \
                --fromDirectory . \
                --includePatterns artifacts/$artifactId.txt
            done
    done
fi

# Run some essential tests to show the system works.  Good hygiene.
if [ $TEST = "1" ] ; then
    ectool evalDsl --dslFile test.groovy --parametersFile $PARAMETERSFILE
fi

# Create the resources needed in this project.
if [ $RESOURCES = "1" ] ; then
    ectool evalDsl --dslFile resources.groovy --parametersFile $PARAMETERSFILE
fi

# Create the environments needed in this project.
if [ $ENVIRONMENTS = "1" ] ; then
    ectool evalDsl --dslFile environments.groovy --parametersFile $PARAMETERSFILE
fi

# all of the helper procedures are defined here.  This includes stubs.  We'll refer to procedures by name.
if [ $PROCEDURES = "1" ] ; then
    ectool evalDsl --dslFile procedures.groovy --parametersFile $PARAMETERSFILE
fi

# If you have workflows, define them here.  NOTE: Helper procedures should be already defined.
if [ $WORKFLOWS = "1" ] ; then
    ectool evalDsl --dslFile workflows.groovy --parametersFile $PARAMETERSFILE
fi

# Your application model uses existing artifacts, procedures, and environments.
if [ $APPLICATIONS = "1" ] ; then
    ectool evalDsl --dslFile applications.groovy --parametersFile $PARAMETERSFILE
fi

# Your services model uses existing procedures
if [ $SERVICES = "1" ] ; then
    ectool evalDsl --dslFile services.groovy --parametersFile $PARAMETERSFILE
fi

# The pipelines tie most things together.  One of the last steps.
if [ $PIPELINES = "1" ] ; then
    ectool evalDsl --dslFile pipelines.groovy --parametersFile $PARAMETERSFILE
fi

# When we're ready, do the same for releases.
#if [ $RELEASES = "1" ] ; then
#   ectool evalDsl --dslFile releases.groovy --parametersFile $PARAMETERSFILE
#fi
