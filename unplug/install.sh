#!/bin/bash

# You can run this script more than once because it checks for the presence of the git repo.  

# You need to be logged on via ectool before running this script (ectool login username password)

if [ ! -d Unplug ] ; then
	echo "Installing unplug."
	git clone https://github.com/electric-cloud/Unplug.git
	cd Unplug
else
	echo "Unplug present, performing an update."
	cd Unplug
	git pull
fi

# TODO : handle the case where the version number may change.
# Please NOTE: The following name is hard-coded.  If/when we update the plugin, this line will break
echo "installing the plugin"
ectool installPlugin unplug-2.0.2.jar --force true

# TODO : handle the case where the version number may change.
# Please NOTE: The following name is hard-coded.  If/when we update the plugin, this line will break
echo "promoting the plugin"
ectool promotePlugin "unplug-2.0.2"
