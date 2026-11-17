# E-Commerce Microservices (Spring Boot)

Client -> API Gateway (8080) -> Inventory Service (8081)
                             -> Shipping Service  (8082) -> Inventory Service (8081)

## Import into STS
File > Import > Maven > Existing Maven Projects > select this folder > tick all 3 projects > Finish.

## Run
Run each *Application.java as Spring Boot App, in this order:
1. InventoryServiceApplication  2. ShippingServiceApplication  3. ApiGatewayApplication

## Test (via gateway)
GET  http://localhost:8080/inventory
GET  http://localhost:8080/inventory/1
POST http://localhost:8080/shipping   body: {"productId":1,"quantity":2,"address":"Hyderabad"}
GET  http://localhost:8080/shipping
