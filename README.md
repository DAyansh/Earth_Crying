# Earth Crying

Earth Crying is a full-stack environmental awareness platform that exposes overlooked human activities damaging the planet while pairing those insights with hope, action, and measurable optimism.

The project combines:
- a Spring Boot backend with JWT auth, PostgreSQL persistence, Flyway migrations, WebSockets, and scheduled data aggregation
- a Next.js 16 frontend with animated storytelling, interactive globe visuals, and impact-driven UX

## Current project state

This repository contains a monorepo-style structure with two main applications:

```text
earth-crying/
├── README.md
├── AGENTS.md
├── docs/
├── earth-crying-backend/     # Spring Boot 3.2.5 API
│   ├── src/
│   ├── logs/
│   ├── pom.xml
│   └── src/main/resources/application.yml
├── earth-crying-frontend/    # Next.js 16 app
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── next.config.ts
│   ├── tailwind.config.js
│   └── eslint.config.mjs
└── frontend_index.html
```

## Tech stack

### Backend
- Java 21
- Spring Boot 3.2.5
- Spring Web, Security, Validation, WebSocket
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Springdoc OpenAPI / Swagger
- JWT authentication
- WebClient for external APIs

### Frontend
- Next.js 16.2.12
- React 19.2.4
- TypeScript
- Tailwind CSS
- GSAP + ScrollTrigger
- Framer Motion
- React Three Fiber / Three.js
- SWR
- STOMP + SockJS for live updates

## Core product features

- 12 hidden environmental impacts with educational explanations and solutions
- interactive narrative landing page with scroll-driven storytelling
- live Earth Pulse websocket feed
- digital carbon footprint and impact calculators
- hope ledger and optimistic sustainability metrics
- confession / solutions wall experience
- time-travel style climate narrative
- JWT-based auth flow and protected endpoints
- Flyway-backed database migrations and seed data

## App flow

### Backend API
The backend exposes a REST API under `/api` and provides Swagger docs at:

```text
http://localhost:8080/swagger-ui.html
```

WebSocket endpoint:

```text
ws://localhost:8080/ws
```

Topic used by the pulse feed:

```text
/topic/earth-pulse
```

### Frontend app
The frontend runs as a Next.js app and expects the backend on localhost:8080 by default.

## Prerequisites

- Java 21+
- Maven
- Node.js 18+
- PostgreSQL 15+
- Redis 7+

## Local setup

### 1. Start the backend

```bash
cd earth-crying-backend
mvn spring-boot:run
```

### 2. Start the frontend

```bash
cd earth-crying-frontend
npm install
npm run dev
```

### 3. Open the app

```text
http://localhost:3000
```

## Environment and config

The backend config is currently in:

```text
earth-crying-backend/src/main/resources/application.yml
```

Important values include:
- PostgreSQL datasource settings
- Redis host and port
- JWT secret and expiration
- CORS allowed origins
- scheduler configuration
- external API configuration for NASA, GFW, OpenAQ, and NOAA

The frontend default API base URL is defined in:

```text
earth-crying-frontend/src/lib/api.ts
```

and defaults to:

```text
http://localhost:8080
```

## Project structure highlights

### Backend
- `controller/` — REST endpoints and request handling
- `service/` — business logic and integrations
- `entity/` — JPA entities
- `repository/` — database access
- `security/` — JWT and auth configuration
- `websocket/` — socket messaging layer
- `db/migration/` — Flyway schema and seed migrations

### Frontend
- `src/app/` — Next.js pages and top-level layout
- `src/components/` — reusable UI and storytelling components
- `src/lib/` — API helpers and request logic
- `src/types/` — TypeScript types

## Scripts

### Backend
```bash
cd earth-crying-backend
mvn test
mvn spring-boot:run
```

### Frontend
```bash
cd earth-crying-frontend
npm run dev
npm run build
npm run lint
```

## Notes

- Some generated and local runtime files exist in the frontend workspace during development and may not be part of source control long-term.
- The app is structured for local development and is ready to be extended with deployment configuration for Vercel/Railway or similar hosting platforms.

## License

This project currently does not declare a license file in the root repository. If needed, add one before public distribution or production deployment.

## Repository

GitHub: https://github.com/DAyansh/Earth_Crying.git