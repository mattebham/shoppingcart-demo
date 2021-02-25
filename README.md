###########   Shopping Cart Demo   ###########
This is Shopping cart application for creating orders, updating and getting orders. 

###########   Prerequisites     ##############
Java 1.8
Maven 3.6.3
Docker Installed

###########   Running in Docker     ##############
Run application in Docker:
* ./run.sh

###########   Manually building Docker     ##############
If you would like to do all the above steps manually, please run the following commands:
* mvn clean install
* docker build -t shoppingcartdemo:1.0 .
* docker run -p 8080:8080 shoppingcartdemo:1.0

###########   API Docs - Swagger     ##############

http://localhost:8080/webjars/swagger-ui/index.html?url=/api/swagger#/orders


###########   API Docs - Swagger     ##############
## Creating Orders

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "id": "1", \ 
   "orderLines": [ \ 
     { \ 
       "product": { \ 
         "productId": "1", \ 
         "productName": "soap" \ 
       }, \ 
       "quantity": 1 \ 
     } \ 
   ] \ 
 }' 'http://localhost:8080/api/orders'
 
 ## Get Orders
 curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/orders'
 
 ## Get Products
 http://localhost:8080/api/orders/1/products
  ## Unhappy path
     http://localhost:8080/api/orders/1/products     (provide invalid product id)

