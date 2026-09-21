# HomeHub V1 — System Design

## 1. Overview

HomeHub is a web application for managing a shared household grocery inventory.

### V1 Goals
- User registration and login
- Family creation and joining
- Shared household inventory management
- Consume and restock inventory
- Inventory history
- Low-stock and expiry alerts
- Secure authentication and authorization

## 2. Architecture

```text
React + TypeScript
        |
     HTTP/JSON
        |
Spring Boot REST API
        |
  +-----+----------------+
  |     |                |
Controller Service     Security
        |
   JPA / Hibernate
        |
      MySQL
```

### Backend Layers
- **Controller** — receives HTTP requests and returns HTTP responses.
- **Service** — contains business logic and rules.
- **Repository** — communicates with the database through Spring Data JPA.
- **Entity** — represents database tables.
- **DTO** — represents API request/response data.
- **Security** — authentication and authorization.

# 3. Database Design

## 3.1 Tables

### USER
| Column | Description |
|---|---|
| id | Primary key |
| username | User name |
| email | Unique login email |
| password_hash | BCrypt-hashed password |
| created_at | Account creation time |
| updated_at | Last update time |

### FAMILY
| Column | Description |
|---|---|
| id | Primary key |
| name | Family/household name |
| invite_code | Unique code used to join |
| created_at | Creation time |
| updated_at | Last update time |

### FAMILY_MEMBER
| Column | Description |
|---|---|
| id | Primary key |
| user_id | FK → USER |
| family_id | FK → FAMILY |
| role | OWNER or MEMBER |
| joined_at | Membership time |

Constraint: `UNIQUE(user_id, family_id)`

### INVENTORY_ITEM
| Column | Description |
|---|---|
| id | Primary key |
| family_id | FK → FAMILY |
| name | Grocery name |
| category | Grocery category |
| quantity | Current quantity |
| unit | kg, litre, pieces, etc. |
| minimum_stock | Low-stock threshold |
| expiry_date | Optional expiry date |
| created_at | Creation time |
| updated_at | Last update time |

### INVENTORY_HISTORY
| Column | Description |
|---|---|
| id | Primary key |
| inventory_item_id | FK → INVENTORY_ITEM |
| user_id | FK → USER |
| action | CONSUME or RESTOCK |
| quantity_changed | Quantity changed |
| created_at | Action time |

### ALERT
Alert storage can be introduced when notification functionality is implemented.

| Column | Description |
|---|---|
| id | Primary key |
| user_id | FK → USER |
| inventory_item_id | FK → INVENTORY_ITEM |
| type | Alert type |
| message | Alert message |
| is_read | Whether user has read it |
| created_at | Creation time |

## 3.2 Entity Relationships

```text
USER 1 ---- N FAMILY_MEMBER N ---- 1 FAMILY
                                      |
                                      1
                                      |
                                      N
                               INVENTORY_ITEM
                                      |
                                      1
                                      |
                                      N
                              INVENTORY_HISTORY
```

### User ↔ Family

Logically **many-to-many**:
- One user may belong to multiple families.
- One family may contain multiple users.

`FAMILY_MEMBER` is the junction/entity table because membership has attributes such as `role` and `joined_at`.

### Family ↔ InventoryItem

**One-to-many**:
- One family can have many inventory items.
- Each inventory item belongs to one family.

`inventory_item.family_id → family.id`.

### InventoryItem ↔ InventoryHistory

**One-to-many**:
- One inventory item can have many history records.
- Each history record belongs to one inventory item.

### User ↔ InventoryHistory

**One-to-many**:
- One user can perform many inventory actions.
- Each history record records the user who performed the action.

## 3.3 JPA Relationship Ownership

```java
// InventoryItem
@ManyToOne
@JoinColumn(name = "family_id")
private Family family;
```

```java
// Family
@OneToMany(mappedBy = "family")
private List<InventoryItem> items;
```

