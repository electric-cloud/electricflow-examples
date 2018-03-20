def myProject = args.project

project myProject.name, {
    description = myProject.description
}

args.emailConfigs.each { myEmailConfig ->
    emailConfig myEmailConfig.name, {
        description = myEmailConfig.desription
        mailFrom = myEmailConfig.mailFrom
        mailHost = myEmailConfig.mailHost
        mailPort = myEmailConfig.mailPort
        mailProtocol = myEmailConfig.mailProtocol
        mailUser = myEmailConfig.mailUser
    }
}

args.credentials.each { myCredential ->
    project myCredential.project, {
        credential myCredential.name, {
            userName = myCredential.userName
            passwordRecoveryAllowed = true
            projectName = myCredential.project
            if (myCredential.password?.startsWith ("READFROM:")) {
                String path = args.MOUNTDIRECTORY + "/" + myCredential.password.substring(9)
                String fileContents = new File ("$path").text
                println "Password is $fileContents"
                password = fileContents
            }
            else {
                println "Password : $myCredential.password"
                password = myCredential.password
            }
        }
    }
}

args.users.each { myUser ->
    user myUser.name, {
        fullUserName = myUser.fullUserName
        email = myUser.email
    }
}

args.schedules.each { mySchedule ->
    project myProject.name, {
        schedule mySchedule.name, {
            description = mySchedule.description
            interval = null
            intervalUnits = null
            misfirePolicy = mySchedule.misfirePolicy
            priority = mySchedule.priority
            // TODO: The logic behind the CI Manager is broken.  We can't use a reference such as "/projects[projectname]/procedures[myproc]"
            // Instead, we have to use /projects/projectname/procedures/myproc
            procedureName = mySchedule.procedureName
//           projectName = mySchedule.projectName
//            releaseName = mySchedule.releaseName
            scheduleDisabled = mySchedule.scheduleDisabled
            timezone = mySchedule.timezone

            // One or more of these appear to be needed to allow us to specify the procedure name.
            applicationName = null
            applicationProjectName = null
            beginDate = null
            endDate = null
            environmentName = null
            environmentProjectName = null
            environmentTemplateName = null
            environmentTemplateProjectName = null
            environmentTemplateTierMapName = null
            monthDays = null
            pipelineName = null
            processName = null
            releaseName = null
            rollingDeployEnabled = null
            rollingDeployManualStepAssignees = null
            rollingDeployManualStepCondition = null
            rollingDeployPhases = null
            serviceName = null
            snapshotName = null
            startTime = null
            startingStage = null
            startingStateName = null
            stopTime = null
            weekDays = null
            workflowName = null

//            actualParameter = mySchedule.actualParameters?.collectEntries { aParam ->
//                [
//                        (aParam.name): aParam.value,
//                ]
//            }
            property 'ec_customEditorData', {
                GitBranch = mySchedule.GitBranch
                GitRepo = mySchedule.GitRepo
                QuietTimeMinutes = mySchedule.QuietTimeMinutes
                TriggerFlag = mySchedule.TriggerFlag
                dest = mySchedule.dest
                //            formType = '$[/plugins/ECSCM-Git/project/scm_form/sentry]'
                lsRemote = '1'
                monitorTags = '0'
                scmConfig = mySchedule.scmConfig
            }
        }
    }
}