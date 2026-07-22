# Invest Analyzer

Invest Analyzer is an AI-assisted web application that evaluates the viability of an investment project in Tunisia.

The investor fills in a structured form, pays through PayPal Sandbox, and receives:

- a global viability score
- five detailed dimension scores
- the top three risks
- mitigation measures
- personalized recommendations
- a downloadable PDF report
- saved analysis history
- a redirect option to the Business Plan service

## Project context

This project is developed as part of an internship by a team of two people.

The application is not a full Business Plan generator. It provides a rapid diagnostic to help an investor decide whether a project idea is viable before going further.

## Main features

- Investor registration and authentication
- JWT-based security
- Three-section project analysis form
- Real-time Angular form validation
- PayPal Sandbox payment
- Groq API integration with a LLaMA model
- Five viability scores from 0 to 100
- Weighted global score
- Risk analysis
- Mitigation measures
- Personalized recommendations
- Visual result dashboard
- PDF report generation
- Analysis history
- Relaunch analysis without repaying
- Business Plan service transfer
- Groq fallback system
- Admin statistics

## Evaluated dimensions

Each project is evaluated on five dimensions:

1. Financial viability
2. Market opportunity
3. Operational feasibility
4. Regulatory framework
5. Project owner profile

Score colors:

- 0–40: red
- 41–65: orange
- 66–100: green

## Technology stack

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- Bean Validation
- MySQL
- Maven
- Groq REST API
- PayPal Sandbox
- PDF generation library

### Frontend

- Angular
- TypeScript
- SCSS
- Reactive Forms
- Angular HttpClient
- Route Guards
- HTTP Interceptors
- Nginx

### Infrastructure

- Docker
- Docker Compose
- Git

## Project structure

```text
invest-analyzer/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/example/backend/
│   │   │   │   ├── admin/
│   │   │   │   ├── analysis/
│   │   │   │   ├── auth/
│   │   │   │   ├── common/
│   │   │   │   ├── groq/
│   │   │   │   ├── payment/
│   │   │   │   ├── pdf/
│   │   │   │   ├── transfer/
│   │   │   │   ├── user/
│   │   │   │   └── BackendApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/app/
│   │   ├── core/
│   │   ├── features/
│   │   ├── layouts/
│   │   └── shared/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docs/
├── docker-compose.yaml
├── .env.example
├── .gitignore
└── README.md
```

## Prerequisites

Install:

- Docker Desktop
- Git
- Java 21
- Node.js
- Angular CLI
- Maven

Java, Node.js, Angular CLI, and Maven are only required when running the backend or frontend outside Docker.

## Environment configuration

Copy the example file:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Then update `.env`:

```env
MYSQL_DATABASE=tunisia_invest_analyzer
MYSQL_ROOT_PASSWORD=change_me
MYSQL_USER=analyzer_user
MYSQL_PASSWORD=change_me

JWT_SECRET=replace_with_a_long_random_secret
JWT_EXPIRATION=86400000

GROQ_API_KEY=
GROQ_API_URL=https://api.groq.com/openai/v1/chat/completions
GROQ_MODEL=
GROQ_TIMEOUT_SECONDS=30

PAYPAL_CLIENT_ID=
PAYPAL_CLIENT_SECRET=
PAYPAL_MODE=sandbox

BUSINESS_PLAN_BASE_URL=http://localhost:4300
```

Do not commit `.env`.

## Run with Docker

From the project root:

```bash
docker compose up -d --build
```

Check containers:

```bash
docker compose ps
```

View backend logs:

```bash
docker compose logs -f backend
```

View frontend logs:

```bash
docker compose logs -f frontend
```

View database logs:

```bash
docker compose logs -f mysql
```

Stop the project:

```bash
docker compose down
```

Stop the project and delete MySQL data:

```bash
docker compose down -v
```

Warning: `docker compose down -v` permanently deletes the local database volume.

## Application URLs

When Docker Compose is running:

- Frontend: `http://localhost:4200`
- Backend API: `http://localhost:8080`
- MySQL from the host machine: `localhost:3307`
- MySQL from the backend container: `mysql:3306`

## Local development

For faster development, run only MySQL in Docker:

```bash
docker compose up -d mysql
```

### Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

On Windows:

```powershell
cd backend
mvnw.cmd spring-boot:run
```

### Start the frontend

```bash
cd frontend
npm install
npm start
```

The Angular development server normally runs on:

```text
http://localhost:4200
```

## Database configuration

Spring Boot uses Spring Data JPA and Hibernate.

