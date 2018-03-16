def myProject = args.project

//Define environments for this project
project myProject.name, {
    myProject.environments?.each { myEnvironment->
        environment myEnvironment.name, {
            println "Environment: $myEnvironment.name"
            description = myEnvironment.description
            environmentEnabled = '1'
            projectName = myProject.name
            reservationRequired = '0'
            rollingDeployEnabled = null
            rollingDeployType = null

            myEnvironment.rollingDeployPhases?.each {myRollingDeployPhase ->
                rollingDeployPhase myRollingDeployPhase.name, {
                    orderIndex = myRollingDeployPhase.orderIndex
                    phaseExpression = myRollingDeployPhase.phaseExpression
                    rollingDeployPhaseType = myRollingDeployPhase.rollingDeployPhaseType
                }
            }

            myEnvironment.environmentTiers?.each {myEnvironmentTier ->
                environmentTier myEnvironmentTier.name, {
                    println "   EnvironmentTier : $myEnvironmentTier.name "
                    batchSize = null
                    batchSizeType = null
                    resourceName = myEnvironmentTier.resources
                    resourcePhaseMapping = myEnvironmentTier.resourcePhaseMappings?.collectEntries {mapping ->
                        [
                                (mapping.name) : mapping.value,
                        ]
                    }
                }
            }
            myEnvironment.clusters.each {myCluster ->
                cluster myCluster.name, {
                    println "   Cluster : $myCluster.name "
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
