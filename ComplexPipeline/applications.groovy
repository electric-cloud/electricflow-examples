def myProject = args.project

project myProject.name, {
    description = myProject.description
    myProject.applications.each { myApplication ->
        application myApplication.name, {
            println "Application $myApplication.name"
            description = myApplication.description

            myApplication.applicationTiers?.each { myApplicationTier->
                applicationTier myApplicationTier.name, {
                    println "  ApplicationTier $myApplicationTier.name"
                    applicationName = myApplication.name
                    projectName = myProject.name

                    myApplicationTier.components?.each { myComponent->
                        println "    Component $myComponent.name"

                        component myComponent.name, pluginName: null, {
                            actualParameter = [
                                    'artifactId': myComponent.name
                            ]
                            applicationName = myApplication.name
                            // TODO : For now, we handle components directly defined.
                            // We do not yet handle Master Components, or those components defined from them.
                            // The difference is in how we specify the pluginName / pluginKey
                            pluginKey = 'EC-Artifact'
                            //pluginKey = null
                            reference = "1"
                            // sourceComponentName = myComponent.name
                            // sourceProjectName = myProject.Name
							
							myComponent.processes?.each { myProcess->
								process myProcess.name, {
								}
							}
							property 'artifactName', value: "$myComponent.groupId:$myComponent.artifactId", {
								expandable = '1'
							}
							artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
							filterList = ''
							overwrite = 'update'
							pluginProcedure = 'Retrieve'

							property 'pluginProjectName', value: 'EC-Artifact', {
								expandable = '1'
							}
							retrieveToDirectory = ''

							property 'versionRange', value: '', {
								expandable = '1'
							}

                        }
                    }
                }

                process 'Deploy', {

                    args.components.each { comp ->
                        formalParameter 'ec_' + comp.appName + '-run', defaultValue: '1', {
                            expansionDeferred = '1'
                            label = null
                            orderIndex = null
                            required = '0'
                            type = 'checkbox'
                        }

                        formalParameter 'ec_' + comp.appName + '-version', defaultValue: '$[/projects/CarMax/components/carmax-app/ec_content_details/versionRange]', {
                            expansionDeferred = '1'
                            label = null
                            orderIndex = null
                            required = '0'
                            type = 'entry'
                        }
                    }

                    formalParameter 'ec_enforceDependencies', defaultValue: '0', {
                        expansionDeferred = '1'
                        label = null
                        orderIndex = null
                        required = '0'
                        type = 'checkbox'
                    }

                    formalParameter 'ec_smartDeployOption', defaultValue: '1', {
                        expansionDeferred = '1'
                        label = null
                        orderIndex = null
                        required = '0'
                        type = 'checkbox'
                    }

                    formalParameter 'ec_stageArtifacts', defaultValue: '0', {
                        expansionDeferred = '1'
                        label = null
                        orderIndex = null
                        required = '0'
                        type = 'checkbox'
                    }

                    args.components.each { comp ->
                        processStep 'deploy ' + comp.appName, {
                            afterLastRetry = null
                            alwaysRun = '0'
                            applicationTierName = args.appTier
                            componentRollback = null
                            dependencyJoinType = 'and'
                            errorHandling = 'abortJob'
                            instruction = null
                            notificationTemplate = null
                            processStepType = 'process'
                            retryCount = null
                            retryInterval = null
                            retryType = null
                            rollbackSnapshot = null
                            rollbackType = null
                            rollbackUndeployProcess = null
                            skipRollbackIfUndeployFails = null
                            smartRollback = null
                            subcomponent = comp.appName
                            subcomponentApplicationName = args.appName
                            subcomponentProcess = 'Deploy'
                            subprocedure = null
                            subproject = null
                            subservice = null
                            timeLimitUnits = null
                            workspaceName = null

                            property 'ec_deploy', {
                                ec_notifierStatus = '0'
                            }
                        }
                    }
                    property 'ec_deploy', {
                        ec_notifierStatus = '0'
                    }
                }

                args.stores.each { store ->
                    def storeName = store.Store + '-' + store.ID
                    tierMap args.appName + '-' + storeName + '-map', {
                        applicationName = args.appName
                        environmentName = storeName
                        environmentProjectName = args.projName
                        projectName = args.projName

                        tierMapping args.appName + args.appTier + args.appEnvTier + storeName, {
                            applicationTierName = args.appTier
                            environmentTierName = args.appEnvTier
                            tierMapName = args.appName + '-' + storeName + '-map'
                        }
                    }
                }
            }
        }
    }
}