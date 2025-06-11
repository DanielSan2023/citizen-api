# Citizen API

Citizen API is a RESTful Jakarta EE application for managing citizens and their associated documents.  
It uses Open Liberty server with Jakarta EE 10, PostgreSQL database running in Docker, and provides a secure,
transactional API to create, read, citizen records and their documents.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- IDE of y- IDE of your choice (IntelliJ IDEA, Eclipse, VS Code, etc.)- Docker Desktop:
- Docker Desktop (for running PostgreSQL and the application in containers)
  [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)  choice for your OS

## Architecture and Technologies

| Category              | Technology / Tool            | Description                                     |
|-----------------------|------------------------------|-------------------------------------------------|
| **Java EE Server**    | Open Liberty (Jakarta EE 10) | Lightweight, production-ready Java EE server    |
| **Database**          | PostgreSQL 15 (Docker)       | Relational database in a Docker container       |
| **Build Tool**        | Maven                        | Dependency management and build automation      |
| **JPA Provider**      | Hibernate / EclipseLink      | ORM for database persistence                    |
| **API Documentation** | MicroProfile OpenAPI         | Auto-generated API docs with Swagger UI         |
| **Testing**           | JUnit 5, Mockito             | Unit testing and mocking                        |
| **Containerization**  | Docker Compose               | Running services in isolated network containers |
| **Mapping**           | MapStruct                    | DTO-entity mapping                              |
| **Other APIs**        | Jakarta Batch, Mail API      | Batch processing and email support              |

## Docker Setup

The project is designed to run both the **citizen-api** and the **PostgreSQL** database within the same Docker network
for seamless communication.

### Docker Compose services:

- **db** (PostgreSQL 15)
    - Database: `citizens`
    - User: `postgres`
    - Password: `postgres`
    - Exposed port: `5432`

- **citizen-api**
    - Open Liberty Jakarta EE server
    - HTTP port: `9080`
    - HTTPS port: `9443`
    - Depends on the `db` service
    - Uses JNDI datasource `jdbc/citizenDS` pointing to PostgreSQL

### Docker network

Both containers run on a custom Docker network named `backend` for inter-service communication.

---

## 🔨 Run the App

1. **Build the project**

```bash
mvn clean package
```

2. **Start Docker Compose**

```bash 
docker-compose up --build
```

3. **Access the application:**
   -Open Liberty server HTTP endpoint: http://localhost:9080/
   -Open Liberty server HTTPS endpoint: https://localhost:9443/
   -Swagger/OpenAPI UI: http://localhost:9080/openapi/ui (or as configured in Open Liberty)

4. **Configuration Highlights**
    - Open Liberty server.xml configures Jakarta EE 10 features including batch processing and OpenAPI.

- JNDI datasource jdbc/citizenDS connects to PostgreSQL at hostname postgres-db (the container name).

- Security realm and batch job roles are defined for batch processing authorization.

- PostgreSQL JDBC driver included in the server resources.

📦 PostgreSQL-Docker Container – Table Design

🧾 Citizen entity

| Field         | Type   | Description                           |
|---------------|--------|---------------------------------------|
| `id`          | Long   | Primary key                           |
| `firstName`   | String | Citizen's first name                  |
| `lastName`    | String | Citizen's last name                   |
| `birthNumber` | String | Unique personal identification number |

🧾 Document entity

| Field         | Type      | Description                            |
|---------------|-----------|----------------------------------------|
| `id`          | Long      | Primary key                            |
| `number`      | String    | Document number                        |
| `type`        | Enum      | Document type (passport, ID, etc.)     |
| `dateOfIssue` | LocalDate | Date of issue                          |
| `expiryDate`  | LocalDate | Expiry date                            |
| `status`      | Enum      | Document status (valid, expired, etc.) |
| `citizen`     | Citizen   | Associated citizen                     |

# 📚 API Endpoints

- POST /citizens - Create new citizen
- GET /citizens - Get all citizens
- GET /citizens/{id} - Get citizen details with documents
- GET /citizens/birthNumber/{birthNumber} - Find citizen by birth number
- POST /citizens/{birthNumber}/documents - Add a document for a citizen

## License

This project is provided for demonstration and learning purposes.

## Contact

For any questions, please contact:  
[Daniel](mailto:electronic.san@gmail.com)

