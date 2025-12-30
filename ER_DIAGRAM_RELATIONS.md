# Entity Relationship (ER) Diagram & Table Relations

## Project Overview
WeCode BookIt is a meeting room booking system with credit-based management. This document outlines all the entity relationships and data flow in the system.

---

## Table of Contents
1. [Entity Definitions](#entity-definitions)
2. [Relationship Diagram](#relationship-diagram)
3. [Detailed Relationships](#detailed-relationships)
4. [Data Flow](#data-flow)

---

## Entity Definitions

### 1. **users** (Core Entity)
- **Primary Key**: `user_id` (UUID)
- **Role Types**: ADMIN, MANAGER, MEMBER
- **Purpose**: Stores all system users
- **Key Attributes**: 
  - `name`, `email`, `password_hash`, `role`, `credits`
  - Timestamp tracking: `created_at`, `updated_at`
  - Email is UNIQUE

### 2. **meeting_rooms** (Core Entity)
- **Primary Key**: `room_id` (UUID)
- **Purpose**: Stores meeting room details
- **Key Attributes**:
  - `room_name`, `room_type`, `seating_capacity`
  - `per_hour_cost` (hourly rate)
  - `room_cost` (total cost based on capacity + amenities)
  - `is_active` (Boolean status)

### 3. **amenities** (Master Data)
- **Primary Key**: `amenity_id` (UUID)
- **Purpose**: Maintains list of available amenities
- **Key Attributes**:
  - `amenity_name` (PROJECTOR, WIFI, CONFERENCE_CALL, etc.)
  - `credit_cost` (cost per amenity)
  - `is_active` (enable/disable amenity)

### 4. **room_amenities** (Junction/Bridge Table)
- **Primary Key**: Composite (`room_id`, `amenity_id`)
- **Purpose**: Maps many-to-many relationship between rooms and amenities
- **Key Attributes**:
  - Links rooms to their available amenities
  - Cascade delete enabled

### 5. **bookings** (Core Entity)
- **Primary Key**: `booking_id` (UUID)
- **Purpose**: Stores all room bookings made by users
- **Key Attributes**:
  - `meeting_title`, `meeting_date`, `meeting_type`
  - `start_time`, `end_time` (with validation: end_time > start_time)
  - `total_credits` (cost deducted from user)
  - `check_in_status` (PENDING, CHECKED_IN, NO_SHOW)
  - `penalty_applied`, `penalty_amount`
  - `status` (ACTIVE, CANCELLED)

### 6. **room_schedule** (Audit/Schedule Table)
- **Primary Key**: `schedule_id` (UUID)
- **Purpose**: Tracks booking schedule with audit trail
- **Key Attributes**:
  - `meeting_title`, `meeting_type`, `start_time`, `end_time`
  - `booked_by` (which user made the booking)
  - `status` (ACTIVE, CANCELLED)
  - One-to-one with bookings (unique `booking_id`)

### 7. **credit_transactions** (Audit Trail)
- **Primary Key**: `transaction_id` (UUID)
- **Purpose**: Records all credit movements
- **Key Attributes**:
  - `amount` (credits deducted/added)
  - `transaction_type` (BOOKING, REFUND, RESET)
  - `description` (reason for transaction)

### 8. **manager_credit_summary** (Aggregation Table)
- **Primary Key**: `user_id` (UUID)
- **Purpose**: Maintains credit balance summary for managers
- **Key Attributes**:
  - `total_credits` (2000 default)
  - `credits_used` (accumulated usage)
  - `penalty` (accumulated penalties)
  - `last_reset_at` (for credit reset cycle)

### 9. **seating_capacity_credits** (Configuration Table)
- **Primary Key**: Composite (`min_capacity`, `max_capacity`)
- **Purpose**: Lookup table for seating capacity cost mapping
- **Data Examples**:
  - 0-5 seats = 0 credits
  - 6-10 seats = 10 credits
  - 11-1000 seats = 20 credits

---

## Relationship Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                       │
│                     USERS (Central Entity)                           │
│              ┌──────────────────────┬──────────────┐                │
│              │ user_id (PK)         │              │                │
│              │ name, email          │              │                │
│              │ password_hash        │              │                │
│              │ role, credits        │              │                │
│              └──────────────────────┴──────────────┘                │
│                    │         │              │                       │
│          ┌─────────┴────┬────┴────┬─────────┴──────┐               │
│          │              │         │                 │               │
│          ▼              ▼         ▼                 ▼               │
│    ┌──────────┐  ┌────────────┐ ┌─────────────┐ ┌─────────────────┐│
│    │ bookings │  │ room_      │ │credit_trans │ │manager_credit_  ││
│    │          │  │schedule    │ │actions      │ │summary          ││
│    │ (1:M)    │  │ (1:1)      │ │ (1:M)       │ │ (1:1)           ││
│    └──────────┘  └────────────┘ └─────────────┘ └─────────────────┘│
│          │                                                           │
│          │ room_id (FK)                                             │
│          ▼                                                           │
│    ┌───────────────────────────────────────────┐                  │
│    │     MEETING_ROOMS (Core Entity)            │                  │
│    │  ┌─────────────────────────────────────┐  │                  │
│    │  │ room_id (PK)                        │  │                  │
│    │  │ room_name, room_type                │  │                  │
│    │  │ seating_capacity, per_hour_cost     │  │                  │
│    │  │ room_cost, is_active                │  │                  │
│    │  └─────────────────────────────────────┘  │                  │
│    └───────────────────────────────────────────┘                  │
│          │                                                           │
│          │ room_id (FK) - Composite PK with amenity_id             │
│          ▼                                                           │
│    ┌─────────────────────────┐                                     │
│    │  ROOM_AMENITIES         │                                     │
│    │ (Junction/Bridge Table) │                                     │
│    │  (M:M Relationship)     │                                     │
│    └─────────────────────────┘                                     │
│          │                                                           │
│          │ amenity_id (FK)                                          │
│          ▼                                                           │
│    ┌─────────────────────────────────────┐                         │
│    │      AMENITIES (Master Data)        │                         │
│    │  ┌─────────────────────────────────┐│                         │
│    │  │ amenity_id (PK)                 ││                         │
│    │  │ amenity_name (UNIQUE)           ││                         │
│    │  │ credit_cost, is_active          ││                         │
│    │  └─────────────────────────────────┘│                         │
│    └─────────────────────────────────────┘                         │
│                                                                       │
│  ┌─────────────────────────────────────────┐                       │
│  │ SEATING_CAPACITY_CREDITS (Config Table) │                       │
│  │  Lookup: seating_capacity → credit_cost │                       │
│  └─────────────────────────────────────────┘                       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Detailed Relationships

### 1️⃣ **USERS → BOOKINGS** (One-to-Many)
- **Cardinality**: 1 User : Many Bookings
- **FK Column**: `bookings.user_id` → `users.user_id`
- **Meaning**: One user can make multiple bookings
- **Example**: Manager "John" can book 5 different meeting rooms

### 2️⃣ **MEETING_ROOMS → BOOKINGS** (One-to-Many)
- **Cardinality**: 1 Room : Many Bookings
- **FK Column**: `bookings.room_id` → `meeting_rooms.room_id`
- **Meaning**: One meeting room can be booked multiple times
- **Example**: Room "Bhimtal" has 10 bookings

### 3️⃣ **BOOKINGS → ROOM_SCHEDULE** (One-to-One)
- **Cardinality**: 1 Booking : 1 Schedule Record
- **FK Column**: `room_schedule.booking_id` → `bookings.booking_id` (UNIQUE)
- **Meaning**: Each booking has exactly one schedule entry (audit trail)
- **Purpose**: Maintains read-only historical record of when room was booked

### 4️⃣ **USERS → ROOM_SCHEDULE** (One-to-Many)
- **Cardinality**: 1 User : Many Schedule Records
- **FK Column**: `room_schedule.booked_by` → `users.user_id`
- **Meaning**: One user can have multiple schedule entries (booking history)
- **Tracking**: Who booked what and when

### 5️⃣ **MEETING_ROOMS → ROOM_SCHEDULE** (One-to-Many)
- **Cardinality**: 1 Room : Many Schedule Records
- **FK Column**: `room_schedule.room_id` → `meeting_rooms.room_id`
- **Meaning**: One room can have many schedule entries
- **Purpose**: Track all bookings for a specific room

### 6️⃣ **USERS → CREDIT_TRANSACTIONS** (One-to-Many)
- **Cardinality**: 1 User : Many Transactions
- **FK Column**: `credit_transactions.user_id` → `users.user_id`
- **Meaning**: One user can have multiple credit transactions
- **Example**: User "John" has transactions: BOOKING(-140), REFUND(+50), PENALTY(-50)

### 7️⃣ **BOOKINGS → CREDIT_TRANSACTIONS** (One-to-Many)
- **Cardinality**: 1 Booking : Many Transactions
- **FK Column**: `credit_transactions.booking_id` → `bookings.booking_id`
- **Nullable**: Yes (for RESET transactions not tied to bookings)
- **Meaning**: One booking can trigger multiple transactions
- **Example**: One booking might have: Initial charge, then refund, then penalty

### 8️⃣ **USERS → MANAGER_CREDIT_SUMMARY** (One-to-One)
- **Cardinality**: 1 User : 1 Credit Summary
- **FK Column**: `manager_credit_summary.user_id` → `users.user_id` (PRIMARY KEY)
- **Meaning**: Each manager has exactly one credit summary record
- **Purpose**: Quick lookup of manager's current credit balance

### 9️⃣ **MEETING_ROOMS ↔ AMENITIES** (Many-to-Many)
- **Cardinality**: Many Rooms : Many Amenities
- **Junction Table**: `room_amenities`
- **Composite PK**: (`room_id`, `amenity_id`)
- **Meaning**: 
  - One room can have multiple amenities (e.g., "Bhimtal" has WIFI, PROJECTOR, CONFERENCE_CALL)
  - One amenity can be in multiple rooms (e.g., WIFI is in all 10 rooms)
- **Cascade Delete**: Enabled (deleting room/amenity deletes junction records)

### 🔟 **MEETING_ROOMS → SEATING_CAPACITY_CREDITS** (Many-to-One Logical)
- **Cardinality**: Many Rooms : One Capacity Range
- **Type**: Lookup Reference (No explicit FK)
- **Meaning**: Seating capacity determines credit cost
- **Example**: Room with 8 seats → 6-10 range → 10 credits
- **Usage**: Calculate `room_cost` = seating_capacity_credits + amenities_cost + per_hour_cost

---

## Data Flow

### 📌 Booking Flow (User Perspective)

```
MANAGER BOOKS A ROOM:
│
├─ Authenticate User (user_id from users table)
├─ Select Room (meeting_rooms table)
├─ Select Amenities (room_amenities lookup)
│
├─ Calculate Cost:
│  ├─ Base Cost = meeting_rooms.per_hour_cost
│  ├─ Seating Cost = seating_capacity_credits lookup
│  ├─ Amenity Cost = SUM(amenities.credit_cost) for selected amenities
│  └─ Total Cost = Base + Seating + Amenity + (hours * per_hour_cost)
│
├─ Deduct Credits:
│  ├─ Update users.credits (reduce by total cost)
│  ├─ Create booking record (bookings table)
│  ├─ Create schedule record (room_schedule table)
│  ├─ Create transaction record (credit_transactions table)
│  └─ Update manager_credit_summary (credits_used += cost)
│
└─ BOOKING COMPLETE
```

### 📌 Check-In Flow

```
MANAGER CHECK-IN FOR BOOKING:
│
├─ Find Today's Bookings:
│  └─ SELECT FROM bookings WHERE user_id = ? AND meeting_date = TODAY
│
├─ Manager Clicks Check-In:
│  └─ UPDATE bookings SET check_in_status = 'CHECKED_IN'
│
└─ CHECKED_IN COMPLETE
```

### 📌 No-Show Penalty Flow

```
END OF DAY - AUTO PENALTY CHECK:
│
├─ Find All Bookings for Today:
│  └─ SELECT FROM bookings WHERE meeting_date = TODAY
│
├─ For Each Booking:
│  ├─ IF check_in_status = 'PENDING' (not checked in):
│  │  ├─ Mark as NO_SHOW
│  │  ├─ penalty_applied = TRUE
│  │  ├─ penalty_amount = 50 (fixed)
│  │  ├─ Deduct from users.credits (-50)
│  │  ├─ Create penalty transaction (credit_transactions)
│  │  └─ Update manager_credit_summary.penalty += 50
│  │
│  └─ ELSE: No action
│
└─ PENALTY APPLIED IF APPLICABLE
```

### 📌 Credit Update Flow

```
MANAGER CREDIT SUMMARY UPDATE:
│
├─ When Booking Created:
│  ├─ manager_credit_summary.credits_used += booking_cost
│  └─ remaining = total_credits - credits_used - penalty
│
├─ When Penalty Applied:
│  ├─ manager_credit_summary.penalty += 50
│  └─ remaining = total_credits - credits_used - penalty
│
└─ Frontend Shows:
   └─ Remaining Credits = total_credits - credits_used - penalty
```

---

## SQL Constraint Summary

| Table | Constraint Type | Details |
|-------|-----------------|---------|
| **users** | UNIQUE | email must be unique |
| **users** | CHECK | role IN ('ADMIN', 'MANAGER', 'MEMBER') |
| **users** | CHECK | credits >= 0 |
| **meeting_rooms** | UNIQUE | room_name must be unique |
| **meeting_rooms** | CHECK | seating_capacity > 0 |
| **meeting_rooms** | CHECK | per_hour_cost >= 0 |
| **bookings** | FK | room_id → meeting_rooms.room_id |
| **bookings** | FK | user_id → users.user_id |
| **bookings** | CHECK | end_time > start_time |
| **bookings** | CHECK | check_in_status IN ('PENDING', 'CHECKED_IN', 'NO_SHOW') |
| **room_amenities** | CASCADE | Foreign keys with ON DELETE CASCADE |
| **credit_transactions** | FK | user_id → users.user_id |
| **credit_transactions** | FK | booking_id → bookings.booking_id (nullable) |
| **manager_credit_summary** | FK | user_id → users.user_id |

---

## Key Points

✅ **User Management**: Central `users` table with role-based access (ADMIN, MANAGER, MEMBER)

✅ **Room Management**: Rooms linked to amenities via many-to-many relationship

✅ **Booking Tracking**: Complete booking history via bookings + room_schedule + credit_transactions

✅ **Credit System**: Three levels of tracking:
   - Individual transactions (credit_transactions)
   - User balance (users.credits)
   - Manager summary (manager_credit_summary)

✅ **Audit Trail**: room_schedule provides immutable record of all bookings

✅ **Penalty System**: Check-in status with automatic no-show penalty calculation

✅ **Cost Calculation**: Composite cost from seating capacity + per-hour rate + amenities

---

## Index for Performance

```sql
-- Already created
CREATE INDEX idx_room_amenities_amenity_id ON room_amenities(amenity_id);

-- Recommended additions:
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_room_id ON bookings(room_id);
CREATE INDEX idx_bookings_meeting_date ON bookings(meeting_date);
CREATE INDEX idx_credit_transactions_user_id ON credit_transactions(user_id);
CREATE INDEX idx_room_schedule_booked_by ON room_schedule(booked_by);
```

---

## Document Version
- **Created**: December 30, 2025
- **Project**: WeCode BookIt Backend
- **Status**: Active Schema

