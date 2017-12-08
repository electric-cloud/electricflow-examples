#!/usr/bin/env bash

# Add Configurations, Credentials, ACL entries, special users for email notifications

echo "Add configurations via DSL"
ectool evalDsl --dslFile configuration.groovy --parametersFile configuration.json

echo "Add passwords to configurations"
ectool modifyEmailConfig "gmail" --mailUserPassword < passwords/password-email-gmail.txt
ectool modifyEmailConfig "test" --mailUserPassword < passwords/password-email-test.txt
ectool modifyCredential --projectName "My Project" --credentialName "Artifactory" --password < passwords/password-credential-artifactory.txt
ectool modifyCredential --projectName "My Project" --credentialName "Jenkins" --password < passwords/password-credential-jenkins.txt

ectool modifyUser "marco" --password "marco"  --sessionPassword < passwords/password-user-admin.txt
ectool modifyUser "seymour" --password "seymour"  --sessionPassword < passwords/password-user-admin.txt
