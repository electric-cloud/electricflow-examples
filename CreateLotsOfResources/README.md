In this directory, we have an example that creates over 100 Resources into different categories of ResourcePools. We also create one Environment for each Resource.

For convenience, we're using Resources named after the elements in the periodic table of elements.

In this example, we start with an Excel spreadsheet with the names of the resources, the description, their groups, and the hostname.  This list may be similar to what you have in your environment, especially if the network name is different from the name you wish to use for the Resource.

We converted the spreadsheet to JSON using an [online tool](http://www.convertcsv.com/csv-to-json.htm).

Next, we added some up-front JSON values for the Project name, the Environment Tier Name, and all the elements.  This is not require, but it does how how to use different styles of data in a JSON file in a parameter file.

The script iterates over the JSON and defines Resources using the information in the file.

# Setup

It's simple - you run a setup script in a machine that has CLI access to your ElectricFlow server.


1. Logon onto a agent that has access to your ElectricFlow Server: `ectool login yourusername your password`
2. Checkout this repository
`git clone https://github.com/electric-cloud/electricflow-examples.git`
3.  in this working directory, CreateLotsOfResources, run the startup script:
```
cd CreateLotsOfResources
./install.sh
```

