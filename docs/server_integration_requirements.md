# Server Integration Requirements for Inventory Android App

This document outlines the requirements and expectations for the server-side API to ensure smooth integration and operation of the Inventory Android app.

## 1. Server Address Configuration
- The app allows users to specify the server base URL on first launch (e.g., `https://api.example.com/`).
- All API endpoints are relative to this base URL.

## 2. Authentication Endpoints
### Login
- **Endpoint:** `POST {base_url}/auth/login`
- **Request Body:**
  - `username` (string)
  - `password` (string)
- **Response:**
  - On success: JSON object containing at least:
    - `token` (JWT string)
    - `role` (string, e.g., `user`, `admin`)
  - On failure: appropriate HTTP error code and message.

### Register
- **Endpoint:** `POST {base_url}/auth/register`
- **Request Body:**
  - `username` (string)
  - `password` (string)
  - (Optionally) other fields such as `email`, `name`, etc.
- **Response:**
  - On success: JSON object containing at least:
    - `token` (JWT string)
    - `role` (string)
  - On failure: appropriate HTTP error code and message.

## 3. JWT Token Handling
- The app expects a JWT token in the login/register response.
- This token will be sent in the `Authorization` header as `Bearer <token>` for all subsequent API requests.
- The server must validate the JWT and return 401 Unauthorized if invalid or expired.

## 4. Role-Based Access
- The `role` field in the response determines menu and feature access in the app.
- Supported roles should be documented (e.g., `user`, `admin`).

## 5. Error Handling
- All endpoints should return meaningful HTTP status codes and error messages.
- 401 Unauthorized for invalid/expired tokens.
- 400 Bad Request for invalid input.
- 500 Internal Server Error for unexpected issues.

## 6. Additional Endpoints
- The app will require endpoints for item/category management, user profile, etc. (to be detailed as features are implemented).

## 7. CORS & HTTPS
- The server must support HTTPS.
- CORS should be enabled for mobile clients if needed.

## 8. Example Responses
### Login/Register Success
```json
{
  "token": "<jwt-token>",
  "role": "user"
}
```

### Login/Register Failure
```json
{
  "error": "Invalid credentials"
}
```

---

**Please ensure the above requirements are met for the app to function correctly.**

For questions or clarifications, contact the Android app development team.

