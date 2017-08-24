#!/bin/bash

echo "text for file named a" > a.txt
echo "text for file named b" > b.txt
echo "text for file named c" > c.txt

ectool createArtifact "com.ec.example" "a" --description "simple text file."
ectool createArtifact "com.ec.example" "b" --description "simple text file."
ectool createArtifact "com.ec.example" "c" --description "simple text file."

for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
  echo "Publishing com.ec.example:a version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.ec.example:a \
	--fromDirectory . \
	--includePatterns a.txt

  echo "Publishing com.ec.example:b version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.ec.example:b \
	--fromDirectory . \
	--includePatterns b.txt

  echo "Publishing com.ec.example:c version $i..."
  ectool --silent publishArtifactVersion \
	--version $i --artifactName com.ec.example:c \
	--fromDirectory . \
	--includePatterns c.txt
done


ectool deleteProject "Examples"

ectool evalDsl --dslFile application.groovy

