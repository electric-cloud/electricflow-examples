project args.projName, {
  procedure 'setup', {
    description = 'Placeholder for something real'

    step 'Process file', {
      description = 'Here is where we start'
      command = 'echo "starting the process."'
      errorHandling = 'failProcedure'
    }

    step 'Finish Setup', {
      description = 'We finished something'
      command = 'echo "transfer file."'
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
    }
  }
}

//  Create Pipelines for the different pipeline types first.
project args.projName, {
	args.pipelineTypes.each { pipelineType ->
		println "Creating pipeline for $pipelineType"
		pipeline "Deploy to " + pipelineType, {
			description = 'Auto-generated Pipeline for the deployment of applications to ' + pipelineType
			enabled = '1'
			
			// Iterate over all stage names, and create the stages per the name.
			args.pipelineStages.each { stageName ->
				stage stageName, {
					// For now, assume we have a standard stage template of "setup" followed by a deployment.  Group the 
					// deployments into a parallel task.
					task "setup deployment", {
						description = 'Dummy Step for a multi-task pipeline'
						advancedMode = '0'
						alwaysRun = '0'
						enabled = '1'
						errorHandling = 'stopOnError'
						insertRollingDeployManualStep = '0'
						skippable = '0'
						subprocedure = 'setup'
						subproject = args.projName
						taskType = 'PROCEDURE'
					}
				}
			}
		}
	}
}

// With basic pipelines defined, now let's create groups for the resources of each type.
// We'll work with the names of the fields for each environment - Region, State, and individual
project args.projName, {
	// For each environment, add a task to deploy to the named environment.
	args.stores.each { store ->
		def storeName = store.Store + '-' + store.ID
		// Add the Store/Environment to the Pipeline Stage named for the type.
		args.pipelineTypes.each { pipelineType ->
			pipeline "Deploy to " + pipelineType, {
				// Add a parameter to enable each deployment Group, as needed
				def enableGroupName = 'enable' + pipelineType + '-' + store."$pipelineType"
				formalParameter enableGroupName, defaultValue: null, {
					description = 'Enable Deployments to the $pipelineType named ' + store."$pipelineType"
					expansionDeferred = '0'
					label = 'Deploy to ' + pipelineType + ' ' + store."$pipelineType"
					orderIndex = '1'
					required = '0'
					type = 'checkbox'
				}

				// Lookup the group name based on the data - variable de-reference the pipeline type for each environment
				println "Pipeline type is $pipelineType, store is $storeName"
				def groupName = pipelineType + '-' + store."$pipelineType"
				args.pipelineStages.each { stageName ->
					stage stageName, {
						if (pipelineType != "Store") {
							// (Re)define the Group for the Region, or State.  
							// Handle Individual Stores serially for now.
							task groupName, {
								advancedMode = '0'
								alwaysRun = '0'
								enabled = '1'
								errorHandling = 'stopOnError'
								insertRollingDeployManualStep = '0'
								projectName = args.projName
								skippable = '0'
								subproject = args.projName
								taskType = 'GROUP'
							}
						}
						task "deploy-" + storeName, {
							description = 'Deploy the application to the environment named ' + storeName
							// For all the components in our incoming JSON, set the map for the -run and -version actualParameter
							// Need to figure out how to inline this definition, but the following seems to work just fine
							actualParameter = args.parameters.collectEntries {comp ->
								[
									('ec_' + comp.name + '-run') : comp.run,
									('ec_' + comp.name + '-version') : comp.version
								]
							} << // Turn on Smart Deploy so we don't re-deploy existing artifacts, might as well stage the artifacts, too.
								[
									'ec_smartDeployOption': '1',
									'ec_stageArtifacts': '1',
								]
							advancedMode = '0'
							alwaysRun = '0'
							condition = '$[' + enableGroupName + ']'
							enabled = '1'
							environmentName = storeName
							environmentProjectName = args.projName
							errorHandling = 'stopOnError'
							if (pipelineType != "Store") {
								groupName = groupName
							}
							else {
								groupName = null
							}
							insertRollingDeployManualStep = '0'
							rollingDeployEnabled = '0'
							skippable = '0'
							subapplication = args.appName
							subprocess = 'Deploy'
							subproject = args.projName
							taskType = 'PROCESS'
						}
					}
				}
				property 'ec_customEditorData', {
					property 'parameters', {
						property enableGroupName, {
							checkedValue = 'true'
							initiallyChecked = 'false'
							uncheckedValue = 'false'
						}
					}
				}
			}
		}
	}
}
