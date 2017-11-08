// //Define procedures in our project that contain the definitions of our operations
project args.projName, {
  procedure 'Transfer Files', {
    description = 'This procedure transfer file over the network to the store'

    step 'Process file', {
      description = 'Here is where we transform the file, if that is necessary'
      command = 'echo "transforming the file."'
      errorHandling = 'failProcedure'
    }

    step 'Deliver the file', {
      description = 'Send the file over the network to the agent'
      command = 'echo "transfer file."'
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
    }
  }

  procedure 'Unpack file', {
    description = 'Unpack the file on the store and send it to the destination directory'

    step 'unpack file', {
      description = 'Unpack into a local workspace'
      command = 'echo "unpack into a local workspace."'
      errorHandling = 'failProcedure'
      timeLimitUnits = 'minutes'
    }

    step 'transfer to the z drive', {
      description = 'Transfer files to the appropriate z drive folder'
      command = 'echo "z-transfer"'
      errorHandling = 'failProcedure'
      timeLimitUnits = 'minutes'
    }
  }
}

// //Define the Master Components, and their processes
project args.projName, {
	component 'app', pluginName: null, {
		applicationName = null
		pluginKey = 'EC-Artifact'
		reference = '0'
		sourceComponentName = null
		sourceProjectName = null

		formalParameter 'artifactId', defaultValue: null, {
			expansionDeferred = '0'
			label = null
			orderIndex = '1'
			required = '1'
			type = 'entry'
		}

		process 'Deploy', {
			applicationName = null
			processType = 'DEPLOY'
			smartUndeployEnabled = false

			processStep 'Get Artifact', {
				actualParameter = [
					'artifactName': '$[/myComponent/ec_content_details/artifactName]',
					'artifactVersionLocationProperty': '$[/myComponent/ec_content_details/artifactVersionLocationProperty]',
					'filterList': '$[/myComponent/ec_content_details/filterList]',
					'overwrite': '$[/myComponent/ec_content_details/overwrite]',
					'retrieveToDirectory': '$[/myComponent/ec_content_details/retrieveToDirectory]',
					'versionRange': '$[/myJob/ec_app-version]',
				]
				alwaysRun = '0'
				dependencyJoinType = 'and'
				errorHandling = 'abortJob'
				processStepType = 'component'
				subprocedure = 'Retrieve'
				subproject = '/plugins/EC-Artifact/project'
			}

			processStep 'Transfer File', {
				alwaysRun = '0'
				dependencyJoinType = 'and'
				errorHandling = 'abortJob'
				processStepType = 'procedure'
				subprocedure = 'Transfer Files'
				subproject = args.projName
			}

			processStep 'send to drive', {
				alwaysRun = '0'
				dependencyJoinType = 'and'
				errorHandling = 'abortJob'
				processStepType = 'procedure'
				subprocedure = 'Unpack file'
				subproject = args.projName
			}

			processDependency 'Transfer File', targetProcessStepName: 'send to drive', {
				branchCondition = null
				branchConditionName = null
				branchConditionType = null
				branchType = 'ALWAYS'
			}

			processDependency 'Get Artifact', targetProcessStepName: 'Transfer File', {
				branchCondition = null
				branchConditionName = null
				branchConditionType = null
				branchType = 'ALWAYS'
			}
		}

		property 'ec_content_details', {
			property 'artifactName', value: 'com.stores:$[artifactId]', {
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

// TODO : Add a master component for PDF file delivery, if needed

// Define standard Applications, that use Master Components
project args.projName, {
	application args.appName, {
		description = args.appDescription
		
		applicationTier args.appTier, {
			applicationName = args.appName
			projectName = args.projName

			args.components.each { comp ->
				component comp.appName, pluginName: null, {
					actualParameter = [
					  'artifactId': comp.appName,
					]
					applicationName = args.appName
					pluginKey = null
					reference = '1'
					sourceComponentName = 'app'
					sourceProjectName = args.projName
				}
			}

			process 'Deploy', {
				applicationName = args.appName
				processType = 'OTHER'
				smartUndeployEnabled = null
				timeLimitUnits = null
				workspaceName = null

				args.components.each { comp ->
					formalParameter 'ec_' + comp.appName + '-run', defaultValue: '1', {
						expansionDeferred = '1'
						label = null
						orderIndex = null
						required = '0'
						type = 'checkbox'
					}

					formalParameter 'ec_' + comp.appName + '-version', defaultValue: '$[/projects/' + args.projName + '/components/app/ec_content_details/versionRange]', {
						expansionDeferred = '1'
						label = null
						orderIndex = null
						required = '0'
						type = 'entry'
					}
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

				args.components.each { comp ->
					processStep 'deploy ' + comp.appName , {
						afterLastRetry = null
						alwaysRun = '0'
						applicationTierName = args.appTier
						componentRollback = null
						dependencyJoinType = 'and'
						errorHandling = 'abortJob'
						instruction = null
						notificationTemplate = null
						processStepType = 'process'
						retryCount = null
						retryInterval = null
						retryType = null
						rollbackSnapshot = null
						rollbackType = null
						rollbackUndeployProcess = null
						skipRollbackIfUndeployFails = null
						smartRollback = null
						subcomponent = comp.appName
						subcomponentApplicationName = args.appName
						subcomponentProcess = 'Deploy'
						subprocedure = null
						subproject = null
						subservice = null
						timeLimitUnits = null
						workspaceName = null

						property 'ec_deploy', {
							  ec_notifierStatus = '0'
						}
					}
				}
				property 'ec_deploy', {
					ec_notifierStatus = '0'
				}
			}

			args.stores.each { store ->
				def storeName = store.Store + '-' + store.ID
				tierMap args.appName + '-' + storeName + '-map', {
					applicationName = args.appName
					environmentName = storeName
					environmentProjectName = args.projName
					projectName = args.projName

					tierMapping args.appName + args.appTier + args.appEnvTier + storeName , {
						applicationTierName = args.appTier
						environmentTierName = args.appEnvTier
						tierMapName = args.appName + '-' + storeName + '-map'
					}
				}
			}
		}
	}
}

// Define mappings from the applications to Store and QA Environments


