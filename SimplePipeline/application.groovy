// This is a stubbed implementation of a simple pipeline with three deployments to DEV-QA-PROD.
// Some of the basic features in a pipeline are demonstrated here.


project 'Simple Pipeline', {
  resourceName = null
  workspaceName = null

  procedure 'Build Code', {
    description = 'Stubbed procedure to give the impression of building code'
    jobNameTemplate = ''
    resourceName = ''
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    step 'build code', {
      description = ''
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo "here is where you would run your build scripts"'
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

  procedure 'Get Code', {
    description = ''
    jobNameTemplate = ''
    resourceName = ''
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    step 'checkout code', {
      description = ''
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo "simulated checkout"'
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

  procedure 'Pre-Deploy Checks', {
    description = ''
    jobNameTemplate = ''
    resourceName = 'local'
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    step 'Run Predeploy Checks', {
      description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo \'insert pre-deploy checks here.\''
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

  procedure 'Test Environment', {
    description = ''
    jobNameTemplate = ''
    resourceName = 'local'
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    step 'Test Environment', {
      description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
      alwaysRun = '0'
      broadcast = '0'
      command = 'echo \'Test the environment\''
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
}
