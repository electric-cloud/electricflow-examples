import java.io.File

def myProject = args.project

project myProject.name, {
    myProject.procedures.each { myProcedure ->
        procedure myProcedure.name, {
            description = myProcedure.description
            timeLimitUnits = 'minutes'

            myProcedure.formalParameters?.each { myParameter ->
                formalParameter myParameter.name, {
                    type = myParameter.type
                    defaultValue = myParameter.defaultValue
                    description = myParameter.description
                    expansionDeferred = myParameter.expansionDeferred
                    label = myParameter.label
                    required = myParameter.required

                    switch (myParameter.type) {
                        case ~/checkbox/:
                            break
                        default:
                            break
                    }
                }
                property 'ec_customEditorData', {
                    property 'parameters', {
                        property myParameter.name, {
                            formType = 'standard'
                            switch (myParameter.type) {
                                case ~/checkbox/:
                                    checkedValue = myParameter.checkedValue
                                    initiallyChecked = myParameter.initiallyChecked
                                    uncheckedValue = myParameter.uncheckedValue
                                    break
                            // TODO: Need to handle other use cases for text, radio buttons, etc.
                                default:
                                    break
                            }
                        }
                    }
                }
            }
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
                    // If the command has the defintion "READFROM:filename," then read that file into the command.  This path is for the server.  So if this file is on your local disk,

                    if (myStep?.command.startsWith ("READFROM:")) {
                        String path = args.MOUNTDIRECTORY + "/" + myStep.command.substring(9)
                        println "Your full path is $path"

                        String fileContents = new File ("$path").text
                        command = fileContents
                    }
                    else {
                        command = myStep.command
                    }
                    errorHandling = 'failProcedure'
                    exclusiveMode = 'none'
                    parallel = '0'
                    releaseMode = 'none'
                    timeLimitUnits = myStep.timeLimitUnits
                    subprocedure = myStep.subprocedure
                    subproject = myStep.subproject
                }
            }
        }
    }
}