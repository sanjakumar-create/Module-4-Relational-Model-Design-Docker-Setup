


---

# JDBC Connection Pooling Experiment

![Java](https://img.shields.io/badge/Java-21-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![HikariCP](https://img.shields.io/badge/HikariCP-ConnectionPool-green)

## Objective

This project demonstrates **why connection pooling is required in multi-threaded Java applications interacting with databases**.

A simple experiment compares:

* A **custom `DataSource` that returns a single cached connection**
* A **connection pool implementation using HikariCP**

The system runs multiple threads executing a slow database query to simulate concurrent backend requests.

---

# Architecture

### Single Connection (Custom DataSource)

```
Threads
  │
  ▼
Custom DataSource
  │
  ▼
Single JDBC Connection
  │
  ▼
PostgreSQL
```

All threads share the same connection, so queries execute **sequentially**.

---

### Connection Pool (HikariCP)

```
Threads
  │
  ▼
HikariCP Pool
 │   │   │
 ▼   ▼   ▼
Conn Conn Conn
  │
  ▼
PostgreSQL
```

Each thread receives a connection from the pool, allowing **parallel execution**.

---

# Environment

* Java
* JDBC
* PostgreSQL (Docker)
* HikariCP
* Maven

Run PostgreSQL using Docker:

```bash
docker run --name postgres-db \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-e POSTGRES_DB=testdb \
-p 5432:5432 \
-d postgres
```

---

# Experiment Setup

A multi-threaded program simulates concurrent database access.

Each thread:

1. Requests a connection
2. Executes a slow query
3. Reads the result
4. Releases the connection

Slow query used for simulation:

```sql
SELECT pg_sleep(1);
```

This helps observe the behavior of concurrent database requests.

---

# Implementation

## Custom DataSource

A minimal implementation of `javax.sql.DataSource` was created.

Behavior:

* Creates **one JDBC connection**
* Caches it
* Returns the same instance for every `getConnection()` call

All other interface methods were left unimplemented as allowed.

This forces all threads to share **one connection**.

---

## Connection Pool (HikariCP)

The custom `DataSource` was replaced with **HikariCP**, a high-performance JDBC connection pool.

HikariCP manages:

* Connection creation
* Connection reuse
* Pool size
* Connection lifecycle

This allows multiple queries to run **concurrently**.

---

# Results

| Configuration     | Execution Model      |
| ----------------- | -------------------- |
| Custom DataSource | Sequential execution |
| HikariCP Pool     | Parallel execution   |

The connection pool significantly improves concurrency because multiple threads can execute database queries simultaneously.

---

# Why Connection Pooling Is Needed

Creating a database connection involves:

* Network connection setup
* Authentication
* Session initialization

These operations are expensive. Creating connections repeatedly can reduce performance.

Connection pools solve this by **reusing existing connections instead of creating new ones for every request**.

---

# Advantages

* Faster database access
* Better performance under concurrent workloads
* Efficient resource management
* Controlled number of active connections

---

# Disadvantages

* Requires configuration and tuning
* Each open connection consumes memory and DB resources
* Poor configuration may cause contention

---

# How to Run

Clone the repository:

```bash
git clone <repo-url>
```

Run PostgreSQL using Docker.

Start the application:

```bash
mvn spring-boot:run
```

The application will execute the multi-threaded workload and print execution behavior.

---

# Conclusion

This experiment shows that using a **single database connection creates a bottleneck** in multi-threaded applications.

Connection pools such as **HikariCP** allow applications to reuse connections and execute queries in parallel, making them essential for scalable backend systems.

---

