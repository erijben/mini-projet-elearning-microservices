# E-Learning Microservices Platform

Plateforme e-learning construite avec une architecture microservices Spring Boot / Spring Cloud.

---

## Architecture

```
                        ┌─────────────────┐
                        │  discovery-service │  :8761 (Eureka)
                        └────────┬────────┘
                                 │ enregistrement
          ┌──────────────────────┼──────────────────────┐
          │                      │                       │
   ┌──────▼──────┐       ┌───────▼──────┐       ┌───────▼──────┐
   │ api-gateway │       │ auth-service │       │course-service│
   │   :8080     │       │   :8081      │       │   :8082      │
   └──────┬──────┘       └──────────────┘       └───────┬──────┘
          │ route /courses/**                            │ RabbitMQ
          │                                    ┌─────────▼────────┐
   ┌──────▼──────────┐                         │notification-svc  │
   │enrollment-svc   │                         │   :8084          │
   │   :8083         │                         └──────────────────┘
   └─────────────────┘
```

---

## Services

| Service | Port | Rôle |
|---|---|---|
| `discovery-service` | 8761 | Registre Eureka — point de découverte de tous les services |
| `api-gateway` | 8080 | Point d'entrée unique, routage via load balancer Eureka |
| `auth-service` | 8081 | Inscription / connexion, génération de tokens JWT |
| `course-service` | 8082 | CRUD cours et modules, publication d'événements RabbitMQ |
| `enrollment-service` | 8083 | Inscription des étudiants aux cours, validation JWT |
| `notification-service` | 8084 | Consommation des événements RabbitMQ, stockage des notifications |

---

## Stack technique

- **Java 21** (17 pour enrollment-service)
- **Spring Boot 3.2.x / 3.3.x**
- **Spring Cloud 2023/2024** — Eureka, Gateway
- **Spring Security + JWT** (jjwt 0.11.5)
- **Spring AMQP / RabbitMQ** — communication asynchrone
- **Spring Data JPA + H2** — base de données en mémoire par service
- **Lombok** — réduction du boilerplate
- **Maven** — build tool

---

## Prérequis

- Java 17 ou 21
- Maven 3.8+
- Docker (pour RabbitMQ)

---

## Démarrage

### 1. Lancer RabbitMQ

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

Interface de gestion disponible sur `http://localhost:15672` (guest / guest).

### 2. Lancer les services

Respecter cet ordre de démarrage :

```bash
# 1. Discovery (Eureka doit être up en premier)
cd discovery-service && ./mvnw spring-boot:run

# 2. Auth
cd auth-service && ./mvnw spring-boot:run

# 3. Course
cd course-service && ./mvnw spring-boot:run

# 4. Enrollment
cd enrollment-service && ./mvnw spring-boot:run

# 5. Notification
cd notification-service && ./mvnw spring-boot:run

# 6. API Gateway (en dernier, après que les services soient enregistrés dans Eureka)
cd api-gateway && ./mvnw spring-boot:run
```

---

## API Reference

### Auth Service — `http://localhost:8081`

#### Inscription
```http
POST /auth/register
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```

Réponse :
```json
{ "id": 1, "username": "john" }
```

#### Connexion
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```

Réponse :
```json
{ "token": "<JWT>" }
```

---

### Course Service — `http://localhost:8082` (ou via gateway `http://localhost:8080`)

#### Créer un cours
```http
POST /courses
Content-Type: application/json

{
  "title": "Spring Boot Avancé",
  "description": "Microservices avec Spring Cloud",
  "teacherId": "teacher-1",
  "level": "ADVANCED",
  "modules": [
    { "title": "Introduction", "content": "Contenu du module 1" },
    { "title": "Eureka & Gateway", "content": "Contenu du module 2" }
  ]
}
```

#### Lister tous les cours
```http
GET /courses
```

#### Récupérer un cours par ID
```http
GET /courses/{id}
```

#### Modifier un cours
```http
PUT /courses/{id}
Content-Type: application/json

{
  "title": "Nouveau titre",
  "description": "Nouvelle description",
  "teacherId": "teacher-1",
  "level": "BEGINNER"
}
```

#### Supprimer un cours
```http
DELETE /courses/{id}
```

---

### Enrollment Service — `http://localhost:8083`

> Toutes les requêtes nécessitent un header `Authorization: Bearer <JWT>`

#### S'inscrire à un cours
```http
POST /enrollments
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "courseId": 1
}
```

#### Mes inscriptions
```http
GET /enrollments/me
Authorization: Bearer <JWT>
```

---

### Notification Service — `http://localhost:8084`

#### Toutes les notifications
```http
GET /notifications
```

#### Notifications par étudiant
```http
GET /notifications/by-student/{studentId}
```

#### Notifications par cours
```http
GET /notifications/by-course/{courseId}
```

---

## Flux de données

### Authentification
```
Client → POST /auth/login → auth-service → JWT token
Client → requête + JWT → enrollment-service → validation signature JWT locale
```

### Création de cours avec modules
```
Client → POST /courses → course-service
  → sauvegarde en BDD
  → publie message RabbitMQ (queue: "new-module") pour chaque module
  → notification-service consomme le message
  → sauvegarde une Notification en BDD
```

### Routage via Gateway
```
Client → GET http://localhost:8080/courses/** 
  → api-gateway 
  → lb://course-service (résolution Eureka) 
  → course-service:8082
```

---

## Consoles H2

Chaque service expose sa base de données en mémoire :

| Service | URL |
|---|---|
| auth-service | `http://localhost:8081/h2-console` — JDBC: `jdbc:h2:mem:authdb` |
| course-service | `http://localhost:8082/h2-console` — JDBC: `jdbc:h2:mem:coursedb` |
| enrollment-service | `http://localhost:8083/h2-console` — JDBC: `jdbc:h2:mem:enrolldb` |
| notification-service | `http://localhost:8084/h2-console` — JDBC: `jdbc:h2:mem:notifdb` |

Identifiants : `sa` / *(pas de mot de passe)*

---

## Eureka Dashboard

`http://localhost:8761` — visualise tous les services enregistrés.

---

## Configuration JWT

Le secret JWT est partagé entre `auth-service` et `enrollment-service`. Il doit être identique dans les deux `application.properties` :

```properties
app.jwt.secret=K3yVeryS3cret_ChangeThisToASecureRandomString_AtLeast_32_bytes_long!
app.jwt.expiration-ms=86400000  # 24h
```

> En production, externaliser ce secret via des variables d'environnement ou un vault.

---

## Structure du projet

```
.
├── discovery-service/       # Eureka Server
├── api-gateway/             # Spring Cloud Gateway
├── auth-service/            # Authentification + JWT
├── course-service/          # Gestion des cours et modules
├── enrollment-service/      # Inscriptions étudiants
└── notification-service/    # Notifications via RabbitMQ
```
