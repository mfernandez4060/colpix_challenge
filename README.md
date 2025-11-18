# Colpix Challenge — RESTful API

API REST construida en **Java 21**, **Spring Boot 3**, con autenticación JWT, jerarquía de empleados, Docker, testing avanzado y arquitectura modular.

---

## 📑 Tabla de Contenidos
1. Descripción General  
2. Tecnologías  
3. Arquitectura  
4. Requisitos  
5. Instalación y Ejecución Local  
6. Ejecución con Docker  
7. Autenticación JWT  
8. Endpoints de la API  
9. Estructura del Proyecto  
10. Ejecución de Tests  
11. Errores Estándar  
12. Troubleshooting  

---

# 🧾 Descripción General

Este proyecto implementa una API REST para administrar empleados, autenticación mediante JWT, jerarquías de supervisión y actualización segura de contraseñas.

---

# 🛠 Tecnologías

- Java 21  
- Spring Boot 3.3  
- Spring Security / JWT  
- Maven  
- Docker / Docker Compose  
- H2 Database  
- JUnit 5 + Mockito  
- SLF4J + MDC  

---

# 🏗 Arquitectura

```
src/main/java/com/colpix/challenge
├── config            # JWT, filtros, auditoría
├── controller        # Endpoints REST
├── entity            # Entidades JPA
├── exception         # Excepciones + Handler global
├── repository        # Persistencia con Spring Data JPA
├── service           # Lógica de negocio
└── ChallengeApplication.java
```

---

# 📦 Requisitos

- Java 21  
- Maven 3.9+  
- Docker 20+  

---

# 💻 Instalación y Ejecución Local

```bash
git clone https://github.com/usuario/colpix_challenge.git
cd colpix_challenge
mvn clean package
java -jar target/challenge-0.0.1-SNAPSHOT.jar
```

---

# 🐳 Ejecución con Docker

```bash
docker compose build --no-cache
docker compose up
```

---

# 📌 Show swagger UI

```
http://localhost:8080/swagger-ui/index.html
```
---

# 🔐 Autenticación JWT

## POST /login

Request:
```json
{
  "username": "admin",
  "password": "password"
}
```

Response:
```json
{
  "token": "xxxxx.yyyyy.zzzzz",
  "expiresIn": 300000
}
```

---

# 🧭 Endpoints de la API
```

| Método  | Endpoint                | Descripción                              |
|---------|-------------------------|------------------------------------------|
| POST    | `/employees`            | Crear empleado                           |
| PUT     | `/employees`            | Actualizar empleado                      |
| GET     | `/employees`            | Listar todos los empleados               |
| GET     | `/employees/{id}`       | Obtener detalle + subordinados           |
| DELTETE | `/employees/{id}`       | Borra empleado                           |
| PUT     | `/employees/password`   | Cambiar contraseña del empleado logueado |
```
---

# 🧪 Ejecución de Tests

```bash
mvn test
mvn test jacoco:report
```

---

# ⚠️ Errores Estándar

```json
{
  "code": "AUTH_ERROR",
  "message": "Invalid credentials",
  "traceId": "a1d23c4f-bf20-4e3d-a0b4-c31f52b8a2f6",
  "timestamp": "2025-11-17T14:25:22.451Z"
}
```
