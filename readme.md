# Csv Services

Mendix module that creates a rest endpoint for all entities supporting csv data format. This can be used to load
data into a Mendix application, and to export data. For example for reporting.

Load csv records into the ProductLabels entity:

    curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"

Retrieve all records in ProductLabels:

    curl -v -X GET http://MxAdmin:1@localhost:8080/ws-doc/Orders/ProductLabels
