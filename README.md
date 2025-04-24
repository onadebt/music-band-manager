# 🎸 music-band-manager

## 📖 Description
The web application allows one music band to manage their activities.

## 🧩 Microservices Description
### User Management Service
- Handles authentication and authorization
- Stores user profiles and their role in the system
- Allows team members to accept / reject offers, view planned activities and their bandmates

### Music Catalog Service
- Stores song details
- Manages albums and songs
- Allows adding, editing, deleting albums and songs

### Band Management Service
- Stores band details
- Handles band creatin and management
- Manages the hiring process (sending invitations to team members)

### Tour Management Service
- Manages tour schedules
- Allows band managers to create, update, and cancel tours
- Stores information about tour dates, locations, and participating bands


## 🚀 Build & Run

### Requirements
- Java 21+
- Maven
- Docker & Docker Compose

### Running the application

#### Option 1: Docker Compose (Recommended for Development)
```bash
docker compose up --build
```
This command builds and starts all microservices and dependencies (e.g., databases, message brokers) as defined in the `docker-compose.yml` file.

#### Option 2: Manually with Maven
In each microservice directory (`user-service`, `band-service`, `music-catalog-service`, `tour-service`), run:

```bash
mvn clean install
mvn spring-boot:run
```


## 📌 Use Case Diagram
![Use case diagram](assets/UsecaseDiagram.png)

### Diagram Overview
This diagram shows the interactions between two primary actors:
- **Band Manager**: Responsible for creating bands, managing content, and scheduling activities
- **Team Member**: Musicians who respond to offers and participate in band activities

Key functionality includes:
- Band creation and configuration
- Team member recruitment
- Music content management (albums/songs)
- Tour scheduling
- Collaboration features for band members

Both actors share a common **Login** system, while other use cases are specific to their roles.


## 🧬 Class Diagram
This diagram illustrates the core data structures and relationships between key entities in the system. The classes map to the microservices described and support functionalities like band management, music cataloging, user roles, and tour scheduling.
![Class diagram](assets/ClassDiagram.png)




## 📚 API Documentation

Each service exposes its API documentation using **Springdoc OpenAPI** (Swagger UI):

- **User Service**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Band Service**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Music Catalog Service**: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
- **Tour Service**: [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)


## AI Disclosure
During the development of this project, AI tools were utilized.
