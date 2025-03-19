import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class IrrigationControlPage {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private MainApp mainApp;
    private Random random = new Random();
    
    // UI components
    private ToggleGroup systemModeGroup;
    private ToggleButton autoButton;
    private ToggleButton manualButton;
    private Slider waterVolumeSlider;
    private Slider durationSlider;
    private ComboBox<String> zoneSelector;
    private Button startIrrigationButton;
    private Button stopIrrigationButton;
    private ProgressBar irrigationProgressBar;
    private Text statusText;
    private Label timerLabel;
    
    // Irrigation status
    private boolean irrigationActive = false;
    private Timeline irrigationTimer;
    private int secondsRemaining = 0;
    
    // Irrigation log
    private final ObservableList<IrrigationEvent> irrigationLog = FXCollections.observableArrayList();
    
    public IrrigationControlPage() {
        // Default constructor
    }
    
    public IrrigationControlPage(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Header
        VBox headerBox = createHeader();
        root.setTop(headerBox);
        
        // Left - Controls
        VBox leftPanel = createControlPanel();
        root.setLeft(leftPanel);
        
        // Center - Current Status
        VBox centerPanel = createStatusPanel();
        root.setCenter(centerPanel);
        
        // Right - Irrigation Log
        VBox rightPanel = createLogPanel();
        root.setRight(rightPanel);
        
        // Setup irrigation timer
        setupIrrigationTimer();
        
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
        
        Text title = new Text("Irrigation Control Center");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#333333"));
        
        Text subtitle = new Text("Monitor and control your irrigation system in real-time");
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
        
        Text controlsTitle = new Text("System Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // System Mode
        Text modeLabel = new Text("System Mode");
        modeLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        HBox modeBox = new HBox(10);
        modeBox.setAlignment(Pos.CENTER);
        
        systemModeGroup = new ToggleGroup();
        
        autoButton = new ToggleButton("Auto");
        autoButton.setToggleGroup(systemModeGroup);
        autoButton.setSelected(true);
        styleToggleButton(autoButton, true);
        
        manualButton = new ToggleButton("Manual");
        manualButton.setToggleGroup(systemModeGroup);
        styleToggleButton(manualButton, false);
        
        systemModeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == autoButton) {
                styleToggleButton(autoButton, true);
                styleToggleButton(manualButton, false);
                updateControlsAvailability(false);
            } else {
                styleToggleButton(autoButton, false);
                styleToggleButton(manualButton, true);
                updateControlsAvailability(true);
            }
        });
        
        autoButton.setPrefWidth(120);
        manualButton.setPrefWidth(120);
        modeBox.getChildren().addAll(autoButton, manualButton);
        
        // Zone Selection
        Text zoneLabel = new Text("Irrigation Zone");
        zoneLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        zoneSelector = new ComboBox<>();
        zoneSelector.getItems().addAll(
            "Zone 1 - North Field",
            "Zone 2 - South Field",
            "Zone 3 - East Garden",
            "Zone 4 - West Orchard"
        );
        zoneSelector.setValue("Zone 1 - North Field");
        zoneSelector.setMaxWidth(Double.MAX_VALUE);
        
        // Water Volume
        Text volumeLabel = new Text("Water Volume");
        volumeLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        waterVolumeSlider = new Slider(10, 100, 50);
        waterVolumeSlider.setShowTickMarks(true);
        waterVolumeSlider.setShowTickLabels(true);
        waterVolumeSlider.setMajorTickUnit(30);
        
        HBox volumeBox = new HBox();
        volumeBox.setAlignment(Pos.CENTER_RIGHT);
        
        Text volumeValue = new Text("50 L/min");
        volumeValue.setFont(Font.font("Arial", 14));
        
        waterVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            volumeValue.setText(String.format("%.0f L/min", newVal.doubleValue()));
        });
        
        volumeBox.getChildren().add(volumeValue);
        
        // Duration
        Text durationLabel = new Text("Duration");
        durationLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        durationSlider = new Slider(1, 60, 15);
        durationSlider.setShowTickMarks(true);
        durationSlider.setShowTickLabels(true);
        durationSlider.setMajorTickUnit(15);
        
        HBox durationBox = new HBox();
        durationBox.setAlignment(Pos.CENTER_RIGHT);
        
        Text durationValue = new Text("15 min");
        durationValue.setFont(Font.font("Arial", 14));
        
        durationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            durationValue.setText(String.format("%.0f min", newVal.doubleValue()));
        });
        
        durationBox.getChildren().add(durationValue);
        
        // Control Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        startIrrigationButton = new Button("Start Irrigation");
        startIrrigationButton.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        startIrrigationButton.setDisable(true);
        startIrrigationButton.setMaxWidth(Double.MAX_VALUE);
        startIrrigationButton.setOnAction(e -> startIrrigation());
        
        stopIrrigationButton = new Button("Stop");
        stopIrrigationButton.setStyle(
            "-fx-background-color: " + ACCENT_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        stopIrrigationButton.setDisable(true);
        stopIrrigationButton.setMaxWidth(Double.MAX_VALUE);
        stopIrrigationButton.setOnAction(e -> stopIrrigation());
        
        HBox.setHgrow(startIrrigationButton, Priority.ALWAYS);
        HBox.setHgrow(stopIrrigationButton, Priority.ALWAYS);
        
        buttonBox.getChildren().addAll(startIrrigationButton, stopIrrigationButton);
        
        // System Status Information
        Text systemStatusLabel = new Text("System Information");
        systemStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(5);
        
        Text pressureLabel = new Text("Water Pressure:");
        Text pressureValue = new Text("4.5 bar");
        infoGrid.add(pressureLabel, 0, 0);
        infoGrid.add(pressureValue, 1, 0);
        
        Text flowLabel = new Text("Flow Rate:");
        Text flowValue = new Text("12.3 L/min");
        infoGrid.add(flowLabel, 0, 1);
        infoGrid.add(flowValue, 1, 1);
        
        Text tankLabel = new Text("Tank Level:");
        Text tankValue = new Text("87%");
        infoGrid.add(tankLabel, 0, 2);
        infoGrid.add(tankValue, 1, 2);
        
        Text batteryLabel = new Text("Battery:");
        Text batteryValue = new Text("92%");
        infoGrid.add(batteryLabel, 0, 3);
        infoGrid.add(batteryValue, 1, 3);
        
        Text signalLabel = new Text("Signal Strength:");
        Text signalValue = new Text("Strong");
        infoGrid.add(signalLabel, 0, 4);
        infoGrid.add(signalValue, 1, 4);
        
        // Add all to panel
        panel.getChildren().addAll(
            controlsTitle,
            new Separator(),
            modeLabel,
            modeBox,
            zoneLabel,
            zoneSelector,
            volumeLabel,
            waterVolumeSlider,
            volumeBox,
            durationLabel,
            durationSlider,
            durationBox,
            buttonBox,
            new Separator(),
            systemStatusLabel,
            infoGrid
        );
        
        return panel;
    }
    
    private VBox createStatusPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10, 20, 10, 20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Text statusTitle = new Text("Current Irrigation Status");
        statusTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Status indicator
        statusText = new Text("System Idle");
        statusText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        statusText.setFill(Color.web("#6C757D"));
        
        // Timer for active irrigation
        timerLabel = new Label("00:00");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        timerLabel.setTextFill(Color.web(PRIMARY_COLOR));
        
        // Progress indicator
        irrigationProgressBar = new ProgressBar(0);
        irrigationProgressBar.setPrefWidth(Double.MAX_VALUE);
        irrigationProgressBar.setPrefHeight(20);
        irrigationProgressBar.setStyle("-fx-accent: " + SECONDARY_COLOR + ";");
        
        // Current zone info
        VBox zoneInfoBox = new VBox(5);
        zoneInfoBox.setPadding(new Insets(20, 0, 0, 0));
        
        Text zoneInfoLabel = new Text("Zone Information");
        zoneInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Zone details grid
        GridPane zoneGrid = new GridPane();
        zoneGrid.setHgap(20);
        zoneGrid.setVgap(10);
        zoneGrid.setPadding(new Insets(10, 0, 0, 0));
        
        Text currentZoneLabel = new Text("Current Zone:");
        Text currentZoneValue = new Text("Zone 1 - North Field");
        currentZoneValue.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        zoneGrid.add(currentZoneLabel, 0, 0);
        zoneGrid.add(currentZoneValue, 1, 0);
        
        Text soilTypeLabel = new Text("Soil Type:");
        Text soilTypeValue = new Text("Clay Loam");
        zoneGrid.add(soilTypeLabel, 0, 1);
        zoneGrid.add(soilTypeValue, 1, 1);
        
        Text areaLabel = new Text("Area:");
        Text areaValue = new Text("2.5 hectares");
        zoneGrid.add(areaLabel, 0, 2);
        zoneGrid.add(areaValue, 1, 2);
        
        Text cropLabel = new Text("Crop Type:");
        Text cropValue = new Text("Corn");
        zoneGrid.add(cropLabel, 0, 3);
        zoneGrid.add(cropValue, 1, 3);
        
        Text lastIrrigationLabel = new Text("Last Irrigation:");
        Text lastIrrigationValue = new Text("2 days ago");
        zoneGrid.add(lastIrrigationLabel, 0, 4);
        zoneGrid.add(lastIrrigationValue, 1, 4);
        
        Text moistureLabel = new Text("Current Moisture:");
        Text moistureValue = new Text("42%");
        zoneGrid.add(moistureLabel, 0, 5);
        zoneGrid.add(moistureValue, 1, 5);
        
        zoneInfoBox.getChildren().addAll(zoneInfoLabel, zoneGrid);
        
        // Weather information
        VBox weatherBox = new VBox(5);
        weatherBox.setPadding(new Insets(20, 0, 0, 0));
        
        Text weatherLabel = new Text("Weather Conditions");
        weatherLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Weather details grid
        GridPane weatherGrid = new GridPane();
        weatherGrid.setHgap(20);
        weatherGrid.setVgap(10);
        weatherGrid.setPadding(new Insets(10, 0, 0, 0));
        
        Text tempLabel = new Text("Temperature:");
        Text tempValue = new Text("24°C");
        weatherGrid.add(tempLabel, 0, 0);
        weatherGrid.add(tempValue, 1, 0);
        
        Text humidityLabel = new Text("Humidity:");
        Text humidityValue = new Text("65%");
        weatherGrid.add(humidityLabel, 0, 1);
        weatherGrid.add(humidityValue, 1, 1);
        
        Text windLabel = new Text("Wind Speed:");
        Text windValue = new Text("8 km/h");
        weatherGrid.add(windLabel, 0, 2);
        weatherGrid.add(windValue, 1, 2);
        
        Text rainLabel = new Text("Rainfall (24h):");
        Text rainValue = new Text("0 mm");
        weatherGrid.add(rainLabel, 0, 3);
        weatherGrid.add(rainValue, 1, 3);
        
        Text forecastLabel = new Text("Forecast:");
        Text forecastValue = new Text("Clear, no rain expected");
        weatherGrid.add(forecastLabel, 0, 4);
        weatherGrid.add(forecastValue, 1, 4);
        
        weatherBox.getChildren().addAll(weatherLabel, weatherGrid);
        
        panel.getChildren().addAll(
            statusTitle, 
            new Separator(),
            statusText,
            timerLabel,
            irrigationProgressBar,
            zoneInfoBox,
            weatherBox
        );
        
        return panel;
    }
    
    private VBox createLogPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        panel.setPrefWidth(300);
        
        Text logTitle = new Text("Irrigation Log");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Create table
        TableView<IrrigationEvent> logTable = new TableView<>();
        logTable.setItems(irrigationLog);
        logTable.setPrefHeight(500);
        
        TableColumn<IrrigationEvent, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().timestamp));
        timeCol.setPrefWidth(80);
        
        TableColumn<IrrigationEvent, String> zoneCol = new TableColumn<>("Zone");
        zoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().zone));
        zoneCol.setPrefWidth(60);
        
        TableColumn<IrrigationEvent, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().action));
        actionCol.setPrefWidth(70);
        
        TableColumn<IrrigationEvent, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().details));
        detailsCol.setPrefWidth(90);
        
        logTable.getColumns().addAll(timeCol, zoneCol, actionCol, detailsCol);
        
        // Add sample log entries
        addSampleLogEntries();
        
        // Filter section
        Text filterLabel = new Text("Filter by:");
        filterLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All Events", "Manual Only", "Auto Only", "Errors", "Today Only");
        filterComboBox.setValue("All Events");
        filterComboBox.setMaxWidth(Double.MAX_VALUE);
        
        // Export button
        Button exportButton = new Button("Export Log");
        exportButton.setStyle(
            "-fx-background-color: #6C757D;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5;"
        );
        exportButton.setMaxWidth(Double.MAX_VALUE);
        
        panel.getChildren().addAll(
            logTitle, 
            new Separator(), 
            logTable, 
            filterLabel, 
            filterComboBox, 
            exportButton
        );
        
        return panel;
    }
    
    private void styleToggleButton(ToggleButton button, boolean selected) {
        if (selected) {
            button.setStyle(
                "-fx-background-color: " + PRIMARY_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5 15;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 3;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: #DEE2E6;" +
                "-fx-text-fill: #495057;" +
                "-fx-padding: 5 15;" +
                "-fx-background-radius: 3;"
            );
        }
    }
    
    private void updateControlsAvailability(boolean enable) {
        // In Auto mode, the controls are disabled
        // In Manual mode, the controls are enabled
        waterVolumeSlider.setDisable(!enable);
        durationSlider.setDisable(!enable);
        startIrrigationButton.setDisable(!enable);
        
        if (enable) {
            statusText.setText("Manual Mode Active");
            statusText.setFill(Color.web(PRIMARY_COLOR));
        } else {
            statusText.setText("Auto Mode - System Managed");
            statusText.setFill(Color.web(SECONDARY_COLOR));
        }
    }
    
    private void setupIrrigationTimer() {
        irrigationTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                if (irrigationActive && secondsRemaining > 0) {
                    secondsRemaining--;
                    updateTimerDisplay();
                    
                    // Update progress bar
                    int totalSeconds = (int) durationSlider.getValue() * 60;
                    irrigationProgressBar.setProgress(1.0 - ((double) secondsRemaining / totalSeconds));
                    
                    if (secondsRemaining <= 0) {
                        stopIrrigation();
                    }
                }
            })
        );
        irrigationTimer.setCycleCount(Animation.INDEFINITE);
    }
    
    private void updateTimerDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
    
    private void startIrrigation() {
        irrigationActive = true;
        secondsRemaining = (int) (durationSlider.getValue() * 60);
        irrigationTimer.play();
        
        // Update UI
        startIrrigationButton.setDisable(true);
        stopIrrigationButton.setDisable(false);
        statusText.setText("Irrigation Active");
        statusText.setFill(Color.web(SECONDARY_COLOR));
        
        // Add to log
        addIrrigationEvent("Start", String.format("%.0f L/min, %d min", 
                waterVolumeSlider.getValue(), (int) durationSlider.getValue()));
    }
    
    private void stopIrrigation() {
        irrigationActive = false;
        irrigationTimer.stop();
        
        // Update UI
        if (manualButton.isSelected()) {
            startIrrigationButton.setDisable(false);
            statusText.setText("Manual Mode Active");
        } else {
            startIrrigationButton.setDisable(true);
            statusText.setText("Auto Mode - System Managed");
        }
        
        stopIrrigationButton.setDisable(true);
        irrigationProgressBar.setProgress(0);
        timerLabel.setText("00:00");
        
        // Add to log
        if (secondsRemaining <= 0) {
            // Completed the full cycle
            addIrrigationEvent("Complete", "Full cycle completed");
        } else {
            // Manually stopped
            addIrrigationEvent("Stop", "Manually stopped");
        }
    }
    
    private void addIrrigationEvent(String action, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String zone = zoneSelector.getValue().split(" - ")[0];
        
        irrigationLog.add(0, new IrrigationEvent(timestamp, zone, action, details));
    }
    
    private void addSampleLogEntries() {
        // Add some sample log entries for demonstration
        irrigationLog.add(new IrrigationEvent("08:15:32", "Zone 1", "Start", "Auto, 45 L/min, 15 min"));
        irrigationLog.add(new IrrigationEvent("08:30:32", "Zone 1", "Complete", "Full cycle completed"));
        irrigationLog.add(new IrrigationEvent("12:45:17", "Zone 2", "Start", "Manual, 60 L/min, 20 min"));
        irrigationLog.add(new IrrigationEvent("12:53:42", "Zone 2", "Stop", "Manually stopped"));
        irrigationLog.add(new IrrigationEvent("18:00:00", "Zone 3", "Start", "Auto, 50 L/min, 10 min"));
        irrigationLog.add(new IrrigationEvent("18:10:00", "Zone 3", "Complete", "Full cycle completed"));
        irrigationLog.add(new IrrigationEvent("23:30:00", "System", "Error", "Low water pressure"));
    }
    
    // Simple class to represent irrigation events in the log
    private static class IrrigationEvent {
        private final String timestamp;
        private final String zone;
        private final String action;
        private final String details;
        
        public IrrigationEvent(String timestamp, String zone, String action, String details) {
            this.timestamp = timestamp;
            this.zone = zone;
            this.action = action;
            this.details = details;
        }
    }
} 