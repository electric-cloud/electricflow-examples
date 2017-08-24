#!/bin/bash

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

# Please NOTE: The following name is hard-coded.  If/when we update the plugin, this line will break
echo "installing the plugin"
ectool installPlugin unplug-2.0.2.jar --force true

# Please NOTE: The following name is hard-coded.  If/when we update the plugin, this line will break
echo "promoting the plugin"
ectool promotePlugin "unplug-2.0.2"
