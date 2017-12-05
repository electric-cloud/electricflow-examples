
project 'T', {
  description = '''Examples of more recent features
'''
  resourceName = null
  workspaceName = null

  environment 'mytest', {
    description = 'CarMax Store Application at each location'
    environmentEnabled = '1'
    projectName = 'T'
    reservationRequired = '0'
    rollingDeployEnabled = null
    rollingDeployType = null

    environmentTier 'App', {
      batchSize = null
      batchSizeType = null
    }
  }

  procedure 'Stub', {
    description = ''
    jobNameTemplate = ''
    resourceName = ''
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    step 'echo hello', {
      description = ''
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo "Hello World"'
      condition = ''
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
      logFileName = ''
      parallel = '0'
      postProcessor = ''
      precondition = ''
      releaseMode = 'none'
      resourceName = ''
      shell = ''
      subprocedure = null
      subproject = null
      timeLimit = ''
      timeLimitUnits = 'minutes'
      workingDirectory = ''
      workspaceName = ''
    }
  }

  workflowDefinition 'Prototypical Workflow', {
    description = ''
    workflowNameTemplate = ''

    stateDefinition 'Initialize System', {
      description = ''
      startable = '1'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Checkout', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Build', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Test', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Fail - Remdiate', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Success', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    stateDefinition 'Quit', {
      description = ''
      startable = '0'
      subprocedure = 'Stub'
      subproject = ''
      substartingState = ''
      subworkflowDefinition = ''
    }

    transitionDefinition 'T1', {
      description = ''
      condition = ''
      stateDefinitionName = 'Initialize System'
      targetState = 'Checkout'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T2', {
      description = ''
      condition = '$[/javascript mySubjob.outcome == "success";]'
      stateDefinitionName = 'Checkout'
      targetState = 'Build'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T2', {
      description = ''
      condition = ''
      stateDefinitionName = 'Build'
      targetState = 'Test'
      trigger = 'onCompletion'
    }

    transitionDefinition 'F1', {
      description = ''
      condition = '$[/javascript mySubjob.outcome != "success";]'
      stateDefinitionName = 'Test'
      targetState = 'Fail - Remdiate'
      trigger = 'onCompletion'
    }

    transitionDefinition 'T3', {
      description = ''
      condition = '$[/javascript mySubjob.outcome == "success";]'
      stateDefinitionName = 'Test'
      targetState = 'Success'
      trigger = 'onCompletion'
    }

    transitionDefinition 'Restart', {
      description = ''
      condition = ''
      stateDefinitionName = 'Fail - Remdiate'
      targetState = 'Checkout'
      trigger = 'manual'
    }

    transitionDefinition 'F2', {
      description = ''
      condition = ''
      stateDefinitionName = 'Fail - Remdiate'
      targetState = 'Quit'
      trigger = 'manual'
    }
  }

  pipeline 'Multi-purpose Pipeline', {
    description = ''
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
      description = '''Show some essential entry points
'''
      condition = null
      parallelToPrevious = null
      pipelineName = 'Multi-purpose Pipeline'
      precondition = null

      gate 'PRE', {
        condition = null
        precondition = null
      }

      gate 'POST', {
        condition = null
        precondition = null

        task 'Gate out of First Stage', {
          description = ''
          advancedMode = '0'
          afterLastRetry = null
          alwaysRun = '0'
          condition = null
          deployerExpression = null
          deployerRunType = null
          enabled = '1'
          environmentName = null
          environmentProjectName = null
          environmentTemplateName = null
          environmentTemplateProjectName = null
          errorHandling = 'stopOnError'
          gateCondition = null
          gateType = 'POST'
          groupName = null
          groupRunType = null
          insertRollingDeployManualStep = '0'
          instruction = null
          notificationTemplate = 'ec_default_gate_task_notification_template'
          parallelToPrevious = null
          precondition = null
          retryCount = null
          retryInterval = null
          retryType = null
          rollingDeployEnabled = null
          rollingDeployManualStepCondition = null
          skippable = '0'
          snapshotName = null
          startTime = null
          subapplication = null
          subpluginKey = null
          subprocedure = null
          subprocess = null
          subproject = 'T'
          subservice = null
          subworkflowDefinition = null
          subworkflowStartingState = null
          taskProcessType = null
          taskType = 'APPROVAL'
          approver = [
            'admin',
          ]
        }
      }

      task 'Run a Procedure', {
        description = 'Run a procedure'
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = 'Stub'
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'PROCEDURE'
      }

      task 'Run a command line', {
        description = 'run some type of command-line expression'
        actualParameter = [
          'commandToRun': 'echo "Hello world"',
        ]
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = 'EC-Core'
        subprocedure = 'RunCommand'
        subprocess = null
        subproject = null
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'COMMAND'
      }

      task 'Group 1', {
        description = null
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = 'parallel'
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'GROUP'

        task 'Run a workflow', {
          description = 'Run workflow we have developed'
          advancedMode = '0'
          afterLastRetry = null
          alwaysRun = '0'
          condition = null
          deployerExpression = null
          deployerRunType = null
          enabled = '1'
          environmentName = null
          environmentProjectName = null
          environmentTemplateName = null
          environmentTemplateProjectName = null
          errorHandling = 'stopOnError'
          gateCondition = null
          gateType = null
          groupName = 'Group 1'
          groupRunType = null
          insertRollingDeployManualStep = '0'
          instruction = null
          notificationTemplate = null
          parallelToPrevious = null
          precondition = null
          retryCount = null
          retryInterval = null
          retryType = null
          rollingDeployEnabled = null
          rollingDeployManualStepCondition = null
          skippable = '0'
          snapshotName = null
          startTime = null
          subapplication = null
          subpluginKey = null
          subprocedure = null
          subprocess = null
          subproject = 'T'
          subservice = null
          subworkflowDefinition = 'Prototypical Workflow'
          subworkflowStartingState = 'Initialize System'
          taskProcessType = null
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
          afterLastRetry = null
          alwaysRun = '0'
          condition = null
          deployerExpression = null
          deployerRunType = null
          enabled = '1'
          environmentName = 'Austin-5'
          environmentProjectName = 'Stores'
          environmentTemplateName = null
          environmentTemplateProjectName = null
          errorHandling = 'stopOnError'
          gateCondition = null
          gateType = null
          groupName = 'Group 1'
          groupRunType = null
          insertRollingDeployManualStep = '0'
          instruction = null
          notificationTemplate = null
          parallelToPrevious = null
          precondition = null
          retryCount = null
          retryInterval = null
          retryType = null
          rollingDeployEnabled = '0'
          rollingDeployManualStepCondition = null
          skippable = '0'
          snapshotName = null
          startTime = null
          subapplication = 'Sample App'
          subpluginKey = null
          subprocedure = null
          subprocess = 'Deploy'
          subproject = 'Stores'
          subservice = null
          subworkflowDefinition = null
          subworkflowStartingState = null
          taskProcessType = 'APPLICATION'
          taskType = 'PROCESS'
        }

        task 'Run a plugin', {
          description = ''
          actualParameter = [
            'Name': 'foo.txt',
          ]
          advancedMode = '0'
          afterLastRetry = null
          alwaysRun = '0'
          condition = null
          deployerExpression = null
          deployerRunType = null
          enabled = '1'
          environmentName = null
          environmentProjectName = null
          environmentTemplateName = null
          environmentTemplateProjectName = null
          errorHandling = 'stopOnError'
          gateCondition = null
          gateType = null
          groupName = 'Group 1'
          groupRunType = null
          insertRollingDeployManualStep = '0'
          instruction = null
          notificationTemplate = null
          parallelToPrevious = null
          precondition = null
          retryCount = null
          retryInterval = null
          retryType = null
          rollingDeployEnabled = null
          rollingDeployManualStepCondition = null
          skippable = '0'
          snapshotName = null
          startTime = null
          subapplication = null
          subpluginKey = 'EC-FileOps'
          subprocedure = 'CreateEmptyFile'
          subprocess = null
          subproject = null
          subservice = null
          subworkflowDefinition = null
          subworkflowStartingState = null
          taskProcessType = null
          taskType = 'PLUGIN'
        }
      }

      task 'Manual Intervention', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = 'Approve to continue the pipeline'
        notificationTemplate = 'ec_default_pipeline_manual_task_notification_template'
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
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
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '0'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = 'EF-Utilities'
        subprocedure = 'Create Snapshot'
        subprocess = null
        subproject = null
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'UTILITY'
      }
    }

    stage 'Test Software', {
      description = '''Show some essential entry points
'''
      condition = null
      parallelToPrevious = null
      pipelineName = 'Multi-purpose Pipeline'
      precondition = null

      gate 'PRE', {
        condition = null
        precondition = null

        task 'Gate into Second', {
          description = ''
          advancedMode = '0'
          afterLastRetry = null
          alwaysRun = '0'
          condition = null
          deployerExpression = null
          deployerRunType = null
          enabled = '1'
          environmentName = null
          environmentProjectName = null
          environmentTemplateName = null
          environmentTemplateProjectName = null
          errorHandling = 'stopOnError'
          gateCondition = null
          gateType = 'PRE'
          groupName = null
          groupRunType = null
          insertRollingDeployManualStep = '0'
          instruction = null
          notificationTemplate = 'ec_default_gate_task_notification_template'
          parallelToPrevious = null
          precondition = null
          retryCount = null
          retryInterval = null
          retryType = null
          rollingDeployEnabled = null
          rollingDeployManualStepCondition = null
          skippable = '0'
          snapshotName = null
          startTime = null
          subapplication = null
          subpluginKey = null
          subprocedure = null
          subprocess = null
          subproject = 'T'
          subservice = null
          subworkflowDefinition = null
          subworkflowStartingState = null
          taskProcessType = null
          taskType = 'APPROVAL'
          approver = [
            'admin',
          ]
        }
      }

      gate 'POST', {
        condition = null
        precondition = null
      }

      task 'Run a Procedure', {
        description = 'Run a procedure'
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = 'Stub'
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'PROCEDURE'
      }

      task 'Run a command line', {
        description = 'run some type of command-line expression'
        actualParameter = [
          'commandToRun': 'echo "Hello world"',
        ]
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = 'EC-Core'
        subprocedure = 'RunCommand'
        subprocess = null
        subproject = null
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'COMMAND'
      }

      task 'Run a workflow', {
        description = 'Run workflow we have developed'
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = 'Prototypical Workflow'
        subworkflowStartingState = 'Initialize System'
        taskProcessType = null
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
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = 'Chicago-1'
        environmentProjectName = 'Stores'
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = '0'
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = 'Sample App'
        subpluginKey = null
        subprocedure = null
        subprocess = 'Deploy'
        subproject = 'Stores'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = 'APPLICATION'
        taskType = 'PROCESS'
      }

      task 'Run a plugin', {
        description = ''
        actualParameter = [
          'Name': 'foo.txt',
        ]
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = 'EC-FileOps'
        subprocedure = 'CreateEmptyFile'
        subprocess = null
        subproject = null
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'PLUGIN'
      }

      task 'Manual Intervention', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = 'Approve to continue the pipeline'
        notificationTemplate = 'ec_default_pipeline_manual_task_notification_template'
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
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
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '0'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = 'EF-Utilities'
        subprocedure = 'Create Snapshot'
        subprocess = null
        subproject = null
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = 'UTILITY'
      }
    }

    stage 'test', {
      condition = null
      parallelToPrevious = null
      pipelineName = 'Multi-purpose Pipeline'
      precondition = null

      gate 'PRE', {
        condition = null
        precondition = null
      }

      gate 'POST', {
        condition = null
        precondition = null
      }

      task 'get VMWare info', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = null
      }

      task 'kit not found - create it', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = null
      }

      task 'Kit found', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = '$/myPipeline/run if found it]'
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = null
      }

      task 'test', {
        description = ''
        advancedMode = '0'
        afterLastRetry = null
        alwaysRun = '0'
        condition = null
        deployerExpression = null
        deployerRunType = null
        enabled = '1'
        environmentName = null
        environmentProjectName = null
        environmentTemplateName = null
        environmentTemplateProjectName = null
        errorHandling = 'stopOnError'
        gateCondition = null
        gateType = null
        groupName = null
        groupRunType = null
        insertRollingDeployManualStep = '0'
        instruction = null
        notificationTemplate = null
        parallelToPrevious = null
        precondition = null
        retryCount = null
        retryInterval = null
        retryType = null
        rollingDeployEnabled = null
        rollingDeployManualStepCondition = null
        skippable = '0'
        snapshotName = null
        startTime = null
        subapplication = null
        subpluginKey = null
        subprocedure = null
        subprocess = null
        subproject = 'T'
        subservice = null
        subworkflowDefinition = null
        subworkflowStartingState = null
        taskProcessType = null
        taskType = null
      }
    }

    // Custom properties

    property 'ec_counters', {

      // Custom properties
      pipelineCounter = '3'
    }
  }
}
