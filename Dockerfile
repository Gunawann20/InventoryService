FROM openjdk:17-jdk-slim
EXPOSE 8080
VOLUME /tmp
COPY target/InventoryService-0.0.1-SNAPSHOT.jar inventory-service.jar
LABEL authors="Gunawan"

ENTRYPOINT ["java","-jar","/inventory-service.jar"]