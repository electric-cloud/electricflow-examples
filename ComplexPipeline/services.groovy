def myProject = args.project

// Define the Service Model for the services
// Here, iterate over all services in the project.
// Services may contain one or more Containers
// Containers may contain one or more Port and EnvironmentVariable definitions - base information.

// One assumption we'll make is that Service (myService.name) and Project (myProject.name) references are to our parents.
// Yes, we can make this uber-cool and allow for arbitrary references, but let's delay cool for now.  If somebody knows of a neat
// way of letting me optionally specify the project and parent only if it is present, but defaulting to my parent objects,
// please let me know.

project myProject.name, {
    myProject.services.each {myService->
        println " ADDING Service: $myService.name"
        service myService.name, {
            applicationName = null
            defaultCapacity = null
            description = myService.description
            maxCapacity = null
            minCapacity = null
            volume = myService.volume

            myService.containers.each { myContainer ->
                println "  ADDING Container: $myContainer.name"
                container myContainer.name, {
                    serviceName = myService.name
                    description = myContainer.description
                    applicationName = null
                    command = myContainer.command
                    cpuCount = myContainer.cpuCount
                    cpuLimit = myContainer.cpuLimit
                    // Is there a smarter way of defaulting to "null" if the value is not specified in JSON?
                    // We can't assign this to "null" otherwise the reference fails.
                    if (myContainer.credentialName == "null") {
                        credentialName = null
                    }
                    else {
                        credentialName = myContainer.credentialName
                    }

                    entryPoint = myContainer.entryPoint
                    imageName = myContainer.imageName
                    if (myContainer.imageVersion == "null") {
                        imageVersion = null
                    }
                    else {
                        imageVersion = myContainer.imageVersion
                    }

                    memoryLimit = myContainer.memoryLimit
                    memorySize = myContainer.memorySize
                    registryUri = myContainer.registryUri
                    serviceName = myService.name
                    volumeMount = myContainer.volumeMount

                    // Add environment variables only if they are present.
                    myContainer.environmentVariables?.each {envVar ->
                        println "   ADDING EnvVar: $envVar.name"
                        applicationName = null
                        environmentVariable envVar.name, {
                            type = envVar.type
                            value = envVar.value
                        }
                    }
                    // These ports are defined at the Container Definition, specific to the container
                    myContainer.ports?.each {myPort ->
                        println "   ADDING Container Port: $myPort.name"
                        port myPort.name, {
                            applicationName = null
                            containerName = myContainer.name
                            containerPort = myPort.containerPort
                            projectName = myProject.name
                            serviceName = myService.name
                        }
                    }
                }
            }

            // These are the ports on the service definition.
            myService.ports?.each {myPort ->
                println "  ADDING Service Port: $myPort.name"
                port myPort.name, {
                    listenerPort = myPort.listenerPort
                    projectName = myProject.name
                    serviceName = myService.name
                    subcontainer = myPort.subcontainer
                    subport = myPort.subport
                }
            }

            // NOTE: The service contains the mapping information.
            myService.containers.each {myContainer->

                myService.environmentMaps.each {myEnvironmentMap ->
                    def myMapName = "$myEnvironmentMap.serviceName-$myEnvironmentMap.environmentName"
                    println " ADDING TierMap $myMapName for $myService.name"
                    environmentMap myMapName, {
                        serviceName = myService.name
                        environmentName = myEnvironmentMap.environmentName
                        environmentProjectName = myProject.name

                        def scmName = myService.name + myEnvironmentMap.environmentName + 'map'
                        // This next section contains syntactic sugar to create an array of actualParameters from
                        // JSON data, if the data is there.  The loop will create array entries in the JSON args
                        // file and assign it to the 'actualParameter' property
                        serviceClusterMapping scmName, {
                            actualParameter = myEnvironmentMap.serviceClusterMapping?.actualParameters?.collectEntries {aParam->
                                [
                                        (aParam.name) : aParam.text,
                                ]
                            }
                            clusterName = myEnvironmentMap.clusterName
                            serviceName = myService.name
                            environmentMapName= myMapName

                            serviceMapDetail myContainer.name, {
                                serviceMapDetailName = myService.name + myService.name
                                serviceClusterMappingName = scmName

                                // This next section contains syntatic sugar to create a list of property assignments
                                // from the data in the JSON file.  The () assignment in the actualParameter section
                                // does not work, probably because it is easier to use for the definition of items in an array.
                                // Here, we want to create a list of properties.
                                myEnvironmentMap.serviceClusterMapping?.serviceMapDetail?.each { entry ->
                                    println "  ADD Detail: $entry.name = $entry.text"
                                    this[entry.name] = entry.text
                                }
                            }

                            //TODO: Need ports for the configuration-level definition, mapping into the cluster.
                            // These are the ports on the serviceCluster definition.
                            myEnvironmentMap.serviceClusterMapping.ports?.each {myPort ->
                                println "  ADDING Service Port: $myPort.name"
                                port myPort.name, {
                                    environmentName = myEnvironmentMap.environmentName
                                    listenerPort = myPort.listenerPort
                                    protocol = null
                                    serviceClusterMappingName = scmName
                                    subcontainer = myPort.subcontainer
                                    subport = myPort.subport
                                }
                            }

                        }
                    }
                }
            }
        }

        myService.processes?.each { myProcess->
            process myProcess.name, {
                serviceName = myService.name
                description = myProcess.description
                processType = myProcess.processType
                smartUndeployEnabled = null
                timeLimitUnits = null
                workspaceName = null


                myProcess.processSteps?.each {myProcessStep->
                    processStep myProcessStep.name, {
                        println " ADDING processSteps $myProcess.name : $myProcessStep.name"
                        actualParameter = myProcessStep.actualParameters?.collectEntries { aParam ->
                            [
                                    (aParam.name): aParam.value,
                            ]
                        }

                        alwaysRun = '0'
                        dependencyJoinType = 'and'
                        description = myProcessStep.description
                        errorHandling = 'abortJob'
                        processStepType = myProcessStep.processStepType

                        switch (myProcessStep.processStepType) {
                            case ~/service/:
                                subservice = myProcessStep.subservice
                                break
                            case ~/procedure/:
                                subprocedure = myProcessStep.subprocedure
                                subproject = myProcessStep.subproject
                                project = myProject.name
                                break
                            default:
                                break
                        }

                        property 'ec_deploy', {
                            ec_notifierStatus = '0'
                        }
                    }
                }
                myProcess.processDependencies?.each { myProcessDependency ->
                    processDependency myProcessDependency.source, targetProcessStepName: myProcessDependency.target, {
                        branchCondition = null
                        branchConditionName = null
                        branchConditionType = null
                        branchType = myProcessDependency.branchType
                    }
                    println "           myProcessDependency: $myProcessDependency.source -> $myProcessDependency.target"
                }
            }

        }
        property 'ec_deploy', {
            ec_notifierStatus = '0'
        }
    }
}
