# PacePlate — REST API Contract

## 1. Overview

PacePlate exposes a REST API through its Java Spring Boot backend.

The API allows the React frontend to submit user ingredients and nutritional targets, request AI-generated recipes, and receive structured recipe results.

**Base URL (local development):**

`http://localhost:8080/api`

**Data format:** JSON

**Architecture:** React → Spring Boot → Business Services → Database / External APIs

The frontend is responsible for collecting and displaying information. The backend is responsible for validation, business logic, nutrition calculations, and external API communication.

---

## 2. Health Check

### GET /api/health

**Description:** Verifies that the Spring Boot application is running and responding to requests.

**Request body:** None

**Success response — HTTP 200 OK**

```json
{
  "status": "UP",
  "service": "paceplate-backend"
}
```

This will be our first implemented endpoint.

---

## 3. Recipe Generation

### POST /api/recipes/generate

**Description:** Generates a recipe using the user's available ingredients and nutritional requirements.

The backend coordinates recipe generation through Hugging Face and uses USDA nutrition data to calculate and validate nutritional values.

### Request Body

```json
{
  "ingredients": [
    "chicken breast",
    "rice",
    "broccoli"
  ],
  "maxCalories": 650,
  "minProtein": 45,
  "goal": "CUT",
  "servings": 1
}
```

### Request Fields

| Field | Type | Required | Description |
|---|---|---|---|
| ingredients | string[] | Yes | Available ingredients |
| maxCalories | integer | Yes | Maximum calories per serving |
| minProtein | number | Yes | Minimum protein in grams per serving |
| goal | string | Yes | CUT, MAINTAIN, or BULK |
| servings | integer | Yes | Number of servings to generate |

### Validation Rules

- `ingredients` must contain at least one nonblank ingredient.
- `maxCalories` must be greater than zero.
- `minProtein` must be greater than or equal to zero.
- `goal` must match one of the supported values.
- `servings` must be at least one.
- Invalid requests must return HTTP 400 Bad Request.

---

## 4. Successful Recipe Response

**HTTP Status:** 200 OK

**Application Status:** SUCCESS

The generated recipe satisfies the requested nutritional constraints.

```json
{
  "status": "SUCCESS",
  "targetsMet": true,
  "recipe": {
    "name": "High-Protein Chicken Bowl",
    "description": "A protein-rich meal with chicken, rice, and broccoli.",
    "servings": 1,
    "prepTimeMinutes": 10,
    "cookTimeMinutes": 20,
    "ingredients": [
      {
        "name": "Chicken breast",
        "quantity": 180,
        "unit": "g",
        "foodState": "RAW"
      },
      {
        "name": "Rice",
        "quantity": 60,
        "unit": "g",
        "foodState": "DRY"
      },
      {
        "name": "Broccoli",
        "quantity": 100,
        "unit": "g",
        "foodState": "RAW"
      }
    ],
    "nutrition": {
      "calories": 550,
      "protein": 55,
      "carbs": 52,
      "fat": 12
    },
    "instructions": [
      "Cook the rice according to package instructions.",
      "Season and cook the chicken thoroughly.",
      "Steam the broccoli until tender.",
      "Combine the ingredients and serve."
    ]
  },
  "suggestions": []
}
```

**Important:** Nutritional values in this documentation are illustrative placeholders, not verified calculations. Actual values will be calculated by the backend using ingredient quantities and nutrition reference data.

Nutrition values represent one serving.

---

## 5. Alternative Recipe Response

**HTTP Status:** 200 OK

**Application Status:** ALTERNATIVE

When the backend cannot find a recipe satisfying all requested nutritional targets, it may return the closest suitable alternative.

The response must clearly indicate that the original requirements were not fully satisfied.

### Example

The user requests:

- Maximum calories: 500 kcal
- Minimum protein: 100 g

The backend finds an alternative that contains 480 calories and 90 g of protein.

