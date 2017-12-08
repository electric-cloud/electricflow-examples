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