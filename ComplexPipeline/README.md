# ComplexPipeline

This project exists because I found people wanted to see a Pipeline with multiple features enabled.  In short, this Pipeline contains a little bit of a lot (not a little bit of everything, because there is too much).  You will see the following items complete or _incomplete_:

* Pipeline
  * _Parameters_
  * Stages
      * Gates - Entry and Exit
        * Approvals with 2 people
      * Tasks
        * Procedures
        * Workflows
        * Manual
        * Plugin
        * Utility
        * _Application Processes_
        * _Skipped Steps_
        * _Run Conditions_
        * _Groups_
        * _Retry logic_
* Users
* Examples of Email Configurations
* Examples of Credentials
* _Applications_
  * _Snapshots_
* _Services_
* _Artifacts_

# Setup

There are two sets of scripts to set up the system.  

## Essential Configuration

The first sets up essential configuration, and are bundled in these files:
```
install-config.sh
configuration.groovy
configuration.json
```

These setup files are roughly one-time operations.  For your system, you need to make sure you modify the file `configuration.json` with details specific to you, and update the password files in `passwords/*` with information specific to you.  NOTE: The included password files are not likely to let you do anything, because they all use the value "mypass."

## Models

The next set of files setup the models within.  The data file `complexPipeline.json` contains all of the data-input for the models.  The bulk of the remaining `*.groovy` scripts contain the definitions of the different object types in this model.  The best place to look for a top-level view is the installation script, which you invoke with:

```
ectool login <your username> <your password>
./install.sh
```

The sequence may change, but it is roughly:

* `test.groovy` prints out your object tree.  THIS SCRIPT IS REALLY USEFUL when you start making changes to YOUR model.
* `resources.groovy` sets up the resources we use in the model here.  Essential as a first step.
* `environments.groovy` creates Environments for your model.  You need these for the Application Processes.  Depends on resources.
* `procedures.groovy` creates the procedures used within.  These may be stubbed, or they may contain real implementation.
* `workflows.groovy` creates workflows used within.  Depends on the existence of procedures.
* `applications.groovy` creates application models.  Depends on th existence of Environments, Procedures.
* `pipelines.groovy` creates pipeline models.  Depends on everything to exist.


