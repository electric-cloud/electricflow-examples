#!/bin/bash

echo "text for file named pipeline" > pipeline.txt

ectool createArtifact "com.johnhancock.example" "pipeline" --description "simple text file."

for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
  echo "Publishing com.johnhancock.example:rollback version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.johnhancock.example:pipeline \
	--fromDirectory . \
	--includePatterns pipeline.txt
done


ectool deleteProject "John Hancock"

ectool evalDsl --dslFile JohnHancock.groovy

