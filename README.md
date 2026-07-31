# Smart Expense Tracker

A full-stack, in-memory Spring Boot + Thymeleaf application to track expenses, filter by category, and view a monthly summary.

## Prerequisites
- Java 17+
- Maven (only for local dev)

## Local Run Instructions
To build and run the application locally:
```bash
./mvnw clean install
./mvnw spring-boot:run
```
Then, open http://localhost:8080 in your browser.

## Running Tests
To run the automated test suite:
```bash
./mvnw test
```

## API Endpoints

1. **Add Expense**
   - **POST** `/api/expenses`
   - **Request Body (JSON):**
     ```json
     {
       "title": "Lunch",
       "amount": 15.50,
       "category": "Food",
       "date": "2026-07-31"
     }
     ```
   - **Success Response (201 Created):**
     ```json
     {
       "id": 1,
       "title": "Lunch",
       "amount": 15.50,
       "category": "Food",
       "date": "2026-07-31"
     }
     ```

2. **List Expenses**
   - **GET** `/api/expenses` (optional: `?category=Food`)
   - **Response (200 OK):**
     ```json
     [
       {
         "id": 1,
         "title": "Lunch",
         "amount": 15.50,
         "category": "Food",
         "date": "2026-07-31"
       }
     ]
     ```

3. **Total Amount**
   - **GET** `/api/expenses/total` (optional: `?category=Food`)
   - **Response (200 OK):**
     `15.50`

4. **Delete Expense**
   - **DELETE** `/api/expenses/{id}`
   - **Response:** `204 No Content` on success, `404 Not Found` if id doesn't exist.

5. **Monthly Summary (Bonus Feature)**
   - **GET** `/api/expenses/summary/monthly`
   - **Response (200 OK):**
     ```json
     {
       "2026-07": {
         "total": 15.50,
         "byCategory": {
           "Food": 15.50
         }
       }
     }
     ```

**Note:** This project's chosen bonus feature is the Monthly Summary endpoint only. The Docker/render.yaml setup exists purely for optional deployment convenience and is not claimed as a second bonus.
**Note:** All data is stored in-memory and will reset upon server restart.

## One-Click Render Deploy
You can deploy this application for free on Render with zero manual configuration.
1. Go to https://render.com, log in with GitHub
2. Click "New +" -> "Blueprint"
3. Select the `smart-expense-tracker` repo — Render reads `render.yaml` automatically
4. Click "Apply"/"Create" without changing any field
5. Wait for build + deploy
6. Copy the live URL (e.g. `https://smart-expense-tracker.onrender.com`)

No environment variables, build commands, or start commands need to be entered manually.

## Live Demo
<RENDER_URL_HERE>

## Deployment Notes
Render's free tier spins down the application after 15 minutes of inactivity. The first request after it sleeps will experience a cold start (can take 1-2 minutes). Using an external uptime monitor (like UptimeRobot) pinging `/` every 5 minutes is optional to prevent this, but is not required for grading.
