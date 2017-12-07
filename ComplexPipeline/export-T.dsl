def myProject = args.projectName
project myProject, {
  description = '''Examples of more recent features
'''

  environment 'mytest', {
    rollingDeployEnabled = null
    rollingDeployType = null

    environmentTier 'App', {
      batchSize = null
      batchSizeType = null
    }
    description = 'CarMax Store Application at each location'
    environmentEnabled = '1'
    projectName = myProject
    reservationRequired = '0'
  }

  procedure 'Stub', {
    description = 'Stubbed procedure to mock out the model'
    timeLimitUnits = 'minutes'

    step 'echo hello', {
      description = 'Echo something simple to let us know we work'
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo "Hello World"'
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
      parallel = '0'
      releaseMode = 'none'
      subprocedure = null
      subproject = null
      timeLimitUnits = 'minutes'
    }
  }

  workflowDefinition 'Prototypical Workflow', {
    description = 'Mock Workflow to show some prototypical behavior'

    stateDefinition 'Initialize System', {
      startable = '1'
      subprocedure = 'Stub'
    }

    stateDefinition 'Checkout', {
      startable = '0'
      subprocedure = 'Stub'
    }

    stateDefinition 'Build', {
      startable = '0'
      subprocedure = 'Stub'
    }

    stateDefinition 'Test', {
      startable = '0'
      subprocedure = 'Stub'
    }

    stateDefinition 'Fail - Remdiate', {
      startable = '0'
      subprocedure = 'Stub'
    }

    stateDefinition 'Success', {
      startable = '0'
      subprocedure = 'Stub'
    }

    stateDefinition 'Quit', {
      startable = '0'
      subprocedure = 'Stub'
    }

    transitionDefinition 'T1', {
      condition = ''
      stateDefinitionName = 'Initialize System'
      targetState = 'Checkout'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T2', {
      condition = '$[/javascript mySubjob.outcome == "success";]'
      stateDefinitionName = 'Checkout'
      targetState = 'Build'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T2', {
      condition = ''
      stateDefinitionName = 'Build'
      targetState = 'Test'
      trigger = 'onCompletion'
    }

    transitionDefinition 'F1', {
      condition = '$[/javascript mySubjob.outcome != "success";]'
      stateDefinitionName = 'Test'
      targetState = 'Fail - Remdiate'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T3', {
      condition = '$[/javascript mySubjob.outcome == "success";]'
      stateDefinitionName = 'Test'
      targetState = 'Success'
      trigger = 'onCompletion'
    }

    transitionDefinition 'Restart', {
      condition = ''
      stateDefinitionName = 'Fail - Remdiate'
      targetState = 'Checkout'
      trigger = 'manual'
    }

    transitionDefinition 'F2', {
      condition = ''
      stateDefinitionName = 'Fail - Remdiate'
      targetState = 'Quit'
      trigger = 'manual'
    }
  }

  pipeline 'Multi-purpose Pipeline', {
    description = 'Example of a pipeline with entry points of each type'
    enabled = '1'
    type = null

    formalParameter 'ec_stagesToRun', defaultValue: null, {
      expansionDeferred = '1'
      label = null
      orderIndex = null
      required = '0'
      type = null
    }

    stage 'Build Software', {
      description = '''Show some essential entry points'''
      pipelineName = 'Multi-purpose Pipeline'

      gate 'POST', {
        condition = null
        precondition = null

        task 'Gate out of First Stage', {
          description = ''
          advancedMode = '0'
          alwaysRun = '0'
          enabled = '1'
          errorHandling = 'stopOnError'
          gateType = 'POST'
          insertRollingDeployManualStep = '0'
          notificationTemplate = 'ec_default_gate_task_notification_template'
          skippable = '0'
          subproject = args.myProject
          taskType = 'APPROVAL'
          approver = [
            'admin',
          ]
        }
      }

      task 'Run a Procedure', {
        description = 'Run a procedure'
        advancedMode = '0'
        alwaysRun = '0'
        condition = null
        enabled = '1'
        errorHandling = 'stopOnError'
        insertRollingDeployManualStep = '0'
        skippable = '0'
        subprocedure = 'Stub'
        subproject = args.myProject
        taskType = 'PROCEDURE'
      }

      task 'Run a command line', {
        description = 'run some type of command-line expression'
        actualParameter = [
          'commandToRun': 'echo "Hello world"',
        ]
        advancedMode = '0'
        alwaysRun = '0'
        enabled = '1'
        errorHandling = 'stopOnError'
        insertRollingDeployManualStep = '0'
        skippable = '0'
        subpluginKey = 'EC-Core'
        subprocedure = 'RunCommand'
        taskType = 'COMMAND'
      }

        task 'Run a workflow', {
          description = 'Run workflow we have developed'
          advancedMode = '0'
          alwaysRun = '0'
          enabled = '1'
          errorHandling = 'stopOnError'
          insertRollingDeployManualStep = '0'
          skippable = '0'
          subproject = args.projectName
          subworkflowDefinition = 'Prototypical Workflow'
          subworkflowStartingState = 'Initialize System'
          taskType = 'WORKFLOW'
        }

        task 'Deploy', {
          description = ''
          actualParameter = [
            'ec_enforceDependencies': '1',
            'ec_smartDeployOption': '1',
            'ec_stageArtifacts': '1',
          ]
          advancedMode = '0'
          alwaysRun = '0'
          enabled = '1'
          environmentName = ''
          environmentProjectName = ''
          errorHandling = 'stopOnError'
          insertRollingDeployManualStep = '0'
          rollingDeployEnabled = '0'
          skippable = '0'
          subapplication = ''
          subprocess = ''
          subproject = ''
          taskProcessType = ''
          taskType = ''
        }

        task 'Run a plugin', {
          description = ''
          actualParameter = [
            'Name': 'foo.txt',
          ]
          advancedMode = '0'
          alwaysRun = '0'
          enabled = '1'
          errorHandling = 'stopOnError'
          insertRollingDeployManualStep = '0'
          skippable = '0'
          subpluginKey = 'EC-FileOps'
          subprocedure = 'CreateEmptyFile'
          taskType = 'PLUGIN'
        }

      task 'Manual Intervention', {
        description = ''
        advancedMode = '0'
        alwaysRun = '0'
        enabled = '1'
        errorHandling = 'stopOnError'
        insertRollingDeployManualStep = '0'
        instruction = 'Approve to continue the pipeline'
        notificationTemplate = 'ec_default_pipeline_manual_task_notification_template'
        rollingDeployManualStepCondition = null
        skippable = '0'
        subproject = args.projectName
        taskType = 'MANUAL'
        approver = [
          'admin',
        ]
      }

      task 'Create Snapshot', {
        description = ''
        actualParameter = [
          'ApplicationName': '$[/myApplication/applicationName]',
          'EnvironmentName': '$[/myEnvironment/environmentName]',
          'EnvironmentProjectName': '$[/myEnvironment/projectName]',
          'Overwrite': 'false',
          'ProjectName': '$[/myApplication/projectName]',
          'ServiceName': '$[/myService/serviceName]',
          'SnapshotName': 'mysnap-1.0',
        ]
        advancedMode = '0'
        alwaysRun = '0'
        enabled = '0'
        errorHandling = 'stopOnError'
        insertRollingDeployManualStep = '0'
        skippable = '0'
        subpluginKey = 'EF-Utilities'
        subprocedure = 'Create Snapshot'
        taskType = 'UTILITY'
      }
    }
    property 'ec_counters', {
      pipelineCounter = '3'
    }
  }
}
