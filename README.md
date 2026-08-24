# Earth Crying - Environmental Awareness Platform

A full-stack web application that exposes overlooked human activities damaging the environment, paired with hope and actionable solutions.

## Project Structure

```
earth-crying/
├── earth-crying-backend/     # Spring Boot backend
│   ├── src/main/java/com/earthcrying/
│   │   ├── EarthCryingApplication.java
│   │   ├── config/           # Security, WebSocket, CORS config
│   │   ├── controller/       # REST API controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entity/           # JPA entities
│   │   ├── repository/       # Spring Data JPA repositories
│   │   ├── security/         # JWT security components
│   │   └── service/          # Business logic services
│   ├── src/main/resources/
│   │   ├── application.yml   # Application configuration
│   │   └── db/migration/     # Flyway migrations
│   └── pom.xml               # Maven dependencies
│
└── earth-crying-frontend/    # Next.js frontend
    ├── src/
    │   ├── app/              # Next.js App Router pages
    │   ├── components/       # React components
    │   ├── hooks/            # Custom hooks
    │   ├── lib/              # API utilities
    │   └── types/            # TypeScript types
    ├── public/               # Static assets
    ├── tailwind.config.js    # Tailwind configuration
    └── package.json          # npm dependencies
```

## Tech Stack

### Backend (Spring Boot 3)
- **Java 21** with Spring Boot 3
- **Spring Data JPA** with PostgreSQL
- **Redis** for caching
- **WebSocket (STOMP)** for Earth Pulse feed
- **Spring Security** with JWT authentication
- **Flyway** for database migrations
- **Spring WebFlux** for external API calls

### Frontend (Next.js 16)
- **React 19** with App Router
- **TypeScript**
- **Tailwind CSS** for styling
- **GSAP** with ScrollTrigger for animations
- **Framer Motion** for UI animations
- **React Three Fiber** / Three.js for 3D globe
- **Socket.IO Client** for WebSocket
- **SWR** for data fetching
- **Lenis** for smooth scrolling

## Backend Features

### 1. Impact Entity CRUD API
Manage the 12 hidden impact categories:
- Digital/AI Carbon Footprint
- Tire & Brake Dust
- Fast Fashion Microplastics
- Light Pollution
- Underwater Noise Pollution
- Fertilizer Runoff
- E-waste & Rare-Earth Mining
- Hidden Water Footprint
- Palm Oil Supply Chains
- Space Debris/Satellite Pollution
- Soil Erosion from Monoculture
- Indoor VOCs/Fragrance Chemicals

### 2. Hope Ledger API
Positive impact counterpoints with real-time data from:
- Global Forest Watch
- NOAA
- OpenAQ
- World Bank
- NASA APIs

### 3. Earth Pulse WebSocket
Real-time incrementing counters (trees lost, e-waste generated, etc.)

### 4. Personal Footprint Calculator
Quiz-style API scoring user lifestyle against 12 categories

### 5. Pledge & Streak Tracker
Gamified habit change with JWT authentication

### 6. Community Solutions Wall
User-submitted local actions with upvote/moderation

### 7. Geo Impact API
Country-level datasets for interactive maps

## Frontend Features

### Homepage
- Scrollytelling hero with video morph animation
- 3D interactive Earth globe with live Pulse data
- Individual sections for each impact category

### Components
- `HeroSection.tsx` - Full-screen hero with animated counters
- `EarthPulseGlobe.tsx` - Three.js globe with WebSocket data
- `ImpactSection.tsx` - Individual impact category display
- `FootprintQuiz.tsx` - Interactive lifestyle quiz

## Getting Started

### Backend
```bash
cd earth-crying-backend
mvn spring-boot:run
```

### Frontend
```bash
cd earth-crying-frontend
npm install
npm run dev
```

## Environment Variables

Copy the example files to get started:

```bash
# Backend — copy to earth-crying-backend/.env
cp earth-crying-backend/.env.example earth-crying-backend/.env

# Frontend — copy to earth-crying-frontend/.env.local
cp earth-crying-frontend/.env.example earth-crying-frontend/.env.local
```

### Backend (`earth-crying-backend/.env`)
- `DATABASE_URL` - PostgreSQL connection string
- `DATABASE_USER` / `DATABASE_PASSWORD` - DB credentials
- `REDIS_HOST` / `REDIS_PORT` / `REDIS_PASSWORD` - Redis cache
- `JWT_SECRET` - Long random secret for JWT signing (32+ chars)
- `CORS_ALLOWED_ORIGINS` - Comma-separated frontend origins
- `MAIL_HOST` / `MAIL_PORT` / `MAIL_USERNAME` / `MAIL_PASSWORD` - SMTP
- `GFW_API_KEY` / `NOAA_API_KEY` / `OPENAQ_API_KEY` / `NASA_API_KEY` - External data APIs

### Frontend (`earth-crying-frontend/.env.local`)
- `NEXT_PUBLIC_API_BASE_URL` - Backend base URL (e.g. `http://localhost:8080`)

## API Endpoints

### Public
- `GET /api/impacts` - All impact categories
- `GET /api/impacts/{id}` - Specific impact
- `GET /api/hope-ledger` - All hope entries
- `GET /api/geo` - Geo impact data

### Auth
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### WebSocket
- `ws://localhost:8080/ws` - Earth Pulse real-time feed

## Development

### Run Both locally
```bash
# Terminal 1 - Backend
cd earth-crying-backend && mvn spring-boot:run

# Terminal 2 - Frontend
cd earth-crying-frontend && npm run dev
```

## License

MIT