- `@JoinColumn` means the FK is managed/stored on this side.
- `mappedBy` means this side refers to the relationship already defined on the other side.
- `@JoinColumn(name = "family_id")` uses the database column name.
- `mappedBy = "family"` uses the Java field name in `InventoryItem`.

# 4. API Design

## Authentication

### Register
`POST /api/users/register`

Request:
```json
{
  "username": "Divya",
  "email": "divya@example.com",
  "password": "password"
}
```

Response:
```json
{
  "id": 1,
  "username": "Divya",
  "email": "divya@example.com"
}
```

### Login
`POST /api/auth/login`

Request:
```json
{
  "email": "divya@example.com",
  "password": "password"
}
```

Response:
```json
{
  "token": "<JWT>"
}
```

## Family

```text
POST /api/families
GET  /api/families/me
POST /api/families/join
```

Join request:
```json
{
  "inviteCode": "ABC123"
}
```

## Inventory

```text
POST   /api/inventory
GET    /api/inventory
GET    /api/inventory/{id}
PATCH  /api/inventory/{id}
DELETE /api/inventory/{id}
```

### Inventory actions
```text
POST /api/inventory/{id}/consume
POST /api/inventory/{id}/restock
```

### History
`GET /api/inventory/{id}/history`

### Dashboard
`GET /api/dashboard`

## API Design Principles

Every API should define:
- Request body
- Response body
- HTTP status code
- Validation rules
- Error response
- Authentication requirement
- Authorization requirement

# 5. Security Design

## Password Security

Passwords must never be stored as plain text.

```text
Registration
     |
Password
     |
BCrypt
     |
Password hash
     |
MySQL
```

## Login

```text
Email + Password
       |
Spring Security
       |
Compare password with BCrypt hash
       |
    Valid?
    /    \
  Yes     No
   |       |
  JWT     401
```

## Protected Request

```text
Client
  |
Authorization: Bearer <JWT>
  |
JWT Filter
  |
Validate token
  |
Identify user
  |
Controller
  |
Service
  |
Family authorization check
  |
Database
```

### Authentication vs Authorization

**Authentication:** Who is this user?

**Authorization:** Is this user allowed to access this resource?

Example:

```text
User A
  |
  | JWT identifies User A
  v
GET /api/inventory/25
  |
  v
Does inventory 25 belong to User A's family?
  |
  +-- Yes → Continue
  |
  +-- No  → 403 Forbidden
```

# 6. Inventory Business Rules

- A family member can add an inventory item to their family's inventory.
- Requested consume quantity must not exceed current quantity.
- Restock increases the current quantity by the requested amount.
- `quantity <= minimum_stock` triggers the low-stock condition.
- Users can only access inventory belonging to a family they are members of.

# 7. UI Design

V1 pages:

```text
Register
Login
Create / Join Family
Dashboard
Inventory
Add / Edit Item
History
```

# 8. Design Decisions

### Spring Boot
Provides a structured Java backend for REST APIs, dependency injection, validation, security and database integration.

### JPA/Hibernate
Provides ORM mapping between Java entities and relational database tables.

### MySQL
Fits the structured relationships between users, families, inventory and history.

### FamilyMember
Models family membership as an entity because membership has attributes such as role and joined time.

### DTOs
Keep API contracts separate from database entities and avoid exposing internal entity structure directly.

### JWT
Provides token-based authentication for protected API requests.

# 9. Design-to-Development Flow

HomeHub will be developed using an Agile/Scrum-style approach.

```text
Requirement
    ↓
Analysis
    ↓
Design
    ↓
Development
    ↓
Testing
    ↓
Review
    ↓
Deployment
    ↓
Feedback
    ↓
Next Iteration
```

Each feature will be developed as a complete slice where practical:

```text
Requirement → API → Database → Backend → Frontend → Test → Review
```

This document is the initial **HomeHub V1 design baseline** and may evolve as implementation reveals new requirements or technical constraints.
