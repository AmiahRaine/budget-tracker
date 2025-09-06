# Budget Tracker

Budget Tracker is a web app that helps users track their expenses and view their total balance. It utilizes a React front-end and a RESTful Spring Boot back-end. The back-end makes use of MariaDB for data persistence.

Demo video on YouTube:

[![Demo Video](https://i.ytimg.com/vi/rxc4wPu4Kh8/maxresdefault.jpg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGCwgZSgpMA8=&rs=AOn4CLAo9NYcxbT9uZYIoKAbOdCQlKWDPw)](https://www.youtube.com/watch?v=rxc4wPu4Kh8)

# Technologies

## Front-End
* React
* JavaScript
* Vite
* Axios
* Tanstack Query

## Back-End
* Spring Boot
* Java
* MariaDB
* Hibernate JPA

# Local Setup

## Back-End Setup Instructions

1. Create a database and a user to access it. The back-end is setup to use MySQL or MariaDB, but can easily be changed.

2. Set the environment variables for your database's URL `DB_URL` and your user's name `DB_USERNAME` and password `DB_PASSWORD`.

3. The API is located at `localhost:8080/api/expenses`. For paginated results append `/paged` and for total balance append `/total`.

## Front-End Setup Instructions

1. Run `npm install` in the front-end's root directory.

2. Run `npm run dev` and the front-end will be available at `localhost:5173`.
