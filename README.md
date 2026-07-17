# SFF E-Commerce - Java Spring Boot Application

SFF E-Commerce is a backend e-commerce platform built using a microservices architecture with Java and Spring Boot. The project utilizes independent service communication through REST APIs, asynchronous messaging, service discovery, caching, and centralized routing.

The application includes authentication, product management, inventory management, order processing, payment handling, email notifications, and API documentation. Each service is independently deployable and containerized with Docker.

## Features

### Authentication & Authorization

- JWT-based authentication
- Role-based authorization
- Secure communication between services

### User Management

- User registration and login
- Role management
- Automatic sample user creation

### Product Management

- Create, update and remove products
- Product categorization
- Seller ownership validation
- Redis caching for frequently requested products

### Inventory Management

- Inventory creation and updates
- Automatic inventory creation for newly created products
- Stock increase and decrease operations
- Stock validation

### Order Management

- Create orders
- View personal orders
- Cancel pending orders
- Order status management

### Payment Management

- Process payments
- Payment history
- Payment status updates
- Automatic order payment synchronization

### Notification System

- Welcome email after registration
- Order confirmation email after successful order creation

### API Documentation

- Swagger/OpenAPI documentation for every service

## Architecture

The project consists of the following microservices:

- Eureka Server
- API Gateway
- User Service
- Product Service
- Inventory Service
- Order Service
- Payment Service
- Notification Service

All services register themselves with Eureka and communicate through OpenFeign clients or RabbitMQ depending on the use case.

## Service Responsibilities

### Eureka Server

- Service discovery
- Registers all microservices

### API Gateway

- Single entry point for all client requests
- JWT validation
- Request routing

### User Service

- User registration
- Login
- JWT generation
- User management

### Product Service

- Product CRUD operations
- Product ownership validation
- Redis caching
- Publishes product creation events

### Inventory Service

- Inventory management
- Stock validation
- Automatically creates inventory for newly created products

### Order Service

- Order creation
- Order cancellation
- Order management
- Publishes order creation events

### Payment Service

- Payment processing
- Payment history
- Publishes payment completed events

### Notification Service

- Consumes RabbitMQ events
- Sends welcome emails
- Sends order confirmation emails

## RabbitMQ Communication

The application uses asynchronous communication through RabbitMQ.

| Producer | Consumer | Purpose |
|----------|----------|---------|
| User Service | Notification Service | Send welcome email after user registration |
| Product Service | Inventory Service | Automatically create inventory for newly created products |
| Order Service | Notification Service | Send order confirmation email |
| Payment Service | Order Service | Mark orders as paid |

## Redis Caching

Redis is used by the Product Service to cache frequently requested data.

The following data is cached:

- Product by ID
- All products
- Products by category
- Products by seller
- Current user's products

The cache is automatically invalidated whenever a product is created, updated, or removed.

## Sample Data

To make the application ready for immediate use, several services populate the database during startup.

The following services contain a `DataInitializer`:

- User Service
- Product Service
- Inventory Service

This automatically creates example users, products, and inventory records, allowing the application to be tested immediately without manual setup.

## Technologies

- Java
- Spring Boot
- Spring Security
- JWT Authentication & Authorization
- Spring Cloud
- Eureka Service Discovery
- Spring Cloud Gateway
- OpenFeign
- RabbitMQ
- Redis
- PostgreSQL
- Spring Data JPA
- Hibernate
- Docker
- Docker Compose
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Maven

## Installation

### Clone Repository

```bash
git clone https://github.com/denizbyrk/Spring-Boot-SFF-ECommerce-App.git

cd Spring-Boot-SFF-ECommerce-App
```

### Start the Application

The project includes a Docker Compose configuration that starts every required service, including PostgreSQL, RabbitMQ and Redis.

```bash
docker compose up --build
```

After startup, all services register automatically with Eureka.

## Docker

Docker Compose containerizes the complete application including:

- PostgreSQL
- RabbitMQ
- Redis
- Eureka Server
- API Gateway
- User Service
- Product Service
- Inventory Service
- Order Service
- Payment Service
- Notification Service

A single command is enough to build and run the entire system.

## Swagger Documentation

Each microservice exposes its own Swagger/OpenAPI documentation.

Swagger screenshots and API examples will be added here.

## Unit Testing

The project includes unit tests using:

- JUnit 5
- Mockito

Service layer business logic is tested independently by mocking external dependencies such as repositories, OpenFeign clients, RabbitMQ producers, and other external components.
