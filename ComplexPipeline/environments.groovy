def myProject = args.project

//Define environments for this project
project myProject.name, {
    args.resources.each { myResource ->
        environment myResource, {
            environmentEnabled = '1'
            projectName = myProject.name
            reservationRequired = '0'
            rollingDeployEnabled = null
            rollingDeployType = null

            environmentTier 'AppTier', {
                batchSize = null
                batchSizeType = null
                resourceName = myResource
            }
            environmentTier 'DBTier', {
                batchSize = null
                batchSizeType = null
                resourceName = myResource
            }
        }
    }
}
