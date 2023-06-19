curl --verbose -H "Accept: text/csv"  \
  -X GET http://localhost:8080/csv/Tests2/Products \
  | sed 's/,/\^/g' \
  | curl --verbose \
      -H "X-Alternative-Header: -Id^-Products_Category^ProductID*^Description^Name" \
      -H "X-Has-Header: true"  \
      -H "X-Max-Records: -1" \
      -H "X-Delimiter: \^" \
      -H "Content-type: text/csv" \
      -X POST http://localhost:8080/csv/Tests2/Products_2 --data-binary @-
