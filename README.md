# MovieApp 

A Java web application for discovering movies, managing personal watchlists, and sharing movie reviews. Built with **Spring Boot**, **Thymeleaf**, **Spring Security**, and **PostgreSQL**.

This project demonstrates server-side rendering, layered application design, relational data modeling, and role-based features in a Java web application.

## Features

### Movie browsing
- Browse a movie catalog.
- Search and filter movies by **title, director, genre, and release year**.
- View reviews associated with a movie.

### Registered users (`USER`)
- Register and log in.
- Add movies to a personal watchlist and remove them later.
- Access a user dashboard.

### Critics (`CRITIC`)
- Submit reviews for movies.
- View their own reviews and reviews associated with a movie.
- Delete their own reviews (ownership is checked in the service layer).

### Administrators (`ADMIN`)
- Add new movies and view the movie catalog through administration pages.
- View user management and promote eligible users to the critic role.


## Tech stack

| Area | Technologies |
| --- | --- |
| Language | Java 17 |
| Backend | Spring Boot 3.5.3, Spring MVC |
| Views | Thymeleaf, HTML, CSS |
| Security | Spring Security, BCrypt password hashing |
| Data | PostgreSQL, Spring Data JPA / Hibernate |
| Build | Maven / Maven Wrapper |

## Project structure

The Spring Boot application is located in the [`movieapp/`](movieapp/) directory.

```text
AppForMovies/
└── movieapp/
    ├── src/main/java/com/example/movieapp/
    │   ├── controllers/       # Request handling and page routes
    │   ├── models/            # JPA entities and roles
    │   ├── repositories/      # Database access
    │   ├── services/          # Business logic
    │   └── SecurityConfig.java
    ├── src/main/resources/
    │   ├── templates/         # Thymeleaf pages
    │   └── application.properties
    ├── pom.xml
    └── mvnw.cmd
```

### Main entities

- **User** — application accounts and user roles.
- **Movie** — movie metadata.
- **Review** — reviews associated with a critic and a movie.
- **Watchlist** — associations between users and saved movies.

## Run locally

**Requirements:** Java 17 and PostgreSQL. The project includes Maven Wrapper scripts, so a separate Maven installation is not required.

1. Clone the repository and enter the application folder:

   ```bash
   git clone https://github.com/Ela1009/AppForMovies.git
   cd AppForMovies/movieapp
   ```

2. Create a PostgreSQL database named `movieapp`.

3. Configure the database connection in `src/main/resources/application.properties` for your own local PostgreSQL instance. **Do not use real or reused passwords in a public repository.**

4. Run the application:

   **Windows (PowerShell):**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **macOS / Linux:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. Visit **http://localhost:8080**.

> These instructions reflect the project's checked-in configuration; a fresh setup has not been independently verified. The current repository includes a hard-coded development database password in `application.properties`, which should be removed or externalized before sharing the project more broadly.

## Architecture

The application follows a layered structure:

```text
Browser
   ↓ HTTP requests
Spring MVC Controllers
   ↓
Services (business rules)
   ↓
Spring Data JPA Repositories
   ↓
PostgreSQL

Thymeleaf renders HTML views on the server.
```

Spring Security handles authentication, while application services implement operations such as watchlist management and review ownership checks.
