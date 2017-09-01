#http://www.convertcsv.com/csv-to-json.htm

#!/bin/sh

# You can run this operation many times, as it overwrites resources + pools if they already exist.
echo "Create resources, resourcePools, and Enviroments"
ectool evalDsl --dslFile resources.groovy --parametersFile elements.json --timeout 600

