# Anime Store API

This project is an API for User and Animes management. It provides endpoints to create, read, update and delete users and animes. 

## 🚀 Used tecnologies
- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **MapStruct**
- **Lombok**
- **PostgreSQL**
- **Swagger**

## 📌 How to run locally

### 🛠️ Pre-requisites
Before starting, make sure you have installed:
- **Java 17**
- **Maven**
- **Docker** (optional,so you can run PostgreSQL)

### ⚙️ Configuração do Banco de Dados
If you wish to run the database via **Docker**, use the command:
```bash
docker run --name anime-store-db -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=anime_store -p 5432:5432 -d postgres
```

In case you prefer to configure manually, adjust the credentials on the `application-dev.yml`:
```properties
spring:
    datasource:
    url: jdbc:postgresql://localhost:5432/anime_store
    username: root
    password: root
```

### ▶️ Executando o Projeto
1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/springboot-anime-store.git
   cd springboot-anime-store.git
   ```
2. Compile and run project with:
   ```bash
   mvn spring-boot:run
   ```
3. API will be available in: `http://localhost:8081`

## 🔑 Authentication and Security
A API utiliza **Spring Security** para autenticação baseada em **JWT**. Para acessar os endpoints protegidos:
1. Faça login no endpoint `/auth/login` informando `username` e `password`.
2. No retorno, copie o token JWT e use-o no **Authorization Header** das requisições.
   ```bash
   Authorization: Bearer <seu-token-jwt>
   ```

## 📖 Documentação da API
A documentação interativa do Swagger está disponível em:
🔗 **[Swagger UI](http://localhost:8080/swagger-ui.html)**

## 📌 Principais Endpoints
A lista completa de endpoints está disponível no Swagger, mas aqui estão alguns principais:
- `POST /auth/login` → Autentica um usuário e retorna um token JWT.
- `POST /users` → Cria um novo usuário.
- `GET /users/{id}` → Busca um usuário pelo ID.
- `PUT /users/{id}` → Atualiza um usuário existente.
- `DELETE /users/{id}` → Remove um usuário.

Para mais detalhes, acesse a documentação completa no **Swagger**.

## 🛠️ Contribuição
Fique à vontade para contribuir! Para isso:
1. Faça um **fork** do repositório.
2. Crie uma nova **branch**: `git checkout -b minha-feature`.
3. Faça suas alterações e **commite**: `git commit -m 'Minha nova feature'`.
4. Envie suas alterações: `git push origin minha-feature`.
5. Abra um **Pull Request**.

---

📌 **Anime Store API** - Um projeto para estudo e prática de desenvolvimento backend em Java e Spring Boot. 😊

