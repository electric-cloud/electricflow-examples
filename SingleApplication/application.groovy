// application.groovy

def projName = "Example"
def appNameOne = "Single Application 1 Comp"
def appNameTwo = "Single Application 2 Comp"
def appTiers = [ appTierOne: "Primary Tier"]
def envNames = [ "DEV", "QA", "PROD" ]
def envTiers = [ envTierOne: "Tier One" ]
def groupId = "com.ec"
def resourceNameBase = ['SingleApp']

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
	description = "Project with sample applications, pipelines, releases, and accompanying objects"
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

//Define an application with ONE component
project projName, {
	application appNameOne, {
		description = "Single application with a single component"
		
		applicationTier 'Primary Tier', {
			component 'Component A', pluginName: null, {
				description = 'simple text file'
				pluginKey = 'EC-Artifact'
				reference = '0'

				process 'Deploy Component A', {
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
						actualParameter 'versionRange', '$[/myJob/ec_Component A-version]'
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
		
		process 'Deploy App1', {
		  applicationName = appNameOne
		  processType = 'OTHER'

		  formalParameter 'ec_Component A-run', defaultValue: '1', {
			expansionDeferred = '1'
			required = '0'
			type = 'checkbox'
		  }

		  formalParameter 'ec_Component A-version', defaultValue: '$[/projects/Examples/applications/' + appNameOne + '/components/Component A/ec_content_details/versionRange]', {
			expansionDeferred = '1'
			required = '0'
			type = 'entry'
		  }

		  formalParameter 'ec_enforceDependencies', defaultValue: '0', {
			expansionDeferred = '1'
			required = '0'
			type = 'checkbox'
		  }

		  formalParameter 'ec_smartDeployOption', defaultValue: '1', {
			expansionDeferred = '1'
			required = '0'
			type = 'checkbox'
		  }

		  formalParameter 'ec_stageArtifacts', defaultValue: '0', {
			expansionDeferred = '1'
			required = '0'
			type = 'checkbox'
		  }

		  processStep 'Deploy A', {
			applicationTierName = 'Primary Tier'
			dependencyJoinType = 'and'
			errorHandling = 'failProcedure'
			processStepType = 'process'
			subcomponent = 'Component A'
			subcomponentApplicationName = appNameOne
			subcomponentProcess = 'Deploy Component A'

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

//Define an application with TWO components
project projName, {
	application appNameTwo, {
		description = "Single application with a two components"
		
		applicationTier 'Primary Tier', {
			component 'Component A', pluginName: null, {
				description = 'simple text file'
				pluginKey = 'EC-Artifact'
				reference = '0'

				process 'Deploy Component A', {
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
						actualParameter 'versionRange', '$[/myJob/ec_Component A-version]'
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
			component 'Component B', pluginName: null, {
				description = 'simple text file'
				pluginKey = 'EC-Artifact'
				reference = '0'

				process 'Deploy Component B', {
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
						actualParameter 'versionRange', '$[/myJob/ec_Component B-version]'
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
		
		process 'Deploy App2', {
			applicationName = appNameTwo
			processType = 'OTHER'

			formalParameter 'ec_Component A-run', defaultValue: '1', {
				expansionDeferred = '1'
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_Component A-version', defaultValue: '$[/projects/Examples/applications/' + appNameTwo + '/components/Component A/ec_content_details/versionRange]', {
				expansionDeferred = '1'
				required = '0'
				type = 'entry'
			}

			formalParameter 'ec_Component B-run', defaultValue: '1', {
				expansionDeferred = '1'
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_Component B-version', defaultValue: '$[/projects/Examples/applications/' + appNameTwo + '/components/Component B/ec_content_details/versionRange]', {
				expansionDeferred = '1'
				required = '0'
				type = 'entry'
			}

			formalParameter 'ec_enforceDependencies', defaultValue: '0', {
				expansionDeferred = '1'
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_smartDeployOption', defaultValue: '1', {
				expansionDeferred = '1'
				required = '0'
				type = 'checkbox'
			}

			formalParameter 'ec_stageArtifacts', defaultValue: '0', {
				expansionDeferred = '1'
				required = '0'
				type = 'checkbox'
			}

			processStep 'Deploy A', {
				applicationTierName = 'Primary Tier'
				dependencyJoinType = 'and'
				errorHandling = 'failProcedure'
				processStepType = 'process'
				subcomponent = 'Component A'
				subcomponentApplicationName = appNameTwo
				subcomponentProcess = 'Deploy Component A'

				property 'ec_deploy', {
					ec_notifierStatus = '0'
				}
			}

			processStep 'Deploy B', {
			applicationTierName = 'Primary Tier'
			dependencyJoinType = 'and'
			errorHandling = 'failProcedure'
			processStepType = 'process'
			subcomponent = 'Component B'
			subcomponentApplicationName = appNameTwo
			subcomponentProcess = 'Deploy Component B'

			property 'ec_deploy', {
			  ec_notifierStatus = '0'
			}
		}

		// If you comment out the next block, you will allow the two component processes to run in parallel.  
		// This block definition establishes the processDependency to be serial.
		// These are the names as defined in the applcation process.
		processDependency 'Deploy A', targetProcessStepName: 'Deploy B', {
			branchCondition = null
			branchConditionName = null
			branchConditionType = null
			branchType = 'ALWAYS'
		}

		property 'ec_deploy', {
			ec_notifierStatus = '0'
		  }
		}

		envNames.each { eName->
			tierMap projName + '-' +  eName, {
				applicationName = appNameTwo
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
