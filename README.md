# OilerRig Backend

OilerRig Backend is the Spring Boot–based broker service in our distributed vendor–broker system. It exposes RESTful APIs for managing products and orders and coordinates transactions across external vendor services. The service handles business logic, persistence (via PostgreSQL), authentication, and asynchronous messaging to implement distributed workflows.

## Features

* **RESTful API (Products/Orders):** Exposes CRUD endpoints for products and orders. These are standard Spring MVC/REST controllers supporting JSON requests and responses.
* **Saga Orchestration:** Implements long-running transactions as orchestrated sagas using Azure Service Bus queues.  It sends and receives messages via the Spring Cloud Azure Service Bus starter to coordinate multi-step workflows across services.
* **Authentication (JWT / Auth0):** Secures endpoints using JWT tokens issued by Auth0. Spring Security checks incoming JWTs in the `Authorization` header and validates them against Auth0’s JSON Web Key Set. (User authentication is delegated to Auth0.)
* **Vendor API Orchestration:** Calls external vendor APIs to fulfill orders. These calls are managed (e.g. in parallel or sequentially) by the broker, with vendor-specific DTOs and mapping logic.
* **Resilience (Circuit Breaker & Retry):** Uses Resilience4j to make vendor and messaging calls fault-tolerant.  Annotations like `@CircuitBreaker` and `@Retry` wrap outgoing calls so that failures trigger retries or fallbacks.
* **DTO Mapping (MapStruct):** Automates mapping between domain entities and DTOs using MapStruct, reducing boilerplate. MapStruct generates type-safe mapper code at compile time.
* **API Documentation:** Auto-generated OpenAPI/Swagger documentation (hosted at `/docs` or Swagger UI endpoint) for all REST endpoints. (For example, Springdoc generates a UI at `/swagger-ui.html` loading the OpenAPI JSON from `/v3/api-docs`.)

## Key Dependencies

* **Spring Boot:** Core framework for building the microservice (embedded server, dependency injection, etc.). Spring MVC, Spring Data JPA, Spring Security, etc.
* **PostgreSQL:** The backing relational database (configured via Spring Data JPA/Hibernate).
* **Azure Service Bus:** Used for reliable messaging and saga orchestration.  We use the Spring Cloud Azure Service Bus starter (`com.azure.spring:spring-cloud-azure-starter-servicebus`) to auto-configure Service Bus clients.
* **Docker Compose:** For local development, a `docker-compose.yml` defines the PostgreSQL and application containers. Docker Compose simplifies running multi-container setups.
* **Resilience4j:** Fault-tolerance library providing circuit breakers, retries, rate limiters, etc.  Integrated via Spring Boot starters so you can add annotations like `@CircuitBreaker` on methods.
* **MapStruct:** Code generator for bean-to-bean mapping (e.g. entity-to-DTO). MapStruct automates mapper creation at compile time, producing fast, type-safe mapping code.
* **Auth0 (JWT Security):** Identity-as-a-Service for user auth. We use Spring Security OAuth2 support to validate Auth0 JWTs (issuer and audience) on incoming requests.
* **Springdoc OpenAPI:** Generates the OpenAPI specification and Swagger UI endpoints. With the `springdoc-openapi-starter-webmvc-ui` dependency, the documentation UI is exposed automatically (e.g. at `/swagger-ui.html` or a custom `/docs` path).

## Local Development Setup

Follow these steps to get the backend running locally:

1. **Prerequisites:** Install Java 17+ and Maven. Also install Docker and Docker Compose for running the database and containers.
2. **Configuration:** Copy the example configuration or create a new `application.properties`/`.yml` or `.env` file. Set the key environment variables or properties:

    * Database connection (e.g. `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/oilerrig`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).
    * Auth0 settings (`AUTH0_DOMAIN`, `AUTH0_AUDIENCE` or `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI`, etc.) to point to your Auth0 tenant and API.
    * Azure Service Bus connection (e.g. `AZURE_SERVICEBUS_CONNECTIONSTRING`) for saga messaging.
    * Other app secrets (if any) as needed.

   In the included `docker-compose.yml`, environment variables are used for service configs (e.g. `POSTGRES_USER`, `POSTGRES_PASSWORD` in the `db` service). For example, you might create a `.env` file with DB credentials and reference it from `docker-compose.yml`.  The Compose file defines services and their environments.
