# Inventory Service

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://coveralls.io/github/codecentric/springboot-sample-app?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Requirements

For building and running the application you need:

- [JDK 17](https://www.azul.com/downloads/#downloads-table-zulu)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.inventoryservice.InventoryServiceApplication` class from your IDE.

### Steps
1. Clone the repository
   
   ```
   git clone https://github.com/Gunawann20/InventoryService.git
   ```
   
3. Open the project in your IDE.
4. Navigate to `com.example.inventoryservice.InventoryServiceApplication`.
5. Run the `main` method.

## Running via Docker

You can also run the application using Docker. Ensure you have Docker installed on your machine.

### Steps
1. Pull the Docker image:
   
   ```
   docker pull gunawan75/inventory-service
   ```
3. Run the Docker container:

   ```
   docker run -p 8080:8080 gunawan75/inventory-service
   ```

## Accessing API Documentation

Once the application is running, you can access the API documentation at:

```
  localhost:8080/swagger-ui/index.html
```

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
