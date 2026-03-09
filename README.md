

---

# 🐳 Mini_Student - Module 4: Docker & Databases

This project implements a fully containerized development environment for a Spring Boot application and a PostgreSQL database. It satisfies the core requirements for the Docker configuration module.

## 🏗 Workflow and Implementation Details

The project fulfills the following technical requirements from the module:

* **Declarative Manifest**: Uses a `docker-compose.yml` file to orchestrate the application and database services.
* **Configurable Credentials**: Overrides default PostgreSQL credentials to ensure a custom, secure setup.
* **Security & .env**: Passwords and port mappings are managed via an external `.env` file, ensuring no sensitive data is hardcoded.
* **Persistence**: Configured a Docker Volume (`student_final_db_disk`) to ensure database data persists even if containers are recreated.

## 🚀 Setup and Execution

### 1. Build the Application

Run Maven from the root directory to generate the executable JAR file:

```bash
./mvnw clean package -DskipTests -Dmaven.compiler.release=17

```

### 2. Configure Environment

Ensure a `.env` file exists in the root directory. This file passes variables to the Docker containers.

**Required `.env` content:**

```properties
DB_NAME=sijay
DB_USER=sanjay
DB_PASSWORD=1234
DB_PORT_EXTERNAL=5433
APP_PORT_EXTERNAL=8090

```

### 3. Launch Environment

Start the containers using Docker Compose to bootstrap the database and application together:

```bash
docker compose up --build

```

## 🧪 Verification & Access

### Database Connectivity

The database is exposed on **localhost:5433**. You can connect using DBeaver or verify via terminal:

```bash
docker exec -it postgres psql -U sanjay -d sijay -c "SELECT * FROM student;"

```

### API Endpoints

* **View All Students**: http://localhost:8090/getstudents
* **Add Student**: http://localhost:8090/addstudent?name=Sanjay&age=22

