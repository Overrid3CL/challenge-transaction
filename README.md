# Challenge Transaction

Monorepo con API REST de transacciones (Spring Boot) y frontend (Vite + React) para la gestión de transacciones, usuarios y negocios.

## Tecnologías

- **Frontend:** React 19, TypeScript, Vite, Tailwind CSS
- **Backend:** Spring Boot 3.5, Java 21
- **Base de datos:** PostgreSQL 16
- **Containerización:** Docker, Docker Compose

## Requisitos previos

- Docker y Docker Compose instalados
- Node.js 18+ (para desarrollo local del frontend)
- Java 21+ (para desarrollo local del backend)
- Variables de entorno configuradas en `.env` (ver sección Variables de entorno)

## Inicio rápido

```bash
# Clonar el repositorio
git clone https://github.com/Overrid3CL/challenge-transaction.git
cd challenge-transaction

# Copiar y configurar variables de entorno
cp .env.example .env
# Editar .env y definir POSTGRES_PASSWORD y DB_PASSWORD (mismo valor recomendado)

# Levantar con Docker Compose
docker compose up -d --build

# La aplicación estará disponible en:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# PostgreSQL: localhost:5432
```

## Estructura del proyecto

```
challenge-transaction/
├── web-transaction/
│   ├── src/
│   ├── Dockerfile
│   └── package.json
├── ms-transaction/
│   ├── src/
│   ├── Dockerfile
│   └── build.gradle.kts
├── docker-compose.yml
├── .env.example
└── README.md
```

## Variables de entorno

### Backend (ms-transaction)

| Variable            | Descripción                  | Default                                         |
| ------------------- | ---------------------------- | ----------------------------------------------- |
| `DB_URL`            | URL JDBC PostgreSQL          | jdbc:postgresql://localhost:5432/transaction_db |
| `DB_USER`           | Usuario BD                   | postgres                                        |
| `DB_PASSWORD`       | Contraseña BD                | (requerido)                                     |
| `POSTGRES_USER`     | Usuario Postgres (Docker)    | postgres                                        |
| `POSTGRES_PASSWORD` | Contraseña Postgres (Docker) | (requerido)                                     |
| `POSTGRES_DB`       | Nombre BD                    | transaction_db                                  |

### Frontend (web-transaction)

| Variable       | Descripción     | Default               |
| -------------- | --------------- | --------------------- |
| `VITE_API_URL` | URL del backend | http://localhost:8080 |

## Desarrollo local

### Frontend

```bash
cd web-transaction
npm install
npm run dev
```

### Backend

```bash
cd ms-transaction
./gradlew bootRun
```

> **Nota:** El backend requiere PostgreSQL corriendo y `.env` configurado en la raíz del proyecto.

## API Endpoints

Base URL: `http://localhost:8080`

### Usuarios (`/user`)

| Método | Endpoint     | Descripción                   |
| ------ | ------------ | ----------------------------- |
| GET    | `/user`      | Lista todos los usuarios      |
| GET    | `/user/{id}` | Obtiene usuario por ID        |
| POST   | `/user`      | Crea usuario                  |
| PUT    | `/user/{id}` | Actualiza usuario             |
| DELETE | `/user/{id}` | Elimina usuario (soft delete) |

### Transacciones (`/transaction`)

| Método | Endpoint                             | Descripción                                              |
| ------ | ------------------------------------ | -------------------------------------------------------- |
| GET    | `/transaction`                       | Lista transacciones (paginado: page, size, sort, search) |
| GET    | `/transaction/stats`                 | Estadísticas de transacciones                            |
| GET    | `/transaction/{id}`                  | Obtiene transacción por ID                               |
| POST   | `/transaction`                       | Crea transacción                                         |
| PUT    | `/transaction/{id}`                  | Actualiza transacción                                    |
| DELETE | `/transaction/{id}`                  | Elimina transacción (soft delete)                        |
| GET    | `/transaction/user/{userId}`         | Transacciones por usuario                                |
| GET    | `/transaction/business/{businessId}` | Transacciones por negocio                                |

### Negocios (`/business`)

| Método | Endpoint    | Descripción              |
| ------ | ----------- | ------------------------ |
| GET    | `/business` | Lista todos los negocios |

**Documentación interactiva:** Swagger UI en `http://localhost:8080/swagger-ui.html`

## Testing

```bash
# Tests del backend
cd ms-transaction && ./gradlew test

# Tests del frontend (no implementados actualmente)
# cd web-transaction && npm test
```

## Despliegue

Para desplegar con Docker:

```bash
docker compose up -d --build
```

## Licencia

MIT
