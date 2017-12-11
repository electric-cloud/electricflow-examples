def myProject = args.project

// Application Models have these objects:
// Project -
//      Applications -
//          ApplicationTiers
//              Components
//                  Processes
//                      Process Steps
//          Processes
//              Process Steps
//          TierMaps
//              TierMappings
// The structure in this groovy model will match this overall object model.


project myProject.name, {
    description = myProject.description
    myProject.applications.each { myApplication ->
        application myApplication.name, {
            println "Application $myApplication.name"
            description = myApplication.description

            myApplication.applicationTiers?.each { myApplicationTier ->
                applicationTier myApplicationTier.name, {
                    println "  ApplicationTier $myApplicationTier.name"
                    applicationName = myApplication.name
                    projectName = myProject.name

                    myApplicationTier.components?.each { myComponent ->
                        println "    Component $myComponent.name"

                        component myComponent.name, pluginName: null, {
                            applicationName = myApplication.name
                            description = "$myComponent.groupId : $myComponent.artifactId"
                            // TODO : For now, we handle components directly defined.
                            // We do not yet handle Master Components, or those components defined from them.
                            // The difference is in how we specify the pluginName / pluginKey
                            pluginKey = 'EC-Artifact'
                            //pluginKey = null
                            reference = "1"
                            sourceComponentName = null
                            sourceProjectName = null

                            myComponent.processes?.each { myProcess ->
                                println "       Process $myProcess.name"
                                process myProcess.name, {
                                    description = myProcess.name
                                    processType = myProcess.processType
                                    myProcess.processSteps?.each { myProcessStep ->
                                        println "           ProcessStep $myProcessStep.name"
                                        processStep myProcessStep.name, {
                                            actualParameter = myProcessStep.actualParameters?.collectEntries { aParam ->
                                                [
                                                        (aParam.name): aParam.value,
                                                ]
                                            }
                                            description = myProcessStep.description
                                            // Here, we will use the name of the process Step to guide us.  This forms a
                                            // Dependency on the name and the behavior, which we should optimize.

                                            // These should become JSON-level details
                                            alwaysRun = '0'
                                            dependencyJoinType = 'and'
                                            errorHandling = 'abortJob'

                                            // There are a number of null assignments not included here.  These are either
                                            // utilized rarely, or are for specific types of process steps.

                                            // In the following switch statements, we have limited custom information.
                                            // The reason is simple - we have a demonstration system and we want to make
                                            // the JSON model the "data entry" screen for our automations.
                                            // Here, we're leveraging the names of the step names to

                                            // TODO: Notice how we are not doing the switch statement on the processStepType,
                                            // and the consequence is that we have duplicate entries for some fields

                                            switch (myProcessStep.name) {
                                                case ~/acquire artifact/:
                                                    println "Handling acquire case"
                                                    processStepType = myProcessStep.processStepType
                                                    // TODO : We have an EF-centric view of artifact repositories.  When we
                                                    // add Artifactory, we'll need to adjust this code.
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    break
                                                case ~/deployit/:
                                                    println "Handling deploy"
                                                    processStepType = myProcessStep.processStepType
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    break
                                                case ~/removeit/:
                                                    println "Handling undeploy"
                                                    processStepType = myProcessStep.processStepType
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    break
                                                default:
                                                    break
                                            }
                                        }
                                    }

                                    // Each processStep has a dependency, defined at the Process Level
                                    myProcess.processDependencies?.each { myProcessDependency ->
                                        processDependency myProcessDependency.source, targetProcessStepName: myProcessDependency.target, {
                                            branchCondition = null
                                            branchConditionName = null
                                            branchConditionType = null
                                            branchType = myProcessDependency.branchType
                                        }
                                    }
                                }
                            }

                            // These details are for each Component.
                            property 'ec_content_details', {
                                property 'artifactName', value: "$myComponent.groupId:$myComponent.artifactId", {
                                    expandable = '1'
                                }
                                artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
                                filterList = ''
                                overwrite = 'update'
                                pluginProcedure = 'Retrieve'

                                property 'pluginProjectName', value: 'EC-Artifact', { expandable = '1' }
                                retrieveToDirectory = ''
                                property 'versionRange', value: '', { expandable = '1' }
                            }
                        }
                    }
                }
            }
            // Define Application Processes
            myApplication.processesq?.each { myProcess->

            }
        }
    }
}