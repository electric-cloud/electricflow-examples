#!/bin/bash

echo "text for file named rollback" > rollback.txt

ectool createArtifact "com.ec.example" "rollback" --description "simple text file."

for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
  echo "Publishing com.ec.example:rollback version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.ec.example:rollback \
	--fromDirectory . \
	--includePatterns rollback.txt
done


ectool deleteProject "Rollback"

ectool evalDsl --dslFile application.groovy

