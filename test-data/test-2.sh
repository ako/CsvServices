cat - | curl --verbose -H "Content-type: text/csv" -X POST http://localhost:8080/csv/Tests2/Products --data-binary @-  <<EOS
ProductID*, Name, Description
p4, Logitech P1, A logitech mouse
P5, Bose X1, A Bose headset
EOS


curl --verbose -H "Content-type: text/csv" -X POST http://localhost:8080/csv/Tests2/Products2 --data-binary @-