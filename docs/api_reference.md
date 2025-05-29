# API Documentation


### Project Description and Scope

This project is a web application consisting of:

    Backend: A Node.js API server that handles data operations and business logic.
    Frontend Clients: An Android app and potentially other clients that interact with the API.

The application manages user accounts with properties such as username, email, name, address, phone number, and country. It implements role-based access control with at least two roles:

    User: Standard users with basic access rights.
    Admin: Users with elevated privileges who can perform administrative tasks.

The UI adjusts its content based on the user's role upon login.

---

### API Endpoints Overview

We'll categorize the endpoints into several groups:

    Authentication Endpoints
        User registration and login.
    User Management Endpoints
        CRUD operations for user profiles.
    Role Management Endpoints
        Managing user roles and permissions.
    Additional Endpoints
        Any other necessary endpoints (e.g., password reset).

---

### Endpoint Details

#### 1. Authentication Endpoints
##### 1.1. Register a New User

##### Endpoint: POST /api/auth/register

Description: Creates a new user account.

Request Body:

```json

{
    "username": "johndoe",
    "email": "johndoe@example.com",
    "password": "SecurePassword123",
    "name": "John Doe",
    "address": "123 Main St",
    "phone": "+1234567890",
    "country": "USA"
}
```

###### Response:

    Success (201 Created):

```json
{
  "message": "User registered successfully.",
  "userId": "user_12345"
}
```

###### Error (400 Bad Request):

```json

{
  "error": "Email already exists."
}
```
1.2. User Login

    Endpoint: POST /api/auth/login

    Description: Authenticates a user and returns an access token.

    Request Body:

```json

{
    "email": "johndoe@example.com",
    "password": "SecurePassword123"
}
```

###### Response:

    Success (200 OK):

```json

{
    "message": "Login successful.",
    "token": "jwt_token_here",
    "user": {
        "id": "user_12345",
        "username": "johndoe",
        "role": "admin"
    }
}
```
###### Error (401 Unauthorized):

```json

{
  "error": "Invalid email or password."
}
```
##### 1.3. User Logout

##### Endpoint: POST /api/auth/logout

    Description: Logs out the authenticated user.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json

{
  "message": "Logout successful."
}
```

#### 2. User Management Endpoints
##### 2.1. Get User Profile

##### Endpoint: GET /api/users/profile

###### Description: Retrieves the profile of the authenticated user.

Headers:

makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json

{
      "id": "user_12345",
      "username": "johndoe",
      "email": "johndoe@example.com",
      "name": "John Doe",
      "address": "123 Main St",
      "phone": "+1234567890",
      "country": "USA",
      "role": "admin"
}
```

##### 2.2. Update User Profile

##### Endpoint: PUT /api/users/profile

    Description: Updates the profile of the authenticated user.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Request Body (only include fields to update):

```json
{
    "address": "456 Elm St",
    "phone": "+0987654321"
}
```

###### Response:

    Success (200 OK):

```json

{
      "message": "Profile updated successfully.",
      "user": {
        "id": "user_12345",
        "username": "johndoe",
        "address": "456 Elm St",
        "phone": "+0987654321"
  }
}
```

##### 2.3. Get All Users (Admin Only)

##### Endpoint: GET /api/users

    Description: Retrieves a list of all users. Accessible only by admins.
    
    Headers:
    
    makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json

[
    {
        "id": "user_12345",
        "username": "johndoe",
        "email": "johndoe@example.com",
        "role": "admin"
    },
    {
        "id": "user_67890",
        "username": "janedoe",
        "email": "janedoe@example.com",
        "role": "user"
    }
]
```

###### Error (403 Forbidden):

```json

{
  "error": "Access denied."
}
```

##### 2.4. Get User by ID (Admin Only)

##### Endpoint: GET /api/users/{id}

    Description: Retrieves details of a specific user by ID.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json

{
    "id": "user_67890",
    "username": "janedoe",
    "email": "janedoe@example.com",
    "name": "Jane Doe",
    "address": "789 Maple Ave",
    "phone": "+1122334455",
    "country": "USA",
    "role": "user"
    }
```

###### Error (404 Not Found):

```json
{
  "error": "User not found."
}
```

##### 2.5. Update User by ID (Admin Only)

