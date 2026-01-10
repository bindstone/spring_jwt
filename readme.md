
# Spring JWT Authentication Project

A comprehensive demonstration of JWT-based authentication using Spring Boot, React, and Keycloak. This project showcases modern security patterns with OAuth2 Resource Server implementation.

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.4.1 with Java 21
- **Frontend**: React 19.2.3 with TypeScript
- **Identity Provider**: Keycloak 26.5.0
- **Database**: PostgreSQL 15.3
- **Containerization**: Docker & Docker Compose

### Project Structure
```
spring_jwt/
├── backend/                    # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/bindstone/backend/
│   │       ├── config/         # Security configuration
│   │       ├── controller/     # REST endpoints
│   │       └── BackendApplication.java
│   └── src/main/resources/
│       └── application.properties
├── frontend_react/             # React TypeScript application
│   ├── src/
│   │   ├── App.tsx            # Main application component
│   │   └── keycloak.ts        # Keycloak configuration
│   └── package.json
├── infrastructure/            # Docker configurations
│   ├── keycloak/              # Keycloak setup with realm
│   └── postgres/              # PostgreSQL initialization
├── compose.yaml               # Docker Compose configuration
└── readme.md                  # This file
```

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Node.js 18+ (for frontend development)
- Java 21+ (for backend development)

### Running the Application

1. **Start Infrastructure Services**
   ```bash
   docker-compose up -d
   ```
   This starts:
   - PostgreSQL on port 5432
   - Keycloak on port 8888

2. **Start Backend**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   The backend will be available on http://localhost:8080

3. **Start Frontend**
   ```bash
   cd frontend_react
   npm install
   npm start
   ```
   The frontend will be available on http://localhost:3000

## 🔐 Authentication Flow

### Keycloak Configuration
- **Realm**: CONTINENTAL
- **Client**: CONTINENTAL-CLIENT
- **Admin Console**: http://localhost:8888/admin
  - Username: `admin`
  - Password: `password`

### API Endpoints

| Endpoint | Access Level | Description |
|----------|-------------|-------------|
| `GET /public` | Public | Accessible without authentication |
| `GET /private` | Authenticated | Requires valid JWT token |
| `GET /admin` | Admin Role | Requires `CONTINENTAL_ROLE_ADMIN` role |

### Frontend Features
- **Authentication**: Automatic Keycloak SSO integration
- **Token Management**: JWT token display and refresh
- **API Testing**: Interactive buttons to test all endpoints
- **Role-based UI**: Different access levels based on user roles

## 🛠️ Development

### Backend Development

#### Security Configuration
The `SecurityConfig.java` implements:
- OAuth2 Resource Server with JWT validation
- Role-based access control (RBAC)
- CORS configuration for frontend integration
- Custom JWT authorities converter for Keycloak roles

#### Key Components
- **Controllers**: REST endpoints for public, private, and admin access
- **Security Config**: JWT validation and role mapping
- **Application Properties**: Keycloak integration settings

### Frontend Development

#### Key Components
- **App.tsx**: Main application with authentication state management
- **keycloak.ts**: Keycloak client configuration
- **API Integration**: Axios-based HTTP client with JWT headers

