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

echo "Create artifacts for this example."
#!/bin/bash

GROUPID=com.ec.samples

#NOTE: Each artifact is published separately because we need to pay attention to the name of the file.
for artifactId in "web1" "web2" "db" "mobile" "mainframe" ; do
	echo "Creating $GROUPID:$artifactId "
	echo "Creating $GROUPID:$artifactId " > $artifactId.txt
	ectool createArtifact "$GROUPID" "$artifactId" --description "simple text file."

	for version in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
		echo "Publishing com.ec.samples:$artifactId version $version..."
		ectool --silent publishArtifactVersion \
			--version $version --artifactName $GROUPID:$artifactId \
			--fromDirectory . \
			--includePatterns $artifactId.txt
		done
done
