# Csv Services

Mendix module to easily import and export csv files. It provides:
 * A rest endpoint for all entities supporting csv data format. Basically a Excel Exporter, Excel Importer meets Rest services. This enables you to automate csv export and import using script, reducing the number of manual actions required.
 * Microflow activities to import and export csv data.
 * Support for comma, semicolon, and tab delimited data, both single line and multiline.

Currently this serves 2 main use cases:

 * This can be used to script initial data loading into a Mendix application, to quickly setup demo or test applications. 
 * Export data for reporting in external tools like R.

Csv import has support for specifying associations so you can have a csv's for all your entities and initialize you data entirely from csv files. 
There are a number of website that will generate random data in csv format, including valid address information with longitude and latitude so you can test your google maps widgets.

 * http://www.convertcsv.com/generate-test-data.htm

Load csv records into the ProductLabels entity:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/ProductLabels --data-binary "@labels-data.csv"

Retrieve all records in ProductLabels:

    curl -v -X GET http://MxAdmin:1@localhost:8080/csv/Orders/ProductLabels

## Installation

To install:

 1. Import the module into your project.
 2. Configure your project to call MF_StartCsvServices in the after startup. You can provide a user_role name when calling the MF_StartCsvServices microflow. All calls to the csv services endpoints will be validated to have this user role.

Notes:

 1. Currently you need to provide a username and password when calling a csv services endpoint. Anonymous usage is not supported.
 
## Usage

### Import CSV

Use the POST operation to create new objects of an entity:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/ProductLabels --data-binary "@labels-data.csv"

Notes:

 * You need to specify the content type: text/csv
 * You can specify a format for date attributes:
 
        DateTime1,           DateTime2,  DateTime3(dd/MM/yyyy), DateTime4(dd-MM-yyyy), DateTime5(yyyyMMdd), DateTimeString
        1970-01-01 00:00:00, 1970-01-01, 01/01/1970,            01-01-1970,            19700101,            January 1st 1970
        1970-11-23 13:15:00, 1970-11-23, 23/11/1970,            23-11-1970,            19701123,            November 23rd 1970
        2001-07-06 23:00:10, 2001-07-06, 06/07/2001,            06-07-2001,            20010706,            July 6th 2001
 
 * You can define associations by refering to an association name and attribute id in the attribute header. In the following example two associations exist, billing_address and delivery address. An association will be made with the AddressId specified:
 
        CustomerId, Firstname, Lastname, DateOfBirth, Billing_Address.AddressId, Delivery_Address.AddressId
        1,          Ivan,      Freeman,  16/6/1907,   1,                         1
        2,          Anthony,   Robinson, 24/12/1938,  2,                         2

 * To specify a set of associations you can include multiple ids in brackets:
 
        ProductId, Name,      Description,     Price, Product_Labels.LabelId
        1,         reubo,     Pipopwu ric.,    92468, [57]
        2,         hujvi,     Kiuci cuguwkus., 25801, [4]
        3,         gi,        Fuli juja.,      19411, [3]
        4,         cas,       Pon duz apkar.,  4596,  [23;2]
        5,         pignadhiz, Hevizkug.,       25041, [1;2;3]
        
 * Alternatively, to define a set of associations in the header use [association_name.attribute_Name;], where the comma indicates the separator
 
         ProductId, Name,      Description,     Price, [Product_Labels.LabelId]
         1,         reubo,     Pipopwu ric.,    92468, 57
         2,         hujvi,     Kiuci cuguwkus., 25801, 4
         3,         gi,        Fuli juja.,      19411, 3
         4,         cas,       Pon duz apkar.,  4596,  23;2
         5,         pignadhiz, Hevizkug.,       25041, 1;2;3
                
 * You can specify which columns make up a unique key for the record. You indicate that a columns is part of the unique 
   key by adding an asteriks (*) after the column name. If you do this, when inserting new objects the module will
   check if the object already exists. If it does, the existing record will be updated.
     
        AddressId*, Street,              City,        County,     State, Zipcode, Country
        1,          6649 N Blue Gum St,  New Orleans, Orleans,    LA,    70116,   US
        2,          4 B Blue Ridge Blvd, Brighton,    Livingston, MI,    48116,   US

