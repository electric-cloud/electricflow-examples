#!/usr/bin/env bash
# This script runs deployments for a first-time initialiation of the environment.
# We run through several deployments, create several snapshots, and then deploy a bunch of snapshots
# Finally, we do partial deployments.
# Please NOTE: The initial deployments are meant to be partial, and http://jira/browse/CEV-15489 is a 
# related bug in our system.  If we do a sparse deployment, snapshot it, and then deploy the snapshot, 
# the components omitted in the initial deployment are included as LATEST.  It is our belief these
# should remain as not deployed, or that their ec_*-run value is set to 0 (zero)

echo "Perform several deployments to establish inventory for snapshots"
ectool runProcess Stores "Sample App" Deploy --environmentName "Chicago-1" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=1.0" \
--actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=1.0" \
--actualParameter "ec_db-run=1" --actualParameter "ec_db-version=1.0"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "Detroit-2" \
--actualParameter "ec_backend-run=1" --actualParameter "ec_backend-version=1.1" \
--actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=1.1" \
--actualParameter "ec_db-run=1" --actualParameter "ec_db-version=1.1"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "Philadelphia-3" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=2.0" \
--actualParameter "ec_web2-service-run=1" --actualParameter "ec_web2-service-version=2.0" \
--actualParameter "ec_db-run=1" --actualParameter "ec_db-version=2.0"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "San Jose-4" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=2.1" \
--actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=2.1" \
--actualParameter "ec_backend-run=1" --actualParameter "ec_backend-version=2.1"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "Austin-5" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=2.2" \
--actualParameter "ec_backend-run=1" --actualParameter "ec_backend-version=2.2"
sleep 20

echo "create several snapshots for the different versions"
ectool createSnapshot "Stores" "Sample App" "cx-1.0" --environmentName "Chicago-1"
ectool createSnapshot "Stores" "Sample App" "cx-1.1" --environmentName "Detroit-2"
ectool createSnapshot "Stores" "Sample App" "cx-2.0" --environmentName "Philadelphia-3"
ectool createSnapshot "Stores" "Sample App" "cx-2.1" --environmentName "San Jose-4"
ectool createSnapshot "Stores" "Sample App" "cx-2.2" --environmentName "Austin-5"

echo "Deploy snapshots to several environments in two regions"
sleep 20
ectool runProcess Stores "Sample App" Deploy --environmentName "New York-6" --snapshotName "cx-1.1"
sleep 20
ectool runProcess Stores "Sample App" Deploy --environmentName "St. Louis-7" --snapshotName "cx-1.1"
sleep 20
ectool runProcess Stores "Sample App" Deploy --environmentName "Miami-8" --snapshotName "cx-1.1"
sleep 20
 

echo "perform different sparse deployments"
ectool runProcess Stores "Sample App" Deploy --environmentName "Chicago-1" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=1.0" \
--actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=1.1" \
--actualParameter "ec_db-run=1" --actualParameter "ec_db-version=2.0"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "Detroit-2" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=1.1" \
--actualParameter "ec_web2-run=1" --actualParameter "ec_web2-version=1.1" \
--actualParameter "ec_backend-run=1" --actualParameter "ec_backend-version=2.0"
sleep 20

ectool runProcess Stores "Sample App" Deploy --environmentName "Miami-8" \
--actualParameter "ec_web1-run=1" --actualParameter "ec_web1-version=2.0" \
--actualParameter "ec_web2-service-run=1" --actualParameter "ec_web2-service-version=2.1" \
--actualParameter "ec_db-run=1" --actualParameter "ec_db-version=2.2"
sleep 20
