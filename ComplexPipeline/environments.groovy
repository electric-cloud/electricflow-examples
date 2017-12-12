def myProject = args.project

//Define environments for this project
project myProject.name, {
    myProject.environments?.each { myEnvironment->
        environment myEnvironment.name, {
            description = myEnvironment.description
            environmentEnabled = '1'
            projectName = myProject.name
            reservationRequired = '0'
            rollingDeployEnabled = null
            rollingDeployType = null

            myEnvironment.environmentTiers?.each {myEnvironmentTier ->
                environmentTier myEnvironmentTier.name, {
                    batchSize = null
                    batchSizeType = null
                    resourceName = myEnvironmentTier.resources
                }
            }
        }
    }
}
