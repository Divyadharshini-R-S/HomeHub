# HomeHub V1 — Requirements Analysis

## 1. Purpose of Analysis

Requirements analysis converts the initial HomeHub requirements into clear, structured, and testable system behavior.

The analysis answers:

> **What exactly must the system do, who interacts with it, what rules apply, and what happens when something goes wrong?**

This analysis is the bridge between **Requirements** and **Design**.

---

# 2. Actors and Responsibilities

| Actor | Responsibilities |
|---|---|
| User | Register, login, access HomeHub |
| Family Owner | Create and manage a family |
| Family Member | Join a family and manage shared inventory |
| System | Authenticate users, enforce rules, maintain history, detect alerts |

A user becomes associated with a family through a family membership record.

---

# 3. Core Use Cases

## UC-01 — Register

**Actor:** User

### Main Flow

```text
User enters username, email, password
        ↓
System validates input
        ↓
System checks email uniqueness
        ↓
Password is hashed
        ↓
User is saved
        ↓
Registration succeeds
```

### Exceptions

- Email already exists.
- Required data is missing.
- Input format is invalid.

---

## UC-02 — Login

**Actor:** User

### Main Flow

```text
User enters email + password
        ↓
System finds user
        ↓
System verifies password
        ↓
JWT is generated
        ↓
Token returned to client
```

### Exception

Invalid credentials → authentication failure.

---

## UC-03 — Create Family

**Actor:** Authenticated User

### Main Flow

```text
User requests family creation
        ↓
System validates request
        ↓
Family created
        ↓
Unique invite code generated
        ↓
Creator becomes OWNER
```

---

## UC-04 — Join Family

**Actor:** Authenticated User

### Main Flow

```text
User submits invite code
        ↓
System validates invite code
        ↓
Family identified
        ↓
Membership created
        ↓
User becomes MEMBER
```

### Exceptions

- Invite code does not exist.
- User is already a member.

---

## UC-05 — Add Inventory Item

**Actor:** Family Member

### Main Flow

```text
User submits item details
        ↓
System authenticates user
        ↓
System verifies family membership
        ↓
System validates item
        ↓
Inventory item created
```

---

## UC-06 — Consume Inventory

**Actor:** Family Member

### Main Flow

```text
User requests consumption
        ↓
System authenticates user
        ↓
System checks family authorization
        ↓
System checks available quantity
        ↓
Quantity is reduced
        ↓
History record created
        ↓
Low-stock condition evaluated
```

### Business Rule

```text
consumeQuantity <= currentQuantity
```

If:

```text
consumeQuantity > currentQuantity
```

the operation must be rejected.

---

## UC-07 — Restock Inventory

**Actor:** Family Member

```text
User requests restock
        ↓
Authenticate
        ↓
Authorize family access
        ↓
Validate quantity
        ↓
Increase inventory quantity
        ↓
Create history record
```

---

## UC-08 — View Inventory

**Actor:** Family Member

The system shall return inventory items belonging to the user's family.

The system must not return inventory belonging to another family.

---

## UC-09 — View History

**Actor:** Family Member

The system shall return inventory changes associated with the requested item.

Each record identifies:

- Who performed the action
- What action occurred
- How much quantity changed
- When it occurred

---

## UC-10 — Dashboard and Alerts

**Actor:** Family Member

The dashboard should identify:

- Total inventory items
- Low-stock items
- Near-expiry items

Low-stock condition:

```text
quantity <= minimum_stock
```

Near-expiry behavior requires a defined warning period.

---

# 4. Functional Requirement Analysis

## 4.1 Authentication

The application requires identity before accessing protected household data.

Therefore:

```text
Registration
→ Login
→ JWT
→ Authenticated Requests
```

Authentication answers:

> **Who is the user?**

---

## 4.2 Authorization

Authentication alone is not sufficient.

After identifying the user, the system must determine whether the user is allowed to access the requested family data.

Authorization answers:

> **Is this user allowed to access this resource?**

Example:

```text
User A
  ↓
JWT identifies User A
  ↓
Request inventory item 25
  ↓
Check item's family
  ↓
Check User A's family membership
```

If the user does not belong to that family:

```text
403 Forbidden
```

---

# 5. Family Membership Analysis

A user and family have a relationship that needs additional information:

- Role
- Joined date

Therefore, membership should be represented explicitly by `FamilyMember`.

Conceptually:

```text
USER
  │
  │ N
  ↓
FAMILY_MEMBER
  ↑
  │ N
  │
FAMILY
```

This allows the system to represent:

```text
User X → Family A → OWNER
User Y → Family A → MEMBER
```

The model can also support a user belonging to multiple families if that rule is enabled.

---

# 6. Inventory Business Rules

### BR-01 — Family Ownership

Every inventory item must belong to one family.

### BR-02 — Family Access

Only members of the item's family may access it.

### BR-03 — Positive Consumption

