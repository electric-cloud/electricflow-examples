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
//                    applicationName = myApplication.name
//                    projectName = myProject.name

                    myApplicationTier.components?.each { myComponent ->
                        println "       myComponent is $myComponent.name, $myComponent.groupId:$myComponent.artifactId"

                        component myComponent.name, pluginName: null, {
//                            applicationName = myApplication.name
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
                                process myProcess.name, {
                                    println "         myProcess is $myProcess.name"
                                    description = myProcess.description
                                    processType = myProcess.processType
                                    applicationName = null
                                    serviceName = null
                                    smartUndeployEnabled = null


                                    myProcess.processSteps?.each { myProcessStep ->
                                        processStep myProcessStep.name, {
                                            actualParameter = myProcessStep.actualParameters?.collectEntries { aParam ->
                                                [
                                                        (aParam.name): aParam.value,
                                                ]
                                            }
                                            description = myProcessStep.description

                                            alwaysRun = '0'
                                            dependencyJoinType = 'and'
                                            errorHandling = 'abortJob'

                                            // TODO : Need to understand why applicationTierName has to be present.  All others do not
                                            applicationTierName = null

                                            // This next list seems largely optional, or at least not primary fields
                                            afterLastRetry = null
                                            componentRollback = null
                                            instruction = null
                                            notificationTemplate = null
                                            retryCount = null
                                            retryInterval = null
                                            retryType = null
                                            rollbackSnapshot = null
                                            rollbackType = null
                                            rollbackUndeployProcess = null
                                            skipRollbackIfUndeployFails = null
                                            smartRollback = null
                                            subcomponentApplicationName = null
                                            subservice = null
                                            subserviceProcess = null
                                            timeLimitUnits = null
                                            workspaceName = null

                                            processStepType = myProcessStep.processStepType
                                            switch (myProcessStep.processStepType) {
                                                case ~/component/:
                                                    subcomponent = myProcessStep.subcomponent
                                                    subcomponentProcess = myProcessStep.subcomponentProcess
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    break
                                                case ~/procedure/:
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    break
                                                case ~/manual/:
                                                    instruction = myProcessStep.instruction
                                                    notificationTemplate = myProcessStep.notificationTemplate
                                                    assignee = myProcessStep.assignee
                                                    break
                                                case ~/command/:
                                                    subprocedure = myProcessStep.subprocedure
                                                    subproject = myProcessStep.subproject
                                                    actualParameter = [
                                                            'commandToRun': myProcessStep.commandToRun,
                                                    ]
                                                    break
                                                default:
                                                    break
                                            }
                                        }
                                    }
                                    myProcess.processDependencies?.each { myProcessDependency ->
                                        processDependency myProcessDependency.source, targetProcessStepName: myProcessDependency.target, {
                                            branchCondition = myProcessDependency.branchCondition
                                            branchConditionName = myProcessDependency.branchConditionName
                                            branchConditionType = myProcessDependency.branchConditionType
                                            branchType = myProcessDependency.branchType
                                        }
                                        println "           myProcessDependency: $myProcessDependency.source -> $myProcessDependency.target"
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
            myApplication.processes?.each { myProcess->
                process myProcess.name, {
                    println "   my(application)Process is $myProcess.name"
                    applicationName = myApplication.name
                    processType = 'DEPLOY'
                    myProcess.formalParameters?.each { myParameter ->

                        formalParameter myParameter.name, {
                            type = myParameter.type
                            defaultValue = myParameter.defaultValue
                            description = myParameter.description
                            expansionDeferred = myParameter.expansionDeferred
                            label = myParameter.label
                            orderIndex = myParameter.orderIndex
                            required = myParameter.required

                            switch (myParameter.type) {
                                case ~/checkbox/:
                                    break
                                default:
                                    break
                            }
                        }
                        property 'ec_customEditorData', {
                            property 'parameters', {
                                property myParameter.name, {
                                    switch (myParameter.type) {
                                        case ~/checkbox/:
                                            checkedValue = myParameter.checkedValue
                                            initiallyChecked = myParameter.initiallyChecked
                                            uncheckedValue = myParameter.uncheckedValue
                                            break
                                        // TODO: Need to handle other use cases for text, radio buttons, etc.
                                        default:
                                            break
                                    }
                                }
                            }
                        }
                    }

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

                            formalParameter 'ec_' + myComponent.name + '-version', defaultValue: "\$[/projects/" + myProject.name + " /applications/Two-Tier/components/$myComponent.name/ec_content_details/versionRange]", {
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
                            description = myProcessStep.description
                            println "       myProcessStep is $myProcessStep.name"
                            applicationTierName = myProcessStep.applicationTierName
                            dependencyJoinType = 'and'
                            errorHandling = 'failProcedure'
                            processStepType = myProcessStep.processStepType

                            // This next list seems largely optional, or at least not primary fields
                            afterLastRetry = null
                            componentRollback = null
                            retryCount = null
                            retryInterval = null
                            retryType = null
                            skipRollbackIfUndeployFails = null
                            smartRollback = null
                            subcomponent = null
                            subservice = null
                            subserviceProcess = null
                            timeLimitUnits = null
                            workspaceName = null

                            switch (myProcessStep.processStepType) {
                                case ~/process/:
                                    subcomponent = myProcessStep.subcomponent
                                    subcomponentApplicationName = myApplication.name
                                    subcomponentProcess = myProcessStep.subcomponentProcess
                                    break
                                case ~/procedure/:
                                    subprocedure = myProcessStep.subprocedure
                                    subproject = myProcessStep.subproject
                                    break
                                case ~/manual/:
                                    instruction = myProcessStep.instruction
                                    notificationTemplate = myProcessStep.notificationTemplate
                                    assignee = myProcessStep.assignee
                                    break
                                case ~/rollback/:
                                    rollbackType = myProcessStep.rollbackType
                                    rollbackType = "environment"
                                    smartRollback = myProcessStep.smartRollback
                                    rollbackSnapshot = null
                                    rollbackUndeployProcess = null
                                    break
                                case ~/command/:
                                    subprocedure = myProcessStep.subprocedure
                                    subproject = myProcessStep.subproject
                                    actualParameter = [
                                            'commandToRun': myProcessStep.commandToRun,
                                    ]
                                    break
                                default:
                                    break
                            }

                            property 'ec_deploy', {
                                ec_notifierStatus = '0'
                            }
                        }
                    }

                    // Each processStep has a dependency, defined at the Process Level
                    myProcess.processDependencies?.each { myProcessDependency ->
                        processDependency myProcessDependency.source, targetProcessStepName: myProcessDependency.target, {
                            branchCondition = myProcessDependency.branchCondition
                            branchConditionName = myProcessDependency.branchConditionName
                            branchConditionType = myProcessDependency.branchConditionType
                            branchType = myProcessDependency.branchType
                        }
                    }
                    property 'ec_deploy', {
                        ec_notifierStatus = '0'
                    }
                }
            }

            // When we build TierMaps in the UI, we get UUIDs for the names of the mappings.  As far as I can tell, what we
            // need to do is provide unique names.  Here, we state the combination of :
            //      application name + environment name + apptier name + envtier name
            //

            myApplication.tierMaps?.each { myTierMap ->
                def myMapName = "$myTierMap.applicationName-$myTierMap.environmentName"
                tierMap myMapName, {
                    applicationName = myTierMap.applicationName
                    environmentName = myTierMap.environmentName
                    environmentProjectName = myProject.name
                    projectName = myProject.name
                    myTierMap.tierMappings?.each { myTierMapping->
                        tierMapping myMapName + "-" + myTierMapping.applicationTierName + "-" + myTierMapping.environmentTierName, {
                            applicationTierName = myTierMapping.applicationTierName
                            environmentTierName = myTierMapping.environmentTierName
                        }
                    }
                }
            }

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
                            // These ports are defined at the Container Definition, specific to the container
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

                    // NOTE: The service contains the mapping information.  Not at the application level.
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

                                    //TODO: Need ports for the configuration-level definition, mapping into the cluster.
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

            property 'ec_deploy', {
                ec_notifierStatus = '0'
            }
        }
    }
}