def myProject = args.projectName

// Iterate over all the stores in our list and create resources, resourcePools, and the Environments
args.resources.each { myResource ->
    resource myResource, {
        description = "Resource for $myProject"
        artifactCacheDirectory = null
        hostName = 'localhost'
        port = '7800'
        resourceDisabled = '0'
        trusted = '0'
        useSSL = '1'
        zoneName = 'default'
    }
}