#### Authentication Flow
1. Application initializes Keycloak client
2. Checks SSO status (`check-sso`)
3. If authenticated, retrieves JWT token
4. Includes token in API calls via Authorization header
5. Handles 401 responses with automatic login redirect

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend_react
npm test
```

## 🔧 Configuration

### Environment Variables
Create `.env` files for environment-specific configuration:

**Backend (.env)**:
```
KEYCLOAK_URL=http://127.0.0.1:8888
KEYCLOAK_REALM=CONTINENTAL
FRONTEND_URL=http://localhost:3000
```

**Frontend (.env)**:
```
REACT_APP_KEYCLOAK_URL=http://127.0.0.1:8888
REACT_APP_API_URL=http://localhost:8080
```

### Keycloak Realm Configuration
The project includes a pre-configured realm (`infrastructure/keycloak/realm.json`) with:
- Continental realm setup
- Client configuration for React application
- User roles and permissions
- Test users with different access levels

## 📚 API Documentation

### Authentication Header
```
Authorization: Bearer <JWT_TOKEN>
```

### Response Format
All endpoints return JSON responses:
```json
{
  "message": "Hello PUBLIC"
}
```

### Error Handling
- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Insufficient role permissions
- **500 Internal Server Error**: Backend processing errors

### JWT Sample

Token

```
eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnWmxxYkV3Qm9ZanU2QTV2VTJGaVAwVEJvTGZUcEVESl9sZzNYR2xPQllZIn0.eyJleHAiOjE3NjgyNDE4NTUsImlhdCI6MTc2ODI0MTU1NSwianRpIjoib25ydHJvOjFkODQ0MWM2LTIwYTMtOTM5OS05NGE2LTkyZmU5YTU1NTJlNSIsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6ODg4OC9yZWFsbXMvQ09OVElORU5UQUwiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiODU0NDU4NWItNTNjZC00ZjI4LTk5OTAtNjE3YTE5M2RmOWU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiQ09OVElORU5UQUwtQ0xJRU5UIiwic2lkIjoiSVJBTjIyZU9GQ1BPaklCczlzVmltSkZEIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjMwMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWNvbnRpbmVudGFsIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiQ09OVElORU5UQUwtQ0xJRU5UIjp7InJvbGVzIjpbIkNPTlRJTkVOVEFMX1JPTEVfQURNSU4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiYWRtaW4gYWRtaW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJhZG1pbiIsImZhbWlseV9uYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluQHR0LmNvbSJ9.0X50uG3Adrir9FowVaH_fYEVPQi0T06RpwiLd_zblNPGruZ_zdc05j1Tj6WVOQCzCzYg4_dte_0ye8IhZMSvXE7eYVAF5IE3WsCJ-NGoNHsFXDJV6Br51Fmxxb6m2Ao9HnkQup6Fv8hb53CCVeEvCoEe9WQQJt1pI_YNuh_G0eoS2Hnvk4Lts2YSKeGG-KafzEa0WLqLfy79BHoXXac5H4dOW2r9Vq7pSoIWvIPIeJsEj2vrr-G2B8vnDk2ufmCpN4Ej0ZRb831uJNRDW9zl9qNg9LdArxSwMg5vux8GYDu9lfoqEQ5d2RaFRZDL7nbwON75mjFnOQpsBMBXzEA67A
```

https://www.jwt.io/

```json
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "gZlqbEwBoYju6A5vU2FiP0TBoLfTpEDJ_lg3XGlOBYY"
}
```

```json
{
  "exp": 1768241855,
  "iat": 1768241555,
  "jti": "onrtro:1d8441c6-20a3-9399-94a6-92fe9a5552e5",
  "iss": "http://127.0.0.1:8888/realms/CONTINENTAL",
  "aud": "account",
  "sub": "8544585b-53cd-4f28-9990-617a193df9e4",
  "typ": "Bearer",
  "azp": "CONTINENTAL-CLIENT",
  "sid": "IRAN22eOFCPOjIBs9sVimJFD",
  "acr": "1",
  "allowed-origins": [
    "http://localhost:3000"
  ],
  "realm_access": {
    "roles": [
      "offline_access",
      "uma_authorization",
      "default-roles-continental"
    ]
  },
  "resource_access": {
    "CONTINENTAL-CLIENT": {
      "roles": [
        "CONTINENTAL_ROLE_ADMIN"
      ]
    },
    "account": {
      "roles": [
        "manage-account",
        "manage-account-links",
        "view-profile"
      ]
    }
  },
  "scope": "email profile",
  "email_verified": true,
  "name": "admin admin",
  "preferred_username": "admin",
  "given_name": "admin",
  "family_name": "admin",
  "email": "admin@tt.com"
}
```

## 🔒 Security Features

### Implemented
- JWT token validation using Keycloak public keys
- Role-based access control (RBAC)
- CORS configuration for cross-origin requests
- OAuth2 Resource Server pattern
- Automatic token refresh (when implemented)

### Security Best Practices
- Use HTTPS in production
- Implement proper logout handling
- Add rate limiting
- Enable security headers
- Regular token rotation

## 🐳 Docker Services

### Keycloak Container
- **Image**: `quay.io/keycloak/keycloak:26.5.0`
- **Port**: 8888:8080
- **Features**: Development mode with realm import

### PostgreSQL Container
- **Image**: `postgres:15.3-alpine`
- **Port**: 5432
- **Database**: keycloak
- **User**: keycloak

## 📝 License

This project is provided as a demonstration of JWT authentication patterns. See LICENSE file for details.

## 🔗 References

- **JWT Specification**: https://datatracker.ietf.org/doc/html/rfc7519
- **OpenID Federation**: https://openid.net/specs/openid-federation-1_0.html
- **Spring Security OAuth2**: https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html
- **Keycloak Documentation**: https://www.keycloak.org/documentation