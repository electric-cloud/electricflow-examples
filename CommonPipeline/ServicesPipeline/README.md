# Services Pipeline

This definition relies on the parent project to work.  If all works well, this definition in JSON format is enough to define a Multi-app Pipeline and a set of Releases for those applications.

This definition also relies on a companion directory for WeaveWorks.  We'll re-use the services in that project for this pipeline.  This means references to the WeaveWorks project have to be explicitly set in the scripting.

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
