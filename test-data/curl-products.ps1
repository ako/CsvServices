curl -v -X GET http://MxAdmin:Welcome1!@localhost:8080/csv/Orders/Products/
curl -v -X GET -u MxAdmin:Welcome1! http://localhost:8080/csv/Orders/Products
curl -v -X GET http://MxAdmin:Welcome1!@localhost:8080/csv/Orders/Products
curl -v -X GET http://MxAdmin:Welcome1!@localhost:8080/csv/Orders/ProductLabels
curl -v -X GET https://demo_user:sQIGwSVv83@orderapp101.mendixcloud.com/ws-doc/Orders/ProductLabels

curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/Customers --data-binary "@customers-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/Products --data-binary "@products-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/Orders --data-binary "@orders-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/OrderLines --data-binary "@orderlines-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/Address --data-binary "@address-data.csv"
curl -v -X GET http://MxAdmin:Welcome1!@localhost:8080/ws-doc/Orders/Address

curl -v -X POST -H "Content-Type: text/csv" https://demo_user:sQIGwSVv83@orderapp101.mendixcloud.com/ws-doc/Orders/Address --data-binary "@address-data.csv"

$usr="MxAmdmin"
$pwd="tgTKDsAYOBWgAiGxMpbj"
$apphost="orderapp101.mendixcloud.com"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/ProductLabels --data-binary "@labels-data.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Products --data-binary "@products-data.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Address --data-binary "@address-data.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Address --data-binary "@google-addresses.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Address --data-binary "@uk-addresses.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Customers --data-binary "@customers-data.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/Orders --data-binary "@orders-data.csv"
curl -v -X POST -H "Content-Type: text/csv" https://$usr:$pwd@$apphost/ws-doc/Orders/OrderLines --data-binary "@orderlines-data.csv"

curl -v -X GET http://$usr:$pwd@$apphost/csv/Orders/ProductLabels