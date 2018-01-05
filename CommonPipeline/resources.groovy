def myProject = args.project

// Iterate over all the stores in our list and create resources, resourcePools, and the Environments
args.resources.each { myResource ->
    resource myResource.name, {
        description = "Resource for $myProject.name"
        artifactCacheDirectory = null
        hostName = myResource.hostName
        port = '7800'
        resourceDisabled = '0'
        trusted = '0'
        useSSL = '1'
        zoneName = 'default'
    }
}

