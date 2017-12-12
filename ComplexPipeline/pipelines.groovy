def myProject = args.project

project myProject.name, {
    description = myProject.description

    myProject.pipelines.each { myPipeline ->
        pipeline myPipeline.name, {
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
                                default:
                                    break
                            }
                        }
                    }
                }
            }

            myPipeline.stages.each { myStage ->
                stage myStage.name, {
                    description = myStage.description

                    myStage.gates?.each { myGate->
                        gate myGate.gateType, {
                            condition = null
                            precondition = null
                            projectName = myProject.name

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
                                projectName = myProject.name
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
                            description = myTask.description
                            advancedMode = '0'
                            alwaysRun = '0'
                            condition = myTask.condition
                            enabled = '1'
                            errorHandling = 'stopOnError'
                            insertRollingDeployManualStep = '0'
                            skippable = '0'
                            taskType = myTask.taskType
                            // TODO: Need to verify this is the right way to make the assignment.  If the field .groupName
                            // does not exist, the intention is to define the object groupName = null.
                            groupName = myTask.groupName

                            switch (myTask.taskType) {
                                case ~/PROCEDURE/:
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
                                    actualParameter = [
                                            'ec_enforceDependencies': '0',
                                            'ec_smartDeployOption': '0',
                                            'ec_stageArtifacts': '0',
                                    ]
                                    environmentName = myTask.environmentName
                                    environmentProjectName = myTask.environmentProjectName
                                    subapplication = myTask.subapplication
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
}
