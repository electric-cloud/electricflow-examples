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
            myEnvironment.clusters.each {myCluster ->
                cluster myCluster.name, {
                    environmentName = myEnvironment.name
                    pluginProjectName = null
                    pluginKey = myCluster.pluginKey
                    providerClusterName = null
                    providerProjectName = null
                    provisionParameter = [
                            'config': myCluster.provisionParameter.config,
                            'project': myCluster.provisionParameter.project
                    ]
                    provisionProcedure = 'Check Cluster'
                }
            }
        }
    }
}
