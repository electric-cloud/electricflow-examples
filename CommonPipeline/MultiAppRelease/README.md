# ComplexPipeline

This definition relies on the parent project to work.  If all works well, this definition in JSON format is enough to define a Complex Pipeline with the items identified below.

I created this project to show a pipeline with examples of all of our entry points, as well as multiple features enabled.  In short, this Pipeline contains a little bit of a lot (not a little bit of everything, because there is too much).  You will see the following items complete or _incomplete_:

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
TO BE DETERMINED:
../install.sh -A
```

