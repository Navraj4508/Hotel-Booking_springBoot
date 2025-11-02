# 🏨 Hotel Booking System (Spring Boot + Docker)

A **Hotel Booking Management System** built using **Spring Boot, Java, Spring Data JPA, and MySQL**.  
This system allows users to **search available rooms, book hotels, make payments**, and for admins to **manage rooms, users, and bookings** through RESTful APIs.



## 🚀 Features

- 🔐 **JWT Authentication & Authorization**  
- 🏠 **Room Management** – Add, edit, and view room details  
- 📅 **Booking Management** – Create and manage room bookings and Notification sends in email
- 💳 **Payment Integration (Razorpay)**  
- 👥 **Role-Based Access** (Admin/User)  
- ⚙️ **Global Exception Handling & Validation**  
- 🧪 **Postman Collection** included for easy API testing  

---

## 🛠️ Tech Stack

| Category | Technology |
|-----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.4.4 |
| Database | MySQL |
| Security | Spring Security, JWT, BCrypt |
| Build Tool | Maven |
| Containerization | Docker, Docker Compose |
| Payment Gateway | Razorpay |
| API Testing | Postman |

---

## 🧩 Architecture Diagram

flowchart TD
    A[Client / Postman] --> B[Spring Boot REST API]
    B --> C[Spring Security + JWT]
    B --> D[Service Layer]
    D --> E[Spring Data JPA]
    E --> F[(MySQL Database)]
    D --> G[Razorpay Payment API]
