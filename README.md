# BFHL API

POST `/bfhl` — categorizes a mixed string array into numbers, alphabets, and specials.

## Update your details

Open `src/com/chitkara/bfhl/BfhlApplication.java` and update the constants at the top,
or set environment variables before running:

```
USER_FULL_NAME=your name
USER_DOB=ddmmyyyy
USER_EMAIL=your@email.com
USER_ROLL_NUMBER=YOUR123
```

## Run locally

```bash
chmod +x build.sh && ./build.sh
java -jar bfhl-api.jar
# API live at http://localhost:8080/bfhl
```

## Run tests

```bash
javac -d out/classes $(find src -name "*.java")
javac -cp out/classes -d out/test-classes test/com/chitkara/bfhl/BfhlTest.java
java -cp out/classes:out/test-classes com.chitkara.bfhl.BfhlTest
```

## Deploy to Render (free)

1. Push this folder to GitHub
2. Render → New Web Service → connect repo
3. Runtime: **Docker**
4. That's it — Render auto-reads the Dockerfile

Your endpoint: `https://your-app.onrender.com/bfhl`

## Example

```bash
curl -X POST https://your-app.onrender.com/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a", "1", "334", "4", "R", "$"]}'
```

```json
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

## Project structure

```
src/com/chitkara/bfhl/
├── BfhlApplication.java          # Entry point + HTTP server
├── controller/BfhlHandler.java   # POST /bfhl route
├── service/
│   ├── BfhlService.java          # Interface
│   └── BfhlServiceImpl.java      # Business logic
├── dto/
│   ├── BfhlRequest.java
│   └── BfhlResponse.java
└── server/
    ├── JsonParser.java           # Request JSON parser
    └── JsonSerializer.java       # Response JSON serializer

test/com/chitkara/bfhl/
└── BfhlTest.java                 # 58 assertions, 0 external deps
```
