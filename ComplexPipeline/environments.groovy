def myProject = args.projectName

//Define environments for this project
project myProject, {
    args.resources.each { myResource ->
        environment myResource, {
            environmentEnabled = '1'
            projectName = myProject
            reservationRequired = '0'
            rollingDeployEnabled = null
            rollingDeployType = null

            environmentTier 'EnvTier', {
                batchSize = null
                batchSizeType = null
                resourceName = myResource
            }
        }
    }
}
