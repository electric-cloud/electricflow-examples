def myProject = args.projectName

project myProject, {
    description = args.description

    args.pipelines.each { myPipeline ->
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

            myPipeline.stages.each { myStage ->
                stage myStage.name, {
                    description = myStage.description

                    myStage.gates?.each { myGate->
                        gate myGate.gateType, {
                            condition = null
                            precondition = null
                            projectName = myProject

                            task myGate.task.name, {
                                description = myGate.task.description
                                advancedMode = '0'
                                alwaysRun = '0'
                                condition = '$[suitableForRelease]'
                                enabled = '1'
                                errorHandling = 'stopOnError'
                                gateType = myGate.gateType
                                insertRollingDeployManualStep = '0'
                                instruction = null
                                notificationTemplate = 'ec_default_gate_task_notification_template'
                                projectName = myProject
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
                        }
                    }
                }
            }
        }
    }
}
