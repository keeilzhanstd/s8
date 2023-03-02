# Synpulse8 backend hiring challenge

## Table of contents
* [General info](#general-info)
* [Requirements](#requirements)
* [Setup](#setup)
* [Api](#api)
* [Security](#security)
* [Data ](#data)
* [Logging](#logging)
* [Testing](#testing)

## General info

This project was developed for Synpulse8 backend hiring challenge.
Throughout the project I had to face new to me technologies such as Apache Kafka, Kubernetes, Docker, as well as concept of unit, integration and functional testing.

## Requirements

* Docker Desktop
* Java 17

## Setup

### Clone project

```
git clone https://github.com/keeilzhanstd/s8.git
```

### Start docker containers using docker compose

Note: Make sure you are in the project directory while running containers. i.e where the Dockerfile is

```
docker-compose up -d --build
```

In case some of the containers failed to run, check if some other containers taking up following ports: 3306, 2181, 9092, 8080

If successfull, you should be seeing 4 docker containers running under 's8' container-group

## API

### Endpoints


| Endpoint                                      | Method | Description                                 | 
|:----------------------------------------------|:-------|:--------------------------------------------| 
| [`/api/v1/auth/register`](#register)          | POST   | Register user and retrieve jwt              |
| [`/api/v1/auth/authenticate`](#authenticate)  | POST   | Authenticate user and retrieve jwt          |
| [`/api/v1/transactions`](#post-transaction)   | POST   | Publish transaction to kafka                |
| [`/api/v1/transactions`](#get-transactions)   | GET    | Retrieve transactions for month from kafka  |

___

#### Register
```http
POST /api/v1/auth/register
```

##### Request body

`(Content-Type: application/json)`

| Parameter       | Type     | Description         | Example              |
|:----------------|:---------|:--------------------|:---------------------|
| `username`      | `String` | Username for user   | ex: "P-000000001"    |
| `password`      | `String` | Password for user   | ex: "Password_pwd"   |


##### Response

| Code | Schema                     | Description                              |
|:-----|:---------------------------|:-----------------------------------------|
| 200  | ```{"token": "string"} ``` | Returns jwt token for newly created user |
 | 403  | `Forbidden`                | User already exist                       |

___

#### Authenticate

```http
POST /api/v1/auth/authenticate
```

##### Request body

`(Content-Type: application/json)`

| Parameter    | Type     | Description       | Example            |
|:-------------|:---------|:------------------|:-------------------|
| `username`   | `String` | Username for user | ex: "P-000000001"  |
| `password`   | `String` | Password for user | ex: "Password_pwd" |



##### Response

| Code  | Schema                     | Description                |
|:------|:---------------------------|:---------------------------|
| 200   | ```{"token": "string"} ``` | Returns jwt token for user |
| 403   | `Forbidden`                | Bad credentials            |

___

#### POST Transaction
```http
POST /api/v1/transactions
```

##### Headers

| Parameter         | Type     | Description        |
|:------------------|:---------|:-------------------|
| `Authorization`   | `String` | JWT token          |

##### Request body

`(Content-Type: application/json)`

| Parameter  | Type                 | Description                                 | Example                    |
|:-----------|:---------------------|:--------------------------------------------|:---------------------------|
| `amount`   | `double`             | Amount spend or deposited (Can be negative) | -100.0 or 100.0            |
| `currency` | `String`             | Currency used                               | USD or CHF etc             |
| `iban`     | `String`             | User iban account number                    | HK93-0000-0000-0000-0021-1 |
| `date`     | `String($date-time)` | YYYY-MM-DD format                           | "2023-03-02T03:58:04.493Z" |

___

#### GET Transactions
```http
GET /api/v1/transactions

Example request with query params
GET /api/v1/transactions?month=3&year=2023&pageSize=2&currency=HKD
```

##### Headers

| Parameter          | Type     | Description |
|:-------------------|:---------|:------------|
| `Authorization`    | `String` | JWT token   |

##### Query parameters

| Parameter        | Type     | Required    | Description                                                              | Example            |
|:-----------------|:---------|:------------|:-------------------------------------------------------------------------|:-------------------|
| `year`           | `int`    | True        | Year of transactions. Acceptable range: `1970` to `2050`                 | `2022`             |
| `month`          | `int`    | True        | Month of transactions. Acceptable range: `1` to `12`                     | `2` (for February) |
| `page_size`      | `int`    | False       | Transactions per page. Default value: `10` Acceptable range: `1` to `25` | `25 `              |
| `base_currency`  | `String` | True        | Base currency to which all credit/debit values will be converted         | `USD` or `CHF` etc |

## Security

All the incoming request goes through `jwtAuthFilter` to authorize the user.  

In case user provides invalid `Authorization` header the request will not be authorized and `403 Unauthorized` response will be issued.  

Only exception is Request matching following patterns: `/api/v1/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**` to allow user registration and allowing access to Swagger-UI api documentation.
## Data

### kafka (producing/consuming)

Transactions are stored in kafka topics by the following principle:

Topic name is determined by `Year` and `Username`  
E.g. topic name = `transactions-Y{YEAR}-{USERNAME}`.  
So for user `P-00000001` all the transactions for `2022` year are stored under kafka topic `transactions-Y2022-P-00000001`

Potentially this approach could lead to millions of topics if we have 100000+ customers, but modern kafka seems to be able to handle it.


Furthermore, each kafka topic is divided into 12 partitions. Each partition represents month, by this we can read directly from the partition for specific month, so we don't need to check for all the records in the topic.

``` 
Partition 0 - January transactions
Partition 1 - February transactions 
...
```

**_Producing_**  
Transaction is published to kafka 
* topic: `transactions-Y{YEAR}-{USERNAME}` 
* partition: `partition: {MONTH}` 
* key: `key: Transaction UUID`  
Note: `MONTH` counts from 0, to 11  



**_Consuming_**  

Transactions are consumed from kafka topic `transactions-Y{YEAR}-{USERNAME}`  
Using query params provided by the user request _(year, month)_ we find topic with the transactions for specified year, then consuming transaction records from partition, which can be determined by the `{MONTH}`

## Logging

Logging implemented by using Spring AOP and aspectj, by creating a PointCuts for custom made `@Loggable` annotation.


```java
@Pointcut(value = "@annotation(com.s8.keeilzhanstd.challenge.annotations.Loggable)");
```

All I need to do, is add `@Loggable` annotation to a method I want to monitor.

Then by using `@Around` annotation provided by aspectj I can log method exectutions, its parameters, and what that method returns.

Note: Logs available only in debug mode, so we need to specify logging level for our packages by configuring application.yml file.

```yaml
logging:
  level:
    root: info
    com:
      s8:
        keeilzhanstd:
          challenge: debug
```

## Testing

Unit and integration test were implemented using `junit` and `testcontainers`.
`testcontainers` was chosen to facilitate the testing of data dependencies etc.


## CI/CD
Linked repository CI service CircleCi: [Pipelines link](https://app.circleci.com/pipelines/github/keeilzhanstd?filter=all&status=canceled&status=failed&status=failing&status=queued&status=running&status=success&status=on_hold)

Configured CircleCi [config.yml](/.circleci/config.yml)

CD: Kubernetes [link]