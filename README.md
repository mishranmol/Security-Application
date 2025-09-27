<h1 align="center">🔐 <b>Spring Boot Security Application</b></h1>

<p align="center">
  A Spring Boot Security Application implementing <b>JWT Authentication & Authorization</b>, 
  <b>role-based access control</b>, and <b>session management</b> with support for 
  <b>Access Tokens and Refresh Tokens</b>.
</p>

<p align="center">
  The project follows the <b>MVC architecture</b>, uses an <b>H2 in-memory database</b> for quick setup, 
  and includes a <b>Global Exception Handler</b> to provide consistent and structured error responses.
</p> 


Features
<h3><b>🔑 Authentication & Authorization</b></h3>

-> Implemented JWT (JSON Web Token)–based authentication for securing API endpoints.

-> Each login request generates an Access Token (short-lived) and a Refresh Token (long-lived).

-> Access Tokens are required to access protected routes.

-> Refresh Tokens allow seamless session continuation without forcing users to re-login.



<h3><b>👥 Role-Based Access Control</b></h3>

-> The application defines multiple user roles (e.g., ROLE_USER, ROLE_ADMIN, etc.).

-> Different roles have access to different routes – for example:

A normal User can only access user-specific endpoints.

An Admin can access both user and admin-level routes.



<h3><b>🔒 Session Management</b></h3>

-> To prevent misuse of accounts, the system enforces a maximum of two active sessions per user.

-> If the same user logs in from multiple devices or browsers, only two sessions remain valid.

-> This provides better control over user access and reduces risks from credential sharing.



<h3><b>⚡ Exception Handling</b></h3>

-> A Global Exception Handler has been implemented using @ControllerAdvice.

-> Ensures all exceptions are caught in a centralized place and converted into clear, consistent error responses.




<h3><b>🛠️ Tech Stack</b></h3>

Backend Framework: Spring Boot

Security: Spring Security, JWT

Database: H2 (in-memory)

Build Tool: Maven

Language: Java
