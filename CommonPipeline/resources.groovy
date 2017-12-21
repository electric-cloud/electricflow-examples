def myProject = args.project

// Iterate over all the stores in our list and create resources, resourcePools, and the Environments
args.resources.each { myResource ->
    resource "$myProject.name-$myResource", {
        description = "Resource for $myProject.name"
        artifactCacheDirectory = null
        hostName = 'localhost'
        port = '7800'
        resourceDisabled = '0'
        trusted = '0'
        useSSL = '1'
        zoneName = 'default'
    }
}

