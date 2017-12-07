def myProject = args.projectName

project myProject, {
    args.procedures.each { myProcedure ->
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
        }
    }
}