package com.mysql;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HashIdentifierApp extends Application {

    // Define the HashIdentifier interface
    public interface HashIdentifier {
        String identifyHash(String hash);
    }

    // Implement the HashIdentifier interface
    public static class HashIdentifierImpl implements HashIdentifier {
        private static final String MD5_PATTERN = "^[a-f0-9]{32}$";
        private static final String SHA1_PATTERN = "^[a-f0-9]{40}$";
        private static final String SHA256_PATTERN = "^[a-f0-9]{64}$";
        private static final String SHA512_PATTERN = "^[a-f0-9]{128}$";

        @Override
        public String identifyHash(String hash) {
            if (hash.matches(MD5_PATTERN)) {
                return "MD5";
            } else if (hash.matches(SHA1_PATTERN)) {
                return "SHA-1";
            } else if (hash.matches(SHA256_PATTERN)) {
                return "SHA-256";
            } else if (hash.matches(SHA512_PATTERN)) {
                return "SHA-512";
            } else {
                return "Hash is not identifiable.";
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a StackPane for layering the background and content
        StackPane root = new StackPane();

        // Create a gradient blue background
        Background blueGradientBackground = new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#001F54")),    // Dark Blue
                        new Stop(1, Color.web("#0044CC"))     // Lighter Blue
                ),
                CornerRadii.EMPTY, Insets.EMPTY
        ));
        root.setBackground(blueGradientBackground);

        // Apply a subtle blur effect to the background elements
        BoxBlur blurEffect = new BoxBlur(0.1, 0.1, 3);  // Subtle blur with low intensity

        // Add animated squares with blur effect to the background
        for (int i = 0; i < 25; i++) { // Increased number of squares for more pattern
            Rectangle square = new Rectangle(30 + Math.random() * 70, 30 + Math.random() * 70, Color.rgb(0, 0, 255, 1)); // Random size, semi-transparent blue
            square.setTranslateX(-300 + i * 60); // Horizontal spacing with variation
            square.setTranslateY(600 + Math.random() * 300); // Start at the bottom with random offset
            square.setOpacity(0.8 + Math.random() * 0.7);  // Set varying opacity for each square
            square.setEffect(blurEffect);  // Apply the subtle blur effect to each square
            root.getChildren().add(square);

            // Animate each square to move upwards, rotate, and fade out
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(square.translateYProperty(), square.getTranslateY(), Interpolator.EASE_BOTH), // Start at current Y position
                            new KeyValue(square.rotateProperty(), 0, Interpolator.EASE_BOTH), // Start without rotation
                            new KeyValue(square.opacityProperty(), square.getOpacity(), Interpolator.EASE_BOTH) // Keep initial opacity
                    ),
                    new KeyFrame(Duration.seconds(12 + Math.random() * 4), // Random duration for variation
                            new KeyValue(square.translateYProperty(), -230, Interpolator.EASE_BOTH), // Move upwards off the screen
                            new KeyValue(square.rotateProperty(), 720, Interpolator.EASE_BOTH), // Rotate 720 degrees for smoother motion
                            new KeyValue(square.opacityProperty(), 0, Interpolator.EASE_BOTH) // Fade out as it moves
                    )
            );
            animation.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
            animation.setAutoReverse(false); // No reverse
            animation.play(); // Start the animation
        }

        // add method


        // Create the GUI components
        TextField hashInputField = new TextField();
        hashInputField.setPromptText("Enter a hash (MD5, SHA-1, SHA-256, SHA-512)");
        hashInputField.setPrefColumnCount(30);

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);

        Button identifyButton = new Button("Identify Hash");
        identifyButton.setOnAction(event -> {
            String inputHash = hashInputField.getText();
            HashIdentifier hashIdentifier = new HashIdentifierImpl();
            String result = hashIdentifier.identifyHash(inputHash);
            resultArea.setText(result);
        });

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(event -> {
            // Open the second interface
            HashApplication hashApplication = new HashApplication();
            hashApplication.start(new Stage());
            primaryStage.close();
        });

        // Create the layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(new Label("Enter a hash (MD5, SHA-1, SHA-256, SHA-512):") {{
            setFont(Font.font("Berlin Sans FB Demi", FontWeight.BOLD, 14));
            setTextFill(Color.WHITE);
        }}, 0, 0);
        gridPane.add(hashInputField, 0, 1);
        gridPane.add(identifyButton, 0, 2);
        gridPane.add(new Label("Result:"), 0, 3);
        gridPane.add(resultArea, 0, 4);
        gridPane.add(continueButton, 0, 5);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);

        // Create a back button and place it at the top left
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold;");
        backButton.setOnAction(event -> {
            // Create a new instance of NinHashLoginSignupPage1235
            NinHashLoginSignupPage123 firstPage = new NinHashLoginSignupPage123();
        
            // Create a new Stage (dashboardStage)
            Stage dashboardStage = new Stage();
        
            try {
                // Start the first page, setting up its scene and root
                firstPage.start(dashboardStage);
        
                // Directly pass the root to showSignupDashboard
                firstPage.showLoginDashboard((StackPane) dashboardStage.getScene().getRoot());
        
                // Close the current HashIdentifierApp window
                primaryStage.close();
               // dashboardStage.setFullScreen(true);
        //dashboardStage.setFullScreenExitHint(""); 
         // Removes default "Exit fullscreen" hint
         dashboardStage.setMaximized(true); // Set the stage to maximize
         dashboardStage.show(); // Show the new stage
     

        primaryStage.close(); 
        
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));  // Padding around the button
        topBar.setAlignment(Pos.TOP_LEFT);  // Align the button to the top left
        topBar.getChildren().add(backButton);  // Add the button to the HBox

        // Add the topBar to the top of the BorderPane
        borderPane.setTop(topBar);

        // Add the border pane to the root pane
        root.getChildren().add(borderPane);

        // Create the scene and set up the stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Hash Identifier");

        // Set fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");  // Removes default "Exit fullscreen" hint
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
