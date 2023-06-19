USR_PWD=csv_user:YiyWAgGH16
APP_PORT=80
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Tests/AllTypes --data-binary "@alltypes.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Tests/Dates --data-binary "@dates.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/ProductLabels --data-binary "@labels-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Products --data-binary "@products-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Address --data-binary "@address-2-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Address --data-binary "@address-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Address --data-binary "@address-uk-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Customers --data-binary "@customers-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/Orders --data-binary "@orders-data.csv"
curl -v -X POST -H "Content-Type: text/csv" http://%USR_PWD%@localhost:%APP_PORT%/csv/Orders/OrderLines --data-binary "@orderlines-data.csv"
