# Event Booking System POC

This project is a Proof of Concept (POC) for an event booking system built with **Spring Boot**. The core objective is to provide a RESTful API for managing and booking events, with a primary focus on handling **concurrent booking requests** robustly to prevent overbooking.

##  Features

* **Create Events**: Define new events with a name, date, and total number of seats.
* **List Events**: View all events that currently have seats available.
* **Book Seats**: Securely book one or more seats for an event. This operation is thread-safe.
* **View Bookings**: Retrieve a list of all bookings made by a specific user.
* **Cancel Bookings**: Cancel a booking and free up the seats.

---

##  Technology Stack

* **Framework**: Spring Boot 3.x
* **Language**: Java 17
* **Data Access**: Spring Data JPA / Hibernate
* **Database**: H2 In-Memory Database
* **Build Tool**: Apache Maven
* **API Documentation**: Springdoc OpenAPI (Swagger UI)
* **Testing**: JUnit 5 & Mockito

---

##  How to Run the Application

1.  **Prerequisites**:
    * JDK 17 or later
    * Apache Maven 3.6 or later

2.  **Clone the repository** and navigate into the directory.

3.  **Build and run the application** using the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```

4.  The application will start on `http://localhost:8080`.

---

## 📖 API Documentation (Swagger)

Once the application is running, you can access the interactive Swagger UI documentation in your browser. This interface allows you to view all API endpoints, see their request/response models, and test them directly.

* **Swagger UI URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)



---

## 🔒 Concurrency Strategy

The critical business logic for booking seats is protected against race conditions using **Pessimistic Locking**. When a booking transaction begins, the `Event` row in the database is locked. This forces any other concurrent transaction attempting to book seats for the *same event* to wait until the first transaction is complete. This strategy guarantees data consistency and strictly prevents overbooking, even under high load.

---

