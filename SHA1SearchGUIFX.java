package com.mysql;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1SearchGUIFX extends Application {

    private TextField sha1HashField;
    private Button chooseFileButton;
    private TextField plaintextField;
    private Button computeHashButton;
    private File dictionaryFile;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SHA-1 Search");

        // Full-screen mode
        primaryStage.setFullScreen(true);

        // Main layout container (AnchorPane)
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0F2027, #203A43, #2C5364);");

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold;");
        backButton.setOnAction(event -> {
            // Re-open the HashApplication interface and close the current stage
            HashApplication hashApp = new HashApplication();
            Stage newStage = new Stage();
            hashApp.start(newStage);
            primaryStage.close();
        });

        // Position the back button in the top-left corner
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 10.0);

        // VBox for centered content
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(670); // Position VBox to be centered horizontally
        vbox.setLayoutY(190); // Position VBox down from the top

        // Title Label
        Label titleLabel = new Label("SHA-1 Search");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // SHA-1 Hash field
        Label sha1HashLabel = new Label("Enter SHA-1 Hash:");
        sha1HashLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        sha1HashField = new TextField();
        sha1HashField.setPromptText("SHA-1 Hash");
        sha1HashField.setMinWidth(150); // Adjusted width
        sha1HashField.setMaxWidth(150); // Adjusted width

        // Dictionary file selection
        Label dictionaryFileLabel = new Label("Dictionary File:");
        dictionaryFileLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        chooseFileButton = new Button("Choose File");
        chooseFileButton.setPrefWidth(150);
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            dictionaryFile = fileChooser.showOpenDialog(primaryStage);
            if (dictionaryFile != null) {
                chooseFileButton.setText(dictionaryFile.getName());
            }
        });

        // Search button
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(200);
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        searchButton.setOnAction(e -> {
            String sha1Hash = sha1HashField.getText();
            if (sha1Hash.isEmpty() || dictionaryFile == null) {
                showAlert(Alert.AlertType.WARNING, "Please enter a SHA-1 hash and choose a dictionary file.");
                return;
            }
            String plaintext = searchDictionary(sha1Hash, dictionaryFile);
            if (plaintext != null) {
                showAlert(Alert.AlertType.INFORMATION, "Found match! The plaintext is: " + plaintext);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No match found in the dictionary.");
            }
        });

        // Plaintext field
        Label plaintextLabel = new Label("Enter Plaintext:");
        plaintextLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        plaintextField = new TextField();
        plaintextField.setPromptText("Plaintext");
        plaintextField.setMinWidth(150); // Adjusted width
        plaintextField.setMaxWidth(150); // Adjusted width

        // Compute SHA-1 Hash button
        computeHashButton = new Button("Compute SHA-1 Hash");
        computeHashButton.setPrefWidth(200);
        computeHashButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        computeHashButton.setOnAction(e -> {
            String plaintext = plaintextField.getText();
            if (plaintext.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter plaintext.");
                return;
            }
            String sha1Hash = calculateSHA1(plaintext);
            showHashAlert("SHA-1 hash of the plaintext is: " , sha1Hash);
        });

        // Adding components to VBox
        vbox.getChildren().addAll(titleLabel, sha1HashLabel, sha1HashField, dictionaryFileLabel, chooseFileButton,
                searchButton, plaintextLabel, plaintextField, computeHashButton);

        // Add components to AnchorPane
        mainLayout.getChildren().addAll(backButton, vbox);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 800, 600); // Width and height for the scene (but it's full screen)
        // Set fullscreen
        primaryStage.setFullScreen(true);  // Fullscreen mode
        primaryStage.setFullScreenExitHint("");  // Removes the default "Exit fullscreen" hint
        primaryStage.setTitle("SHA1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String searchDictionary(String sha1Hash, File dictionaryFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String hash = calculateSHA1(line);
                if (hash.equals(sha1Hash)) {
                    return line;
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error reading dictionary file: " + e.getMessage());
        }
        return null;
    }

    private String calculateSHA1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%040x", new BigInteger(1, hashBytes));
        } catch (NoSuchAlgorithmException e) {
            showAlert(Alert.AlertType.ERROR, "Error calculating SHA-1 hash: " + e.getMessage());
            return null;
        }
    }

    private void showHashAlert(String header, String hash) {
        TextArea textArea = new TextArea(hash);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

