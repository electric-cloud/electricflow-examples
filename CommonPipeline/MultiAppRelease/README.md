# Multi Application Release

This definition relies on the parent project to work.  If all works well, this definition in JSON format is enough to define a Multi-app Pipeline and a set of Releases for those applications.

The three applications are named Web Site, Mobile, and Mainframe.
The destination environments are dev, qa, stage, pre-prod, and prod
The development Pipeline publishes the three artifacts to dev/qa/stage and does some additional operations to show a prototypical dev-like pipeline.
The Release Pipeline publishes to pre-prod and prod, and the automation sets up three releases for three months.  They are presently coded in for January, February, March, and pick the environments and configure one deployment to enable a manual check.


# Setup

To setup, start with these commands:

```
ectool login admin <your password>
./install.sh
```

To seed the environment with some deployments, run these commands:

```
./deploy.sh
```

# TODO

* Add a link to the source artifact, as an example of showing traceability.
* Add a step to add values to DevOps Insight Server.  Right now, we automatically add data to Deployments.  Add more for other areas.
* Update Dev Pipeline to use the snapshot published in the dev Stage into qa and stage.
