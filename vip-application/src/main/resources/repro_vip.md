# replayer.py
Once you have downloaded this script from Zenodo, you'll need to handle a few things to ensure it works properly.  

# General
You will need to create an account on this VIP server: [VIP_SERVER], and generate an API key to allow the script to replay executions.  

> [!NOTE]
> We highly recommend running the script once without validating executions to get additional information!

## Case 1: Girder Data Inputs
In this case, the inputs used during the execution are available on a Girder server.  
To use them, you must request access to the specific collection [COLLECTION] on this Girder server: [GIRDER_SERVER].  

You'll need to create an account on the Girder server and generate an API key (available in your account profile). This API key must be passed to the script.  

Once access is granted, you can run the script by providing the necessary information (python replayer.py --help).  

## Case 2: Other Data Inputs
In this case, the input data used in the execution is available at [EXTERNAL_LINK].  
The script does not automatically handle downloading these inputs, so you will need to download them manually into an **inputs/** folder.  

You must organize the inputs exactly as described by the script. When you run the script for the first time, it will generate a folder tree describing the expected structure.  
Each file and folder must have exactly the same name and organization as specified. If not, the script will fail.  

Once the data is properly organized, you can run the script by providing the required information (python replayer.py --help).