def myProject = args.project

project myProject.name, {
    myProject.procedures.each { myProcedure ->
        procedure myProcedure.name, {
            description = myProcedure.description
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
                timeLimitUnits = 'minutes'
            }

            myProcedure.steps?.each { myStep ->
                step myStep.name, {
                    description = myStep.description
                    alwaysRun = '0'
                    broadcast = '0'
                    command = myStep.command
                    errorHandling = 'failProcedure'
                    exclusiveMode = 'none'
                    parallel = '0'
                    releaseMode = 'none'
                    timeLimitUnits = 'minutes'
                    subprocedure = myStep.subprocedure
                    subproject = myStep.subproject

                }
            }
        }
    }
}