#!/bin/sh

# Run this command to start brand-new
echo "Delete project to start fresh"
ectool deleteProject "Stores"

# You can run this operation many times, as it overwrites resources + pools if they already exist.
echo "Create resources, resourcePools, and Enviroments"
./artifacts.sh
ectool evalDsl --dslFile resources.groovy --parametersFile data.json --timeout 600

# You can run this operation many times, as it redefines using DSL
echo "Create Master Components, Applications, and Environment Mappings"
ectool evalDsl --dslFile model.groovy --parametersFile data.json --timeout 720

echo "create pipeline"
ectool evalDsl --dslFile pipeline.groovy --parametersFile data.json

echo "setup plugin"
../unplug/install.sh

ectool setProperty "/projects[Stores]/unplug1" --valueFile storeInventory.pl
ectool setProperty "/server/unplug/v1" '$[/projects[Stores]/unplug1]'
ectool setProperty "/server/ec_ui/availableViews/Stores" --valueFile stores_ui.xml
ectool setProperty "/server/ec_ui/defaultView" "Stores"
