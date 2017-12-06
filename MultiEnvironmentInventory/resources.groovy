// Iterate over all the stores in our list and create resources, resourcePools, and the Environments
args.stores.each { store ->
	// For now, the name of the store is the location plus the ID in this format: "Chicago-1234"
	def storeName = store.Store + "-" + store.ID

	// The resourcePool is defined by a comma-separated list, which we create here for later use.
	def poolList = store.Region + ", " + store.State + ", TZ-" + store.TimeZone
	
	resource storeName, {
		description = 'Store for ' + store.Store
		artifactCacheDirectory = null
		// TODO: This hostName should become a lookup
		hostName = 'localhost'
		port = '7800'
		resourceDisabled = '0'
		trusted = '0'
		useSSL = '1'
		zoneName = 'default'
		resourcePools = poolList
    }

	//Define environments for Store resources.  There is a 1:1 mapping for resource to environments
	project args.projName, {
		environment storeName, {
		    environmentEnabled = '1'
			projectName = args.projName
			reservationRequired = '0'
			rollingDeployEnabled = null
			rollingDeployType = null

			environmentTier args.appEnvTier, {
				batchSize = null
				batchSizeType = null
				resourceName = storeName
			}
		}
	}
}