Example configuration in `application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3307/tunisia_invest_analyzer?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:analyzer_user}
spring.datasource.password=${DB_PASSWORD:change_me}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Hibernate creates and updates the database tables from the Spring Boot entities during development.

## Planned API endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

### Analysis

```http
GET  /api/analyse/service
POST /api/analyse/lancer
GET  /api/analyse/{id}/statut
GET  /api/analyse/{id}/resultat
GET  /api/analyse/{id}/pdf
GET  /api/analyse/historique
```

### Payment

```http
POST /api/analyse/paiement
POST /api/analyse/paiement/confirmer
```

### Administration

```http
GET /api/admin/analyse/stats
```

The final endpoint naming must remain consistent across the backend and frontend.

## Analysis workflow

```text
Investor login
    ↓
Complete the three-section form
    ↓
Save project data
    ↓
Create PayPal Sandbox order
    ↓
Confirm payment
    ↓
Launch Groq analysis
    ↓
Display processing animation
    ↓
Save scores, risks, and recommendations
    ↓
Display result dashboard
    ↓
Download PDF
    ↓
Save analysis in history
```

## Analysis form sections

### Project

- Sector
- Target region
- Short description
- Current project stage

### Finance

- Estimated initial budget
- Main financing source
- Expected first-year revenue
- Expected ROI duration

### Context

- Project owner experience
- Direct competitors in Tunisia
- Identified tax advantages
- Free-zone status

## Analysis statuses

Recommended statuses:

```text
DRAFT
PAYMENT_PENDING
PAID
PROCESSING
COMPLETED
FAILED
```

## Groq response format

The backend should require a structured JSON response containing:

```json
{
  "dimensions": [
    {
      "dimension": "FINANCIAL_VIABILITY",
      "score": 75,
      "explanation": "..."
    }
  ],
  "risks": [
    {
      "description": "...",
      "criticality": "HIGH",
      "mitigation": "..."
    }
  ],
  "recommendations": [
    {
      "title": "...",
      "description": "...",
      "priority": 1
    }
  ]
}
```

The backend must validate the response before saving it.

## Fallback behavior

When Groq is unavailable, the backend must:

- catch the error
- generate a rule-based fallback result
- save the result
- mark the generation source as `FALLBACK`
- notify the user that the AI service was temporarily unavailable

Possible generation sources:

```text
GROQ
FALLBACK
```

## Git workflow

Recommended branches:

```text
main
develop
feature/*
fix/*
```

Example:

```bash
git checkout develop
git pull
git checkout -b feature/analysis-form
```

After completing a task:

```bash
git add .
git commit -m "feat: implement analysis form"
git push origin feature/analysis-form
```

Then create a merge request or pull request into `develop`.

Do not work directly on `main`.

## Team organization

### Person 1

Main focus:

- Spring Boot
- JPA entities
- repositories
- authentication
- Groq integration
- payment backend
- PDF
- backend tests

### Person 2

Main focus:

- Angular
- layouts
- authentication pages
- analysis form
- payment UI
- processing page
- result dashboard
- history
- frontend tests

### Shared work

- API contract
- integration testing
- Business Plan transfer
- documentation
- final demonstration

## Development order

1. Validate Docker setup
2. Connect Angular to Spring Boot
3. Create authentication
4. Create analysis entities
5. Build the analysis form
6. Save analysis data
7. Integrate Groq
8. Build the result dashboard
9. Add history
10. Add PDF generation
11. Add PayPal Sandbox
12. Add Business Plan transfer
13. Add fallback logic
14. Add admin statistics
15. Test and document

## Final demonstration scenario

The expected demonstration uses:

- Project: three-star hotel
- Region: Djerba
- Budget: 500,000 TND

The complete demonstration must show:

1. Investor login
2. Form completion
3. PayPal Sandbox payment
4. AI processing animation
5. Global score
6. Five dimension scores
7. Risks and recommendations
8. PDF download
9. Analysis history
10. Business Plan redirect

## Security rules

- Never commit `.env`
- Never expose the Groq API key in Angular
- Never expose PayPal secrets in Angular
- Never trust payment status sent by the frontend
- Obtain the authenticated user from JWT
- Verify that analyses belong to the connected user
- Validate all input in both Angular and Spring Boot
- Store passwords using BCrypt
- Restrict admin endpoints to the `ADMIN` role

## Current status

Project structure initialized.

Next milestone:

```text
Angular
    ↓
Nginx
    ↓
Spring Boot
    ↓
MySQL
```

After the base connection works, the next feature is JWT authentication.
