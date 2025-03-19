import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SensorSimulationPage {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private MainApp mainApp;
    
    // Simulation controls
    private ComboBox<String> locationDropdown;
    private Slider temperatureSlider;
    private Slider soilMoistureSlider;
    private Slider humiditySlider;
    private Slider rainfallSlider;
    private Slider windSpeedSlider;
    private Slider lightIntensitySlider;
    
    // Decision output
    private Text decisionText;
    private Text reasonText;
    private ProgressBar irrigationProgress;
    
    // Data table
    private TableView<SensorReading> dataTable;
    private ObservableList<SensorReading> sensorReadings = FXCollections.observableArrayList();
    
    // Chart
    private LineChart<String, Number> soilMoistureChart;
    private XYChart.Series<String, Number> moistureSeries;
    
    // Simulation
    private Random random = new Random();
    private SensorSimulator simulator = new SensorSimulator();
    
    public SensorSimulationPage() {
        // Default constructor
    }
    
    public SensorSimulationPage(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent(), 1000, 800);
        primaryStage.setTitle("AgriTech Smart Irrigation System - Sensor Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Header
        VBox headerBox = createHeader();
        root.setTop(headerBox);
        
        // Left - Simulation controls
        VBox controlsPanel = createControlsPanel();
        root.setLeft(controlsPanel);
        
        // Center - Chart and decision panel
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
        
        // Right - Data table
        VBox dataPanel = createDataPanel();
        root.setRight(dataPanel);
        
        return root;
    }
    
    private VBox createHeader() {
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setSpacing(20);
        
        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + PRIMARY_COLOR + ";" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
        );
        
        // We'll use this event later when we set the main application reference
        backButton.setOnAction(e -> {
            // This will be handled by the MainApplication
            if (mainApp != null) {
                mainApp.showDashboard();
            }
        });
        
        VBox titleBox = new VBox(5);
        
        Text title = new Text("Sensor Simulation");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#333333"));
        
        Text subtitle = new Text("Simulate sensor readings and view irrigation decisions");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.web("#6C757D"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        headerContainer.getChildren().addAll(backButton, titleBox);
        
        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        headerBox.getChildren().add(headerContainer);
        return headerBox;
    }
    
    private VBox createControlsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        panel.setPrefWidth(280);
        
        Text controlsTitle = new Text("Simulation Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Location selector
        Label locationLabel = new Label("Location:");
        locationDropdown = new ComboBox<>();
        locationDropdown.getItems().addAll("Field A - North", "Field A - South", "Field B - East", "Field B - West");
        locationDropdown.setValue("Field A - North");
        locationDropdown.setMaxWidth(Double.MAX_VALUE);
        
        // Create sliders for each parameter
        temperatureSlider = createSlider("Temperature (°C)", 5, 40, 24, "°C");
        soilMoistureSlider = createSlider("Soil Moisture (%)", 5, 95, 42, "%");
        humiditySlider = createSlider("Humidity (%)", 20, 95, 65, "%");
        rainfallSlider = createSlider("Rainfall (mm)", 0, 50, 0, "mm");
        windSpeedSlider = createSlider("Wind Speed (km/h)", 0, 30, 12, "km/h");
        lightIntensitySlider = createSlider("Light Intensity (lux)", 0, 15000, 8500, "lux");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 5, 0));
        
        Button randomizeButton = new Button("Randomize");
        randomizeButton.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        randomizeButton.setOnAction(e -> randomizeValues());
        
        Button generateButton = new Button("Generate Reading");
        generateButton.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        generateButton.setOnAction(e -> generateReading());
        
        buttonBox.getChildren().addAll(randomizeButton, generateButton);
        
        panel.getChildren().addAll(
            controlsTitle,
            new Separator(),
            locationLabel,
            locationDropdown,
            (VBox)temperatureSlider.getUserData(),
            (VBox)soilMoistureSlider.getUserData(),
            (VBox)humiditySlider.getUserData(),
            (VBox)rainfallSlider.getUserData(),
            (VBox)windSpeedSlider.getUserData(),
            (VBox)lightIntensitySlider.getUserData(),
            buttonBox
        );
        
        
        return panel;
    }
    
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(0, 20, 0, 20));
        
        // Decision panel
        VBox decisionPanel = createDecisionPanel();
        
        // Chart
        VBox chartPanel = createChartPanel();
        
        centerPanel.getChildren().addAll(decisionPanel, chartPanel);
        return centerPanel;
    }
    
    private VBox createDecisionPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Text decisionTitle = new Text("Irrigation Decision");
        decisionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        decisionText = new Text("No data available");
        decisionText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        decisionText.setFill(Color.web("#6C757D"));
        
        reasonText = new Text("Generate a reading to see the irrigation decision");
        reasonText.setFont(Font.font("Arial", 16));
        reasonText.setFill(Color.web("#6C757D"));
        
        Text progressLabel = new Text("Irrigation Progress:");
        progressLabel.setFont(Font.font("Arial", 14));
        
        irrigationProgress = new ProgressBar(0);
        irrigationProgress.setPrefWidth(Double.MAX_VALUE);
        irrigationProgress.setStyle("-fx-accent: " + SECONDARY_COLOR + ";");
        
        panel.getChildren().addAll(decisionTitle, new Separator(), decisionText, reasonText, progressLabel, irrigationProgress);
        return panel;
    }
    
    private VBox createChartPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Text chartTitle = new Text("Soil Moisture Trend");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Creating the chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        xAxis.setLabel("Time");
        yAxis.setLabel("Soil Moisture (%)");
        
        soilMoistureChart = new LineChart<>(xAxis, yAxis);
        soilMoistureChart.setTitle("Soil Moisture Over Time");
        soilMoistureChart.setAnimated(false);
        soilMoistureChart.setCreateSymbols(true);
        
        moistureSeries = new XYChart.Series<>();
        moistureSeries.setName("Soil Moisture");
        soilMoistureChart.getData().add(moistureSeries);
        
        panel.getChildren().addAll(chartTitle, new Separator(), soilMoistureChart);
        return panel;
    }
    
    private VBox createDataPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        panel.setPrefWidth(300);
        
        Text dataTitle = new Text("Recent Readings");
        dataTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Create table
        dataTable = new TableView<>();
        dataTable.setItems(sensorReadings);
        dataTable.setPrefHeight(500);
        
        TableColumn<SensorReading, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));
        timeCol.setPrefWidth(120);
        
        TableColumn<SensorReading, String> moistureCol = new TableColumn<>("Moisture");
        moistureCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.1f%%", data.getValue().getSoilMoisture())));
        moistureCol.setPrefWidth(80);
        
        TableColumn<SensorReading, String> tempCol = new TableColumn<>("Temp");
        tempCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.1f°C", data.getValue().getTemperature())));
        tempCol.setPrefWidth(80);
        
        dataTable.getColumns().addAll(timeCol, moistureCol, tempCol);
        
        Button clearButton = new Button("Clear Data");
        clearButton.setStyle(
            "-fx-background-color: #6C757D;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        clearButton.setOnAction(e -> clearData());
        clearButton.setMaxWidth(Double.MAX_VALUE);
        
        panel.getChildren().addAll(dataTitle, new Separator(), dataTable, clearButton);
        return panel;
    }
    
    private Slider createSlider(String name, double min, double max, double value, String unit) {
        Label label = new Label(name);
        label.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        Slider slider = new Slider(min, max, value);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit((max - min) / 5);
        slider.setBlockIncrement((max - min) / 10);
        
        Text valueText = new Text(String.format("%.1f %s", value, unit));
        valueText.setFont(Font.font("Arial", 14));
        
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueText.setText(String.format("%.1f %s", newVal.doubleValue(), unit));
        });
        
        HBox valueBox = new HBox(5);
        valueBox.setAlignment(Pos.CENTER_RIGHT);
        valueBox.getChildren().add(valueText);
        
        // Create the container and add all components
        VBox container = new VBox(5);
        container.getChildren().addAll(label, slider, valueBox);
        
        // Store the container as a user data property of the slider for later retrieval
        slider.setUserData(container);
        
        return slider;
    }
    
    private void randomizeValues() {
        temperatureSlider.setValue(15 + random.nextDouble() * 20); // 15-35°C
        soilMoistureSlider.setValue(20 + random.nextDouble() * 60); // 20-80%
        humiditySlider.setValue(40 + random.nextDouble() * 40); // 40-80%
        rainfallSlider.setValue(random.nextDouble() * 15); // 0-15mm
        windSpeedSlider.setValue(random.nextDouble() * 20); // 0-20km/h
        lightIntensitySlider.setValue(2000 + random.nextDouble() * 10000); // 2000-12000 lux
    }
    
    private void generateReading() {
        // Create reading from current slider values
        SensorReading reading = new SensorReading(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            temperatureSlider.getValue(),
            soilMoistureSlider.getValue(),
            humiditySlider.getValue(),
            rainfallSlider.getValue(),
            windSpeedSlider.getValue(),
            lightIntensitySlider.getValue(),
            locationDropdown.getValue()
        );
        
        // Add to table (limit to last 10 readings)
        sensorReadings.add(0, reading);
        if (sensorReadings.size() > 10) {
            sensorReadings.remove(10);
        }
        
        // Update chart
        if (moistureSeries.getData().size() >= 10) {
            moistureSeries.getData().remove(0);
        }
        moistureSeries.getData().add(new XYChart.Data<>(reading.getTimestamp(), reading.getSoilMoisture()));
        
        // Make irrigation decision
        IrrigationDecision decision = simulator.makeIrrigationDecision(reading);
        
        // Update decision panel
        updateDecisionPanel(decision);
    }
    
    private void updateDecisionPanel(IrrigationDecision decision) {
        decisionText.setText(decision.getDecision());
        reasonText.setText(decision.getReason());
        
        // Set color based on decision
        if (decision.getDecision().contains("Irrigate")) {
            decisionText.setFill(Color.web(SECONDARY_COLOR));
            irrigationProgress.setProgress(decision.getIrrigationAmount() / 100.0);
        } else {
            decisionText.setFill(Color.web(PRIMARY_COLOR));
            irrigationProgress.setProgress(0);
        }
    }
    
    private void clearData() {
        sensorReadings.clear();
        moistureSeries.getData().clear();
        decisionText.setText("No data available");
        decisionText.setFill(Color.web("#6C757D"));
        reasonText.setText("Generate a reading to see the irrigation decision");
        irrigationProgress.setProgress(0);
    }
}
        