# Multi Environment Inventory Example

This example does a few things to create artifacts and deploy them to multiple environments.  The results are then presented in a custom screen via the Electric Cloud Unplug utility, made available elsewhere in this repository.

See the bottom of this file for a list of side-effects.

# Instructions.

Once you checkout, the installation should be easy:

```./install.sh```

Within that install.sh file, you will notice we cycle through the files contained in this directory.  These are:

* artifacts.sh - simple script creates some text files and addes them to the ElectricFlow Artifact Repository as multiple versions.
* data.json - primary source of data for this project, with lists of resources, artifacts, pipeline stages
* resources.groovy - groovy file contains ElectricFlow DSL and iterates over the resources in the data file and creates them.
* model.groovy - groovy file contains ElectricFlow DSL and iterates over the different objects and creates Applications, Pipelines, and more.
* pipeline.groovy - groovy file contains the definition of the pipeline object in this project.
* deploy.sh - run this script (once) to create several deployments to populate your environment.



# Side-effects

When you run the installation script, you will have these observable side-effects

* A new project entitled "Stores" that contains ElectricFlow Application, Environment, Pipeline Models, and Snapshots.  If you delete the "Stores" project, all these definitions will also be deleted.
* New resources used and assigned to the environments.  There is no current procedure or step to delete these, and the resources will linger.  Fortunately, you can search for the common text in the description.
* The Unplug plugin will be installed and setup in your environment.  There is no uninstall option here.
* Your default view in the platform screens (i.e. Electric Commander) will be changed to the "Stores" view as governed by the command:

```ectool setProperty "/server/ec_ui/defaultView" "Stores"```

We don't store the name of the previous view, which is ususally "default."
