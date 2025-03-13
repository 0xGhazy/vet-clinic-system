# Vet Clinic System

## Overview

The Vet Clinic System is a RESTful API built using Spring Boot and Hibernate, following the MVC design pattern. This system manages pets, their owners, clinics, doctors, and visits, allowing seamless interaction between them.

## Features Implemented

- **Entity Management**:
  - [✔] Create entities: Pet, Owner, Clinic, Doctor, Visit
  - [✔] Retrieve entities by ID
- **Relationship Management**:
  - [✔] List all owner's pets by owner ID
  - [✔] List all clinic's doctors by clinic ID
- **Search & Assignment**:
  - [✔] Search for clinics by phone number or address
  - [✔] Assign and de-assign doctors to a clinic
- **Validations & Constraints**:
  - Enforced database-level unique constraints
  - Implemented field-level validations

## System Entities

## Technologies Used

- **Spring Boot v3.4.3** – For developing the REST APIs
- **Hibernate (JPA)** – For ORM and database interactions
- **PostgreSQL** – As the database
- **Lombok** – To reduce boilerplate code
- **Spring Validation** – For enforcing field constraints

## Run and test the application

### Prerequisites

- Java 17+
- Maven
- IDE (IntelliJ IDEA as preferred in document)

### Steps

1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd vet-clinic-system
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. Access APIs via Postman (collection provided in `/docs`)

---

## Stack trace

### List all business classes and methods

To extract the business classes and methods we need to focus only on the following stack trace snippet as it's the only part under `com` package

```java
at com.server.gateway.client.base.BaseGatewayIntegrationService.executePost(BaseGatewayIntegrationService.java:60)
at com.server.gateway.client.base.BaseTransactionGatewayIntegrationService.find(BaseTransactionGatewayIntegrationService.java:61)
at com.server.msales.sales.salesorder.businessmanager.SalesOrderManagerImpl.isTransactionCreatedOnERP(SalesOrderManagerImpl.java:1038)
at com.server.msales.sales.salesorder.businessmanager.SalesOrderManagerImpl.isTransactionCreatedOnERP(SalesOrderManagerImpl.java:222)
at com.server.core.businesslayer.businessmanager.AbstractERPPublishedEntityManager.doActualPublishingInErp(AbstractERPPublishedEntityManager.java:325)
at com.server.core.businesslayer.businessmanager.AbstractERPPublishedEntityManager.publishInErp(AbstractERPPublishedEntityManager.java:261)
at com.server.core.businesslayer.businessmanager.AbstractERPPublishedEntityManager.publish(AbstractERPPublishedEntityManager.java:157)
at com.server.core.businesslayer.businessmanager.AbstractERPPublishedEntityManager.publish(AbstractERPPublishedEntityManager.java:183)
at com.server.msales.sales.salesorder.handlers.SalesOrderInvoicingHandlerImpl.doBeforeInvoicing(SalesOrderInvoicingHandlerImpl.java:203)
at com.server.msales.sales.salesorder.businessmanager.SalesOrderManagerImpl.invoice(SalesOrderManagerImpl.java:2377)
at com.server.msales.sales.salesorder.businessmanager.SalesInvoiceManagerImpl.create(SalesInvoiceManagerImpl.java:349)
at com.server.msales.sales.salesorder.businessmanager.SalesInvoiceManagerImpl.create(SalesInvoiceManagerImpl.java:243)
at com.server.core.businesslayer.businessservice.AbstractSyncEntityServiceImpl.doCreate(AbstractSyncEntityServiceImpl.java:51)
at com.server.core.businesslayer.businessservice.AbstractSyncEntityService.create(AbstractSyncEntityService.java:64)
at com.server.core.businesslayer.servicerequest.AbstractSyncServiceDispatcher.dispatch(AbstractSyncServiceDispatcher.java:114)
```

**BaseGatewayIntegrationService**

|   Method    | Line No. |
| :---------: | :------: |
| executePost |    60    |

**BaseTransactionGatewayIntegrationService**

| Method | Line No. |
| :----: | :------: |
|  find  |    61    |

**SalesOrderManagerImpl**

|          Method           | Line No. |
| :-----------------------: | :------: |
|          invoice          |   2377   |
| isTransactionCreatedOnERP |   1038   |
| isTransactionCreatedOnERP |   222    |

**AbstractERPPublishedEntityManager**

|         Method          | Line No. |
| :---------------------: | :------: |
| doActualPublishingInErp |   325    |
|      publishInErp       |   261    |
|         publish         |   157    |
|         publish         |   183    |

**SalesOrderInvoicingHandlerImpl**

|      Method       | Line No. |
| :---------------: | :------: |
| doBeforeInvoicing |   203    |

**SalesInvoiceManagerImpl**

| Method | Line No. |
| :----: | :------: |
| create |   349    |
| create |   243    |

**AbstractSyncEntityServiceImpl**

|    Method     | Line No. |
| :-----------: | :------: |
| creadoCreatee |    51    |

**AbstractSyncEntityService**

| Method | Line No. |
| :----: | :------: |
| create |    64    |

**AbstractSyncServiceDispatcher**

|  Method  | Line No. |
| :------: | :------: |
| dispatch |   114    |

### Break point position

While trying to call the server on `localhost:9090` over HTTP via `RestTemplate` got _Connection refused_ which means the server maybe down rightnow. The `BaseGatewayIntegrationService` has method `executePost` to use RestTemplate as mentioned above. So this will be the point i'll put a break point on it.

Position: **BaseGatewayIntegrationService line 60**

```java
org.springframework.web.client.ResourceAccessException: I/O error on POST request for
"http://localhost:9090/integration-gateway/salesOrder/find": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect
at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:607)
at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:565)
at org.springframework.web.client.RestTemplate.postForObject(RestTemplate.java:367)
at com.server.gateway.client.base.BaseGatewayIntegrationService.executePost(BaseGatewayIntegrationService.java:60)
```
