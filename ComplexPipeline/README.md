# ComplexPipeline

This project exists because I found people wanted to see a Pipeline with multiple features enabled.  In short, this Pipeline contains a little bit of a lot (not a little bit of everything, because there is too much).  You will see the following items complete or _incomplete_:

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

# Setup

To setup, start with these commands:

```
ectool login admin <your password>
./install.sh -a
```

To see the list of arguments, run `install.sh -?` to see something like this:

```
Usage:
./install.sh [-A] [-c] [-t] [-r] [-e] [-p] [-w] [-a] [-s] [-p] [-R] [-N <PROJECT NAME>] [-G <GROUPID>] [-P <PARAMETERSFILE>]
  -A Do everything
  -t run unit tests on the model.  No objects are modified
  -c run the configuration
  -r create resources
  -e create environments
  -p create procedures
  -w create workflows
  -a create applications
  -s create services
  -p create pipelines
  -R create releases
  -G <GROUPID> specify the groupId in the format of 'com.ec.group.id
  -N <PROJECTNAME> specify the name of the project
  -P <PARAMETERSFILE> use the named parameters file in JSON format as an input
  ```

There are some essential files to setup and configuration this system described next.

## Essential Configuration

The installation script, `install.sh` processes groovy scripts one at a time.  Each time we invoke the `ectool evalDsl` command, we also use a parameters file in JSON format.  The JSON file is the core data-driver in this model.  We start with the template file named `input-model.json` and transform it to suit your personality along these dimensions:

* We replace the name of the project with the command-line value you provide or the built-in default.
* We generate resources and environments that use the Project Name as the base.  This ensures some uniqueness in your setup.
* We create artifacts using the groupId you supply at the command-line or the built-in default.

In short, when you run this command:

```
./install.sh -A -N "My Project" -G "com.mycompany.division"
```
We will build your models into a new project named, "My Project."  Internally, we run a bunch of `ectool evalDsl` commands and the parametersFile is rendered as `model.json` by default.

Password files are assumed to reside in the passwords subdirectory.  At this time, the passwords are simply, "mypass" and you should not expect them to work.

## Models

The models are all defined in filenames that match the object types.  This works well because they also align well with the ElectricFlow object model hierarchy.  In other words, the Pipelines and Applications objects are separate, and are also in separate files.

The order by which the models are installed by `install.sh` generally favor the most logical way to define a system.  You start with configuration and infrastructure (resoruces, credentials, artifacts) and then move onto common objects such as environments and applications to be used in higher-level objects such as pipelines.

You are encouraged to look through the installation script `install.sh` to gain a better understanding of this sequence.  The sequence may change, but it is approximately:

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


