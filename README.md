# PSP Service

[![Build Status](https://github.com/chumakig86/psp-service/actions/workflows/ci.yml/badge.svg)](https://github.com/chumakig86/psp-service/actions/workflows/ci.yml)


A simple **Payment Service Provider (PSP)** backend that processes payment requests, validates card data using Luhn’s algorithm, and routes transactions to mock acquirers based on BIN rules.

---

## 🧩 Features
- REST API for processing payments (`POST /api/payments`)
- BIN-based routing logic:
    - Even BIN sum → Acquirer A
    - Odd BIN sum → Acquirer B
- Mock acquirer decision (approve/deny)
- In-memory transaction storage
- Lightweight, no external dependencies

---

## 🛠️ Build and Run
### Option 1  (Gradle)

### Prerequisites
- **Java 17+**
- **Gradle** (wrapper included)

```bash
# Clone repository
git clone https://github.com/chumakig86/psp-service.git
cd psp-service

# Build project
./gradlew build

# Run application
./gradlew bootRun

Application will start at:
http://localhost:8080
```

### Option 2  (Docker)

### Prerequisites
- **Docker installed**

```bash
# Clone repository
git clone https://github.com/chumakig86/psp-service.git
cd psp-service

# Build the Docker image
docker build -t psp-service .

# Run it
docker run -p 8080:8080 psp-service


Application will start at:
http://localhost:8080
```

### Test API

You can send a payment request using `curl` like this:

```bash
curl --location 'http://localhost:8080/api/payments' \
--header 'Content-Type: application/json' \
--data '{
  "cardNumber": "4242424242424242",
  "expiryMonth": 12,
  "expiryYear": 2025,
  "cvv": "123",
  "amount": 150.75,
  "currency": "USD",
  "merchantId": "merchant_001"
}'
```

Or use swagger for testing:
### Swagger
http://localhost:8080/swagger-ui.html


### Example Response

```json
{
  "transactionId": "txn_1734567890",
  "status": "Approved",
  "message": "Transaction approved by Acquirer A"
}

```
