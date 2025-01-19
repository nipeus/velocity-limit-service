# velocity-limit-service

This project is a Spring Boot application that processes financial transactions with velocity limits and exposes API for managing deposits. It uses an in-memory H2 database for storage.

## Features
- **Transaction Processing:** The application processes financial transactions, ensuring that they adhere to predefined velocity limits.
- Exposes a REST API to manage deposits.
- Transactions are stored in an H2 in-memory database
- On start application loads transactions from `input.txt` and stores processing results to `output.txt` that can be compared with reference `output-expected.txt`
- **Transaction Validation:** Implements a Chain of Responsibility pattern for validating transactions.
    - The chain of validators is configurable through `AppConfig.java`.
    - If any validation fails, the process terminates and returns an appropriate result.
    - The `ValidationResult` is returned with an `ErrorCode` enum that can be extended for localization of error messages.

### **Running the Application**

Start the application:
```bash
./gradlew bootRun
```

The application will run on `http://localhost:8080` by default.

---

## **Configuration**

The application can be configured using the `application.properties` file located in the `src/main/resources` directory.

### Default Configuration:
```properties
spring.application.name=velocity-limit-service
spring.datasource.username=sa
spring.datasource.password=pwd
spring.datasource.url=jdbc:h2:mem:transactionsdb
spring.h2.console.enabled=true
velocity.day.limit.amount=5000
velocity.day.limit.count=3
velocity.week.limit.amount=20000
app.input.file=input.txt
app.output.file=output.txt
```

- **H2 Console:** Accessible at `http://localhost:8080/h2-console`.
---

## **API Documentation**

### **Deposit API**

**Endpoint:** `POST /deposit`

**Request Body:**
```json
{
  "id": "1234",
  "customerId": "5678",
  "loadAmount": 100.50,
  "time": "2025-01-01T10:00:00Z"
}
```

**Response:**
```json
{
  "id": "1234",
  "customerId": "5678",
  "accepted": true
}
```

---


### **Run Tests**
Run the unit tests using:
```bash
./gradlew test
```
