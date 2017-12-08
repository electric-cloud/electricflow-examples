#!/usr/bin/env bash

ectool evalDsl --dslFile test.groovy --parametersFile complexPipeline.json

ectool evalDsl --dslFile resources.groovy --parametersFile complexPipeline.json

ectool evalDsl --dslFile environments.groovy --parametersFile complexPipeline.json

ectool evalDsl --dslFile procedures.groovy --parametersFile complexPipeline.json

ectool evalDsl --dslFile workflows.groovy --parametersFile complexPipeline.json

#ectool evalDsl --dslFile applications.groovy --parametersFile complexPipeline.json

ectool evalDsl --dslFile pipelines.groovy --parametersFile complexPipeline.json

