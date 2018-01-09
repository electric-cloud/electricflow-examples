#!/usr/bin/env bash

PROJECTNAME=MultiApplicationRelease

# Parse command line
# Only a few options in place - see the help for details and update as needed.
while getopts ":crP:f:G:" opt; do
  case $opt in
    P)
        PROJECTNAME=$OPTARG
        echo "New project name is $PROJECTNAME"
        ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage:" >&2
      echo "$0 [-P <PROJECT NAME>]" >&2
      echo "  -P <PROJECTNAME> specify the name of the project"
      exit 1
      ;;
  esac
done

for version in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
    echo "Deploy version $version to Web Site"
    ectool runProcess $PROJECTNAME "Web Site" "Deploy Application" --environmentName "dev" \
    --actualParameter "ec_smartDeployOption=0" \
    --actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=$version" \
    --actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=$version" \
    --actualParameter "ec_db-run=1" --actualParameter "ec_db-version=$version"
    sleep 10

    echo "Deploy version $version to Mobile"
    ectool runProcess $PROJECTNAME "Mobile" "Deploy Application" --environmentName "dev" \
    --actualParameter "ec_smartDeployOption=0" \
    --actualParameter "ec_mobile-run=1" --actualParameter "ec_mobile-version=$version"
    sleep 10

    echo "Deploy version $version to Mainframe"
    ectool runProcess $PROJECTNAME "Mainframe" "Deploy Application" --environmentName "dev" \
    --actualParameter "ec_smartDeployOption=0" \
    --actualParameter "ec_mainframe-run=1" --actualParameter "ec_mainframe-version=$version"
    sleep 10

    echo "Create snapshots for Web Site version $version"
    ectool deleteSnapshot $PROJECTNAME "ws-$version" --applicationName "Web Site"
    ectool createSnapshot $PROJECTNAME "Web Site" "ws-$version" --environmentName "dev"
    echo "Create snapshots for Mobile version $version"
    ectool deleteSnapshot $PROJECTNAME "mo-$version" --applicationName "Mobile"
    ectool createSnapshot $PROJECTNAME "Mobile" "mo-$version" --environmentName "dev"
    echo "Create snapshots for Mainframe version $version"
    ectool deleteSnapshot $PROJECTNAME "mf-$version" --applicationName "Mainframe"
    ectool createSnapshot $PROJECTNAME "Mainframe" "mf-$version" --environmentName "dev"
done

echo "Deploy Web Site snapshot ws-2.0 to qa"
ectool runProcess $PROJECTNAME "Web Site" "Deploy Application" --environmentName "qa" --snapshotName "ws-2.0"
sleep 15
echo "Deploy Web Site snapshot ws-1.1 to stage"
ectool runProcess $PROJECTNAME "Web Site" "Deploy Application" --environmentName "stage" --snapshotName "ws-1.1"
sleep 15
echo "Deploy Mobile snapshot mo-2.0 to qa"
ectool runProcess $PROJECTNAME "Mobile" "Deploy Application" --environmentName "qa" --snapshotName "mo-2.0"
sleep 15
echo "Deploy Mobile snapshot mo-1.1 to stage"
ectool runProcess $PROJECTNAME "Mobile" "Deploy Application" --environmentName "stage" --snapshotName "mo-1.1"
sleep 15
echo "Deploy Mainframe snapshot mf-2.0 to qa"
ectool runProcess $PROJECTNAME "Mainframe" "Deploy Application" --environmentName "qa" --snapshotName "mf-2.0"
sleep 15
echo "Deploy Mainframe snapshot mf-1.1 to stage"
ectool runProcess $PROJECTNAME "Mainframe" "Deploy Application" --environmentName "stage" --snapshotName "mf-1.1"
