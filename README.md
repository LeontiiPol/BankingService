# Bank Service

RESTful API allowing to create banking account, 
transfer money and get information about transaction and accounts.

## Stack

+ SpringBoot 
+ H2
+ Liquibase
+ Junit 5
+ Mockito
+ Lombok
+ Swagger
+ Mapstruct
+ Jackson

## Run 

1. clone this repo
2. run application
3. POST http://localhost:8080/api/accounts - to create an account. 
   Request body example:
   ```json
   {
       "beneficiaryName": "Sam",
       "pin": "3333"
   }
   ```
4. POST http://localhost:8080/api/accounts/{accountNumber}/deposit - to deposit money.
   Request body example:
   ```json
   {
        "sum": 1000,
        "pin": "3333"
    }
   ```
5. POST http://localhost:8080/api/accounts/{accountNumber}/withdraw - to withdraw money.
   Request body example:
   ```json
   {
        "sum": 400,
        "pin": "3333"
    }
   ```
6. POST http://localhost:8080/api/accounts/{accountNumber}/transfer - to transfer money.
   Request body example:
   ```json
   {
        "sum": 2000,
        "receiver": "35d0bd77-50f8-4017-a465-f761270d8e18",
        "pin": "3333"
    }
   ```
7. GET http://localhost:8080/api/accounts - to get information about all accounts
8. GET http://localhost:8080/api/accounts/{accountNumber}/transactions - to get information about all transactions of specified account
9. http://localhost:8080/h2-console/ - DB
   1. login - admin
   2. password - admin
   3. JDBC URL - jdbc:h2:mem:bank-service-db
10. http://localhost:8080/swagger-ui/index.html Swagger documentation