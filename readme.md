# Csv Services

Mendix module that creates a rest endpoint for all entities supporting csv data format. 

Currently this serves 2 main use cases:

 * This can be used to load data into a Mendix application, to quickly setup demo or test applications. 
 * Export data for reporting in external tools like R.

Csv import has support for specifying associations so you can have a csv's for all your entities and initialize you data entirely from csv files. 
There are a number of website that will generate random data in csv format, including valid address information with longitude and latitude so you can test your google maps widgets.

 * http://www.convertcsv.com/generate-test-data.htm

Load csv records into the ProductLabels entity:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"

Retrieve all records in ProductLabels:

    curl -v -X GET http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels

## Installation

To install:

 1. Import the module into your project.
 2. Configure your project to call MF_StartCsvServices in the after startup.

Notes:

 1. Currently you need to provide a username and password when calling a csv services endpoint. Anonymous usage is not supported.
 2. The ws-doc endpoint is reused to make it work in sandboxes. This means it is not compatible with the Rest services module. This restriction will be removed in the near future.
 
## Usage

### Import CSV

Use the POST operation to create new objects of an entity:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"

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

### Export CSV

Use the GET operation to export all objects of an entity:

    curl -v -X GET http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels

#### Reporting with R

You can easily use live Mendix data in R or Rstudio using the csv services module.

An example how you can load data from Mendix into R:

    require(httr)
    secret <- RCurl::base64(paste('MxAdmin', '1', sep = ":"));
    req1 <- GET("http://localhost:8080/ws-doc/Orders/Orders",config(httpheader = c("Authorization" = paste("Basic",secret))))
    orders <- content(req1)
