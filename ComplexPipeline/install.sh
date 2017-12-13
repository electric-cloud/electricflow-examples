#!/usr/bin/env bash

# TODO - make the parametersFile a CLI argument so we can test against different definitions.

# Run some essential tests to show the system works.  Good hygiene.
ectool evalDsl --dslFile test.groovy --parametersFile complexPipeline.json

# Create the resources needed in this project.
ectool evalDsl --dslFile resources.groovy --parametersFile complexPipeline.json

# Create the environments needed in this project.
ectool evalDsl --dslFile environments.groovy --parametersFile complexPipeline.json

# all of the helper procedures are defined here.  This includes stubs.  We'll refer to procedures by name.
ectool evalDsl --dslFile procedures.groovy --parametersFile complexPipeline.json

# If you have workflows, define them here.  NOTE: Helper procedures should be already defined.
ectool evalDsl --dslFile workflows.groovy --parametersFile complexPipeline.json

# Your application model uses existing artifacts, procedures, and environments.
ectool evalDsl --dslFile applications.groovy --parametersFile complexPipeline.json

# Your services model uses existing procedures
ectool evalDsl --dslFile services.groovy --parametersFile complexPipeline.json

# The pipelines tie most things together.  One of the last steps.
ectool evalDsl --dslFile pipelines.groovy --parametersFile complexPipeline.json

# When we're ready, do the same for releases.
#ectool evalDsl --dslFile releases.groovy --parametersFile complexPipeline.json
