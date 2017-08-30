#!/bin/bash

# Setup the Heat Clinic demonstration environment

# Find the root Git workspace directory.
ROOT=$(cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd)

# Place to find artifacts
ARTIFACTS=$ROOT/downloads

# Add third-party bin directories to the flow user's path for ease of use.
echo "Adding third-party bin directories to the flow user's path for ease of use"
PROFILE=.profile