3. **Start PostgreSQL:** Use Docker Compose to launch the database. For example, run:

   ```bash
   docker-compose up --build
   ```

   This command builds the `app` image and starts both `db` (Postgres) and `app` containers. The Compose file is a YAML manifest that defines these services and their dependencies. The `environment` section in the YAML provides DB user and password to the container.
4. **SSL Configuration:** (Optional for local dev) Generate a self-signed SSL certificate and configure Spring Boot to use it. For example, create a PKCS12 keystore with `keytool` and set properties like `server.ssl.key-store=classpath:keystore.p12` and `server.ssl.key-store-password=...`. **Note:** Browsers will not trust a self-signed certificate by default. You will see warnings like “ERR\_CERT\_AUTHORITY\_INVALID” when accessing the site. This is expected for internal certs.
5. **Build & Run the App:** You can run the backend jar directly or via Maven. For example:

   ```bash
   mvn clean package
   java -jar target/backend.jar
   ```

   Alternatively, if the `app` service in Docker Compose is set up to build from source, it will start automatically with the `docker-compose up` command above. Ensure that the application picks up the same environment settings (DB, Auth0, etc.) as configured.
6. **Access the APIs:** Once running, the service will listen on the configured port (e.g. `https://localhost:8443` if SSL is enabled, or `http://localhost:8080` otherwise). You can test the REST endpoints (e.g. via `curl` or Postman).

## API Documentation

With the service running, view the OpenAPI/Swagger documentation in your browser. Springdoc makes the API spec available (JSON/YAML) at `/v3/api-docs` and provides a Swagger UI. For example, browse to:

```
https://localhost:<port>/docs
```

(or `/swagger-ui.html`) to see the interactive API documentation. Springdoc automatically serves the UI and JSON specification. The `/docs` path is just the configured Swagger UI endpoint; you can adjust `springdoc.swagger-ui.path` if needed.

## Production Deployment

* **CI/CD (GitHub Actions):** A GitHub Actions workflow is provided to automate builds on each push. The pipeline runs tests, packages the app, and builds a Docker image. You can configure it to push the image to a registry or deploy directly. For example, one Action (`docker-to-azure-vm`) can copy the repository to an Azure VM and run `docker-compose up` there.
* **Azure VM Deployment:** In production we deploy to an Azure Virtual Machine. Ensure Docker (and Docker Compose) are installed on the VM. The GitHub Action can SSH into the VM or use Azure CLI to deploy the containers. For instance, the `docker-to-azure-vm` Action will transfer the code and execute `docker-compose up` on the VM. The VM’s Docker Compose file should be similar to the local one, using environment variables for configuration.
* **Environment Secrets:** All sensitive values (database URIs, credentials, Auth0 client IDs/URLs, Azure service bus keys, etc.) must be stored as protected secrets. In GitHub Actions, set these as repository Secrets (for example `AZURE_ARM_CLIENT_ID`, `AZURE_SERVICEBUS_CONNECTIONSTRING`, etc. as shown). These secrets are injected into the workflow and can be referenced by the Docker Compose file or application (for example by writing them into a `.env` file on the VM or by setting them in the service environment).
* **SSL Certificates:** In production, use a valid TLS certificate (for example via Let’s Encrypt or an organizational CA) rather than the self-signed cert used in development. Browsers will display a security warning if the certificate is not from a trusted authority. Make sure `server.ssl.key-store` is configured with the production keystore, and bind to port 443 if needed.

Each deployment step should verify that the service starts correctly on the VM and that all environment variables (DB, Auth0, Service Bus, etc.) are correctly set. The service can then be accessed securely by clients (without browser certificate warnings if a proper SSL cert is in place).

**Sources:** The above setup follows standard Spring Boot, Docker, and Azure deployment practices. For example, Spring Cloud Azure provides the Service Bus starter for messaging, Auth0 documents JWT validation in Spring, and Resilience4j usage with Spring Boot is documented in the Resilience4j guide.
