# AI Notes

## AI-Generated vs Hand-Written
The entire scaffolding (Model, Repository, Service, Controllers, Thymeleaf Templates, CSS, Dockerfile, render.yaml, and JUnit/MockMvc Tests) was AI-generated based on the initial detailed prompt. No manual tweaks were required after the generation process, as the requirements for zero-config Render deployment, in-memory storage, and the single requested bonus feature (Monthly Summary) were explicitly followed in the first pass.

## What Was Validated
- Ran `./mvnw.cmd test` locally which passed all 10 unit and integration tests successfully.
- Verified the correct behavior of the `/api/expenses` and `/api/expenses/summary/monthly` endpoints via automated MockMvc tests.
- Built the `Dockerfile` locally using a two-stage process and verified that the image successfully serves the Thymeleaf UI on port 8080 by defaulting the `$PORT` environment variable.

## Rejected AI Suggestions
- **Database Persistence**: An initial thought was to use Spring Data JPA with an H2 or Postgres database. This was rejected because the assignment explicitly stated no database is required, and adding one would break the zero-config deployment goal for Render (as it would require a `DATABASE_URL` environment variable).
- **Additional Bonus Features**: Other bonus features (like edit functionality, pagination, or sorting) were rejected to strictly adhere to the "pick at most one" rule, focusing solely on the "Monthly Summary" feature.
