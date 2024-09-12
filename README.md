# Money Transfer Service

## Overview

This project implements a **Money Transfer Service** API that supports secure and isolated account operations, including money transactions with high accuracy. The service features robust authentication, caching, validation, and exception handling, with integration into a message queue system for notifications.

## Features

- **Account Operations**: Create, update, and manage accounts securely.
- **Transaction Management**: Process financial transactions between accounts with **high-level isolation** to prevent issues like race conditions and reapetabtable read problem.
- **Authentication**: Utilizes **JWT (JSON Web Token)** for access and refresh tokens to ensure secure access to endpoints.
- **Caching**: Uses **Redis** to cache frequent queries.
- **Exception Handling**:  validation and exception handling to ensure system robustness.
- **Message Queue**: Sends transaction notifications through **RabbitMQ** to a notification service.
- **Database**: Stores account and transaction information using **PostgreSQL** with **Hibernate** ORM.
- **Unit Testing**: Thoroughly tested with unit tests to ensure functionality.
- **Spring Boot Framework**: Built using Spring Boot for fast and efficient development.
- **Spring Security**: Manages access control and security aspects of the service.
  
## Tech Stack

- **Spring Boot**: For building and running the RESTful service.
- **Spring Security**: To handle authentication and authorization using JWT tokens.
- **Hibernate**: ORM for database interaction.
- **Redis**: For caching and fast data retrieval.
- **RabbitMQ**: To publish messages for the notification service.
- **PostgreSQL**: Relational database for storing account and transaction details.
- **JUnit**: For unit testing.
- **JWT (JSON Web Token)**: For secure token-based authentication.
  
## Prerequisites

1. **Java 17** or later
2. **PostgreSQL 13** or later
3. **Redis** server
4. **RabbitMQ** server
5. **Maven** or **Gradle** for building the project

## Deployment 
 deployed using azure for app services 
