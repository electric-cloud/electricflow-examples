# Multi Environment Inventory Example

This example uses the CommonPipeline groovy models to implement a multi-application release pipeline in ElectricFlow.

# Instructions.

Once you checkout, the installation should be easy:

```
ectool login <your username> <your password>
./install.sh```

Within that install.sh file, we work through a couple of operations to setup the environment to render your models.

First, we process the file `input-model.json` by substituting tokens with the project name and groupId.  This lets us specify the personality of the project for an example that better fits your team.  Other substitutions should follow that example.

Next, we setup infrastructure - artifacts, resources, passwords included.  Configure the `install-config.sh` file as necessary to match your infrastructure needs.

Finally, run through the rendering of the model with the parent-level models.


# Side-effects

When you run the installation script, you will have these observable side-effects assuming defaults

* A new project named "MultiApplicationRelease" that contains the Application, Environment, Pipeline, Release models.  If you delete the "MultiApplicationRelease" project, all of these definitions will also be deleted.
* Resources, email configurations, and some other objects in the install-config.sh sequence will exist within the system.  At present, you have to manually delete these items as they don't belong to a project.  There is no current procedure or step to delete these, and the resources will linger.  Fortunately, you can search for the common text in the description that includes the project name.

# Additional Details

The applications within are placeholders and consist of text files with plain-old-text.  These files are not really deployed to a server, and instead are manipulated with plain-old linux commands (mv, cp, cat) to do show something happening.  The reason for this simple model is to highlight the features of ElectricFlow with examples that will succeed quickly.

The assumption is that your installation includes the DevOps Insight Server to show metrics.  We publish some real and simulated data for this example.

# TODO list

No project is complete without a list of what is probably incomplete.  :)  Here is our list:

* Add a service to round-out the example.
* Address the various parent-level TODOs already documented in-line.  See the parent directory for embedded TODO statements.
