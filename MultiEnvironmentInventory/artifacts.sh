#!/bin/bash

#NOTE: Each artifact is published separately because we need to pay attention to the name of the file.
for j in "web1" "web2" "db" "backend" "service" ; do
	echo "Creating com.stores:$j " 
	echo "Creating com.stores:$j " > $j.txt
	ectool createArtifact "com.stores" "$j" --description "simple text file."

	for i in "1.0" "1.1" "2.0" "2.1" "2.2" ; do
		echo "Publishing com.stores:$j version $i..."
		ectool --silent publishArtifactVersion \
			--version $i --artifactName com.stores:$j \
			--fromDirectory . \
			--includePatterns $j.txt
		done
done
