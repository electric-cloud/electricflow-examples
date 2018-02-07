# efmodel

This directory contains a _framework_ to render your ElectricFlow Models.  As such, we intend for you to use this framework to instantiate your different ElectricFlow models, primarily via JSON files.
 
This project contains handlers to instantiate many of the common features available in ElectricFlow for Applications, Services, Pipelines, and (soon) Releases.  The goal is to create a set of groovy scripts that will process each objet type and add them to ElectricFlow for your own use.  Feel free to fork or contribute to this effort.

This Pipeline contains a little bit of a lot (not a little bit of everything, because there is too much).  You will see the following items complete or _incomplete_:

* Pipeline
  * Parameters
  * Stages
      * Gates - Entry and Exit
        * Approvals with 2 people
      * Tasks
        * Procedures
        * Workflows
        * Manual
        * Plugin
        * Utility
        * Run Conditions
        * Groups
        * Application Processes
        * Skipped Steps
        * Manual Steps
        * Rollback Steps
        * _Retry logic_
* Users
* Examples of Email Configurations
* Examples of Credentials
* Artifacts
* Applications
  * _Snapshots_
* Services
* _Notifications_ - configured to use your preferred email system
* _Hybrid application_ - A blend of both application + microservice deployed to a static and cloud resources
* _Job Template Names_ - Show examples of how to name the jobs more cleverly
* _Release Command Center_
* _Dashboard_ examples
* _Self-Service Catalog_

# Setup

To install the scripts onto your system, start with this command: 

```
./install -i <a directory in your PATH to run the script>
```

The result is that you will have `efmodel.sh` in your PATH for future invocations.  All examples in this directory assume you have installed this software on your Linux-friendly system.

There are some essential files to setup and configuration this system described next.

## Essential Configuration

The script, `efmodel.sh` processes groovy scripts one at a time.  Each time we invoke the `ectool evalDsl` command, we also use a parameters file in JSON format.  The JSON file is the core data-driver in this model.  

In short, when you run this command:

```
efmodel.sh -A -f parameters.json
```
We will build all models into the project you name within the file named parameters.json.
Internally, we run a bunch of `ectool evalDsl` commands for each major object type.
You should review the schema with `efmodel.sh -S` to see what is expected in your JSON file.

## Models

The models are all defined in filenames that match the object types.  This works well because they also align well with the ElectricFlow object model hierarchy.  In other words, the Pipelines and Applications objects are separate, and are also in separate files.

The order by which the models are installed by `efmodel.sh` generally favor the most logical way to define a system.  You start with configuration and infrastructure (resoruces, credentials, artifacts) and then move onto common objects such as environments and applications to be used in higher-level objects such as pipelines.

You are encouraged to look through the installation script `efmodel.sh` to gain a better understanding of this sequence.  The sequence may change, but it is approximately:

* `test.groovy` prints out your object tree.  THIS SCRIPT IS REALLY USEFUL when you start making changes to YOUR model.
* `resources.groovy` sets up the resources we use in the model here.  Essential as a first step.
* `environments.groovy` creates Environments for your model.  You need these for the Application Processes.  Depends on resources.
* `procedures.groovy` creates the procedures used within.  These may be stubbed, or they may contain real implementation.
* `workflows.groovy` creates workflows used within.  Depends on the existence of procedures.
* `applications.groovy` creates application models.  Depends on th existence of Environments, Procedures.
* `services.groovy` creates service models.
* `pipelines.groovy` creates pipeline models.  Depends on everything to exist.

## The ElectricFlow Object Model

The models in this example attempt to map over to this object model.  The JSON input file feeds the respectrive groovy files for each object.

![ElectricFlow Object Model](https://github.com/electric-cloud/electricflow-examples/blob/master/ComplexPipeline/EF-ObjectModel.PNG "ElectricFlow Object Model")


## TODO

* The infrastructure setup of passwords and credentials do not yet fit well within this model. I've noticed the need for custom handling of passwords - which we should not include in JSON.  I believe the dependency on ectool subcommands beyond evalDsl are what make the custom handling necessary, and this type of infrastructure may need to rely on custom logic.

* Add new features as they come in.

* Figure out how to best create the idea of this common folder for scripts, with something like `$MODEL_HOME` that lets me run the setup scripts from directories directly.  I have been running this in Cygwin, and I think the path translation is goofy and causing interference (i.e. `/cygdrive/c/Users/Marco/code/flow-standalone/electricflow-examples/CommonPipeline` doesn't work well.)
