# SingleApplication Deployment

This example shows how to use DSL to model an application Deploy process.  The primary features of this example are:

* A one-component deployment
* A two-component deployment
* These two applications are given separate names
* A shared set of environment destinations (DEV/QA/PROD)
* The deploy process is deliberately simple - an echo statement.  No files are actually transferred.
* The artifacts are single text files with one sentence.  Several versions of each file are present.

TODOS:
* Extend this model in other examples for the following (if done, show the link here):
  * Create a snapshot
  * Perform a rollback
  * Create an undeploy operation
  * Other examples to extend its functionality in a pipeline/release/etc.
  * Use an argument to define the project name, shift away from "Examples"
  