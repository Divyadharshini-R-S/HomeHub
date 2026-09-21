# HomeHub V1 — Requirements Specification

## 1. Project Overview

**Project:** HomeHub  
**Version:** V1  
**Purpose:** A household inventory management web application that allows family members to manage shared grocery inventory, track consumption/restocking, and receive low-stock or near-expiry alerts.

## 2. Project Goal

HomeHub should help a household:

- Maintain a shared grocery inventory.
- Allow family members to access and manage the same household inventory.
- Track inventory changes and who performed them.
- Prevent invalid inventory operations such as consuming more than the available quantity.
- Identify low-stock and near-expiry items.
- Secure user accounts and family data.

## 3. Actors

### 3.1 User

A registered person who can log in and use HomeHub.

### 3.2 Family Owner

The user who creates a family. The owner has administrative responsibilities for the family.

### 3.3 Family Member

A registered user who joins a family using an invite code and can manage the shared inventory.

### 3.4 System

The application itself, responsible for authentication, validation, inventory rules, history tracking, and alerts.

---

# 4. Functional Requirements

## FR-01 — User Registration

The system shall allow a new user to register using:

- Username
- Email
- Password

Rules:

- Email must be unique.
- Password must not be stored as plain text.
- A unique user ID shall be generated.
- The system shall store the user's creation information.

## FR-02 — User Login

The system shall allow registered users to log in using:

- Email
- Password

After successful authentication, the system shall provide an authentication token.

Invalid credentials shall result in an authentication error.

## FR-03 — Logout

The system shall allow an authenticated user to log out from the application.

## FR-04 — Create Family

An authenticated user shall be able to create a family.

The system shall:

- Create a family record.
- Assign the creator as the family owner.
- Generate a unique invite code.

## FR-05 — Join Family

A registered user shall be able to join a family using a valid invite code.

The system shall:

- Validate the invite code.
- Create a family membership.
- Assign the appropriate member role.
- Record the join time.

Invalid invite codes shall be rejected.

## FR-06 — Family Membership

The system shall maintain family membership information including:

- User
- Family
- Role
- Join date

The system shall prevent duplicate membership for the same user and family.

## FR-07 — Add Inventory Item

An authorized family member shall be able to add a grocery item.

An item may contain:

- Name
- Category
- Quantity
- Unit
- Minimum stock level
- Expiry date

The item shall belong to a specific family.

## FR-08 — View Inventory

An authenticated family member shall be able to view the inventory belonging to their family.

The system shall not expose another family's inventory.

## FR-09 — View Inventory Item

A family member shall be able to view details of an individual inventory item.

## FR-10 — Update Inventory Item

An authorized family member shall be able to update inventory item information.

## FR-11 — Delete Inventory Item

An authorized family member shall be able to delete an inventory item.

## FR-12 — Consume Inventory

A family member shall be able to record consumption of an inventory item.

Rules:

- The consumed quantity must be greater than zero.
- Consumption must not exceed the current quantity.
- The inventory quantity shall decrease accordingly.
- The operation shall be recorded in inventory history.

If the requested quantity is greater than the available quantity, the system shall reject the operation.

## FR-13 — Restock Inventory

A family member shall be able to restock an inventory item.

Rules:

- Restock quantity must be greater than zero.
- The inventory quantity shall increase accordingly.
- The operation shall be recorded in inventory history.

## FR-14 — Inventory History

The system shall maintain a history of inventory changes.

Each history record shall contain:

- Inventory item
- User who performed the action
- Action
- Quantity changed
- Timestamp

The system shall support viewing the history of an inventory item.

## FR-15 — Search Inventory

Users shall be able to search inventory items.

## FR-16 — Filter and Sort Inventory

Users shall be able to filter and/or sort inventory items based on supported item information.

## FR-17 — Dashboard

The system shall provide a dashboard containing useful inventory information such as:

- Total inventory items
- Low-stock items
- Near-expiry items

## FR-18 — Low-Stock Detection

The system shall identify an inventory item as low-stock when:

`quantity <= minimum_stock`

The system shall make the low-stock condition available for alerting.

## FR-19 — Near-Expiry Detection

The system shall identify inventory items approaching their expiry date.

The exact warning period shall be defined during implementation/configuration.

## FR-20 — Alerts

The system shall support alerts for conditions such as:

- Low stock
- Near expiry

Alerts may initially be displayed inside the application. Email notification can be implemented as a later notification capability.

## FR-21 — Authentication

Protected APIs shall require an authenticated user.

## FR-22 — Authorization

The system shall verify that an authenticated user is authorized to access the requested family/inventory data.

A user shall not be able to access another family's inventory.

## FR-23 — Password Security

The system shall hash passwords using BCrypt before storing them.

Plain-text passwords shall never be stored in the database.

## FR-24 — Validation

The system shall validate user input and reject invalid requests with appropriate error information.

## FR-25 — Error Handling

The system shall return appropriate errors for cases such as:

- Invalid credentials
- Duplicate email
- Invalid invite code
- Unauthorized access
- Inventory item not found
- Insufficient inventory quantity
- Invalid quantity
- Invalid request data

---

# 5. Non-Functional Requirements

## NFR-01 — Security

- Passwords shall be BCrypt hashed.
- JWT shall be used for authenticated API requests.
- Authorization shall be enforced for family-specific resources.
- Sensitive information shall not be exposed in API responses.

## NFR-02 — Performance

The system should provide reasonable response times under the expected V1 workload.

## NFR-03 — Reliability

Failed inventory operations should not leave the inventory and history in an inconsistent state.

## NFR-04 — Maintainability

The backend shall follow a layered architecture:

- Controller
- Service
- Repository
- Entity
- DTO
- Security

## NFR-05 — Scalability

The design should allow the system to support additional users, families, and inventory items as usage grows.

## NFR-06 — Availability

The application should be available for normal household usage when the required services are running.

## NFR-07 — Usability

The UI should provide clear:

- Validation messages
- Error messages
- Loading states
- Empty states
- Success feedback

## NFR-08 — Testability

Backend APIs and business logic should be testable independently.

## NFR-09 — Deployability

The application should be containerizable using Docker.

---

# 6. V1 Scope

### Included

- Registration
- Login
- Family creation
- Family joining
- Family membership
- Inventory CRUD
- Consume/restock
- Inventory history
- Search/filter/sort
- Dashboard
- Low-stock detection
- Near-expiry detection
- Basic alerts
- Authentication and authorization
- Password hashing

### Potential Later Enhancements

- Email notifications
- Advanced reporting
- Multiple notification channels
- More detailed family administration
- Advanced inventory analytics

---

# 7. High-Level User Flow

```text
Register
   ↓
Login
   ↓
Create Family / Join Family
   ↓
Access Family Inventory
   ↓
Add / Update / Delete Items
   ↓
Consume / Restock
   ↓
Inventory History
   ↓
Dashboard + Alerts
```

# 8. Development Approach

HomeHub will be developed using an Agile/Scrum-style approach.

Each feature should progress through:

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

Where practical, features will be implemented as complete vertical slices:

```text
Requirement
→ API
→ Database
→ Backend
→ Frontend
→ Test
→ Review
```