##### Endpoint: PUT /api/users/{id}

    Description: Updates details of a specific user by ID.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Request Body:

```json
{
  "role": "admin"
}
```

###### Response:

    Success (200 OK):

```json
{
      "message": "User updated successfully.",
      "user": {
            "id": "user_67890",
            "username": "janedoe",
            "role": "admin"
      }
}
```

##### 2.6. Delete User by ID (Admin Only)

##### Endpoint: DELETE /api/users/{id}

    Description: Deletes a specific user by ID.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json
    {
      "message": "User deleted successfully."
    }
```

#### 3. Role Management Endpoints
##### 3.1. Get All Roles (Admin Only)

##### Endpoint: GET /api/roles

Description: Retrieves all available user roles.

Headers:

makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json
    [
          {
                "id": "role_user",
                "name": "user",
                "permissions": ["read_own_profile", "update_own_profile"]
          },
          {
                "id": "role_admin",
                "name": "admin",
                "permissions": ["manage_users", "manage_roles"]
          }
    ]
```

##### 3.2. Create a New Role (Admin Only)

##### Endpoint: POST /api/roles

    Description: Creates a new user role.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Request Body:

```json
{
    "name": "editor",
    "permissions": ["edit_content", "publish_content"]
}
```

###### Response:

    Success (201 Created):

```json

{
      "message": "Role created successfully.",
      "role": {
            "id": "role_editor",
            "name": "editor",
            "permissions": ["edit_content", "publish_content"]
      }
}
```

##### 3.3. Update Role by ID (Admin Only)

##### Endpoint: PUT /api/roles/{id}

    Description: Updates a specific role by ID.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Request Body:

```json
{
    "permissions": ["edit_content", "publish_content", "delete_content"]
}
```

###### Response:

    Success (200 OK):

```json

{
    "message": "Role updated successfully.",
    "role": {
        "id": "role_editor",
        "name": "editor",
        "permissions": ["edit_content", "publish_content", "delete_content"]
    }
}
```

##### 3.4. Delete Role by ID (Admin Only)

##### Endpoint: DELETE /api/roles/{id}

    Description: Deletes a specific role by ID.

    Headers:

    makefile

###### Authorization: Bearer jwt_token_here

###### Response:

    Success (200 OK):

```json
{
    "message": "Role deleted successfully."
}
```

#### 4. Additional Endpoints
##### 4.1. Password Reset Request

##### Endpoint: POST /api/auth/password-reset-request

Description: Initiates a password reset process by sending a reset link to the user's email.

Request Body:

```json
{
  "email": "johndoe@example.com"
}
```

###### Response:

    Success (200 OK):

```json
{
    "message": "Password reset link sent to email."
}
```

##### 4.2. Password Reset

##### Endpoint: POST /api/auth/password-reset

    Description: Resets the user's password using a token.

    Request Body:

```json
{
    "token": "reset_token_here",
    "newPassword": "NewSecurePassword123"
}
```

###### Response:

    Success (200 OK):

json

{
"message": "Password has been reset successfully."
}

---

### General Notes

    Authentication: All endpoints requiring authentication must include the Authorization header with a valid JWT token.
    Error Handling: Errors will be returned with an appropriate HTTP status code and a JSON body containing an error message.
    Data Validation: Ensure all input data is validated on the server side to prevent SQL injection, XSS, and other security vulnerabilities.

#### Example HTTP Request and Response
##### Login Example

###### Request:

```bash
POST /api/auth/login HTTP/1.1
Host: api.example.com
Content-Type: application/json

{
  "email": "johndoe@example.com",
  "password": "SecurePassword123"
}
```

###### Response:

```css
HTTP/1.1 200 OK
Content-Type: application/json

{
    "message": "Login successful.",
    "token": "jwt_token_here",
    "user": {
            "id": "user_12345",
            "username": "johndoe",
            "role": "admin"
    }
}
```

##### Get User Profile Example

###### Request:

```vbnet
GET /api/users/profile HTTP/1.1
Host: api.example.com
Authorization: Bearer jwt_token_here
```

###### Response:

```css
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": "user_12345",
    "username": "johndoe",
    "email": "johndoe@example.com",
    "name": "John Doe",
    "address": "123 Main St",
    "phone": "+1234567890",
    "country": "USA",
    "role": "admin"
}
```

---