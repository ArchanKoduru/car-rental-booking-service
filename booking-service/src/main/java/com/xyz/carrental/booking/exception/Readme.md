# Car Rental Booking Service

## Overview

`booking-service` is a Spring Boot microservice for managing car rental bookings. It validates driving licenses, checks car pricing, calculates rental costs, and ensures all business rules are enforced (license validity, booking duration, car category, etc.).

---

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Build Tool:** Gradle
* **Database:** H2 (in-memory for testing; can be switched to MySQL/PostgreSQL)
* **Messaging/Clients:** REST clients for Driving License API & Car Pricing API
* **Testing:** JUnit 5, Mockito
* **Swagger/OpenAPI:** Springdoc OpenAPI 3

---

## Features

* Create a booking with license validation and car pricing.
* Enforce rules:

    * License must exist.
    * License must be at least 1 year old.
    * License must not expire during the booking period (max 30 days).
    * End date cannot be before start date.
    * Booking cannot exceed 30 days.
    * Valid car categories only: `SMALL`, `MEDIUM`, `LARGE`, `EXTRA_LARGE`.
* Retrieve booking by ID.

---

## REST Endpoints

### 1. Confirm Booking

```
POST /api/v1/bookings/confirm
```

**Request Body:**

```json
{
  "licenseNumber": "DL3123",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDIUM"
}
```

**Success Response (200):**

```json
{
  "bookingId": "c20be4df-d69c-4b2a-ad68-91c5d90e1114"
}
```

**Error Responses (400 / 404):**

```json
{ "error": "Driving license not found" }
{ "error": "Invalid driving license" }
{ "error": "Invalid car category" }
{ "error": "End date must be after or equal to start date" }
{ "error": "Reservation cannot exceed 30 days" }
{ "error": "Driving license has already expired" }
```

---

### 2. Get Booking

```
GET /api/v1/bookings/{bookingId}
```

**Response Example:**

```json
{
  "bookingId": "c20be4df-d69c-4b2a-ad68-91c5d90e1114",
  "licenseNumber": "DL3123",
  "customerName": "John Doe",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDIUM",
  "rentalPrice": 229.95
}
```

**Error Response:**

```json
{ "error": "Booking not found: <bookingId>" }
```

---

## Swagger / OpenAPI

Once the service is running, you can view the API documentation at:

```
http://localhost:8080/swagger-ui.html
```

Or OpenAPI JSON:

```
http://localhost:8080/v3/api-docs
```

---

## How to Run

1. **Clone the repository:**

```bash
git clone https://github.com/your-org/booking-service.git
cd booking-service
```

2. **Build the project:**

```bash
./gradlew clean build
```

3. **Run with Gradle:**

```bash
./gradlew bootRun
```

4. **Service runs on:** `http://localhost:8080`

---

## Testing with Postman

### Confirm Booking Examples

**1. Successful Booking**

```json
{
  "licenseNumber": "DL12345",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDIUM"
}
```

**2. License Not Found**

```json
{
  "licenseNumber": "DL99999",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDIUM"
}
```

Response:

```json
{ "error": "Driving license not found" }
```

**3. Invalid Car Category**

```json
{
  "licenseNumber": "DL12345",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDUM"
}
```

Response:

```json
{ "error": "Invalid car category" }
```

**4. End Date Before Start Date**

```json
{
  "licenseNumber": "DL12345",
  "age": 28,
  "startDate": "2025-11-10",
  "endDate": "2025-11-05",
  "carSegment": "MEDIUM"
}
```

Response:

```json
{ "error": "End date must be after or equal to start date" }
```

**5. Reservation Too Long (>30 days)**

```json
{
  "licenseNumber": "DL12345",
  "age": 28,
  "startDate": "2025-11-01",
  "endDate": "2025-12-10",
  "carSegment": "MEDIUM"
}
```

Response:

```json
{ "error": "Reservation cannot exceed 30 days" }
```

**6. License Expired / Too New**

```json
{
  "licenseNumber": "DL5678",
  "age": 28,
  "startDate": "2025-11-05",
  "endDate": "2025-11-10",
  "carSegment": "MEDIUM"
}
```

Response:

```json
{ "error": "Driving license has already expired" }
```

---

## Notes

* All **invalid inputs** or **enum mismatches** are handled via `GlobalExceptionHandler`.
* Internal server errors are logged but not exposed to the user.
* Use **@Valid** annotations in DTOs to enforce required fields (e.g., `licenseNumber` cannot be empty).
