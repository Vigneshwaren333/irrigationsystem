import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OverviewPage extends Application {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent(), 900, 700);
        primaryStage.setTitle("AgriTech Irrigation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Add this method to fix the error
    public Parent createContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Header
        Text title = new Text("System Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#333333"));
        
        Text subtitle = new Text("Monitor your farm's irrigation status at a glance");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.web("#6C757D"));
        
        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(title, subtitle);
        
        // Metrics cards
        HBox metricsBox = new HBox(20);
        metricsBox.setPrefHeight(180);
        metricsBox.getChildren().addAll(
            createSoilMoistureCard(),
            createWindSpeedCard(),
            createTemperatureCard()
        );
        
        // Input form
        VBox inputForm = createInputForm();
        
        // Last updated timestamp
        HBox timestampBox = new HBox();
        timestampBox.setAlignment(Pos.CENTER_RIGHT);
        Text timestamp = new Text("Last updated: March 14, 2025, 10:45 AM");
        timestamp.setFont(Font.font("Arial", 12));
        timestamp.setFill(Color.web("#6C757D"));
        timestampBox.getChildren().add(timestamp);
        
        root.getChildren().addAll(headerBox, metricsBox, inputForm, timestampBox);
        
        return root;
    }
    
    private VBox createSoilMoistureCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        
        Text cardTitle = new Text("Soil Moisture");
        cardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cardTitle.setFill(Color.web("#333333"));
        
        Text valueText = new Text("42%");
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueText.setFill(Color.web("#2E8B57"));
        
        // Progress bar
        HBox progressContainer = new HBox();
        progressContainer.setPrefHeight(10);
        Rectangle progressBg = new Rectangle(210, 8);
        progressBg.setArcWidth(10);
        progressBg.setArcHeight(10);
        progressBg.setFill(Color.web("#E9ECEF"));
        
        StackPane progressStack = new StackPane();
        progressStack.setPrefWidth(210);
        
        Rectangle progressBar = new Rectangle(88.2, 8); // 42% of 210
        progressBar.setArcWidth(10);
        progressBar.setArcHeight(10);
        progressBar.setFill(Color.web("#2E8B57"));
        progressBar.setTranslateX(-61); // Aligns to left side: (210-88.2)/2 * -1
        
        progressStack.getChildren().addAll(progressBg, progressBar);
        progressContainer.getChildren().add(progressStack);
        
        // Status text
        Text optimalRange = new Text("Optimal range: 35% - 45%");
        optimalRange.setFont(Font.font("Arial", 14));
        optimalRange.setFill(Color.web("#6C757D"));
        
        Text status = new Text("Status: Optimal");
        status.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        status.setFill(Color.web("#2E8B57"));
        
        card.getChildren().addAll(cardTitle, valueText, progressContainer, optimalRange, status);
        return card;
    }
    
    private VBox createWindSpeedCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        
        Text cardTitle = new Text("Wind Speed");
        cardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cardTitle.setFill(Color.web("#333333"));
        
        HBox valueBox = new HBox(5);
        valueBox.setAlignment(Pos.BASELINE_LEFT);
        
        Text valueText = new Text("12");
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueText.setFill(Color.web("#8B5CF6"));
        
        Text unitText = new Text("km/h");
        unitText.setFont(Font.font("Arial", 18));
        unitText.setFill(Color.web("#6C757D"));
        
        valueBox.getChildren().addAll(valueText, unitText);
        
        // Progress bar
        HBox progressContainer = new HBox();
        progressContainer.setPrefHeight(10);
        Rectangle progressBg = new Rectangle(210, 8);
        progressBg.setArcWidth(10);
        progressBg.setArcHeight(10);
        progressBg.setFill(Color.web("#E9ECEF"));
        
        StackPane progressStack = new StackPane();
        progressStack.setPrefWidth(210);
        
        Rectangle progressBar = new Rectangle(63, 8); // 30% of 210
        progressBar.setArcWidth(10);
        progressBar.setArcHeight(10);
        progressBar.setFill(Color.web("#8B5CF6"));
        progressBar.setTranslateX(-73.5); // Aligns to left side: (210-63)/2 * -1
        
        progressStack.getChildren().addAll(progressBg, progressBar);
        progressContainer.getChildren().add(progressStack);
        
        // Direction text
        Text direction = new Text("Direction: North-East");
        direction.setFont(Font.font("Arial", 14));
        direction.setFill(Color.web("#6C757D"));
        
        Text status = new Text("Status: Moderate");
        status.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        status.setFill(Color.web("#8B5CF6"));
        
        card.getChildren().addAll(cardTitle, valueBox, progressContainer, direction, status);
        return card;
    }
    
    private VBox createTemperatureCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        
        Text cardTitle = new Text("Temperature");
        cardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cardTitle.setFill(Color.web("#333333"));
        
        HBox valueBox = new HBox(5);
        valueBox.setAlignment(Pos.BASELINE_LEFT);
        
        Text valueText = new Text("24");
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueText.setFill(Color.web("#F97316"));
        
        Text unitText = new Text("°C");
        unitText.setFont(Font.font("Arial", 18));
        unitText.setFill(Color.web("#6C757D"));
        
        valueBox.getChildren().addAll(valueText, unitText);
        
        // Progress bar
        HBox progressContainer = new HBox();
        progressContainer.setPrefHeight(10);
        Rectangle progressBg = new Rectangle(210, 8);
        progressBg.setArcWidth(10);
        progressBg.setArcHeight(10);
        progressBg.setFill(Color.web("#E9ECEF"));
        
        StackPane progressStack = new StackPane();
        progressStack.setPrefWidth(210);
        
        Rectangle progressBar = new Rectangle(126, 8); // 60% of 210
        progressBar.setArcWidth(10);
        progressBar.setArcHeight(10);
        progressBar.setFill(Color.web("#F97316"));
        progressBar.setTranslateX(-42); // Aligns to left side: (210-126)/2 * -1
        
        progressStack.getChildren().addAll(progressBg, progressBar);
        progressContainer.getChildren().add(progressStack);
        
        // Range text
        Text range = new Text("Daily range: 18°C - 27°C");
        range.setFont(Font.font("Arial", 14));
        range.setFill(Color.web("#6C757D"));
        
        Text status = new Text("Status: Normal");
        status.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        status.setFill(Color.web("#F97316"));
        
        card.getChildren().addAll(cardTitle, valueBox, progressContainer, range, status);
        return card;
    }
    
    private VBox createInputForm() {
        VBox formContainer = new VBox(15);
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        formContainer.setPadding(new Insets(20));
        
        Text formTitle = new Text("Manual Sensor Readings Input");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setFill(Color.web("#333333"));
        
        // Location dropdown
        VBox locationBox = new VBox(5);
        Label locationLabel = new Label("Location:");
        locationLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        ComboBox<String> locationDropdown = new ComboBox<>();
        locationDropdown.getItems().addAll("Field A - North", "Field A - South", "Field B - East", "Field B - West");
        locationDropdown.setValue("Field A - North");
        locationDropdown.setMaxWidth(Double.MAX_VALUE);
        locationDropdown.setPrefHeight(35);
        
        locationBox.getChildren().addAll(locationLabel, locationDropdown);
        
        // Create input fields grid
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(20);
        inputGrid.setVgap(15);
        
        // Temperature input
        Label tempLabel = new Label("Temperature (°C):");
        tempLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField tempField = new TextField();
        tempField.setPromptText("e.g., 24");
        tempField.setPrefHeight(35);
        
        // Light intensity input
        Label lightLabel = new Label("Light Intensity (lux):");
        lightLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField lightField = new TextField();
        lightField.setPromptText("e.g., 8500");
        lightField.setPrefHeight(35);
        
        // Humidity input
        Label humidityLabel = new Label("Humidity (%):");
        humidityLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField humidityField = new TextField();
        humidityField.setPromptText("e.g., 65");
        humidityField.setPrefHeight(35);
        
        // Rainfall input
        Label rainfallLabel = new Label("Rainfall (mm):");
        rainfallLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField rainfallField = new TextField();
        rainfallField.setPromptText("e.g., 0.5");
        rainfallField.setPrefHeight(35);
        
        // Soil moisture input
        Label soilMoistureLabel = new Label("Soil Moisture (%):");
        soilMoistureLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField soilMoistureField = new TextField();
        soilMoistureField.setPromptText("e.g., 42");
        soilMoistureField.setPrefHeight(35);
        
        // Wind speed input
        Label windSpeedLabel = new Label("Wind Speed (km/h):");
        windSpeedLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextField windSpeedField = new TextField();
        windSpeedField.setPromptText("e.g., 12");
        windSpeedField.setPrefHeight(35);
        
        // Add form elements to grid
        inputGrid.add(tempLabel, 0, 0);
        inputGrid.add(tempField, 0, 1);
        inputGrid.add(lightLabel, 1, 0);
        inputGrid.add(lightField, 1, 1);
        inputGrid.add(humidityLabel, 2, 0);
        inputGrid.add(humidityField, 2, 1);
        inputGrid.add(rainfallLabel, 0, 2);
        inputGrid.add(rainfallField, 0, 3);
        inputGrid.add(soilMoistureLabel, 1, 2);
        inputGrid.add(soilMoistureField, 1, 3);
        inputGrid.add(windSpeedLabel, 2, 2);
        inputGrid.add(windSpeedField, 2, 3);
        
        // Set column constraints for equal width
        
        // Set column constraints for equal width
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        
        inputGrid.getColumnConstraints().addAll(col1, col2, col3);
        
        // Save button
        Button saveButton = new Button("Save Readings");
        saveButton.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;"
        );
        
        // Button container
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().add(saveButton);
        
        formContainer.getChildren().addAll(formTitle, locationBox, inputGrid, buttonBox);
        return formContainer;
    }
}