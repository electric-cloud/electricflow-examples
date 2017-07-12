// This is a stubbed implementation of a simple pipeline with three deployments to DEV-QA-PROD.
// Some of the basic features in a pipeline are demonstrated here.

def projName = "John Hancock POC"
def component1 = "Binary"
def component2 = "DB App"
def appNameOne = "jh-dotNet"
def appTiers = [ appTierOne: "App", appTierTwo: "DB"]
def envNames = [ "DEV", "STest", "Stage", "Prod" ]
def envTiers = [ envTierOne: "IIS", envTierTwo: "Paas SQL" ]
def groupId = "com.johnhancock.example"
def resourceNameBase = ['dotNet-Application']
def artifactId = 'dotNet'

envNames.each { eName->
	resource  projName + '-' + eName, {
		description = 'local resource for the sample application'
		hostName = '127.0.0.1'
		hostType = 'CONCURRENT'
		port = '7800'
		resourceDisabled = '0'
		trusted = '0'
		useSSL = '1'
		zoneName = 'default'
	}
}

// Create the project and define it's overall behavior
project projName, {
	description = "John Hancockc POC project"
}

// Create environments for this project
project projName, {
	envNames.each {eName ->
		environment projName + '-' + eName, {
			description = 'Environment to represent ' + eName
			environmentEnabled = '1'
			reservationRequired = '0'
			
			environmentTier envTiers.envTierOne, {
				resourceName = [projName + '-' + eName,]
			}
		}
	}
}

// Create procedures for this project that we'll reuse througout.
project projName, {
	procedure 'Build Code', {
		description = 'Stubbed procedure to give the impression of building code'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'build code', {
			description = 'Stubbed step to represent building code'
			command = 'echo "here is where you would run your build scripts"'
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			releaseMode = 'none'
		}
	}

	procedure 'Get Code', {
		description = 'Stubbed procedure to give the impression of getting code'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'checkout code', {
			description = 'Stubbed step to represent getting code'
			command = 'echo "simulated checkout"'
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			parallel = '0'
			releaseMode = 'none'
		}
	}

	procedure 'Pre-Deploy Checks', {
		description = 'Stubbed procedure to give the impression of running tests'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'Run Predeploy Checks', {
			description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
			alwaysRun = '0'
			broadcast = '0'
			command = 'echo \'insert pre-deploy checks here.\''
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			parallel = '0'
			releaseMode = 'none'
			subprocedure = null
			subproject = null
		}
	}

	procedure 'Test Environment', {
		description = 'Stubbed procedure to give the impression of testing an environment'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimit = ''
		timeLimitUnits = 'minutes'

	step 'Test Environment', {
		description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
		alwaysRun = '0'
		broadcast = '0'
		command = 'echo \'Test the environment\''
		errorHandling = 'failProcedure'
		exclusiveMode = 'none'
		parallel = '0'
		releaseMode = 'none'
		}
	}
}



