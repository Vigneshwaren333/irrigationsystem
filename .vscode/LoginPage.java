import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private MainApp mainApp;
    private Stage primaryStage;
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3308/agritech";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1109"; // MySQL root password
    
    // UI components
    private TextField usernameField;
    private PasswordField passwordField;
    private Text statusText;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    
    public LoginPage(Stage stage, MainApp mainApp) {
        this.primaryStage = stage;
        this.mainApp = mainApp;
    }
    
    public Scene createLoginScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Center - Login Form
        VBox loginForm = createLoginForm();
        root.setCenter(loginForm);
        
        // Scene setup
        Scene scene = new Scene(root, 900, 600);
        return scene;
    }
    
    private VBox createLoginForm() {
        VBox loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(50));
        loginForm.setMaxWidth(450);
        loginForm.setMaxHeight(500);
        loginForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );
        
        // Logo and title
        Text title = new Text("AgriTech Smart Irrigation");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web(PRIMARY_COLOR));
        
        Text subtitle = new Text("Login to your account");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.web("#6C757D"));
        
        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", 14));
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle(
            "-fx-background-color: #F8F9FA;" +
            "-fx-border-color: #CED4DA;" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 5 10;"
        );
        
        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", 14));
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle(
            "-fx-background-color: #F8F9FA;" +
            "-fx-border-color: #CED4DA;" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 5 10;"
        );
        
        // Enter key for password field
        passwordField.setOnAction(e -> attemptLogin());
        
        // Remember me checkbox
        rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setFont(Font.font("Arial", 14));
        
        // Forgot password link
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot password?");
        forgotPasswordLink.setFont(Font.font("Arial", 14));
        forgotPasswordLink.setTextFill(Color.web(PRIMARY_COLOR));
        
        HBox optionsBox = new HBox(20);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.getChildren().addAll(rememberMeCheckBox, forgotPasswordLink);
        
        // Login button
        loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(150);
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginButton.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        );
        loginButton.setOnAction(e -> attemptLogin());
        
        // Register option
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Text registerText = new Text("Don't have an account?");
        registerText.setFont(Font.font("Arial", 14));
        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setFont(Font.font("Arial", 14));
        registerLink.setTextFill(Color.web(PRIMARY_COLOR));
        registerBox.getChildren().addAll(registerText, registerLink);
        
        // Status message
        statusText = new Text();
        statusText.setFont(Font.font("Arial", 14));
        statusText.setFill(Color.web(ACCENT_COLOR));
        
        // Add all elements to form
        loginForm.getChildren().addAll(
            title,
            subtitle,
            new Separator(),
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            optionsBox,
            loginButton,
            statusText,
            new Separator(),
            registerBox
        );
        
        // Center the form in its container
        BorderPane.setAlignment(loginForm, Pos.CENTER);
        
        return loginForm;
    }
    
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            statusText.setText("Username and password are required");
            return;
        }
        
        // Attempt database authentication
        if (authenticateUser(username, password)) {
            // Login successful
            mainApp.setLoggedInUser(username);
            mainApp.showWelcomeScreen();
        } else {
            // Login failed
            statusText.setText("Invalid username or password");
            passwordField.clear();
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        // For demo purposes, allow admin/admin to bypass database check
        if (username.equals("admin") && password.equals("admin")) {
            return true;
        }
        
        try {
            // Try to connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password); // In a real application, passwords should be hashed
                    
                    // Execute query
                    try (ResultSet rs = stmt.executeQuery()) {
                        // If we found a matching user, authentication is successful
                        if (rs.next()) {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Log the error
            System.err.println("Database authentication error: " + e.getMessage());
            
            // For demo purposes, show a more user-friendly message
            statusText.setText("Database connection error. Please try again.");
            
            // Since this is a demo, allow fallback to dummy authentication
            // In a real application, you would handle this differently
            if (username.equals("user") && password.equals("password")) {
                return true;
            }
        }
        
        // If we get here, authentication failed
        return false;
    }
    
    public static void initializeDatabase() {
        // This method would be called once to set up the database
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create the database and table if they don't exist
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/", DB_USER, DB_PASSWORD)) {
                
                // Create database if it doesn't exist
                try (PreparedStatement stmt = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS agritech")) {
                    stmt.execute();
                }
                
                // Switch to the database
                try (Connection dbConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    // Create users table if it doesn't exist
                    String createTableSQL = 
                            "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(50) NOT NULL UNIQUE, " +
                            "password VARCHAR(50) NOT NULL, " +
                            "name VARCHAR(100), " +
                            "email VARCHAR(100), " +
                            "role VARCHAR(20) DEFAULT 'user', " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")";
                    
                    try (PreparedStatement stmt = dbConn.prepareStatement(createTableSQL)) {
                        stmt.execute();
                    }
                    
                    // Check if we need to insert default users
                    String checkUserSQL = "SELECT COUNT(*) FROM users";
                    try (PreparedStatement stmt = dbConn.prepareStatement(checkUserSQL);
                         ResultSet rs = stmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            // Insert default admin user
                            String insertUserSQL = 
                                    "INSERT INTO users (username, password, name, email, role) " +
                                    "VALUES (?, ?, ?, ?, ?)";
                            
                            try (PreparedStatement insertStmt = dbConn.prepareStatement(insertUserSQL)) {
                                // Admin user
                                insertStmt.setString(1, "admin");
                                insertStmt.setString(2, "admin123"); // In real app, use hashed password
                                insertStmt.setString(3, "Administrator");
                                insertStmt.setString(4, "admin@agritech.com");
                                insertStmt.setString(5, "admin");
                                insertStmt.executeUpdate();
                                
                                // Regular user
                                insertStmt.setString(1, "user");
                                insertStmt.setString(2, "password"); // In real app, use hashed password
                                insertStmt.setString(3, "Test User");
                                insertStmt.setString(4, "user@agritech.com");
                                insertStmt.setString(5, "user");
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
            
            System.out.println("Database initialization completed successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}
