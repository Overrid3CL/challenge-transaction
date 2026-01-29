# Challenge

Monorepo: API REST de transacciones (Spring Boot) y frontend (Vite + React).

## Configuración local (antes de ejecutar)

1. Copiar `.env.example` a `.env`:
   ```bash
   cp .env.example .env
   ```
2. Editar `.env` y definir al menos las contraseñas: `POSTGRES_PASSWORD` y `DB_PASSWORD` (mismo valor si usas el mismo usuario de Postgres).
3. No subas `.env` a Git; ya está en `.gitignore`.

## Levantar con Docker

Desde la raíz del proyecto (con `.env` ya configurado):

```bash
docker compose up -d --build
```

Servicios:

- **PostgreSQL**: `localhost:5432` (usuario y BD según `.env`)
- **ms-transaction** (API): `http://localhost:8080` (Swagger: `http://localhost:8080/swagger-ui.html`)
- **web-transaction** (frontend): `http://localhost:3000`

Variables: ver `.env.example` para todas las opciones (`VITE_API_URL`, `DB_URL`, etc.).