#### Initializing a Mendix application
 
Using curl you can create scripts to initialize your Mendix application without any manual actions straight from Excel spreadsheets.

The test-data folder contains an example for the Orders module, load-data.sh:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/ProductLabels --data-binary "@labels-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Products --data-binary "@products-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Address --data-binary "@address-2-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Address --data-binary "@address-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Address --data-binary "@addresses-uk-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Customers --data-binary "@customers-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/Orders --data-binary "@orders-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/csv/Orders/OrderLines --data-binary "@orderlines-data.csv"

### Import CSV data using Microflow actions

As of version 1.2 you can also import csv data from microflow actions. You can provide the entire csv document as a string parameter to the 
import csv data action:

 ![Import csv data microflow action][1]
 
Just provide the entire csv file in the string parameter:

 ![Import csv data configuration][2]
 
Alternatively you can read the csv file from your resources folder:

 ![Import csv resource microflow action][5]
 
There is also a microflow action to import csv data from a url:

 ![Import csv from url microflow action][6]
 
### Export CSV

Use the GET operation to export all objects of an entity:

    curl -v -X GET http://MxAdmin:1@localhost:8080/csv/Orders/ProductLabels
    
### Security

When enabling this module, endpoints are available for *all* entities in your application. 

To avoid misuse, all calls to the csv services endpoints need to be authenticated if not running in development mode. Currently only basic-authentication is implemented.

Optionally you can provide a user role name when calling the startup microflow. Accounts will need to have this role.

### Using Powershell on Windows

If you are on Windows and working with Powershell, instead of install curl, you can do the following:

    Invoke-WebRequest -Method Post 
      -Headers @{"Authorization" = "Basic "+[System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("MxAdmin:1"))} 
      -ContentType text/csv
      -InFile .\labels-data.csv  
      "http://localhost:8080/csv/Orders/ProductLabels" 
      
### Reporting with R

You can easily use live Mendix data in R or Rstudio using the csv services module.

An example how you can load data from Mendix into R:

    require(httr)
    secret <- RCurl::base64(paste('MxAdmin', '1', sep = ":"));
    req1 <- GET("http://localhost:8080/csv/Orders/Orders",config(httpheader = c("Authorization" = paste("Basic",secret))))
    orders <- content(req1)

### Tips

* It helps if you create a public key attribute for every entity, which can be used in associations. For example, the Orders entity has a OrderId attribute.
* Set loglevel to DEBUG to get fine grained log information.

### Todo

* Add configuration to specify which modules/entities should have csv endpoints

### Changelog

  * 1.2
  
    * Removed need for sandbox workaround for url path, enabled use of /csv path in sandboxes
    * Added import csv data microflow actions
    * Upgrade to Mendix 7
    
  * 1.2.1
   
    * Fix for including error messages in response
    
  * 2.1.0
  
    * Upgrade to Mendix 7
    
  * 2.1.1
  
    * Ignore UTF-8 BOM if provided
    * Export csv java action added
    
  * 2.1.2

    * Importer will automatically detect delimiter: , or ;
    * Exporter can be configured what delimiter to use
    * Upgrade to Mendix 7.23.3 and Mendix 8
    * Added gradle build script
    * During import ignore csv columns which cannot be mapped 
    * Added support for importing multiline strings, if using double quotes
    * Introduced strict mode for importing data
    
  * 2.2 (2019-10-18)
  
    * Support for tab delimited values
    * New microflow activity, ImportCsvUrlData, to read csv files from url
    * Support for gzipped url served csv files
    * Ability to provide an alternative header (to rename columns)
    * Configuration to specify the maximum number of records to import
    * Upgrade to mendix 7.23.8
  
  * 2.2.1 (2019-10-20)
  
    * Fixed rest handler
    * Fixed batch commits
    
 [1]: docs/csv-import-mf-action.png
 [2]: docs/csv-import-mf-action-usage.png
 [3]: docs/csv-import-resource-mf-action.png
 [4]: docs/csv-import-resource-configuration.png
 [5]: docs/csv-import-resource-model.png
 [6]: docs/csv-import-from-url.png