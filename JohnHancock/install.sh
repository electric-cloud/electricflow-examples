#!/bin/bash

echo "text for file named web applicatino" > web.txt
echo "text for file named db component" > db.txt

ectool createArtifact "com.johnhancock.example" "web" --description "simple text file."
ectool createArtifact "com.johnhancock.example" "db" --description "simple text file."

for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
  echo "Publishing com.johnhancock.example:web version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.johnhancock.example:web \
	--fromDirectory . \
	--includePatterns web.txt
  echo "Publishing com.johnhancock.example:db version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.johnhancock.example:db \
	--fromDirectory . \
	--includePatterns db.txt
done

ectool deleteProject "JH POC"

ectool evalDsl --dslFile JohnHancock.groovy

ectool runProcess --projectName "JH POC" --applicationName "jh-dotNet" --processName "Deploy" --environmentName "JH POC-Dev"
ectool runProcess --projectName "JH POC" --applicationName "jh-dotNet" --processName "Deploy" --environmentName "JH POC-STest"
ectool runProcess --projectName "JH POC" --applicationName "jh-dotNet" --processName "Deploy" --environmentName "JH POC-Stage"
ectool runProcess --projectName "JH POC" --applicationName "jh-dotNet" --processName "Deploy" --environmentName "JH POC-Prod"
