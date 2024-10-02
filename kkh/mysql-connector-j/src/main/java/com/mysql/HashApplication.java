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
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HashApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a StackPane for the animated background and content
        StackPane root = new StackPane();

        // Create a gradient blue background similar to the previous interface
        Background blueGradientBackground = new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#001F54")),    // Dark Blue
                        new Stop(1, Color.web("#0044CC"))     // Lighter Blue
                ),
                CornerRadii.EMPTY, Insets.EMPTY
        ));
        root.setBackground(blueGradientBackground);

        // Apply a subtle blur effect
        BoxBlur blurEffect = new BoxBlur(0.1, 0.1, 3);  // Subtle blur with low intensity

        // Add animated squares to the background
        for (int i = 0; i < 25; i++) {
            Rectangle square = new Rectangle(30 + Math.random() * 70, 30 + Math.random() * 70, Color.rgb(0, 0, 255, 1)); // Random size
            square.setTranslateX(-300 + i * 60); // Horizontal spacing
            square.setTranslateY(600 + Math.random() * 300); // Start position
            square.setOpacity(0.8 + Math.random() * 0.7);  // Random opacity
            square.setEffect(blurEffect);  // Apply the subtle blur effect
            root.getChildren().add(square);

            // Animate each square
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(square.translateYProperty(), square.getTranslateY(), Interpolator.EASE_BOTH),
                            new KeyValue(square.rotateProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(square.opacityProperty(), square.getOpacity(), Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.seconds(12 + Math.random() * 4),
                            new KeyValue(square.translateYProperty(), -230, Interpolator.EASE_BOTH),
                            new KeyValue(square.rotateProperty(), 720, Interpolator.EASE_BOTH),
                            new KeyValue(square.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    )
            );
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.setAutoReverse(false);
            animation.play();
        }

        // GUI Components
        Button md5Button = createStyledButton("MD5");
        md5Button.setOnAction(event -> {
            // Open the second interface
            MD5SearchGUIFX md5 = new MD5SearchGUIFX();
            md5.start(new Stage());
            primaryStage.close();
        });
        Button sha1Button = createStyledButton("SHA1");
        sha1Button.setOnAction(event -> {
            // Open the second interface
            SHA1SearchGUIFX sha1 = new SHA1SearchGUIFX();
            sha1.start(new Stage());
            primaryStage.close();
        });

        Button sha256Button = createStyledButton("SHA256");
        sha256Button.setOnAction(event -> {
            // Open the second interface
            SHA256SearchGUIFX sha256 = new SHA256SearchGUIFX();
            sha256.start(new Stage());
            primaryStage.close();
        });

        Button sha512Button = createStyledButton("SHA512");
        sha512Button.setOnAction(event -> {
            // Open the second interface
            SHA512SearchGUIFX sha512 = new SHA512SearchGUIFX();
            sha512.start(new Stage());
            primaryStage.close();
        });

        Button viewDbButton = createStyledButton("View DB");
        Button updateDbButton = createStyledButton("Update DB");

        // Back button to go to the previous interface (top-left corner)
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold;");
        backButton.setOnAction(event -> {
            // Re-open the HashIdentifierApp interface and close the current stage
            HashIdentifierApp hashIdentifierApp = new HashIdentifierApp();
            Stage newStage = new Stage();
            hashIdentifierApp.start(newStage);
            primaryStage.close();
        });

        // Layout for hash buttons
        VBox hashPanelContainer = new VBox(10);
        hashPanelContainer.setPadding(new Insets(20, 70, 15, 70));
        hashPanelContainer.setStyle("-fx-background-color: white;");

        Label hashLabel = new Label("SELECT HASH TYPE");
        hashLabel.setStyle("-fx-font-size: 21; -fx-font-weight: bold;");

        GridPane hashPanel = new GridPane();
        hashPanel.setPadding(new Insets(10));
        hashPanel.setHgap(60);
        hashPanel.setVgap(30);
        hashPanel.setAlignment(Pos.CENTER);

        hashPanel.add(md5Button, 0, 0);
        hashPanel.add(sha1Button, 1, 0);
        hashPanel.add(sha256Button, 0, 1);
        hashPanel.add(sha512Button, 1, 1);

        hashPanelContainer.getChildren().addAll(hashLabel, hashPanel);

        // Layout for DB buttons
        VBox dbPanelContainer = new VBox(10);
        dbPanelContainer.setPadding(new Insets(20, 70, 15, 70));
        dbPanelContainer.setStyle("-fx-background-color: white;");

        Label dbLabel = new Label("DATABASE OPS");
        dbLabel.setStyle("-fx-font-size: 21; -fx-font-weight: bold;");

        GridPane dbPanel = new GridPane();
        dbPanel.setPadding(new Insets(10));
        dbPanel.setHgap(10);
        dbPanel.setVgap(10);
        dbPanel.setAlignment(Pos.CENTER);

        dbPanel.add(viewDbButton, 0, 0);
        dbPanel.add(updateDbButton, 1, 0);

        dbPanelContainer.getChildren().addAll(dbLabel, dbPanel);

        // Add the panels to the layout
        VBox outerPanel = new VBox(30);
        outerPanel.setPadding(new Insets(200, 430, 200, 430));
        outerPanel.getChildren().addAll(hashPanelContainer, dbPanelContainer);

        // Use BorderPane to add outerPanel in the center
        BorderPane contentPane = new BorderPane();
        contentPane.setCenter(outerPanel);

        // Add back button to the top-left corner of contentPane
        BorderPane.setMargin(backButton, new Insets(10)); // Margin for spacing
        contentPane.setTop(backButton);

        // Add contentPane to root (with animated background)
        root.getChildren().add(contentPane);

        // Set the scene
        Scene scene = new Scene(root, 1400, 800);

        // Set fullscreen
        primaryStage.setFullScreen(true);  // Fullscreen mode
        primaryStage.setFullScreenExitHint("");  // Removes the default "Exit fullscreen" hint
        primaryStage.setTitle("Hash Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-font-size: 15; -fx-font-weight: bold;");
        button.setPrefSize(120, 50);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

