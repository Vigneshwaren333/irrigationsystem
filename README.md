# AgriTech Smart Irrigation System

A Java-based application for monitoring and controlling agricultural irrigation systems.

## Features

- User authentication with MySQL database
- Dashboard with overview of all critical metrics
- Soil moisture monitoring with historical data
- Irrigation control system with automatic and manual modes
- Sensor simulation for testing and development
- Weather forecast integration

## Prerequisites

- Java 11 or higher
- JavaFX (included in the lib directory)
- MySQL Server installed and running
- MySQL Connector/J (automatically downloaded by setup script)

## Database Setup

1. Install MySQL Server if you haven't already:
   - Windows: [MySQL Installer](https://dev.mysql.com/downloads/installer/)
   - Mac: `brew install mysql`
   - Linux: `sudo apt install mysql-server`

2. Start MySQL Server:
   - Windows: MySQL service should start automatically
   - Mac: `brew services start mysql`
   - Linux: `sudo systemctl start mysql`

3. Create a database and user (optional):
   ```sql
   CREATE DATABASE agritech;
   CREATE USER 'agritech_user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON agritech.* TO 'agritech_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

Note: The application will attempt to create the database and tables automatically on first run, but you need MySQL server running.

## Setup Instructions

1. Clone this repository
2. Run the setup script to download MySQL Connector/J:
   - Windows: `.vscode\setup_mysql.bat`
   - Mac/Linux: `chmod +x .vscode/setup_mysql.sh && .vscode/setup_mysql.sh`

3. Compile the application:
   ```
   javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp "lib/mysql-connector-j-8.3.0.jar" .vscode/*.java
   ```

4. Run the application:
   ```
   java --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp ".vscode;lib/mysql-connector-j-8.3.0.jar" MainApplication
   ```

## Login Credentials

The application automatically creates these default users:

- Admin account:
  - Username: `admin`
  - Password: `admin123`

- Regular user:
  - Username: `user`
  - Password: `password`

- Development testing:
  - For testing without MySQL, you can use:
  - Username: `admin`
  - Password: `admin`

## Troubleshooting

If you have issues connecting to MySQL:

1. Verify MySQL server is running:
   ```
   mysql --version
   ```

2. Check database connection parameters in `LoginPage.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/agritech";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = ""; // Update this with your MySQL password
   ```

3. Try connecting manually:
   ```
   mysql -u root -p
   ``` 