# Budget Tracker

Budget Tracker is a web app with a React front-end and a RESTful Spring Boot back-end. 

## Back-End Setup Instructions

1. Create a database and a user to access it. The back-end is setup to use MySQL or MariaDB, but can easily be changed.

2. Set the environment variables for your database's URL `DB_URL` and your user's name `DB_USERNAME` and password `DB_PASSWORD`.

3. The API is located at `localhost:8080/api/expenses`

## Front-End Setup Instructions

1. Run `npm install` in the front-end's root directory.

2. Run `npm run dev` and the front-end will be available at `localhost:5173`.