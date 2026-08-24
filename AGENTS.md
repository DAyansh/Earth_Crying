# Earth Crying - Development Guide

This guide provides instructions for developing the Earth Crying application.

## Project Overview

Earth Crying is a full-stack environmental awareness platform exposing 12 overlooked human activities damaging the environment, paired with hope and actionable solutions.

## Architecture

```
                    ┌─────────────────┐
                    │   Frontend      │
                    │  (Next.js 16)   │
                    │  - React 19     │
                    │  - Tailwind     │
                    │  - GSAP, R3F    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Backend       │
                    │  (Spring Boot) │
                    │  - REST API    │
                    │  - WebSocket   │
                    │  - JWT Auth    │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
        PostgreSQL     Redis Cache    External APIs
```

## Development Setup

### Prerequisites
- Java 21+
- Maven
- Node.js 18+
- PostgreSQL 15+
- Redis 7+

### Backend Setup

1. Configure PostgreSQL database
2. Update `application.yml` with database credentials
3. Add external API keys (NASA, GFW, OpenAQ, etc.)

### Frontend Setup

1. Update `.env.local` with API base URL
2. Install dependencies
3. Run dev server

## Running the Application

### Start Backend
```bash
cd earth-crying-backend
mvn spring-boot:run
```

### Start Frontend
```bash
cd earth-crying-frontend
npm run dev
```

## Testing

### Backend Tests
```bash
cd earth-crying-backend
mvn test
```

### Frontend Lint
```bash
cd earth-crying-frontend
npm run lint
```

## Deployment

Both backend and frontend can be deployed to Railway, Vercel, or similar platforms.

### Backend (Railway)
1. Connect PostgreSQL and Redis services
2. Set environment variables
3. Deploy from GitHub

### Frontend (Vercel)
1. Connect to GitHub repository
2. Set `NEXT_PUBLIC_API_BASE_URL` environment variable
3. Deploy automatically on push to main

## Key Features to Implement

1. [x] Database schema with Flyway migrations
2. [x] Impact CRUD API
3. [x] Hope Ledger with scheduled data fetching
4. [x] WebSocket Earth Pulse feed
5. [x] User authentication with JWT
6. [x] Footprint quiz calculator
7. [x] Pledge/streak tracking
8. [x] Community solutions wall
9. [x] Geo impact data for maps
10. [x] Next.js frontend with animations
11. [x] 3D Earth globe visualization

## API Documentation

Open at: `http://localhost:8080/swagger-ui.html`

## WebSocket Connection

Connect to: `ws://localhost:8080/ws`

Subscribe to: `/topic/earth-pulse`

## Contributing

1. Fork the repository
2. Create feature branch
3. Make changes
4. Submit pull request