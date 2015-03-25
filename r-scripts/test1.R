require(httr)
library(dplyr)

secret <- RCurl::base64(paste('MxAdmin', '1', sep = ":"));

req1 <- GET("http://localhost:8080/ws-doc/Orders/Orders",config(httpheader = c("Authorization" = paste("Basic",secret))))
orders <- content(req1)
orders$OrderDate <- as.Date(orders$OrderDate)

req2 <- GET("http://localhost:8080/ws-doc/Orders/OrderLines",config(httpheader = c("Authorization" = paste("Basic",secret))))
orderLines <- content(req2)

req3 <- GET("http://localhost:8080/ws-doc/Orders/Products",config(httpheader = c("Authorization" = paste("Basic",secret))))
products <- content(req3)

req4 <- GET("http://localhost:8080/ws-doc/Orders/ProductLabels",config(httpheader = c("Authorization" = paste("Basic",secret))))
productlabels <- content(req4)

products %>%
  summarise(avgPrice = mean(Price))