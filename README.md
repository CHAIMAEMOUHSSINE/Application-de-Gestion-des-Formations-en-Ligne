# Gestion des Formations enligne - JavaFX Application

A desktop application for managing employees and their training, built with JavaFX, MySQL, and Maven.

## Features

- Employee list with details (ID, name, email, position, creation date)
- Add, delete, and view employee information
- Export employee data to CSV
- Progress indicators and status bar
- Modern UI with FXML and BootstrapFX

## Technologies

- Java 17+ (tested with JDK 23)
- JavaFX 17
- MySQL
- Maven
- FXML for UI
- BootstrapFX for styling

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven
- MySQL server running with a database named `DB_ENTREPRISE`
- MySQL user `root` with no password (or update credentials in `DBConnection.java`)

### Setup

1. Clone the repository:
2. Configure your MySQL database:
   - Create the database and required tables.

3. Build the project:
4. Run the application:
Or run `MainApplication` from your IDE.

### Testing

- Run `SimpleApp` or `MainAppTest` for simplified interface and DB connection tests.

## Project Structure

- `src/main/java/ma/enset/exam2test/` - Java source code
- `src/main/resources/ma/enset/exam2test/` - FXML and resources
- `DAO/DBConnection.java` - Database connection logic


