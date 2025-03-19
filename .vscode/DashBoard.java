import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DashBoard {

    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private final MainApp mainApp;
    
    public DashBoard(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public Scene createDashboardScene() {
        BorderPane dashboard = new BorderPane();
        dashboard.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Top Navigation Bar
        HBox navBar = createNavigationBar();
        dashboard.setTop(navBar);
        
        // Left Sidebar
        VBox sidebar = createSidebar();
        dashboard.setLeft(sidebar);
        
        // Main Content Area - Default to Overview
        OverviewPage overviewPage = new OverviewPage();
        dashboard.setCenter(overviewPage.createContent());
        
        return new Scene(dashboard, 1000, 700);
    }
    
    private HBox createNavigationBar() {
        HBox navBar = new HBox();
        navBar.setPadding(new Insets(15, 25, 15, 25));
        navBar.setSpacing(15);
        navBar.setStyle("-fx-background-color: " + PRIMARY_COLOR + ";");
        navBar.setPrefHeight(60);
        
        // App title
        Text title = new Text("AgriTech");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        
        // User info
        String username = mainApp.getLoggedInUser();
        HBox userBox = new HBox(8);
        userBox.setAlignment(Pos.CENTER);
        
        Text userIcon = new Text("👤"); // Simple user icon with Unicode
        userIcon.setFont(Font.font("Arial", 16));
        userIcon.setFill(Color.WHITE);
        
        Text userText = new Text(username != null ? username : "Guest");
        userText.setFont(Font.font("Arial", 14));
        userText.setFill(Color.WHITE);
        
        userBox.getChildren().addAll(userIcon, userText);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Nav buttons
        Button helpButton = createNavButton("Help");
        Button settingsButton = createNavButton("Settings");
        
        Button logoutButton = createNavButton("Logout");
        logoutButton.setOnAction(e -> mainApp.logout());
        
        navBar.getChildren().addAll(title, userBox, spacer, helpButton, settingsButton, logoutButton);
        
        return navBar;
    }
    
    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;"
            )
        );
        
        return button;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20, 0, 20, 0));
        sidebar.setSpacing(10);
        sidebar.setStyle("-fx-background-color: #343A40;");
        
        Text menuTitle = new Text("DASHBOARD");
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        menuTitle.setFill(Color.web("#ADB5BD"));
        VBox.setMargin(menuTitle, new Insets(0, 0, 10, 20));
        
        // Menu items
        Button overviewBtn = createSidebarButton("Overview", true);
        overviewBtn.setOnAction(e -> mainApp.showOverviewPage());
        
        Button soilMoistureBtn = createSidebarButton("Soil Moisture", false);
        soilMoistureBtn.setOnAction(e -> mainApp.showSoilMoisturePage());
        
        Button irrigationControlBtn = createSidebarButton("Irrigation Control", false);
        irrigationControlBtn.setOnAction(e -> mainApp.showIrrigationControlPage());
        
        Button sensorSimulationBtn = createSidebarButton("Sensor Simulation", false);
        sensorSimulationBtn.setOnAction(e -> mainApp.showSensorSimulationPage());
        
        sidebar.getChildren().addAll(
            menuTitle, 
            overviewBtn,
            soilMoistureBtn,
            irrigationControlBtn,
            sensorSimulationBtn
        );
        
        return sidebar;
    }
    
    private Button createSidebarButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setPrefWidth(220);
        button.setPrefHeight(40);
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setPadding(new Insets(0, 0, 0, 20));
        
        if (isActive) {
            button.setStyle(
                "-fx-background-color: " + SECONDARY_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;" +
                "-fx-border-width: 0 0 0 5;" +
                "-fx-border-color: " + ACCENT_COLOR + ";"
            );
        } else {
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #ADB5BD;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;"
            );
        }
        
        button.setOnMouseEntered(e -> {
            if (!button.getStyle().contains(SECONDARY_COLOR)) {
                button.setStyle(
                    "-fx-background-color: #2C3338;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!button.getStyle().contains(SECONDARY_COLOR)) {
                button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #ADB5BD;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        return button;
    }
}