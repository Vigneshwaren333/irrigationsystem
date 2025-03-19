import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SoilMoisturePage {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private MainApp mainApp;
    
    // Soil moisture data
    private final ObservableList<SensorReading> moistureData = FXCollections.observableArrayList();
    private final Random random = new Random();
    
    // UI components
    private LineChart<String, Number> moistureChart;
    private TableView<SensorReading> dataTable;
    private ProgressBar moistureProgressBar;
    private Text currentMoistureText;
    private Text statusText;
    private ComboBox<String> sensorLocationDropdown;
    
    public SoilMoisturePage() {
        // Default constructor
    }
    
    public SoilMoisturePage(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Header
        VBox headerBox = createHeader();
        root.setTop(headerBox);
        
        // Left - Controls and Status
        VBox leftPanel = createControlPanel();
        root.setLeft(leftPanel);
        
        // Center - Chart
        VBox centerPanel = createChartPanel();
        root.setCenter(centerPanel);
        
        // Right - Data table
        VBox rightPanel = createDataTablePanel();
        root.setRight(rightPanel);
        
        // Initialize with some sample data
        loadSampleData();
        
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
        
        backButton.setOnAction(e -> {
            if (mainApp != null) {
                mainApp.showDashboard();
            }
        });
        
        VBox titleBox = new VBox(5);
        
        Text title = new Text("Soil Moisture Monitoring");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#333333"));
        
        Text subtitle = new Text("Real-time soil moisture data and historical trends");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.web("#6C757D"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        headerContainer.getChildren().addAll(backButton, titleBox);
        
        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        headerBox.getChildren().add(headerContainer);
        return headerBox;
    }
    
    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        panel.setPrefWidth(280);
        
        Text controlsTitle = new Text("Soil Moisture Status");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Current Moisture Display
        Text moistureLabel = new Text("Current Moisture Level");
        moistureLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        currentMoistureText = new Text("64.5%");
        currentMoistureText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        currentMoistureText.setFill(Color.web(SECONDARY_COLOR));
        
        moistureProgressBar = new ProgressBar(0.645);
        moistureProgressBar.setPrefWidth(Double.MAX_VALUE);
        moistureProgressBar.setStyle("-fx-accent: " + SECONDARY_COLOR + ";");
        
        statusText = new Text("Optimal Moisture Level");
        statusText.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        statusText.setFill(Color.web(SECONDARY_COLOR));
        
        // Sensor Location Selector
        Text locationLabel = new Text("Sensor Location");
        locationLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        sensorLocationDropdown = new ComboBox<>();
        sensorLocationDropdown.getItems().addAll(
            "Field A - North", 
            "Field A - South", 
            "Field B - East", 
            "Field B - West"
        );
        sensorLocationDropdown.setValue("Field A - North");
        sensorLocationDropdown.setMaxWidth(Double.MAX_VALUE);
        sensorLocationDropdown.setOnAction(e -> updateData());
        
        // Details section
        Text detailsTitle = new Text("Soil Details");
        detailsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);
        
        Text soilTypeLabel = new Text("Soil Type:");
        Text soilTypeValue = new Text("Clay Loam");
        detailsGrid.add(soilTypeLabel, 0, 0);
        detailsGrid.add(soilTypeValue, 1, 0);
        
        Text capacityLabel = new Text("Field Capacity:");
        Text capacityValue = new Text("68%");
        detailsGrid.add(capacityLabel, 0, 1);
        detailsGrid.add(capacityValue, 1, 1);
        
        Text wiltingLabel = new Text("Wilting Point:");
        Text wiltingValue = new Text("30%");
        detailsGrid.add(wiltingLabel, 0, 2);
        detailsGrid.add(wiltingValue, 1, 2);
        
        Text targetLabel = new Text("Target Range:");
        Text targetValue = new Text("45% - 65%");
        detailsGrid.add(targetLabel, 0, 3);
        detailsGrid.add(targetValue, 1, 3);
        
        // Buttons for actions
        Button refreshButton = new Button("Refresh Data");
        refreshButton.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> updateData());
        
        Button configureButton = new Button("Configure Thresholds");
        configureButton.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        configureButton.setMaxWidth(Double.MAX_VALUE);
        
        // Add all to panel
        panel.getChildren().addAll(
            controlsTitle,
            new Separator(),
            moistureLabel,
            currentMoistureText,
            moistureProgressBar,
            statusText,
            new Separator(),
            locationLabel,
            sensorLocationDropdown,
            new Separator(),
            detailsTitle,
            detailsGrid,
            new Separator(),
            refreshButton,
            configureButton
        );
        
        return panel;
    }
    
    private VBox createChartPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10, 20, 10, 20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Text chartTitle = new Text("Soil Moisture Trend (Last 24 Hours)");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Creating the chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        xAxis.setLabel("Time");
        yAxis.setLabel("Soil Moisture (%)");
        
        moistureChart = new LineChart<>(xAxis, yAxis);
        moistureChart.setTitle("24-Hour Soil Moisture Readings");
        moistureChart.setAnimated(false);
        moistureChart.setCreateSymbols(true);
        moistureChart.setLegendVisible(false);
        
        // Time range selector
        HBox timeRangeBox = new HBox(10);
        timeRangeBox.setAlignment(Pos.CENTER);
        
        ToggleGroup timeGroup = new ToggleGroup();
        
        ToggleButton dayButton = new ToggleButton("24 Hours");
        dayButton.setToggleGroup(timeGroup);
        dayButton.setSelected(true);
        styleToggleButton(dayButton, true);
        
        ToggleButton weekButton = new ToggleButton("Week");
        weekButton.setToggleGroup(timeGroup);
        styleToggleButton(weekButton, false);
        
        ToggleButton monthButton = new ToggleButton("Month");
        monthButton.setToggleGroup(timeGroup);
        styleToggleButton(monthButton, false);
        
        timeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }
            
            // Style the buttons
            styleToggleButton(dayButton, dayButton.isSelected());
            styleToggleButton(weekButton, weekButton.isSelected());
            styleToggleButton(monthButton, monthButton.isSelected());
            
            // Update chart data
            updateChartData();
        });
        
        timeRangeBox.getChildren().addAll(dayButton, weekButton, monthButton);
        
        panel.getChildren().addAll(chartTitle, new Separator(), moistureChart, timeRangeBox);
        return panel;
    }
    
    private void styleToggleButton(ToggleButton button, boolean selected) {
        if (selected) {
            button.setStyle(
                "-fx-background-color: " + PRIMARY_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5 15;" +
                "-fx-background-radius: 3;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: #E9ECEF;" +
                "-fx-text-fill: #495057;" +
                "-fx-padding: 5 15;" +
                "-fx-background-radius: 3;"
            );
        }
    }
    
    private VBox createDataTablePanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        panel.setPrefWidth(300);
        
        Text dataTitle = new Text("Historical Data");
        dataTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Create table
        dataTable = new TableView<>();
        dataTable.setItems(moistureData);
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
        
        // Export and filter options
        HBox actionBox = new HBox(10);
        
        Button exportButton = new Button("Export Data");
        exportButton.setStyle(
            "-fx-background-color: #6C757D;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        
        Button filterButton = new Button("Filter");
        filterButton.setStyle(
            "-fx-background-color: #6C757D;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        
        HBox.setHgrow(exportButton, Priority.ALWAYS);
        HBox.setHgrow(filterButton, Priority.ALWAYS);
        exportButton.setMaxWidth(Double.MAX_VALUE);
        filterButton.setMaxWidth(Double.MAX_VALUE);
        
        actionBox.getChildren().addAll(exportButton, filterButton);
        
        panel.getChildren().addAll(dataTitle, new Separator(), dataTable, actionBox);
        return panel;
    }
    
    private void loadSampleData() {
        // Clear existing data
        moistureData.clear();
        
        // Create new XYChart.Series for the current location
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Soil Moisture");
        
        // Generate 24 hours of data for the chart and table
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (int i = 24; i >= 0; i--) {
            LocalDateTime time = now.minusHours(i);
            
            // Generate reasonable moisture values (with a daily pattern)
            double hourOfDay = time.getHour();
            
            // Moisture tends to be lower during the day (evaporation) and higher at night
            double baseMoisture = 60.0 + random.nextDouble() * 15.0 - 7.5; // Base 60% with ±7.5% variation
            double timeEffect = -5.0 * Math.sin(Math.PI * (hourOfDay - 6) / 12.0); // -5% to +5% based on time of day
            double moistureValue = Math.max(30, Math.min(95, baseMoisture + timeEffect));
            
            // Temperature follows a daily cycle
            double baseTemp = 22.0 + random.nextDouble() * 4.0 - 2.0; // Base 22°C with ±2° variation
            double timeFactorTemp = 6.0 * Math.sin(Math.PI * (hourOfDay - 2) / 12.0); // +6° during day
            double temperature = Math.max(15, Math.min(35, baseTemp + timeFactorTemp));
            
            // Add to chart
            series.getData().add(new XYChart.Data<>(time.format(timeFormatter), moistureValue));
            
            // Every 2 hours, add to table data (to keep the table manageable)
            if (i % 2 == 0 || i == 0) {
                SensorReading reading = new SensorReading(
                    time.format(fullFormatter),
                    temperature,
                    moistureValue,
                    60 + random.nextDouble() * 20, // Humidity 60-80%
                    0.0, // No rainfall
                    5 + random.nextDouble() * 10, // Wind 5-15 km/h
                    i < 12 ? 10000 * Math.sin(Math.PI * hourOfDay / 12.0) : 0, // Light during day
                    sensorLocationDropdown.getValue()
                );
                moistureData.add(reading);
            }
        }
        
        // Set chart data
        moistureChart.getData().clear();
        moistureChart.getData().add(series);
        
        // Update current moisture value
        if (!moistureData.isEmpty()) {
            SensorReading latest = moistureData.get(moistureData.size() - 1);
            updateCurrentDisplay(latest.getSoilMoisture());
        }
    }
    
    private void updateCurrentDisplay(double moistureValue) {
        currentMoistureText.setText(String.format("%.1f%%", moistureValue));
        moistureProgressBar.setProgress(moistureValue / 100.0);
        
        // Set status text and color based on moisture level
        if (moistureValue < 30) {
            statusText.setText("Critical - Extremely Dry");
            statusText.setFill(Color.web("#DC3545")); // Danger red
            currentMoistureText.setFill(Color.web("#DC3545"));
        } else if (moistureValue < 45) {
            statusText.setText("Warning - Low Moisture");
            statusText.setFill(Color.web("#FFC107")); // Warning yellow
            currentMoistureText.setFill(Color.web("#FFC107"));
        } else if (moistureValue <= 65) {
            statusText.setText("Optimal Moisture Level");
            statusText.setFill(Color.web(SECONDARY_COLOR)); // Success green
            currentMoistureText.setFill(Color.web(SECONDARY_COLOR));
        } else if (moistureValue <= 80) {
            statusText.setText("High Moisture Level");
            statusText.setFill(Color.web("#17A2B8")); // Info blue
            currentMoistureText.setFill(Color.web("#17A2B8"));
        } else {
            statusText.setText("Warning - Excessive Moisture");
            statusText.setFill(Color.web("#FFC107")); // Warning yellow
            currentMoistureText.setFill(Color.web("#FFC107"));
        }
    }
    
    private void updateData() {
        // This would typically fetch real data from a database or API
        // For now, we'll just generate new sample data
        loadSampleData();
    }
    
    private void updateChartData() {
        // This would update the chart with different time ranges
        // For now, we'll just reload the sample data
        loadSampleData();
    }
} 