```json
{
  "status": "ALTERNATIVE",
  "targetsMet": false,
  "recipe": {
    "name": "High-Protein Chicken Bowl",
    "servings": 1,
    "nutrition": {
      "calories": 480,
      "protein": 90,
      "carbs": 18,
      "fat": 8
    },
    "ingredients": [],
    "instructions": []
  },
  "suggestions": [
    {
      "field": "minProtein",
      "currentValue": 100,
      "suggestedValue": 90,
      "reason": "This adjustment would allow the suggested recipe to meet your updated protein target."
    }
  ]
}
```

The example omits ingredient and instruction details for brevity. Production recipe responses must contain the complete recipe.

---

## 6. Accepting Suggested Adjustments

When a user accepts an adjustment, the frontend submits another request to the same endpoint.

### POST /api/recipes/generate

**Updated Request**

```json
{
  "ingredients": [
    "chicken breast",
    "rice",
    "broccoli"
  ],
  "maxCalories": 500,
  "minProtein": 90,
  "goal": "CUT",
  "servings": 1
}
```

Only the accepted nutritional target changes.

The backend processes this as a new recipe-generation request.

If a generated recipe meets the updated targets, the response returns:

```json
{
  "status": "SUCCESS",
  "targetsMet": true,
  "recipe": {
    "name": "High-Protein Chicken Bowl",
    "servings": 1,
    "nutrition": {
      "calories": 480,
      "protein": 90,
      "carbs": 18,
      "fat": 8
    },
    "ingredients": [],
    "instructions": []
  },
  "suggestions": []
}
```

Accepting a suggestion does not permanently modify the user's saved nutritional preferences.

---

## 7. Error Handling

The API must distinguish between invalid requests, internal failures, and valid requests that cannot satisfy nutritional targets.

### 400 Bad Request

Returned when request validation fails.

```json
{
  "status": "ERROR",
  "code": "VALIDATION_ERROR",
  "message": "Invalid recipe generation request.",
  "details": [
    {
      "field": "maxCalories",
      "message": "Must be greater than zero."
    }
  ]
}
```

### 503 Service Unavailable

Returned when a required external service is temporarily unavailable and the backend cannot complete the request.

```json
{
  "status": "ERROR",
  "code": "AI_PROVIDER_UNAVAILABLE",
  "message": "Recipe generation is temporarily unavailable. Please try again later."
}
```

### 500 Internal Server Error

Returned when an unexpected backend failure occurs.

```json
{
  "status": "ERROR",
  "code": "INTERNAL_ERROR",
  "message": "An unexpected error occurred."
}
```

Internal stack traces, credentials, and sensitive implementation details must never be exposed in API responses.

---

## 8. Architectural Rules

1. React communicates with Spring Boot through REST APIs.
2. React never directly accesses PostgreSQL.
3. Private Hugging Face and USDA credentials remain on the backend.
4. Controllers handle HTTP requests and delegate business operations to services.
5. Services coordinate recipe generation, nutrition calculations, and validation.
6. Repositories handle persistent database operations.
7. External API clients handle communication with third-party services.
8. Nutritional calculations are performed by the backend, not trusted directly from AI-generated output.
9. Recipes that fail nutritional constraints must never be presented as satisfying those constraints.
10. User nutritional preferences must not be permanently changed without explicit authorization.

---

## 9. Planned Future Endpoints

The following endpoints are not part of the first implementation milestone.

| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/recipes/{id} | Retrieve a saved recipe |
| GET | /api/recipes | List the user's recipes |
| POST | /api/recipes | Save a recipe |
| DELETE | /api/recipes/{id} | Delete a saved recipe |
| POST | /api/auth/register | Register an account |
| POST | /api/auth/login | Authenticate a user |

Authentication, ownership checks, and persistence details will be designed in later phases.

---

## 10. Implementation Status

**Current phase:** Planning and architecture

**First implementation target:** `GET /api/health`

The API contract is a living document and will evolve as the application is implemented and tested.
