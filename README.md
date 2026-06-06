# RECALL API

> **EN** | [ES](#recall-api-es)

RECALL (*RECALL Enables Contextual And Language Learning*) — a backend REST API for a language learning application inspired by **Anki**, designed to simplify foreign language acquisition through vocabulary management, translations, synonyms, and spaced repetition logic.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot |
| Security | Spring Security · JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA · Hibernate |
| Mapping | MapStruct |
| Boilerplate reduction | Lombok |
| Testing | Mockito · MockMvc · JUnit |
| Documentation | Swagger / OpenAPI |
| Deployment | Docker |

---

## Features

- **JWT Authentication** — Secure login and registration with token refresh via HTTP-only cookies
- **Word Management** — Create, read, update and delete vocabulary entries with pagination
- **Translations** — Associate multiple translations per word with full CRUD support
- **Synonyms** — Many-to-many synonym relationships with `@BatchSize` optimization to avoid N+1 queries
- **User ownership** — Each word is tied to its owner; users only access their own data
- **Global Exception Handling** — Centralized error management with `@ControllerAdvice` and custom exceptions
- **Pagination** — All list endpoints support `pageNo` / `pageSize` parameters via Spring Data `Pageable`
- **Tests** — Unit tests (Mockito) and integration tests (MockMvc) for services and controllers
- **API Docs** — Interactive documentation available via Swagger UI

---

## Architecture

```
src/
├── controller/       # REST controllers (GET, POST, PUT, DELETE)
├── service/          # Service interfaces + implementations
├── repository/       # Spring Data JPA repositories with custom JPQL queries
├── model/            # JPA entities with relationships
├── dto/              # Data Transfer Objects
├── mapper/           # MapStruct mappers
├── security/         # JWT filter, UserDetailsService, SecurityConfig
├── exception/        # Custom exceptions + GlobalExceptionHandler
└── config/           # PasswordEncoder, CORS, etc.
```

---

## Key Technical Decisions

**Lazy loading + @BatchSize** — Synonym collections use `@ManyToMany` with `@BatchSize(size = 20)` to batch SQL queries and avoid the N+1 problem.

**JWT with refresh via cookies** — Access token is short-lived; refresh token is stored in an HTTP-only cookie for security.

**Interface / Implementation separation** — Every service is defined as an interface and implemented separately, following SOLID principles.

**PageResponse wrapper** — All paginated endpoints return a consistent `PageResponse<T>` object with `content`, `pageNo`, `pageSize`, `totalPages`, `totalElements`, and `isLast`.

---

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT |
| `POST` | `/api/auth/refresh` | Refresh access token |
| `GET` | `/api/words?pageNo=0&pageSize=10` | Get paginated word list |
| `POST` | `/api/words` | Create a new word |
| `PUT` | `/api/words/{id}` | Update a word |
| `DELETE` | `/api/words/{id}` | Delete a word |
| `GET` | `/api/words/{id}/translations` | Get translations for a word |
| `POST` | `/api/words/{id}/translations` | Add a translation |
| `GET` | `/api/words/{id}/synonyms` | Get synonyms |

> Full interactive docs available at `/swagger-ui.html` when running locally.

---

## Author

**Dmytro Shyshkin**
DAM Student · Java Backend Developer
[LinkedIn](https://www.linkedin.com/in/dmytro-shyshkin) · [GitHub](https://github.com/DmytroShyshkin)

---
---

# RECALL API (ES)

> [EN](#recall-api) | **ES**

RECALL (*RECALL Enables Contextual And Language Learning*) — API REST backend para una aplicación de aprendizaje de idiomas inspirada en **Anki**, diseñada para simplificar la adquisición de vocabulario en lenguas extranjeras mediante gestión de palabras, traducciones, sinónimos y lógica de repaso espaciado.

---

## Stack Tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17+ |
| Framework | Spring Boot |
| Seguridad | Spring Security · JWT |
| Base de datos | PostgreSQL |
| ORM | Spring Data JPA · Hibernate |
| Mapeo | MapStruct |
| Reducción de boilerplate | Lombok |
| Testing | Mockito · MockMvc · JUnit |
| Documentación | Swagger / OpenAPI |
| Despliegue | Docker |

---

## Funcionalidades

- **Autenticación JWT** — Login y registro seguros con refresco de token via cookies HTTP-only
- **Gestión de palabras** — CRUD completo de vocabulario con paginación
- **Traducciones** — Múltiples traducciones por palabra con soporte CRUD completo
- **Sinónimos** — Relaciones many-to-many con optimización `@BatchSize` para evitar el problema N+1
- **Propiedad por usuario** — Cada palabra está vinculada a su propietario; los usuarios solo acceden a sus propios datos
- **Manejo global de errores** — Gestión centralizada con `@ControllerAdvice` y excepciones personalizadas
- **Paginación** — Todos los endpoints de listas soportan parámetros `pageNo` / `pageSize` via `Pageable`
- **Tests** — Pruebas unitarias (Mockito) y de integración (MockMvc) para servicios y controladores
- **Documentación API** — Documentación interactiva disponible en Swagger UI

---

## Arquitectura

```
src/
├── controller/       # Controladores REST (GET, POST, PUT, DELETE)
├── service/          # Interfaces de servicio + implementaciones
├── repository/       # Repositorios JPA con consultas JPQL personalizadas
├── model/            # Entidades JPA con relaciones
├── dto/              # Data Transfer Objects
├── mapper/           # Mappers con MapStruct
├── security/         # Filtro JWT, UserDetailsService, SecurityConfig
├── exception/        # Excepciones personalizadas + GlobalExceptionHandler
└── config/           # PasswordEncoder, CORS, etc.
```

---

## Decisiones Técnicas Clave

**Lazy loading + @BatchSize** — Las colecciones de sinónimos usan `@ManyToMany` con `@BatchSize(size = 20)` para agrupar consultas SQL y evitar el problema N+1.

**JWT con refresco via cookies** — El token de acceso es de corta duración; el token de refresco se almacena en una cookie HTTP-only por seguridad.

**Separación interfaz / implementación** — Cada servicio se define como interfaz y se implementa por separado, siguiendo los principios SOLID.

**Wrapper PageResponse** — Todos los endpoints paginados devuelven un objeto `PageResponse<T>` consistente con `content`, `pageNo`, `pageSize`, `totalPages`, `totalElements` e `isLast`.

---

## Resumen de la API

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/auth/register` | Registrar nuevo usuario |
| `POST` | `/api/auth/login` | Login y obtener JWT |
| `POST` | `/api/auth/refresh` | Refrescar token de acceso |
| `GET` | `/api/words?pageNo=0&pageSize=10` | Obtener lista paginada de palabras |
| `POST` | `/api/words` | Crear nueva palabra |
| `PUT` | `/api/words/{id}` | Actualizar una palabra |
| `DELETE` | `/api/words/{id}` | Eliminar una palabra |
| `GET` | `/api/words/{id}/translations` | Obtener traducciones de una palabra |
| `POST` | `/api/words/{id}/translations` | Añadir una traducción |
| `GET` | `/api/words/{id}/synonyms` | Obtener sinónimos |

> Documentación interactiva completa disponible en `/swagger-ui.html` al ejecutar localmente.

---

## Autor

**Dmytro Shyshkin**
Estudiante DAM · Java Backend Developer
[LinkedIn](https://www.linkedin.com/in/dmytro-shyshkin) · [GitHub](https://github.com/DmytroShyshkin)
