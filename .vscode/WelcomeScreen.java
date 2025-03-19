import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScreen {
    
    private static final String PRIMARY_COLOR = "#1A5F7A";
    private static final String SECONDARY_COLOR = "#2E8B57";
    private static final String ACCENT_COLOR = "#FF6347";
    private static final String BACKGROUND_COLOR = "#F8F9FA";
    
    private final Stage primaryStage;
    private final MainApp mainApp;
    
    public WelcomeScreen(Stage stage, MainApp mainApp) {
        this.primaryStage = stage;
        this.mainApp = mainApp;
    }
    
    public Scene createWelcomeScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Header
        VBox headerBox = createHeader();
        root.setTop(headerBox);
        
        // Main content
        VBox contentBox = createContent();
        root.setCenter(contentBox);
        
        // Footer
        HBox footerBox = createFooter();
        root.setBottom(footerBox);
        
        return new Scene(root, 900, 600);
    }
    
    private VBox createHeader() {
        VBox headerBox = new VBox();
        headerBox.setPadding(new Insets(30, 50, 10, 50));
        headerBox.setAlignment(Pos.CENTER);
        
        Text title = new Text("AgriTech Smart Irrigation System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setFill(Color.web(PRIMARY_COLOR));
        
        Text subtitle = new Text("Precision Agriculture Monitoring & Control Platform");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setFill(Color.web("#6C757D"));
        
        headerBox.getChildren().addAll(title, subtitle);
        return headerBox;
    }
    
    private VBox createContent() {
        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(20, 50, 20, 50));
        contentBox.setAlignment(Pos.CENTER);
        
        // Image placeholder (replace with actual image path)
        ImageView imageView = new ImageView();
        try {
            // Replace with your image path
            Image image = new Image(getClass().getResourceAsStream("/images/irrigation-system.png"));
            imageView.setImage(image);
            imageView.setFitWidth(400);
            imageView.setFitHeight(250);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            // Fallback if image not found
            Text placeholder = new Text("Smart Irrigation System Image");
            placeholder.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            placeholder.setFill(Color.web("#ADB5BD"));
            contentBox.getChildren().add(placeholder);
        }
        
        Text welcomeText = new Text("Welcome to your intelligent farming assistant");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcomeText.setFill(Color.web("#343A40"));
        
        Text descriptionText = new Text(
            "Monitor soil conditions, control irrigation systems, and optimize water usage " +
            "with our comprehensive agricultural management platform. Make data-driven " +
            "decisions to increase crop yield while conserving resources."
        );
        descriptionText.setFont(Font.font("Arial", 16));
        descriptionText.setFill(Color.web("#6C757D"));
        descriptionText.setWrappingWidth(700);
        
        Button enterButton = new Button("Enter Dashboard");
        enterButton.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 12 30;" +
            "-fx-background-radius: 5;"
        );
        enterButton.setOnAction(e -> mainApp.showDashboard());
        
        contentBox.getChildren().addAll(imageView, welcomeText, descriptionText, enterButton);
        return contentBox;
    }
    
    private HBox createFooter() {
        HBox footerBox = new HBox();
        footerBox.setPadding(new Insets(20, 50, 30, 50));
        footerBox.setAlignment(Pos.CENTER);
        
        Label footerText = new Label("© 2025 AgriTech Solutions • All Rights Reserved");
        footerText.setFont(Font.font("Arial", 12));
        footerText.setTextFill(Color.web("#6C757D"));
        
        footerBox.getChildren().add(footerText);
        return footerBox;
    }
}