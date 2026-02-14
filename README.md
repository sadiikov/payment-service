# Payment Service
Asynchronous payment processing service built with Java and Spring Boot

## Overview
This service accepts payment requests, processes them asynchronously, and updates user balances safely using transactions and optimistic locking

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
