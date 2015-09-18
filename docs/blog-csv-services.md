# Quickly loading your data into a Mendix App using the CSV Service module

"What is the easiest way to get my data into the Mendix App I just created?" This question comes up frequently. In this blogpost i'll explain how you can use the [CsvService][1] module for this purpose.
 
There are a number of modules you can use to get your data into your application, which one you want to use depends on your situation:
 
 * Excel importer - useful if you have MS-Excel files, and you want to configure the mapping from your Excel to the entities in your domain model through pages in your application.
 * Database replicator - this module can help you if your data lives in another database. For this to work you need to be able to connect to the other database from your application.
 
The CsvServices module is really useful if you have comma separated files that map 1-to-1 to entities, and you don't want to configure the mapping in your application. The 2 main scenario's where i use this is for loading test data and for loading initial (configuration) data.

There are a number of websites where you can generate random test data in csv format, using the CsvServices module it's really easy to load this data into your entities.

The module also has an option to download data from entities in csv format. This is really useful if you want to move data from one application to another.

## Simple data loading example

Consider the following domain model. I have a csv file containing product labels and i want to load that data in the the ProductLabels entity.

![Domain model][3]

After you include the CsvServices module from the AppStore into your project, and initialize the module in the application startup microflow, you can load your csv file with one command into your running application:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"

[Curl][4] is an opensource tool available for both windows and linux that functions as a command line browser. It enables you to interact with REST APIs from the command line. You need to install it, and run it from a shell or dos prompt.

This command **posts** the file **labels-data.csv** to the url **http://localhost:8080/ws-doc/Orders/ProductLabels** using the username MxAdmin and the password 1. It also tell the API that it is sending a csv document.

Intead of curl you can also use wget, and on windows you can use Invoke-WebRequest in powershell:

    Invoke-WebRequest -Method Post 
      -Headers @{"Authorization" = "Basic "+[System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("MxAdmin:1"))} 
      -ContentType text/csv
      -InFile .\labels-data.csv  
      "http://localhost:8080/ws-doc/Orders/ProductLabels" 

The CsvServices module creates an API like this for every entity in your project: http://\<servername\>:\<port\>/ws-doc/\<module_name\>/\<entity_name\>. The path ws-doc is used to enable this on sandboxes.

The labels-data.csv file looks like this:

    LabelId*, Label
    1,        furlifot
    2,        tu
    3,        vudef
    4,        waz
    5,        lokavhal

The first line has the attribute names of the entity, followed by one line per record. The star behind the LabelId name indicates that it should be considered a unique key for the object. When inserting objects, CsvServices will try to replace objects with the same unique key with the data from your csv.

The labels in this example are a bit odd, as they have been randomly generated using the [convertcsv.com][6] website. There are more websites like this, some will also create valid address information, if you need data to display on a map. Ofcourse, if you like, you can also use Excel to populate your csv with data.

## Downloading data from a running mendix application

To retrieve all objects from an entity you can call the same API with the GET command:

    curl -v -X GET http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels -o labels.csv
    
## Working with associations between entities

The CsvService module can also handle uploading multiple csv files to different entities with associations. In the domain model above there is an association  between products and product labels. In your csv you can reference objects in other entities by naming the column \<association name\>.\<identifying attribute of other entity\>.

So in the following csv file, the column Product_Labels.LabelId specifies that the column contains the value of LabelId from the entity reached by the association Product_Labels. It basically is a foreign key. In this specific example the association is a reference set, or M-to-N relationship. This can be expressed by including a number of LabelIds in square brackets separated by semicolons.

    ProductId, Name*,  Description,                 Price, Product_Labels.LabelId
    1,         reubo,  Pipopwu ric ceh coniriute.,  92468, [57]
    2,         hujvi,  Kiuci cuguwkus alogagon.,    25801, [4]
    3,         gi,     Fuli juja fimki fum ziptuh., 19411, [3]
    4,         cas,    Pon duz apkar ujacehev iw.,  4596,  [23;2]

To load the data for both Product Labels and Products you can execute the following commands:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"
    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/Products --data-binary "@products-data.csv"

As you can see, it's fairly straightforward to create a script that will populate a number of entites. Very useful for demos or tests. More info can be found in the [readme][5] or in the source code on GitHub: [CsvServices][2].

  [1]: https://appstore.home.mendix.com/link/app/1911/Mendix/CsvServices
  [2]: https://github.com/ako/CsvServices
  [3]: domain-model.png
  [4]: http://curl.haxx.se/
  [5]: https://github.com/ako/CsvServices/blob/master/readme.md
  [6]: http://www.convertcsv.com/generate-test-data.htm