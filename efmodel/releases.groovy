def myProject = args.project

project myProject.name, {
    description = myProject.description

    myProject.pipelines?.each { myPipeline ->
        loadPipeline(myPipeline)
    }

    myProject.releases?.each { myRelease ->
        loadRelease(myRelease)
    }

}

def loadRelease (def myRelease) {
    release myRelease.name, {
        description = myRelease.description
        plannedEndDate = myRelease.plannedEndDate
        plannedStartDate = myRelease.plannedStartDate

        templatePipelineName = myRelease.templatePipelineName

        myRelease.pipelines?.each {myPipeline ->
            loadPipeline(myPipeline)
        }

        myRelease.deployerApplications?.each { myDeployerApplication ->
            loadDeployApplication(myDeployerApplication)
        }
    }
}

def loadDeployApplication (def myDeployerApplication) {
    deployerApplication myDeployerApplication.name, {
        afterLastRetry = null
        applicationProjectName = null
        enforceDependencies = '1'
        errorHandling = 'stopOnError'
        orderIndex = '1'
        processName = myDeployerApplication.processName
        retryCount = null
        retryInterval = null
        retryNotificationTemplate = null
        retryType = null
        smartDeploy = '1'
        snapshotName = null
        stageArtifacts = '1'

        myDeployerApplication.deployerConfigurations?.each { myDeployerConfiguration ->
            deployerConfiguration myDeployerConfiguration.name, {
                deployerTaskName = null
                environmentName = myDeployerConfiguration.environmentName
                environmentProjectName = null
                environmentTemplateName = null
                environmentTemplateProjectName = null
                insertRollingDeployManualStep = '0'
                processName = null
                rollingDeployEnabled = null
                rollingDeployManualStepCondition = null
                skipDeploy = '0'
                stageName = myDeployerConfiguration.stageName

                actualParameter = myDeployerConfiguration.actualParameters?.collectEntries {aParam->
                    [
                            (aParam.name) : aParam.value,
                    ]
                }
            }
        }
    }
}

def loadPipeline (def myPipeline) {
    pipeline myPipeline.name, {
        println "  Pipeline: $myPipeline.name"
        description = myPipeline.description
        enabled = "1"

        formalParameter 'ec_stagesToRun', defaultValue: null, {
            expansionDeferred = '1'
            label = null
            orderIndex = null
            required = '0'
            type = null
        }

        myPipeline.formalParameters?.each { myParameter ->
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
                    case ~/entry/:
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
                            case ~/entry/:
                                break
                            default:
                                break
                        }
                    }
                }
            }
        }

        myPipeline.stages.each { myStage ->
            stage myStage.name, {
                println "    Stage: $myStage.name"
                description = myStage.description
                colorCode = myStage.colorCode
                colorType = myStage.colorType
                plannedEndDate = myStage.plannedEndDate
                plannedStartDate = myStage.plannedStartDate
                completionType = myStage.completionType
                waitForPlannedStartDate = '0'
                pipelineName = myPipeline.name

                myStage.gates?.each { myGate->
                    gate myGate.gateType, {
                        condition = null
                        precondition = null
                        projectName = args.project.name

                        task myGate.task.name, {
                            description = myGate.task.description
                            advancedMode = '0'
                            alwaysRun = '0'
                            condition = myGate.task.condition
                            enabled = '1'
                            errorHandling = 'stopOnError'
                            gateType = myGate.gateType
                            insertRollingDeployManualStep = '0'
                            instruction = null
                            notificationTemplate = 'ec_default_gate_task_notification_template'
                            projectName = args.project.name
                            skippable = '0'
                            subproject = args.projName
                            taskType = myGate.task.type
                            approver = [
                                    myGate.task.approver,
                            ]
                        }
                    }
                }

                myStage.tasks.each { myTask ->
                    task myTask.name, {
                        actualParameter = myTask.actualParameters?.collectEntries {aParam->
                            [
                                    (aParam.name) : aParam.value,
                            ]
                        }
                        description = myTask.description
                        advancedMode = '0'
                        alwaysRun = '0'
                        condition = myTask.condition
                        enabled = '1'
                        errorHandling = 'stopOnError'
                        insertRollingDeployManualStep = '0'
                        skippable = '0'
                        taskType = myTask.taskType
                        // If the field .groupName does not exist, the intention is to define the object groupName = null.
                        groupName = myTask.groupName

                        switch (myTask.taskType) {
                            case ~/DEPLOYER/:
                                println "      Type DEPLOYER"
                                deployerRunType = myTask.deployerRunType
                                break
                            case ~/PROCEDURE/:
                                println "      Type PROCEDURE"
                                subprocedure = myTask.subprocedure
                                subproject = myTask.subproject
                                break
                            case ~/COMMAND/:
                                println "      Type Command"
                                actualParameter = [
                                        'commandToRun': myTask.commandToRun,
                                ]
                                subpluginKey = 'EC-Core'
                                subprocedure = 'RunCommand'
                                break
                            case ~/PROCESS/:
                                println "      Type Process"
                                advancedMode = myTask.advancedMode
                                actualParameter = myTask.actualParameters?.collectEntries {aParam->
                                    [
                                            (aParam.name) : aParam.value,
                                    ]
                                }
                                environmentName = myTask.environmentName
                                environmentProjectName = myTask.environmentProjectName
                                if (myTask.taskProcessType == "SERVICE") {
                                    subservice = myTask.subservice
                                }
                                else if (myTask.taskProcessType == "APPLICATION")
                                {
                                    subapplication = myTask.subapplication
                                }
                                snapshotName = myTask.snapshotName
                                subprocess = myTask.subprocess
                                subproject = myTask.subproject
                                // TODO : Need to be smart here when we do microservices.
                                taskProcessType = myTask.taskProcessType
                                break
                            case ~/MANUAL/:
                                println "      Type Manual"
                                instruction = myTask.instruction
                                notificationTemplate = myTask.notificationTemplate
                                approver = myTask.approver
                                break
                            case ~/PLUGIN/:
                                println "      Type Plugin"
                                subpluginKey = myTask.subpluginKey
                                subprocedure = myTask.subprocedure
                                actualParameter = myTask.actualParameters?.collectEntries {aParam->
                                    [
                                            (aParam.name) : aParam.value,
                                    ]
                                }
                                break
                            case ~/WORKFLOW/:
                                println "      Type Workflow"
                                subproject = myTask.subproject
                                subworkflowDefinition = myTask.subworkflowDefinition
                                subworkflowStartingState = myTask.subworkflowStartingState
                                break
                            case ~/UTILITY/:
                                println "      Type Utility"
                                condition = myTask.condition
                                subpluginKey = myTask.subpluginKey
                                subprocedure = myTask.subprocedure
                                actualParameter = myTask.actualParameters?.collectEntries {aParam->
                                    [
                                            (aParam.name) : aParam.value,
                                    ]
                                }
                                break
                            case ~/GROUP/:
                                println "      Type GROUP"
                                projectName = myTask.projectName
                                subproject = myTask.subproject
                                break
                            default:
                                println "      Type Unhandled"
                                break
                        }
                    }
                }
            }
        }
    }
}