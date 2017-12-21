#!/usr/bin/env bash

GROUPID=com.ec.complex.pipeline
PROJECTNAME=ComplexPipeline

./install-config.sh -c -P input-model.json -N $PROJECTNAME -G $GROUPID
cd ..
./install.sh -A -P ComplexPipeline/input-model.json -N $PROJECTNAME -G $GROUPID
