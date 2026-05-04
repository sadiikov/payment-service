# Payment Service
Asynchronous payment processing service built with Java and Spring Boot

## Overview
This service accepts payment requests, processes them asynchronously, and updates user balances safely using transactions and optimistic locking

## Features
- Asynchronious payment processing
- Wallet balance handling
- Optimistic locking
- Refund functionality
- Background worker (for stucked payments)
- API versioning
- Basic security

## Payment flow
1. Client sends POST /payments
2. Payment is stored with NEW status
3. Async processing starts
4. Wallet balance updates (if there is enough money)
5. Status changes to SUCCESS or FAILED
6. Events are published
7. If there is unprocessed payments, worker will repair their status

## API
- POST /api/v1/payments
- GET /api/v1/payments/{id}
- POST /api/v1/payments/refund/{id}

## Stack
- Java 17
- Spring Boot
- Spring Data Jpa
- PosthreSQL
- @Async processing
- Lombok

# Architecture
1. Clients sends POST /api/v1/payments
2. Payment is saved with status NEW
3. Processing starts asynchronously
4. Status changes: NEW -> PENDING -> SUCCESS / FAILED
5. Controller -> Service -> Async processor -> Wallet -> Database
