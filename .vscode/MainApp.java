import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Import custom classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

// JavaFX imports
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.*;

// Import custom UI classes (these need to be in the classpath when compiling)
// import LoginPage;  // These imports are not needed as the classes are in the default package
// import WelcomeScreen;
// import DashBoard;
// import OverviewPage;
// import SoilMoisturePage;
// import IrrigationControlPage;
// import SensorSimulationPage;

public class MainApp extends Application {
    
    private Stage primaryStage;
    private String loggedInUser; // Track the logged-in user
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("AgriTech Smart Irrigation System");
        
        // Initialize database (only needed once)
        try {
            LoginPage.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            // Continue with the application even if DB fails
        }
        
        // Show login page instead of welcome screen
        showLoginPage();
    }
    
    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(primaryStage, this);
        Scene loginScene = loginPage.createLoginScene();
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    public void showWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(primaryStage, this);
        Scene welcomeScene = welcomeScreen.createWelcomeScene();
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }
    
    public void showDashboard() {
        DashBoard dashboardUI = new DashBoard(this);
        Scene dashboardScene = dashboardUI.createDashboardScene();
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }
    
    public void showOverviewPage() {
        OverviewPage overviewPage = new OverviewPage();
        primaryStage.getScene().setRoot(overviewPage.createContent());
    }
    
    public void showSoilMoisturePage() {
        // Implement soil moisture page
        // This method will be implemented when the page is ready
        SoilMoisturePage soilMoisturePage = new SoilMoisturePage(this);
        Scene soilMoistureScene = new Scene(soilMoisturePage.createContent(), 1000, 800);
        primaryStage.setScene(soilMoistureScene);
        primaryStage.show();
    }
    
    public void showIrrigationControlPage() {
        // Implement irrigation control page
        // This method will be implemented when the page is ready
        IrrigationControlPage irrigationControlPage = new IrrigationControlPage(this);
        Scene irrigationControlScene = new Scene(irrigationControlPage.createContent(), 1000, 800);
        primaryStage.setScene(irrigationControlScene);
        primaryStage.show();
    }
    
    public void showWeatherForecastPage() {
        // Implement weather forecast page
        // This method will be implemented when the page is ready
    }
    
    public void showIrrigationSchedulerPage() {
        // Implement irrigation scheduler page
        // This method will be implemented when the page is ready
    }
    
    public void showAnalyticsPage() {
        // Implement analytics page
        // This method will be implemented when the page is ready
    }
    
    public void showSensorSimulationPage() {
        SensorSimulationPage sensorSimulationPage = new SensorSimulationPage(this);
        Scene sensorScene = new Scene(sensorSimulationPage.createContent(), 1000, 800);
        primaryStage.setScene(sensorScene);
        primaryStage.show();
    }
    
    // Method to handle logout
    public void logout() {
        loggedInUser = null;
        showLoginPage();
    }
    
    // Getter and setter for loggedInUser
    public String getLoggedInUser() {
        return loggedInUser;
    }
    
    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 