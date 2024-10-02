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

public class SHA256SearchGUIFX extends Application {

    private TextField sha256HashField;
    private Button chooseFileButton;
    private TextField plaintextField;
    private Button computeHashButton;
    private File dictionaryFile;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SHA-256 Search");

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
        Label titleLabel = new Label("SHA-256 Search");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // SHA-256 Hash field
        Label sha256HashLabel = new Label("Enter SHA-256 Hash:");
        sha256HashLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        sha256HashField = new TextField();
        sha256HashField.setPromptText("SHA-256 Hash");
        sha256HashField.setMinWidth(150); // Adjusted width
        sha256HashField.setMaxWidth(150); // Adjusted width

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
            String sha256Hash = sha256HashField.getText();
            if (sha256Hash.isEmpty() || dictionaryFile == null) {
                showAlert(Alert.AlertType.WARNING, "Please enter a SHA-256 hash and choose a dictionary file.");
                return;
            }
            String plaintext = searchDictionary(sha256Hash, dictionaryFile);
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

        // Compute SHA-256 Hash button
        computeHashButton = new Button("Compute SHA-256 Hash");
        computeHashButton.setPrefWidth(200);
        computeHashButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        computeHashButton.setOnAction(e -> {
            String plaintext = plaintextField.getText();
            if (plaintext.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter plaintext.");
                return;
            }
            String sha256Hash = calculateSHA256(plaintext);
            showHashDialog(sha256Hash);
        });

        // Adding components to VBox
        vbox.getChildren().addAll(titleLabel, sha256HashLabel, sha256HashField, dictionaryFileLabel, chooseFileButton,
                searchButton, plaintextLabel, plaintextField, computeHashButton);

        // Add components to AnchorPane
        mainLayout.getChildren().addAll(backButton, vbox);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 800, 600); // Width and height for the scene (but it's full screen)
        // Set fullscreen
        primaryStage.setFullScreen(true);  // Fullscreen mode
        primaryStage.setFullScreenExitHint("");  // Removes the default "Exit fullscreen" hint
        primaryStage.setTitle("SHA-256");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String searchDictionary(String sha256Hash, File dictionaryFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String hash = calculateSHA256(line);
                if (hash.equals(sha256Hash)) {
                    return line;
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error reading dictionary file: " + e.getMessage());
        }
        return null;
    }

    private String calculateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hashBytes));
        } catch (NoSuchAlgorithmException e) {
            showAlert(Alert.AlertType.ERROR, "Error calculating SHA-256 hash: " + e.getMessage());
            return null;
        }
    }

    private void showHashDialog(String hash) {
        // Create a new dialog to display the hash
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("SHA-256 Hash");
        dialog.setHeaderText("The SHA-256 hash is:");

        // Create a TextArea to show the hash
        TextArea textArea = new TextArea(hash);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefSize(400, 100); // Adjust the size as needed

        // Add the TextArea to the dialog
        dialog.getDialogPane().setContent(textArea);

        // Add an OK button
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        // Show the dialog and wait for it to be closed
        dialog.showAndWait();
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

