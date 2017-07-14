#!/bin/bash

echo "text for file named pipeline" > pipeline.txt

ectool createArtifact "com.ec.example" "pipeline" --description "simple text file."

for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
  echo "Publishing com.ec.example:rollback version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.ec.example:pipeline \
	--fromDirectory . \
	--includePatterns pipeline.txt
done


ectool deleteProject "Simple Pipeline"

ectool evalDsl --dslFile simplepipeline.groovy

