# Anime Store API

This project is an API for User and Anime management. It provides endpoints to create, read, update, and delete users and anime.

## 🚀 Used Technologies
- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **MapStruct**
- **Lombok**
- **PostgreSQL**
- **Swagger**

## 📌 How to Run Locally

### 🛠️ Prerequisites
Before starting, make sure you have installed:
- **Java 21**
- **Maven**
- **Docker** (optional, so you can run PostgreSQL)

### ⚙️ Database Configuration
If you wish to run the database via **Docker**, use the command:
```bash
docker run --name anime-store-db -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=anime_store -p 5432:5432 -d postgres
```

If you prefer to configure it manually, adjust the credentials in `application-dev.yml`:
```properties
spring:
    datasource:
    url: jdbc:postgresql://localhost:5432/anime_store
    username: root
    password: root
```

### ▶️ Running the Project
1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/springboot-anime-store.git
   cd springboot-anime-store.git
   ```
2. Compile and run the project with:
   ```bash
   mvn spring-boot:run
   ```
3. The API will be available at: `http://localhost:8081`

## 🔑 Authentication and Security
The API uses **Spring Security** for **Basic Authentication**. To access protected endpoints:
1. Create your user through the `/users` endpoint by providing `name`, `username`, `authorities`, and `password`.
2. Use the `username` and `password` in the Basic Authentication header.

## 📚 API Documentation
The interactive Swagger documentation is available at:
🔗 **[Swagger UI](http://localhost:8081/swagger-ui/index.html)**

## 📌 Main Endpoints
The complete list of endpoints is available in Swagger, but here are some key ones:
- `POST /users` → Creates a new user.
- `GET /users/{id}` → Retrieves a user by ID.
- `PUT /users/{id}` → Updates an existing user.
- `DELETE /users/{id}` → Deletes a user.

For more details, check the full documentation in **Swagger**.

---

📌 **Anime Store API** - A project for practicing backend development with Java and Spring Boot.

