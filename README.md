# Leave Management System - AfricaHR Backend

A Spring Boot application for managing employee leave requests with Azure AD authentication.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 13 or higher
- Docker (optional)

## Database Setup

1. Start PostgreSQL using Docker:
```bash
docker run --name mypostgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=leave_management -p 5432:5432 -d postgres
```

2. The database will be automatically initialized with:
   - Default users
   - Department structure
   - Leave types
   - Initial leave balances

## Configuration

### Application Properties

The application uses the following configuration in `application.yml`:

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/leave_management
    username: postgres
    password: postgres
  
  # Azure AD Configuration
  cloud:
    azure:
      active-directory:
        enabled: true
        profile:
          tenant-id: 22081f0f-9d48-4eab-a41d-78eeb43225cf
        credential:
          client-id: 6145da12-853d-4708-84ac-4f08da0e1a6f
        app-id-uri: api://6145da12-853d-4708-84ac-4f08da0e1a6f
```

### Email Configuration

Configure email notifications by setting these environment variables:
- `MAIL_USERNAME`: Your Gmail address
- `MAIL_PASSWORD`: Your Gmail app password

## Default Users

The system comes with three default users:

1. **Admin User**
   - Email: admin@ist.com
   - Role: ADMIN

2. **Manager User**
   - Email: manager@ist.com
   - Role: MANAGER

3. **Staff User**
   - Email: staff@ist.com
   - Role: STAFF

## Database Schema

The system uses the following tables:

1. **departments**
   - Basic department information
   - Tracks creation and update timestamps

2. **users**
   - User authentication and profile data
   - Links to departments and managers
   - Role-based access control

3. **leave_types**
   - Different types of leave (e.g., Annual, Sick)
   - Maximum days allowed per type

4. **leave_requests**
   - Leave application details
   - Approval workflow status
   - Start and end dates

5. **leave_balances**
   - User's leave allocation
   - Tracks used and remaining days
   - Annual carry-forward tracking

## Running the Application

1. Start the database (if using Docker):
```bash
docker start mypostgres
```

2. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

## API Documentation

The API endpoints will be available at:
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8081/v3/api-docs`

## API Endpoints

### User Management
```
GET    /api/users                 - Get all users
GET    /api/users/{id}           - Get user by ID
GET    /api/users/me             - Get current user's profile
PUT    /api/users/{id}           - Update user
DELETE /api/users/{id}           - Delete user
GET    /api/users/managers       - Get all managers
GET    /api/users/subordinates   - Get current user's subordinates
```

### Leave Management
```
# Leave Requests
GET    /api/leaves                      - Get all leave requests
POST   /api/leaves                      - Create new leave request
GET    /api/leaves/{id}                 - Get leave request by ID
PUT    /api/leaves/{id}                 - Update leave request
DELETE /api/leaves/{id}                 - Delete leave request
PUT    /api/leaves/{id}/approve         - Approve leave request
PUT    /api/leaves/{id}/reject          - Reject leave request
GET    /api/leaves/my-requests          - Get current user's leave requests
GET    /api/leaves/pending-approval     - Get leaves pending approval

# Leave Balances
GET    /api/leave-balances             - Get all leave balances
GET    /api/leave-balances/{userId}    - Get user's leave balance
GET    /api/leave-balances/my-balance  - Get current user's leave balance

# Leave Types
GET    /api/leave-types                - Get all leave types
POST   /api/leave-types                - Create new leave type
PUT    /api/leave-types/{id}           - Update leave type
DELETE /api/leave-types/{id}           - Delete leave type
```

### Request/Response Examples

#### Get User Leave Balance
```http
GET /api/leave-balances/my-balance
Response:
{
    "id": 1,
    "userId": 1,
    "leaveType": "Annual Leave",
    "totalDays": 21,
    "usedDays": 5,
    "remainingDays": 16,
    "year": 2024
}
```

#### Create Leave Request
```http
POST /api/leaves
Request:
{
    "leaveTypeId": 1,
    "startDate": "2024-05-01",
    "endDate": "2024-05-05",
    "reason": "Family vacation",
    "isHalfDay": false
}
Response:
{
    "id": 1,
    "status": "PENDING",
    "startDate": "2024-05-01",
    "endDate": "2024-05-05",
    "reason": "Family vacation",
    "isHalfDay": false,
    "createdAt": "2024-04-24T10:30:00Z"
}
```

## Security

The application uses Azure AD for authentication. Make sure to:
1. Configure the correct Azure AD application settings
2. Set up proper redirect URIs
3. Grant necessary API permissions
4. Configure user roles in Azure AD

## Troubleshooting

1. **Database Connection Issues**
   - Verify PostgreSQL is running: `docker ps`
   - Check database logs: `docker logs mypostgres`

2. **Authentication Issues**
   - Verify Azure AD configuration in `application.yml`
   - Check if the application is registered in Azure Portal
   - Ensure proper permissions are granted

3. **Email Notification Issues**
   - Verify SMTP settings
   - Check if environment variables are set correctly

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 