def myProject = args.project

// Define the Application Model for the services
// Here, iterate over all applications, which may contain one or more Services.
// Services may contain one or more Containers
// Containers may contain one or more Port and EnvironmentVariable definitions - base information.

// One assumption we'll make is that Application (myApplication.name) and Project (myProject.name) references are to our parents.
// Yes, we can make this uber-cool and allow for arbitrary references, but let's delay cool for now.  If somebody knows of a neat
// way of letting me optionally specify the project and parent only if it is present, but defaulting to my parent objects,
// please let me know.

project myProject.name, {
    myProject.applications.each { myApplication->
        application myApplication.name, {
            println "ADDING Application: $myApplication.name"
            description = myApplication.description

            myApplication.services.each {myService->
                println " ADDING Service: $myService.name"
                service myService.name, {
                    applicationName = myApplication.name
                    defaultCapacity = null
                    maxCapacity = null
                    minCapacity = null
                    volume = myService.volume

                    myService.containers.each { myContainer ->
                        println "  ADDING Container: $myContainer.name"
                        container myContainer.name, {
                            applicationName = myApplication.name
                            description = myContainer.description
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
                                environmentVariable envVar.name, {
                                    type = envVar.type
                                    value = envVar.value
                                }
                            }
                            myContainer.ports?.each {myPort ->
                                println "   ADDING Container Port: $myPort.name"
                                port myPort.name, {
                                    applicationName = myPort.microserviceName
                                    containerName = myPort.containerName
                                    containerPort = myPort.containerPort
                                    projectName = myProject.name
                                    serviceName = myPort.serviceName
                                }
                            }
                        }
                    }

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

                        myService.tierMaps.each {myTierMap ->
                            def myMapName = "$myTierMap.serviceName-$myTierMap.environmentName"
                            println " ADDING TierMap $myMapName for $myService.name"
                            tierMap myMapName, {
                                applicationName = myApplication.name
                                environmentName = myTierMap.environmentName
                                environmentProjectName = myProject.name
                                projectName = myProject.name

                                def scmName = myApplication.name + myTierMap.environmentName + 'map'
                                // This next section contains syntactic sugar to create an array of actualParameters from
                                // JSON data, if the data is there.  The loop will create array entries in the JSON args
                                // file and assign it to the 'actualParameter' property
                                serviceClusterMapping scmName, {
                                    actualParameter = myTierMap.serviceClusterMapping?.actualParameters?.collectEntries {aParam->
                                        [
                                                (aParam.name) : aParam.text,
                                        ]
                                    }
                                    clusterName = myTierMap.clusterName
                                    serviceName = myService.name
                                    tierMapName = myMapName

                                    serviceMapDetail myContainer.name, {
                                        serviceMapDetailName = myApplication.name + myService.name
                                        serviceClusterMappingName = scmName

                                        // This next section contains syntatic sugar to create a list of property assignments
                                        // from the data in the JSON file.  The () assignment in the actualParameter section
                                        // does not work, probably because it is easier to use for the definition of items in an array.
                                        // Here, we want to create a list of properties.
                                        myTierMap.serviceClusterMapping?.serviceMapDetail?.each { entry ->
                                            println "  ADD Detail: $entry.name = $entry.text"
                                            this[entry.name] = entry.text
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                myService.processes?.each { myProcess->
                    process myProcess.name, {
                        applicationName = myApplication.name
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
    }
}
