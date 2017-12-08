// Iterate over all the items in our list  create resources, resourcePools, and the Environments
args.elements.each { element ->
    // For now, the name of the element is the location 
    def elementName = element.Element

    // The resourcePool is defined by a comma-separated list, which we create here for later use.
    def poolList = element.Subcategory + ", " + ", group-" + element.Group + ", period-" + element.Period
    
        resource elementName, {
            description = element.Description
            artifactCacheDirectory = null
            hostName = element.Hostname
            port = '7800'
            proxyCustomization = null
            proxyHostName = null
            proxyProtocol = null
            repositoryNames = null
            resourceDisabled = '0'
            shell = null
            trusted = '0'
            useSSL = '1'
            workspaceName = null
            zoneName = 'default'
            resourcePools = poolList
        }

    //Define environments for Element resources.  There is a 1:1 mapping for resource to environments
    project args.projName, {
        environment elementName, {
            environmentEnabled = '1'
            projectName = args.projName
            reservationRequired = '0'
            rollingDeployEnabled = null
            rollingDeployType = null

            environmentTier args.envTier, {
                batchSize = null
                batchSizeType = null
                resourceName = elementName
            }
        }
    }
}

