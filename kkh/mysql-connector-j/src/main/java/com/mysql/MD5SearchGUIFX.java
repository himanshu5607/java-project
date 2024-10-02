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

public class MD5SearchGUIFX extends Application {

    private TextField md5HashField;
    private Button chooseFileButton;
    private TextField plaintextField;
    private Button computeHashButton;
    private File dictionaryFile;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MD5 Search");

        // Full-screen mode
        primaryStage.setFullScreen(true);

        // Main layout container (AnchorPane)
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0F2027, #203A43, #2C5364);");

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold;");
        backButton.setOnAction(event -> {
            // Re-open the HashIdentifierApp interface and close the current stage
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
        Label titleLabel = new Label("MD5 Search");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // MD5 Hash field
        Label md5HashLabel = new Label("Enter MD5 Hash:");
        md5HashLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        md5HashField = new TextField();
        md5HashField.setPromptText("MD5 Hash");
        md5HashField.setMinWidth(150); // Adjusted width
        md5HashField.setMaxWidth(150); // Adjusted width

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
            String md5Hash = md5HashField.getText();
            if (md5Hash.isEmpty() || dictionaryFile == null) {
                showAlert(Alert.AlertType.WARNING, "Please enter an MD5 hash and choose a dictionary file.");
                return;
            }
            String plaintext = searchDictionary(md5Hash, dictionaryFile);
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

        // Compute MD5 Hash button
        computeHashButton = new Button("Compute MD5 Hash");
        computeHashButton.setPrefWidth(200);
        computeHashButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        computeHashButton.setOnAction(e -> {
            String plaintext = plaintextField.getText();
            if (plaintext.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter plaintext.");
                return;
            }
            String md5Hash = calculateMD5(plaintext);
            showHashAlert( "MD5 hash of the plaintext is: ",  md5Hash);
        });

        // Adding components to VBox
        vbox.getChildren().addAll(titleLabel, md5HashLabel, md5HashField, dictionaryFileLabel, chooseFileButton,
                searchButton, plaintextLabel, plaintextField, computeHashButton);

        // Add components to AnchorPane
        mainLayout.getChildren().addAll(backButton, vbox);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 800, 600); // Width and height for the scene (but it's full screen)
        // Set fullscreen
        primaryStage.setFullScreen(true);  // Fullscreen mode
        primaryStage.setFullScreenExitHint("");  // Removes the default "Exit fullscreen" hint
        primaryStage.setTitle("MD5");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String searchDictionary(String md5Hash, File dictionaryFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String hash = calculateMD5(line);
                if (hash.equals(md5Hash)) {
                    return line;
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error reading dictionary file: " + e.getMessage());
        }
        return null;
    }

    private String calculateMD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, hashBytes));
        } catch (NoSuchAlgorithmException e) {
            showAlert(Alert.AlertType.ERROR, "Error calculating MD5 hash: " + e.getMessage());
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
