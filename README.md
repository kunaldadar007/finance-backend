Finance Backend – REST API

A robust and scalable Finance Data Processing & Access Control Backend built using modern Java technologies. This system enables secure financial data management with role-based access, analytics, and production-ready deployment.

🌐 Live Deployment
Base URL:
https://finance-backend-3-7rtv.onrender.com
Status: ✅ Live & Production Ready
Environment: Docker (Render Cloud)
Database: PostgreSQL (Render Managed)

⚠️ Note: Since this runs on a free-tier instance, the first request may take ~50 seconds due to cold start.

🎯 Core Features
1. User & Role Management
User registration with email validation
Secure authentication with JWT tokens
Role-based system:
VIEWER
ANALYST
ADMIN
User status tracking (ACTIVE / INACTIVE)
2. Financial Records Management
Full CRUD operations for financial transactions
Accurate monetary handling using BigDecimal
Date management using LocalDate
Advanced filtering:
By type (INCOME / EXPENSE)
By category
By date range
3. Dashboard & Analytics
Total income, expenses, and net balance
Category-wise spending insights
Recent activity tracking (last 10 transactions)
4. Security Architecture
JWT-based stateless authentication
Role-Based Access Control (RBAC) using @PreAuthorize
Data isolation (users access only their own data)
🏗️ Technology Stack
Framework: Spring Boot 3.2.0
Security: Spring Security 6 + JWT (JJWT 0.12.3)
ORM: Hibernate 6.3.1
Database:
Production: PostgreSQL 16
Development: Oracle 21c
Deployment: Docker (Multi-stage Maven Build)
🚀 API Endpoints
🔐 Authentication
POST /api/auth/register → Register new user
POST /api/auth/login → Login & receive JWT
👤 User Management (ADMIN Only)
GET /api/users → List all users
GET /api/users/{id} → Get user details
DELETE /api/users/{id} → Delete user
💰 Financial Records
POST /api/financial-records → Create transaction
GET /api/financial-records → Get all transactions
📊 Dashboard
GET /api/dashboard/summary → Income/Expense/Balance
GET /api/dashboard/category-breakdown → Category insights
🔐 Role-Based Access Control
Feature	VIEWER	ANALYST	ADMIN
View own records	✅	✅	✅
Create / Update	❌	✅	✅
Delete records	❌	❌	✅
Manage users	❌	❌	✅
⚙️ Environment Configuration
Variable	Description
SPRING_DATASOURCE_URL	PostgreSQL connection URL
SPRING_DATASOURCE_DRIVER_CLASS_NAME	PostgreSQL Driver
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT	Hibernate Dialect
APP_JWT_SECRET	JWT Secret Key
🧪 Quick Testing
1. Register User
curl -X POST https://finance-backend-3-7rtv.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name": "Kunal", "email": "test@example.com", "password": "password123"}'
2. Login
curl -X POST https://finance-backend-3-7rtv.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123"}'
🎓 Production Highlights
Cloud deployment on Render with Docker
Schema migration from Oracle → PostgreSQL
Optimized multi-stage Docker build (reduced image size)
Stateless scalable architecture
👨‍💻 Developer

Kunal Dadar

Full Stack Java Developer