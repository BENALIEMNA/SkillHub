# SkillHub ⚡

A **mini freelance platform for students** — post and complete easy tasks with no prior experience required.

## Features

- 🎓 **Student-focused** — all tasks are beginner/easy level
- 📋 **Post Tasks** — describe what you need and set a budget ($10–$100)
- 🔍 **Browse & Filter** — by category, difficulty, and keyword search
- ✉️ **Apply for Tasks** — send cover messages to task owners
- ✅ **Accept/Reject** — task owners review and accept applicants
- 👤 **Profiles** — bio, skills, and dashboard

## Tech Stack

| Layer    | Technology                          |
|----------|-------------------------------------|
| Frontend | React 19, React Router 7, Axios     |
| Backend  | Node.js, Express 5                  |
| Database | SQLite via `better-sqlite3`         |
| Auth     | JWT + bcryptjs                      |

## Quick Start

### 1. Backend

```bash
cd backend
cp .env.example .env        # edit JWT_SECRET
npm install
npm start                   # runs on http://localhost:5000
```

### 2. Frontend

```bash
cd frontend
npm install
npm start                   # runs on http://localhost:3000
```

The frontend proxies `/api` requests to the backend automatically.

## API Routes

| Method | Path                          | Auth | Description              |
|--------|-------------------------------|------|--------------------------|
| POST   | /api/users/register           | No   | Register a new user      |
| POST   | /api/users/login              | No   | Login and get JWT token  |
| GET    | /api/users/me                 | Yes  | Get current user profile |
| PUT    | /api/users/me                 | Yes  | Update profile           |
| GET    | /api/tasks                    | No   | List tasks (filterable)  |
| GET    | /api/tasks/:id                | No   | Get single task          |
| POST   | /api/tasks                    | Yes  | Create a task            |
| PUT    | /api/tasks/:id                | Yes  | Update task (owner only) |
| DELETE | /api/tasks/:id                | Yes  | Delete task (owner only) |
| GET    | /api/tasks/my/posted          | Yes  | My posted tasks          |
| POST   | /api/applications             | Yes  | Apply for a task         |
| GET    | /api/applications/task/:id    | Yes  | Applications for a task  |
| GET    | /api/applications/my          | Yes  | My applications          |
| PUT    | /api/applications/:id         | Yes  | Accept/reject applicant  |

## Categories

Web Development · Graphic Design · Writing & Content · Data Entry · Tutoring · Video Editing · Social Media · Translation · Research · Other

## Difficulty Levels

- **Beginner** — no experience needed
- **Easy** — basic skills required
- **Intermediate** — some experience helpful
