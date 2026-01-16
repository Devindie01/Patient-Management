# Patient Management System (Microservices)

A professional-grade healthcare management ecosystem built with **Java 22** and **Spring Boot 4.x**. This project demonstrates a robust microservices architecture secured by **WSO2 Asgardeo** (Identity-as-a-Service).

## 🏗 Architecture Overview

The system follows the **"Security at the Edge"** pattern, where the API Gateway acts as the primary gatekeeper for authentication.

* **Gateway Service:** Orchestrates the OAuth 2.0 / OpenID Connect (OIDC) login flow.
* **Patient Service:** A protected Resource Server that manages patient records.
* **Billing Service:** Handles financial transactions, communicating via high-performance internal protocols.

[Image of Spring Boot Microservices Security Architecture with API Gateway and WSO2 Asgardeo]

## 🔐 Security Features

Integrated with **WSO2 Asgardeo** to ensure enterprise-level security:
* **OIDC Login:** Implements Authorization Code Flow for secure user sign-in.
* **JWT Validation:** Internal services act as Resource Servers, validating JWT tokens issued by Asgardeo.
* **Centralized Identity:** Managed user lifecycles and attributes within the Asgardeo cloud console.
* **Discovery:** Automatically resolves OIDC configuration using the Provider's Issuer URI.

## 🛠 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 22 |
| **Framework** | Spring Boot 4.0.1, Spring Security |
| **Gateway** | Spring Cloud Gateway |
| **IAM Provider** | WSO2 Asgardeo |
| **Messaging** | Apache Kafka |
| **Communication** | gRPC |
| **Build Tool** | Maven |
