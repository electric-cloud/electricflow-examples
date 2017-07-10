# electricflow-examples
Different examples of how to use features in ElectricFlow.  


# Setting up examples

The requirement is that you are able to access the CLI and have signed on with ectool with permissions to change the system.

* logon with "ectool login _yourusername_ _yourpassword_"
* You have administrative rights and your account lets you create projects, resources, and other objects.  
* This is not a production system and you are working on a system where you can make changes and mistakes.
* You are able to create files on the local file system.


# Guidelines

Keep these guidelines in mind:
* Each example should reside in a separate folder under /examples, named something appropriately.  As an example, the first example is named "SingleApplication" to convey it is about a single application.  I suppose it could be called something like "SingleApplicationDeploy."
* The example folder should contain a top-level "install.sh" script that installs the example.
  * Let's assume plain old "./instal.sh" does a brand-new installation of the application.
  * Re-invoking "./install.sh" should not produce an error.
  * An dependent files should reside in this folder only.
  * If you download from external sources, download them to the root-level (i.e. /vagrant) folder named *downloads*.  Assume you can delete the contents of this folder and restart.
* Document your application process in a suitable README.md at the top-level

The goals are:

* Simplicity.  
  * Each item or widget should be managed managed separately, and not in a large uber-setup script.  This means that if you have an example to showcase rollback, it can (and probably should) be separate from other examples.
  * Each item or widget follows the same setup pattern - a self-contained directory with a setup script.  Nothing too fancy and not too many levels of indirection.

* Focused Examples
  * Create standalone examples to show simple ideas.  A deploy, a deploy with parallel steps, notification, rollback, rolling deploy should all be separate items.
  * Think of this as an environment to answer "Ask" questions.  For example, "How do I create a notification on a pipeline stage?"
  * Also think of this as a way to showcase individual plugin eamples.  For example, if you test a plugin integration for Amazon, create a subdirectory here to showcase the problem.
  * In demonstrations, consider these contributions to be side-examples.  "Here is an example of a rolling deploy using a simple application."  The example is about rolling deploy and not about Tomcat, a plugin, notifications, or something else in particular.  


# TODOs
* Consider separating the projects to a public repository.
* For each project, consider writing up a blog to describe how it works.
* Need to figure out how to specify dependencies.  For now, dependencies are just not handled.  One example - using the same artifacts.
* Where possible, we should be open to running with contemporary patches and update (i.e. apt-get update)
* Figure out if it is worth including arguments to support *c*lean, *r*einstall, *i*nstall.
* Consider adding support for each installation to specify *p*roject, or where the example will be installed.  This will be a paramter to install.sh.


# Demonstration ideas - please Note: each item should be done as a unit operation, not a total end-to-end solution.  We're looking for Lego blocks here.

* Add a notification to a failed deployment via DSL.
* Add a notification to a pipeline stage via DSL.
* Show how we can trigger an event from a GitHub checking (github.com)
* Show how we can spin-up a cloud-based instance from EF
  * Consider Azure, Google Cloud, AWS as examples
  * Use the instance as an endpoint
  * Or, use the instance as an agent
