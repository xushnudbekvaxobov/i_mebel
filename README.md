# iMebel Backend

## 📌 Description

iMebel is a comprehensive furniture e-commerce backend API that provides complete functionality for managing furniture stores, products, categories, and user authentication. 
The system features secure JWT-based authentication, email verification, image management via Cloudinary, and RESTful API endpoints.

## ⚙️ Tech Stack

* Java 17
* Spring Boot 4.0.3
* Spring Security
* Spring Data JPA
* PostgreSQL
* JWT (JSON Web Token)
* Cloudinary
* Flyway (Database Migration)
* Swagger/SpringDoc OpenAPI
* Lombok
* Spring Mail
* Maven

## 🚀 How to Run

### 1. Clone repository

```bash
git clone https://github.com/xushnudbekvaxobov/i_mebel.git
cd i_mebel
```

### 2. Configure environment

Create `application-dev.properties` with your credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/imebel
spring.datasource.username=postgres
spring.datasource.password=yourPassword

# JWT Configuration
jwt.secret.key=qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvtrghbtrbrtbntrnrtgwnwrnwnbnm
jwt.expiration=3600000

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Cloudinary Configuration
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```


### 3. Run with Docker (recommended)

```bash
docker-compose up -d
```

### 4. Or run manually

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

## 📡 API Documentation

**Swagger UI:**

**Local:**
```
http://localhost:8080/swagger-ui/index.html
```

**Production Server:**
```
https://imebel-akg0dfa7f7ffhube.norwayeast-01.azurewebsites.net/swagger-ui/index.html
```

## 👤 Demo Account

* Email: `salomxacker@gmail.com`
* Password: `admin123`