// Create the application model for this project.
project projName, {
	application appNameOne, {
		description = 'John Hancock .NET application'

		applicationTier appTiers.appTierOne, {
			component component1, pluginName: null, {
				description = '.Net example'
				pluginKey = 'EC-Artifact'
				reference = '0'

				process 'Deploy', {
					description = 'Linear deploy process'
					processType = 'DEPLOY'

					processStep 'Acquire Artifact', {
						description = 'Get the artifact from the built-in repository'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'component'
						subprocedure = 'Retrieve'
						subproject = '/plugins/EC-Artifact/project'
						actualParameter 'artifactName', '$[/myComponent/ec_content_details/artifactName]'
						actualParameter 'artifactVersionLocationProperty', '$[/myComponent/ec_content_details/artifactVersionLocationProperty]'
						actualParameter 'filterList', '$[/myComponent/ec_content_details/filterList]'
						actualParameter 'overwrite', '$[/myComponent/ec_content_details/overwrite]'
						actualParameter 'retrieveToDirectory', '$[/myComponent/ec_content_details/retrieveToDirectory]'
						actualParameter 'versionRange', '$[/myJob/ec_' + component1 + '-version]'
					}

					processStep 'Transfer Artifact', {
						description = 'Transfer the artifact to a destination.  TODO: Figure out the syntax to automatically pick up the filename'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'command'
						subprocedure = 'RunCommand'
						subproject = '/plugins/EC-Core/project'
						actualParameter 'commandToRun', 'echo "file transfer happens here."'
					}
					 processDependency 'Acquire Artifact', targetProcessStepName: 'Transfer Artifact', {
						branchCondition = null
						branchConditionName = null
						branchConditionType = null
						branchType = 'ALWAYS'
					}
				}

				process 'UnDeploy ' + component1, {
					description = 'Linear undeploy process'
					processType = 'UNDEPLOY'

					processStep 'Remove Artifact', {
						description = 'Do something here to remove the file from the filesystem.'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'command'
						subprocedure = 'RunCommand'
						subproject = '/plugins/EC-Core/project'
						actualParameter 'commandToRun', 'echo "file transfer happens here."'
					}
  				}
				property 'ec_content_details', {
					property 'artifactName', value: 'com.ec.example:a', {
						expandable = '1'
					}
					artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
					filterList = ''
					overwrite = 'update'
					pluginProcedure = 'Retrieve'

					property 'pluginProjectName', value: 'EC-Artifact', {
						expandable = '1'
					}
					retrieveToDirectory = ''

					property 'versionRange', value: '', {
						expandable = '1'
					}
				}
			}
		}

		applicationTier appTiers.appTierTwo, {
			component 'DB distribution', pluginName: null, {
				description = 'Database files'
				pluginKey = 'EC-Artifact'
				reference = '0'

				process 'Deploy', {
					description = 'Linear deploy process'
					processType = 'DEPLOY'

					processStep 'Acquire Artifact', {
						description = 'Get the artifact from the built-in repository'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'component'
						subprocedure = 'Retrieve'
						subproject = '/plugins/EC-Artifact/project'
						actualParameter 'artifactName', '$[/myComponent/ec_content_details/artifactName]'
						actualParameter 'artifactVersionLocationProperty', '$[/myComponent/ec_content_details/artifactVersionLocationProperty]'
						actualParameter 'filterList', '$[/myComponent/ec_content_details/filterList]'
						actualParameter 'overwrite', '$[/myComponent/ec_content_details/overwrite]'
						actualParameter 'retrieveToDirectory', '$[/myComponent/ec_content_details/retrieveToDirectory]'
						actualParameter 'versionRange', '$[/myJob/ec_' + component2 + '-version]'
					}

					processStep 'Transfer Artifact', {
						description = 'Transfer the artifact to a destination.  TODO: Figure out the syntax to automatically pick up the filename'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'command'
						subprocedure = 'RunCommand'
						subproject = '/plugins/EC-Core/project'
						actualParameter 'commandToRun', 'echo "file transfer happens here."'
					}
					 processDependency 'Acquire Artifact', targetProcessStepName: 'Transfer Artifact', {
						branchCondition = null
						branchConditionName = null
						branchConditionType = null
						branchType = 'ALWAYS'
					}
				}

				process 'UnDeploy ' + component1, {
					description = 'Linear undeploy process'
					processType = 'UNDEPLOY'

					processStep 'Remove Artifact', {
						description = 'Do something here to remove the file from the filesystem.'
						applicationTierName = null
						dependencyJoinType = 'and'
						errorHandling = 'failProcedure'
						processStepType = 'command'
						subprocedure = 'RunCommand'
						subproject = '/plugins/EC-Core/project'
						actualParameter 'commandToRun', 'echo "file transfer happens here."'
					}
  				}
				property 'ec_content_details', {

				property 'artifactName', value: 'com.ec.example:b', {
					expandable = '1'
				}
				artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
				filterList = ''
				overwrite = 'update'
				pluginProcedure = 'Retrieve'

				property 'pluginProjectName', value: 'EC-Artifact', {
					expandable = '1'
				}
				retrieveToDirectory = ''

				property 'versionRange', value: '', {
					expandable = '1'
				}
			}
		}
    }

		process 'Deploy', {
			applicationName = appNameOne
			processType = 'OTHER'

			formalParameter 'ec_' + component1 + '-run', defaultValue: '1', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_' + component1 + '-version', defaultValue: '$[/projects/' + projName +'/applications/' + appNameOne + '/components/' + component1 + '/ec_content_details/versionRange]', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'entry'
			}

			formalParameter 'ec_' + component2 + '-run', defaultValue: '1', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_' + component2 + '-version', defaultValue: '$[/projects/' + projName + '/applications/' + appNameOne + '/components/' + component2 + '/ec_content_details/versionRange]', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'entry'
			}

			formalParameter 'ec_enforceDependencies', defaultValue: '0', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_smartDeployOption', defaultValue: '1', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_stageArtifacts', defaultValue: '0', {
				expansionDeferred = '1'
				label = null
				orderIndex = null
				required = '0'
				type = 'checkbox'
			}

			processStep 'Deploy ' + component1, {
				applicationTierName = appTiers.appTierOne
				dependencyJoinType = 'and'
				errorHandling = 'failProcedure'
				processStepType = 'process'
				subcomponent = component1
				subcomponentApplicationName = appNameOne
				subcomponentProcess = 'Deploy ' + component1

				property 'ec_deploy', {
					ec_notifierStatus = '0'
				}
			}

			processStep 'Deploy ' + component2, {
				applicationTierName = appTiers.appTierTwo
				dependencyJoinType = 'and'
				errorHandling = 'failProcedure'
				processStepType = 'process'
				subcomponent = component2
				subcomponentApplicationName = appNameOne
				subcomponentProcess = 'Deploy ' + component2

				property 'ec_deploy', {
					ec_notifierStatus = '0'
				}
			}
			property 'ec_deploy', {
				ec_notifierStatus = '0'
			}
		}

		envNames.each { eName->
			tierMap projName + '-' +  eName, {
				applicationName = appNameOne
				environmentName = projName + '-' + eName				
				environmentProjectName = projName
				projectName = projName

				tierMapping projName + '-' + envTiers.envTierOne + '-' + eName, {
					applicationTierName = appTiers.appTierOne
					environmentTierName = envTiers.envTierOne
				}	
			}
		}

    property 'ec_deploy', {
      ec_notifierStatus = '0'
    }
  }
}