Consumption quantity must be greater than zero.

### BR-04 — No Negative Inventory

Consumption cannot make quantity negative.

```text
consumeQuantity <= currentQuantity
```

### BR-05 — Positive Restock

Restock quantity must be greater than zero.

### BR-06 — Low Stock

An item is low-stock when:

```text
quantity <= minimum_stock
```

### BR-07 — History

Inventory-changing operations must create history records.

---

# 7. Data Analysis

The requirements imply these main data objects:

```text
User
Family
FamilyMember
InventoryItem
InventoryHistory
Alert
```

### User

Represents an application user.

### Family

Represents a household/group.

### FamilyMember

Represents the relationship between a user and a family.

### InventoryItem

Represents a grocery item owned by a family.

### InventoryHistory

Represents an inventory change performed by a user.

### Alert

Represents a notification generated for a user based on an inventory condition.

---

# 8. Relationship Analysis

## User → FamilyMember

```text
1 : N
```

One user can have multiple membership records if multiple-family membership is supported.

## Family → FamilyMember

```text
1 : N
```

One family can have many members.

Therefore:

```text
USER N : N FAMILY
```

is represented through:

```text
FAMILY_MEMBER
```

## Family → InventoryItem

```text
1 : N
```

One family can have many inventory items.

Each inventory item belongs to one family.

## InventoryItem → InventoryHistory

```text
1 : N
```

One inventory item can have many history records.

## User → InventoryHistory

```text
1 : N
```

One user can perform many inventory actions.

---

# 9. Data Integrity Analysis

Important constraints identified from the requirements:

- User email must be unique.
- Family invite code must be unique.
- A user/family membership combination must not be duplicated.
- Inventory quantity must not become negative.
- Inventory quantity changes must be recorded in history.
- Inventory items must reference an existing family.
- History records must reference an existing inventory item and user.

---

# 10. Error and Exception Analysis

| Situation | Expected Behavior |
|---|---|
| Duplicate email | Reject registration |
| Invalid login | Return authentication error |
| Invalid invite code | Reject join request |
| Duplicate membership | Reject or safely handle |
| Item not found | Return not-found error |
| Invalid quantity | Reject request |
| Consume more than available | Reject operation |
| Access another family's item | Return 403 |
| Invalid request data | Return validation error |

---

# 11. Non-Functional Analysis

## Security

The system handles passwords and household data, so security is a core requirement.

Required approach:

```text
Password
→ BCrypt
→ Password Hash
→ Database
```

JWT will be used for authenticated API requests.

## Reliability

Consume/restock operations affect both:

```text
Inventory quantity
+
Inventory history
```

These changes should be handled consistently so one operation does not succeed while the other fails.

## Maintainability

The backend should use a layered structure:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs should separate API request/response models from database entities.

## Testability

Business rules such as:

- Cannot consume more than available quantity.
- User cannot access another family's inventory.
- Duplicate emails are rejected.

should be testable independently.

---

# 12. Requirement Traceability

| Requirement Area | Main Analysis | Design Impact |
|---|---|---|
| Registration | Unique email, password security | User entity, registration API, BCrypt |
| Login | Credential validation | Security, JWT |
| Family | Ownership and membership | Family + FamilyMember |
| Inventory | Family-scoped data | InventoryItem + family FK |
| Consume | Quantity validation | Service business logic |
| Restock | Quantity increase | Service business logic |
| History | Track actor and action | InventoryHistory |
| Dashboard | Aggregate inventory data | Dashboard API |
| Alerts | Low-stock/expiry conditions | Alert design |
| Authorization | Family-level access | JWT + authorization checks |

---

# 13. Acceptance Criteria Summary

A feature is considered functionally complete when its defined behavior can be verified.

Examples:

### Registration

- Valid user can register.
- Duplicate email is rejected.
- Password is not stored in plain text.

### Login

- Valid credentials return a JWT.
- Invalid credentials are rejected.

### Family

- User can create a family.
- Invite code is generated.
- Another user can join with a valid invite code.

### Inventory

- Authorized family member can add an item.
- Family member can view their family inventory.
- User cannot view another family's inventory.

### Consumption

- Valid consumption decreases quantity.
- Consumption greater than available quantity is rejected.
- History is created.

### Restock

- Valid restock increases quantity.
- History is created.

### Alerts

- Low-stock condition is detected.
- Near-expiry condition is detected according to the configured warning period.

---

# 14. Analysis → Design Handoff

The analysis establishes the following design direction:

```text
Requirements
     ↓
Actors + Use Cases
     ↓
Business Rules
     ↓
Data Objects
     ↓
Relationships
     ↓
Validation + Exceptions
     ↓
Security Requirements
     ↓
Design
```

The next design activities are:

1. System architecture
2. Component/module design
3. ERD/database design
4. API request/response contracts
5. Security design
6. UI design
7. Alert design

Only after these are sufficiently defined should implementation begin.
