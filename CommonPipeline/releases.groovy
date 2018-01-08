def myProject = args.project

project myProject.name, {
    description = myProject.description

    myProject.releases.each { myRelease ->
        release myRelease.name, {
            description = myRelease.description
            plannedEndDate = myRelease.plannedEndDate
            plannedStartDate = myRelease.plannedStartDate
            templatePipelineName = myRelease.templatePipelineName
            pipelineName = myRelease.pipelineName

            myRelease.deployerApplications?.each { myDeployerApplication ->
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
        }
    }
}
