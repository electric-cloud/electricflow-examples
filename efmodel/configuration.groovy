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
            username = myCredential.username
            passwordRecoveryAllowed = true
            projectName = myCredential.project
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
    schedule mySchedule.name, {
        description = mySchedule.description
        misfirePolicy = mySchedule.misfirePolicy
        priority = mySchedule.priority
        // TODO: The logic behind the CI Manager is broken.  We can't use a reference such as "/projects[projectname]/procedures[myproc]"
        // Instead, we have to use /projects/projectname/procedures/myproc
        procedureName = mySchedule.procedureName
        projectName = mySchedule.projectName
        releaseName = mySchedule.releaseName
        scheduleDisabled = mySchedule.scheduleDisabled
        timezone = mySchedule.timezone
        actualParameter = mySchedule.actualParameters?.collectEntries {aParam->
            [
                    (aParam.name) : aParam.value,
            ]
        }
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