# PacePlate - System Architecture

## Architecture Style

PacePlate uses a React frontend and a modular Spring Boot backend with a PostgreSQL database.

The backend follows a layered architecture with controllers, services, repositories, and external API integration components.

## Technology Stack

- Frontend: React, TypeScript, Material UI
- Backend: Java 25, Spring Boot
- Database: PostgreSQL
- AI Integration: Hugging Face
- Nutrition Integration: USDA FoodData Central

## Component Responsibilities

### Frontend
Collects user input, calls backend REST APIs, and renders recipe results.

### Backend
Validates requests, coordinates recipe generation, calculates nutrition, applies business rules, and manages persistence.

### Database
Stores users, saved recipes, ingredients, and relevant application data.

### AI Provider
Generates recipe suggestions, quantities, and cooking instructions.

### Nutrition Provider
Supplies reference nutrition data for ingredient quantities. The backend is responsible for calculating and validating recipe totals.

## Architectural Decisions

1. Use a modular monolith instead of microservices to reduce operational complexity.
2. Keep external API credentials exclusively on the backend.
3. Separate controllers, business logic, persistence, and external integrations.
4. Calculate nutritional values in the backend rather than trusting AI-generated values.
5. Design AI integration behind an interface so providers can be replaced later.
