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

| Endpoint                                     | Method   | Description | 
|:---------------------------------------------|:---------|:-----------| 
| [`/api/v1/auth/register`](#register)         | POST   | Register user and retrieve jwt |
| [`/api/v1/auth/authenticate`](#authenticate) | POST  | Authenticate user and retrieve jwt |
| [`/api/v1/transactions`](#post-transaction)  | POST   | Publish transaction to kafka |
| [`/api/v1/transactions`](#get-transactions)  | GET | Retrieve transactions for month from kafka |

___

#### Register
```http
POST /api/v1/auth/register
```

##### Request body

`(Content-Type: application/json)`

| Parameter        | Type     | Description       | Example |
|:-----------------|:---------|:------------------|:-----------|
| `username` | `String` | Username for user | ex: "P-000000001"
| `password` | `String` | Password for user | ex: "Password_pwd"


##### Response

| Code        | Schema                     | Description                              |
|:-----------------|:---------------------------|:-----------------------------------------|
| 200 | ```{"token": "string"} ``` | Returns jwt token for newly created user |
 | 403 | `Forbidden`                | User already exist                       |

___

#### Authenticate

```http
POST /api/v1/auth/authenticate
```

##### Request body

`(Content-Type: application/json)`

| Parameter        | Type     | Description       | Example |
|:-----------------|:---------|:------------------|:-----------|
| `username` | `String` | Username for user | ex: "P-000000001"
| `password` | `String` | Password for user | ex: "Password_pwd"



##### Response

| Code        | Schema                     | Description                |
|:-----------------|:---------------------------|:---------------------------|
| 200 | ```{"token": "string"} ``` | Returns jwt token for user |
| 403 | `Forbidden`                | Bad credentials            |

___

#### POST Transaction
```http
POST /api/v1/transactions
```

##### Headers

| Parameter        | Type     | Description                                             |
|:-----------------|:---------|:--------------------------------------------------------|
| `Authorization` | `String` | JWT token |

##### Request body

`(Content-Type: application/json)`

| Parameter  | Type                 | Description                                 | Example         |
|:-----------|:---------------------|:--------------------------------------------|:----------------|
| `amount`   | `double`             | Amount spend or deposited (Can be negative) | -100.0 or 100.0 |
| `currency` | `String`             | Currency used                               | USD or CHF etc  |
| `iban`     | `String`             | User iban account number                    | HK93-0000-0000-0000-0021-1  |
| `date`     | `String($date-time)` | YYYY-MM-DD format                           | "2023-03-02T03:58:04.493Z"  |

___

#### GET Transactions
```http
GET /api/v1/transactions
```

##### Headers

| Parameter        | Type     | Description                                             |
|:-----------------|:---------|:--------------------------------------------------------|
| `Authorization` | `String` | JWT token |

##### Query String

| Parameter       | Type     | Required   | Description                                               | Example            |
|:----------------|:---------|:--------|:----------------------------------------------------------|:-------------------|
| `year`          | `int`    | True  |  Year of transactions. Acceptable range: `1970` to `2050` | `2022`             |
| `month`         | `int`    | True|  Month of transactions. Acceptable range: `1` to `12`                     | `2` (for February) |
| `page_size`     | `int`    | False|  Transactions per page. Default value: `10` Acceptable range: `1` to `25` | `25 `              |
| `base_currency` | `String` | True |  Base currency to which all credit/debit values will be converted | `USD` or `CHF` etc |

## Security

## Data

## Logging

## Testing
