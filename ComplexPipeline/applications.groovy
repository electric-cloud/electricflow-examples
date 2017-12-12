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
                        println "       myComponent is $myComponent.name, $myComponent.groupId:$myComponent.artifactId"

                        component myComponent.name, pluginName: null, {
                            description = myComponent.description
                            pluginKey = 'EC-Artifact'
                            reference = '0'

                            property 'ec_content_details', {
                                property 'artifactName', value: "$myComponent.groupId:$myComponent.artifactId", { expandable = '1' }
                                artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
                                filterList = ''
                                overwrite = 'update'
                                pluginProcedure = 'Retrieve'

                                property 'pluginProjectName', value: 'EC-Artifact', { expandable = '1' }
                                retrieveToDirectory = ''

                                property 'versionRange', value: '', { expandable = '1' }
                            }

                            myComponent.processes?.each { myProcess ->
                                println "         myProcess is $myProcess.name"
                                process myProcess.name, {
                                    myProcess.processSteps?.each { myProcessStep ->
                                        println "           myProcessStep is $myProcessStep.name"
                                    }

                                }
                                myProcess.processDependencies?.each { myProcessDependency ->
                                    println "           myProcessDependency: $myProcessDependency.source -> $myProcessDependency.target"
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
            myApplication.processes?.each { myProcess->
                process myProcess.name, {
                    println "   my(application)Process is $myProcess.name"
                    applicationName = myApplication.name
                    processType = 'DEPLOY'

                    formalParameter 'ec_enforceDependencies', defaultValue: '0', {
                        expansionDeferred = '1'
                        required = '0'
                        type = 'checkbox'
                    }
                    formalParameter 'ec_smartDeployOption', defaultValue: '1', {
                        expansionDeferred = '1'
                        required = '0'
                        type = 'checkbox'
                    }

                    formalParameter 'ec_stageArtifacts', defaultValue: '0', {
                        expansionDeferred = '1'
                        required = '0'
                        type = 'checkbox'
                    }

                    myApplication.applicationTiers?.each { myApplicationTier->
                        myApplicationTier.components?.each {myComponent ->
                            formalParameter 'ec_' + myComponent.name + '-run', defaultValue: '1', {
                                expansionDeferred = '1'
                                label = null
                                orderIndex = null
                                required = '0'
                                type = 'checkbox'
                            }

                            formalParameter 'ec_' + myComponent.name + '-version', defaultValue: "\$[/projects/ComplexPipeline/applications/Two-Tier/components/$myComponent.name/ec_content_details/versionRange]", {
                                expansionDeferred = '1'
                                label = null
                                orderIndex = null
                                required = '0'
                                type = 'entry'
                            }
                        }
                    }

                    myProcess.processSteps?.each { myProcessStep ->
                        processStep myProcessStep.name, {
                            println "       myProcessStep is $myProcessStep.name"
                            applicationTierName = myProcessStep.applicationTierName
                            dependencyJoinType = 'and'
                            errorHandling = 'failProcedure'
                            processStepType = myProcessStep.processStepType
                            subcomponent = myProcessStep.subcomponent
                            subcomponentApplicationName = myApplication.name
                            subcomponentProcess = myProcessStep.subcomponentProcess

                            property 'ec_deploy', {
                                ec_notifierStatus = '0'
                            }

                        }
                    }

                    // Each processStep has a dependency, defined at the Process Level
                    myProcess.processDependencies?.each { myProcessDependency ->
                        processDependency myProcessDependency.source, targetProcessStepName: myProcessDependency.target, {
                            branchType = myProcessDependency.branchType
                        }
                    }
                    property 'ec_deploy', {
                        ec_notifierStatus = '0'
                    }
                }
            }
            property 'ec_deploy', {
                ec_notifierStatus = '0'
            }

        }
    }
}