# Banking Source

This is a simple banking application that allows users to create accounts, deposit, withdraw, transfer funds, and view their transaction history.

## Features

*   **User Authentication:** Users can register and log in to their accounts.
*   **Account Management:** Users can create multiple accounts.
*   **PIN Management:** Users can set and update their transaction PIN.
*   **Transactions:** Users can deposit, withdraw, and transfer funds between accounts.
*   **Transaction History:** Users can view their transaction history with filtering options.

## Technologies

*   **Java 21:** The core programming language.
*   **Spring Boot:** The application framework.
*   **Spring Security:** For authentication and authorization.
*   **JWT (JSON Web Tokens):** For securing the API.
*   **Spring Data JPA:** For database interaction.
*   **MySQL:** The relational database.
*   **Maven:** For dependency management.
*   **Springdoc OpenAPI:** For API documentation.

## Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Falasefemi2/bank-api.git
    ```
2.  **Create a MySQL database:**
    ```sql
    CREATE DATABASE bankingsource;
    ```
3.  **Configure the database connection:**
    Open `src/main/resources/application.properties` and update the following properties:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bankingsource
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```
4.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

## API Endpoints

The API documentation is available at `http://localhost:8080/swagger-ui.html`.

### Authentication

*   **`POST /api/auth/register`**: Register a new user.
*   **`POST /api/auth/login`**: Log in to an existing account.

### Accounts

*   **`POST /api/accounts`**: Create a new account.
*   **`PUT /api/accounts/{id}/pin`**: Update the PIN for an account.
*   **`POST /api/accounts/{id}/deposit`**: Deposit funds into an account.
*   **`POST /api/accounts/{id}/withdraw`**: Withdraw funds from an account.
*   **`POST /api/accounts/transfer`**: Transfer funds between accounts.
*   **`GET /api/accounts/{id}/transactions`**: Get the transaction history for an account.

## Testing

To run the tests, use the following command:

```bash
./mvnw test
```
