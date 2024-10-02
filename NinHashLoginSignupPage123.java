package com.mysql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NinHashLoginSignupPage123 extends Application {

    private Stage primaryStage;

    @Override


    
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        StackPane root = new StackPane();
        StackPane animatedBackground = createAnimatedBackground();
        root.getChildren().add(animatedBackground);

        primaryStage.setTitle("NinHash Login & Signup");
        Scene scene = new Scene(root, 600, 600);
        showLoginForm(root, primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   
    private void showLoginForm(StackPane root, Stage primaryStage, Scene scene) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
    
        VBox transparentContainer = createTransparentContainer(gridPane, 400, 300);
    
        Label heading = new Label("Welcome to NinHash");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        heading.setTextFill(Color.WHITE);
        GridPane.setColumnSpan(heading, 2);
        heading.setAlignment(Pos.CENTER);
        gridPane.add(heading, 1, 0);
    
        Label usernameLabel = new Label("Email:");
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(Font.font(14));
    
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your email");
        usernameField.setPrefWidth(240);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
    
        Label passwordLabel = new Label("Password:");
        passwordLabel.setTextFill(Color.WHITE);
        passwordLabel.setFont(Font.font(14));
    
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(240);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
    
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setPrefWidth(240);
        gridPane.add(loginButton, 1, 3);
    
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        gridPane.add(errorMessage, 1, 4);
    
        /*loginButton.setOnAction(e -> {
            String emailInput = usernameField.getText();
            String passwordInput = passwordField.getText();
    
            if (emailInput.isEmpty()) {
                errorMessage.setText("Email is required.");
            } else if (!isValidEmail(emailInput)) {
                errorMessage.setText("Invalid email address.");
            } else if (passwordInput.isEmpty()) {
                errorMessage.setText("Password is required.");
            } else {
                // Clear the error message and proceed with login
                errorMessage.setText("");
                showLoginDashboard(root);  // Open dashboard on successful login
            }
        });*/
        loginButton.setOnAction(e -> {
            String emailInput = usernameField.getText();
            String passwordInput = passwordField.getText();
        
            if (emailInput.isEmpty()) {
                errorMessage.setText("Email is required.");
            } else if (!isValidEmail(emailInput)) {
                errorMessage.setText("Invalid email address.");
            } else if (passwordInput.isEmpty()) {
                errorMessage.setText("Password is required.");
            } else {
                errorMessage.setText("");
                handleLogin(root, emailInput, passwordInput, errorMessage);
            }
        });
        
    
        Hyperlink forgottenPasswordLink = new Hyperlink("Forgot password?");
        forgottenPasswordLink.setTextFill(Color.YELLOW);
        forgottenPasswordLink.setFont(Font.font(14));
        GridPane.setHalignment(forgottenPasswordLink, HPos.RIGHT);
        gridPane.add(forgottenPasswordLink, 1, 5);
    
        forgottenPasswordLink.setOnAction(e -> showForgotPasswordDialog(primaryStage));
    
        Label signupText = new Label("Don't have an account? ");
        signupText.setTextFill(Color.WHITE);
        signupText.setFont(Font.font(14));
    
        Hyperlink signupLink = new Hyperlink("Signup");
        signupLink.setFont(Font.font(14));
        signupLink.setTextFill(Color.YELLOW);
        signupLink.setOnAction(e -> showSignupForm(root, primaryStage, scene));
    
        HBox signupBox = new HBox(5, signupText, signupLink);
        signupBox.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(signupBox, 2);
        gridPane.add(signupBox, 0, 6);
    
        root.getChildren().removeIf(node -> node instanceof VBox);
        root.getChildren().add(transparentContainer);
    }


    private void showForgotPasswordDialog(Stage primaryStage) {
        // Create a dialog for entering the registered email
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Enter your registered email:");
    
        // Set the dialog size
        dialog.setWidth(400);  // Set width to 400 pixels
        dialog.setHeight(200); // Set height to 200 pixels
    
        // Create a TextField for email input
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
    
        // Create a VBox to hold the email field and set padding
        VBox dialogPaneContent = new VBox(10); // 10 pixels spacing between elements
        dialogPaneContent.setPadding(new Insets(20)); // Set padding around the content
        dialogPaneContent.getChildren().add(emailField);
    
        ButtonType sendButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);
        
        // Set the content of the dialog to the VBox with padding
        dialog.getDialogPane().setContent(dialogPaneContent);
    
        // Handle button click
       /*  dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                String emailInput = emailField.getText();
                if (isValidEmail(emailInput)) {
                    // Simulate sending an email (replace with actual email sending logic)
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Email Sent");
                    alert.setHeaderText(null);
                    alert.setContentText("An email has been sent to " + emailInput + " with your password.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid email address.");
                    alert.showAndWait();
                }
                return emailField.getText();
            }
            return null;
        });*/
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                String emailInput = emailField.getText();
                if (isValidEmail(emailInput)) {
                    String password = retrievePasswordForEmail(emailInput);
                    if (password != null) {
                        EmailUtil.sendEmail(emailInput, password);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Email Sent");
                        alert.setHeaderText(null);
                        alert.setContentText("An email has been sent to " + emailInput + " with your password.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Email not found.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid email address.");
                    alert.showAndWait();
                }
                return emailField.getText();
            }
            return null;
        });
        
    
        dialog.showAndWait();
    }
    
    
    private String retrievePasswordForEmail(String email) {
        String password = null;
        String query = "SELECT password FROM usersk WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }
    
/*private void sendEmail(String recipientEmail, String password) {
    // Set the SMTP server properties (replace with actual server details)
    String host = "smtp.example.com"; // e.g., smtp.gmail.com for Gmail
    String from = "your-email@example.com";
    String username = "your-email@example.com"; // Your full email address
    String emailPassword = "your-email-password"; // Your email password or app password

    // Set up properties object for mail configuration
    Properties properties = new Properties();
    properties.put("mail.smtp.host", host); // SMTP Host
    properties.put("mail.smtp.port", "587"); // Port (Gmail uses 587)
    properties.put("mail.smtp.auth", "true"); // Enable authentication
    properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS/SSL

    // Create the mail session
    Session session = Session.getDefaultInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, emailPassword);
        }
    });

    try {
        // Create a new email message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Your Forgotten Password");
        message.setText("Your password is: " + password);

        // Send the message
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientEmail);

    } catch (MessagingException e) {
        e.printStackTrace();
    }
}*/
public class EmailUtil {
    public static void sendEmail(String recipientEmail, String password) {
        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Use your SMTP server
        properties.put("mail.smtp.port", "587"); // Change if necessary

        // Authenticate using your email and password
        String myEmail = "ninhash247help@gmail.com"; // Your email
        String myPassword = "bqzd xkxj jwbq yhbk"; // Your email password

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, myPassword);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Password");
            message.setText("Your password is: " + password);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}



    
    private void handleLogin(StackPane root,String emailInput, String passwordInput, Label errorMessage) {
        String query = "SELECT * FROM usersk WHERE email = ? AND password = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, emailInput);
            pstmt.setString(2, passwordInput);
    
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                // Credentials are valid, proceed to dashboard
                showLoginDashboard(root);
            } else {
                // Invalid credentials
                errorMessage.setText("Invalid email or password.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
private void showSignupForm(StackPane root, Stage primaryStage, Scene scene) {
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    VBox transparentContainer = createTransparentContainer(gridPane, 400, 300);

    Label heading = new Label("Sign Up");
    heading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    heading.setTextFill(Color.WHITE);
    GridPane.setColumnSpan(heading, 2);
    heading.setAlignment(Pos.CENTER);
    gridPane.add(heading, 1, 0);

    Label emailLabel = new Label("Email:");
    emailLabel.setTextFill(Color.WHITE);
    emailLabel.setFont(Font.font(14));

    TextField emailField = new TextField();
    emailField.setPromptText("Enter your email");
    emailField.setPrefWidth(230);
    gridPane.add(emailLabel, 0, 1);
    gridPane.add(emailField, 1, 1);

    Label passwordLabel = new Label("Password:");
    passwordLabel.setTextFill(Color.WHITE);
    passwordLabel.setFont(Font.font(14));

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter your password");
    passwordField.setPrefWidth(230);
    gridPane.add(passwordLabel, 0, 2);
    gridPane.add(passwordField, 1, 2);

    Label confirmPasswordLabel = new Label("Confirmation:");
    confirmPasswordLabel.setTextFill(Color.WHITE);
    confirmPasswordLabel.setFont(Font.font(14));

    PasswordField confirmPasswordField = new PasswordField();
    confirmPasswordField.setPromptText("Confirm your password");
    confirmPasswordField.setPrefWidth(230);
    gridPane.add(confirmPasswordLabel, 0, 3);
    gridPane.add(confirmPasswordField, 1, 3);

    Label errorMessage = new Label();
    errorMessage.setTextFill(Color.RED);
    gridPane.add(errorMessage, 1, 5);

    Button signupButton = new Button("Sign Up");
    signupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    signupButton.setPrefWidth(230);
    gridPane.add(signupButton, 1, 4);

    /*signupButton.setOnAction(e -> {
        String emailInput = emailField.getText();
        String passwordInput = passwordField.getText();
        String confirmPasswordInput = confirmPasswordField.getText();

        if (!isValidEmail(emailInput)) {
            errorMessage.setText("Invalid email address.");
        } else if (passwordInput.isEmpty()) {
            errorMessage.setText("Password is required.");
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            errorMessage.setText("Passwords do not match.");
        } else {
            errorMessage.setText("");
            showSignupDashboard(root);  // Open dashboard on successful signup
        }
    });*/
    signupButton.setOnAction(e -> {
        String emailInput = emailField.getText();
        String passwordInput = passwordField.getText();
        String confirmPasswordInput = confirmPasswordField.getText();
    
        if (!isValidEmail(emailInput)) {
            errorMessage.setText("Invalid email address.");
        } else if (passwordInput.isEmpty()) {
            errorMessage.setText("Password is required.");
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            errorMessage.setText("Passwords do not match.");
        } else {
            errorMessage.setText("");
            handleSignup(root, emailInput, passwordInput);
        }
    });
    

    Label loginText = new Label("Already have an account? ");
    loginText.setTextFill(Color.WHITE);
    loginText.setFont(Font.font(14));

    Hyperlink loginLink = new Hyperlink("Login");
    loginLink.setFont(Font.font(14));
    loginLink.setTextFill(Color.YELLOW);
    loginLink.setOnAction(e -> showLoginForm(root, primaryStage, scene));

    HBox loginBox = new HBox(5, loginText, loginLink);
    loginBox.setAlignment(Pos.CENTER);
    GridPane.setColumnSpan(loginBox, 2);
    gridPane.add(loginBox, 0, 6);

    root.getChildren().removeIf(node -> node instanceof VBox);
    root.getChildren().add(transparentContainer);
}

// Helper method to validate email
private boolean isValidEmail(String email) {
    // Basic email validation regex
    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    return email.matches(emailRegex);
}

private void handleSignup(StackPane root,String emailInput, String passwordInput) {
    String query = "INSERT INTO usersk (email, password, confirm_password) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, emailInput);
        pstmt.setString(2, passwordInput);
        pstmt.setString(3, passwordInput); // Assuming confirm password matches password
        pstmt.executeUpdate();

        // Open signup success dashboard or show success message
        showSignupDashboard(root);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


void showLoginDashboard(StackPane root) {
    VBox dashboard = new VBox(10);
    dashboard.setAlignment(Pos.CENTER);

    Label welcomeLabel = new Label("Welcome to the Dashboard");
    welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    welcomeLabel.setTextFill(Color.WHITE);

    // Wrap each grid in a transparent container
    VBox userProfileContainer = createTransparentContainer(createUserProfileGrid(), 300, 400);
    VBox passwordTrackingContainer = createTransparentContainer(createPasswordTrackingGrid(), 300, 400);
    VBox additionalButtonsContainer = createTransparentContainer(createAdditionalButtonsGrid(), 300, 400);

    // Arrange the containers in a horizontal row
    HBox dashboardLayout = new HBox(20, userProfileContainer, passwordTrackingContainer, additionalButtonsContainer);
    dashboardLayout.setAlignment(Pos.CENTER);

    dashboard.getChildren().addAll(welcomeLabel, dashboardLayout);
    //dashboard.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 20;");
    

    root.getChildren().removeIf(node -> node instanceof VBox);
    root.getChildren().add(dashboard);
}
void showSignupDashboard(StackPane root) {
    VBox dashboard = new VBox(10);
    dashboard.setAlignment(Pos.CENTER);

    Label signupSuccessLabel = new Label("Sign-up successful!");
    signupSuccessLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    signupSuccessLabel.setTextFill(Color.WHITE);

    // Create the transition to change the label after 2 seconds
    PauseTransition delay = new PauseTransition(Duration.seconds(2));
    delay.setOnFinished(event -> signupSuccessLabel.setText("Welcome to Dashboard"));
    delay.play();  // Start the transition

    // Wrap each grid in a transparent container
    VBox userProfileContainer = createTransparentContainer(createUserProfileGrid(), 300, 400);
    VBox passwordTrackingContainer = createTransparentContainer(createPasswordTrackingGrid(), 300, 400);
    VBox additionalButtonsContainer = createTransparentContainer(createAdditionalButtonsGrid(), 300, 400);

    // Arrange the containers in a horizontal row
    HBox dashboardLayout = new HBox(20, userProfileContainer, passwordTrackingContainer, additionalButtonsContainer);
    dashboardLayout.setAlignment(Pos.CENTER);

    dashboard.getChildren().addAll(signupSuccessLabel, dashboardLayout);

    root.getChildren().removeIf(node -> node instanceof VBox);
    root.getChildren().add(dashboard);
}



    private GridPane createUserProfileGrid() {
        // Create the grid layout
        GridPane userProfileGrid = new GridPane();
        userProfileGrid.setAlignment(Pos.CENTER);
        userProfileGrid.setHgap(10);
        userProfileGrid.setVgap(20);
    
        // Heading label
        Label userProfileHeading = new Label("User Profile");
        userProfileHeading.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        userProfileHeading.setTextFill(Color.WHITE);
        userProfileGrid.add(userProfileHeading, 0, 0);
    
        // Get the current login time and format it
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
        // Label to display the last login time
        Label lastLoginLabel = new Label("Last Login: " + currentTime.format(formatter));
        lastLoginLabel.setFont(Font.font("Times Roman", FontWeight.BOLD, 12));
        lastLoginLabel.setTextFill(Color.WHITE);
        userProfileGrid.add(lastLoginLabel, 0, 1);
    
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        logoutButton.setPrefWidth(200);
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userProfileGrid.add(logoutButton, 0, 2);
    
        // Handle logout action
        logoutButton.setOnAction(e -> switchToLoginPage());
    
        return userProfileGrid;
    }
    private void switchToLoginPage() {
        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        showLoginForm(root, primaryStage, primaryStage.getScene());
    }

   
    private GridPane createPasswordTrackingGrid() {
        GridPane passwordTrackingGrid = new GridPane();
        passwordTrackingGrid.setAlignment(Pos.CENTER);
        passwordTrackingGrid.setHgap(10);
        passwordTrackingGrid.setVgap(20);

        Label passwordTrackingHeading = new Label("Password Tracking");
        passwordTrackingHeading.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        passwordTrackingHeading.setTextFill(Color.WHITE);
        passwordTrackingGrid.add(passwordTrackingHeading, 0, 0);

        Button startTrackingButton = new Button("Start Tracking");
        startTrackingButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startTrackingButton.setPrefWidth(200);
        startTrackingButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        passwordTrackingGrid.add(startTrackingButton, 0, 1);

        startTrackingButton.setOnAction(event -> {
            // Open the second page (HashIdentifierApp)
            HashIdentifierApp secondPage = new HashIdentifierApp();
            Stage secondStage = new Stage();
            secondPage.start(secondStage);
        
            // Close the current window
            primaryStage.close();
        });
        
        Label passwordStoringInfoLabel = new Label("Password Storing Info");
        passwordStoringInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        passwordStoringInfoLabel.setTextFill(Color.WHITE);
        passwordTrackingGrid.add(passwordStoringInfoLabel, 0, 2);

        Button storePasswordInfoButton = new Button("Click for Info");
        storePasswordInfoButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        storePasswordInfoButton.setPrefWidth(200);
        storePasswordInfoButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        passwordTrackingGrid.add(storePasswordInfoButton, 0, 3);

        return passwordTrackingGrid;
    }
    
    
    private GridPane createAdditionalButtonsGrid() {
    GridPane additionalButtonsGrid = new GridPane();
    additionalButtonsGrid.setAlignment(Pos.CENTER);
    additionalButtonsGrid.setHgap(10);
    additionalButtonsGrid.setVgap(20);

    // FAQ Button
    Button faqButton = new Button("FAQ");
    faqButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    faqButton.setPrefWidth(200);
    faqButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    faqButton.setOnAction(e -> showFAQDialog());  // Call FAQ dialog
    additionalButtonsGrid.add(faqButton, 0, 1);

    // Guide Button
    Button guideButton = new Button("Guide");
    guideButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    guideButton.setPrefWidth(200);
    guideButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    guideButton.setOnAction(e -> showGuideDialog());  // Call Guide dialog
    additionalButtonsGrid.add(guideButton, 0, 2);

    // Contact Us Button
    Button contactUsButton = new Button("Contact Us");
    contactUsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    contactUsButton.setPrefWidth(200);
    contactUsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    contactUsButton.setOnAction(e -> showContactDialog());  // Call Contact Us dialog
    additionalButtonsGrid.add(contactUsButton, 0, 3);

    // Feedback Button
    Button feedbackButton = new Button("Feedback");
    feedbackButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    feedbackButton.setPrefWidth(200);
    feedbackButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    feedbackButton.setOnAction(e -> showFeedbackDialog());  // Call Feedback dialog
    additionalButtonsGrid.add(feedbackButton, 0, 4);

    // About Button
    Button aboutButton = new Button("About");
    aboutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    aboutButton.setPrefWidth(200);
    aboutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    aboutButton.setOnAction(e -> showAboutDialog());  // Call About dialog
    additionalButtonsGrid.add(aboutButton, 0, 5);

    return additionalButtonsGrid;
}


private void showFAQDialog() {
    // Create a new dialog
    Dialog<Void> faqDialog = new Dialog<>();
    faqDialog.setTitle("Frequently Asked Questions");
    faqDialog.setHeaderText("FAQs");

    // Create a VBox to hold questions and answers
    VBox faqContainer = new VBox();
    faqContainer.setSpacing(10);
    faqContainer.setPrefSize(400, 200); // Increased container size for better display

    // FAQ Question 1
    Label question1 = new Label("1. What is NinHash?");
    question1.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
    Label answer1 = new Label("NinHash is a platform for managing and securing digital identities. It provides tools for password management and enhances online security.");
    answer1.setWrapText(true); // Enable word wrapping
    answer1.setVisible(false); // Initially hidden
    answer1.setManaged(false); // Does not take space when hidden

    question1.setOnMouseClicked(e -> {
        // Hide all other answers
        hideAllAnswers(faqContainer, answer1);

        // Toggle visibility and space management of this answer
        answer1.setVisible(!answer1.isVisible());
        answer1.setManaged(answer1.isVisible());
    });

    faqContainer.getChildren().addAll(question1, answer1);

    // FAQ Question 2
    Label question2 = new Label("2. How do I reset my password?");
    question2.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
    Label answer2 = new Label("To reset your password, go to the 'Forgot Password' section on the login page and follow the instructions.");
    answer2.setWrapText(true); // Enable word wrapping
    answer2.setVisible(false);
    answer2.setManaged(false);

    question2.setOnMouseClicked(e -> {
        // Hide all other answers
        hideAllAnswers(faqContainer, answer2);

        // Toggle visibility and space management of this answer
        answer2.setVisible(!answer2.isVisible());
        answer2.setManaged(answer2.isVisible());
    });

    faqContainer.getChildren().addAll(question2, answer2);

    // FAQ Question 3
    Label question3 = new Label("3. How can I create a new account?");
    question3.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
    Label answer3 = new Label("You can create a new account by clicking on 'Sign Up' on the login page and filling in the required details.");
    answer3.setWrapText(true); // Enable word wrapping
    answer3.setVisible(false);
    answer3.setManaged(false);

    question3.setOnMouseClicked(e -> {
        // Hide all other answers
        hideAllAnswers(faqContainer, answer3);

        // Toggle visibility and space management of this answer
        answer3.setVisible(!answer3.isVisible());
        answer3.setManaged(answer3.isVisible());
    });

    faqContainer.getChildren().addAll(question3, answer3);

    // FAQ Question 4
    Label question4 = new Label("4. How can I contact support?");
    question4.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
    Label answer4 = new Label("You can contact support via the 'Contact Us' section in the app, or email support@ninhash.com.");
    answer4.setWrapText(true); // Enable word wrapping
    answer4.setVisible(false);
    answer4.setManaged(false);

    question4.setOnMouseClicked(e -> {
        // Hide all other answers
        hideAllAnswers(faqContainer, answer4);

        // Toggle visibility and space management of this answer
        answer4.setVisible(!answer4.isVisible());
        answer4.setManaged(answer4.isVisible());
    });

    faqContainer.getChildren().addAll(question4, answer4);

    // Set the dialog content
    faqDialog.getDialogPane().setContent(faqContainer);

    // Add OK button
    ButtonType okButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
    faqDialog.getDialogPane().getButtonTypes().add(okButtonType);

    faqDialog.showAndWait();
}

// Helper method to hide all answers except the current one
private void hideAllAnswers(VBox parentContainer, Label currentAnswer) {
    for (int i = 0; i < parentContainer.getChildren().size(); i++) {
        javafx.scene.Node node = parentContainer.getChildren().get(i);

        if (node instanceof Label && node != currentAnswer) {
            // Check if it's an answer by verifying the index is odd
            if (i % 2 != 0) {
                Label answerLabel = (Label) node;
                answerLabel.setVisible(false);
                answerLabel.setManaged(false);  // Does not take space when hidden
            }
        }
    }
}

// Guide Dialog
private void showGuideDialog() {
    Alert guideDialog = new Alert(Alert.AlertType.INFORMATION);
    guideDialog.setTitle("User Guide");
    guideDialog.setHeaderText("Guide");
    String guideContent = 
          "1. Step 1: Sign up for a new account.\n"
        + "2. Step 2: Log in with your email and password.\n"
        + "3. Step 3: Explore the dashboard for various features.\n";
    guideDialog.setContentText(guideContent);
    guideDialog.showAndWait();
}

// Contact Us Dialog
private void showContactDialog() {
    Alert contactDialog = new Alert(Alert.AlertType.INFORMATION);
    contactDialog.setTitle("Contact Us");
    contactDialog.setHeaderText("Get in Touch");
    String contactContent = 
          "You can contact us via:\n"
        + "Email: ninhash247help@gmail.com\n"
        + "Phone: +1-234-567-890\n"
        + "Working Hours: 9 AM - 5 PM (Mon - Fri)";
    contactDialog.setContentText(contactContent);
    contactDialog.showAndWait();
}


private int selectedRating = 0; // Variable to store the selected rating

private void showFeedbackDialog() {
    // Create a new dialog
    Dialog<Void> feedbackDialog = new Dialog<>();
    feedbackDialog.setTitle("Feedback");
    feedbackDialog.setHeaderText("We value your feedback");

    // Create a VBox to hold all elements
    VBox dialogPane = new VBox();
    dialogPane.setSpacing(10);
    
    // Create a TextArea for user feedback input
    TextArea feedbackArea = new TextArea();
    feedbackArea.setPromptText("Please enter your feedback here...");
    feedbackArea.setPrefSize(300, 100);
    //feedbackArea.setStyle("-fx-border-color: lightgray; -fx-padding: 0;"); // Custom styling
    dialogPane.getChildren().add(feedbackArea);

    // Create a star rating display
    HBox starRatingBox = new HBox();
    starRatingBox.setSpacing(5);
    for (int i = 1; i <= 5; i++) {
        Button starButton = new Button("☆"); // Default empty star
        starButton.setStyle("-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: gold;");
        int rating = i; // Store rating for lambda expression
        starButton.setOnAction(e -> {
            // Handle star rating selection, update all stars accordingly
            for (int j = 0; j < 5; j++) {
                Button btn = (Button) starRatingBox.getChildren().get(j);
                btn.setText(j < rating ? "★" : "☆"); // Fill stars up to the selected rating
            }
            selectedRating = rating; // Store selected rating
        });
        starRatingBox.getChildren().add(starButton);
    }
    dialogPane.getChildren().add(starRatingBox);

    // Create a label for displaying existing ratings and reviews
    Label existingReviewsLabel = new Label("Existing Ratings and Reviews:");
    existingReviewsLabel.setStyle("-fx-font-weight: bold;");
    dialogPane.getChildren().add(existingReviewsLabel);

    // Add existing ratings and reviews here
    TextArea existingReviewsArea = new TextArea();
    existingReviewsArea.setText("Rating: ★★★★☆\nReview: Great platform for managing digital identities!\n\n"
                                + "Rating: ★★★★★\nReview: Excellent user experience and security features.");
    existingReviewsArea.setEditable(false); // Make it read-only
    existingReviewsArea.setPrefSize(300, 100);
    existingReviewsArea.setStyle("-fx-background-color: #f4f4f4; -fx-text-fill: #333;"); // Custom styling
    dialogPane.getChildren().add(existingReviewsArea);

    // Set the dialog content
    feedbackDialog.getDialogPane().setContent(dialogPane);

    // Add OK button to submit feedback
    ButtonType okButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
    feedbackDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Handle submit button action
    /*feedbackDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            String feedbackText = feedbackArea.getText().trim();
            
            // Check if feedback and rating are provided
            if (feedbackText.isEmpty() || selectedRating == 0) {
                System.out.println("Please provide both feedback and a rating.");
            } else {
                // Process the submitted feedback and rating
                System.out.println("Feedback: " + feedbackText + ", Rating: " + selectedRating);
            }
        }
        return null;
    });*/
    feedbackDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            String feedbackText = feedbackArea.getText().trim();
    
            // Check if feedback and rating are provided
            if (feedbackText.isEmpty() || selectedRating == 0) {
                System.out.println("Please provide both feedback and a rating.");
            } else {
                // Save feedback and rating to the database
                handleFeedbackSubmit(feedbackText, selectedRating);
            }
        }
        return null;
    });
    
    existingReviewsArea.setText(fetchExistingFeedback());

    feedbackDialog.showAndWait();
}

private void handleFeedbackSubmit(String feedbackText, int rating) {
    String query = "INSERT INTO feedback (rating, feedback_text) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, rating);
        pstmt.setString(2, feedbackText);
        pstmt.executeUpdate();

        System.out.println("Feedback and rating successfully saved.");
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
/*private String fetchExistingFeedback() {
    StringBuilder feedbackData = new StringBuilder();
    String query = "SELECT rating, feedback_text FROM feedback ORDER BY created_at DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int rating = rs.getInt("rating");
            String feedback = rs.getString("feedback_text");
            feedbackData.append("Rating: ").append("★".repeat(rating))
                        .append("☆".repeat(5 - rating))
                        .append("\nReview: ").append(feedback).append("\n\n");
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return feedbackData.toString();
}*/
private String fetchExistingFeedback() {
    StringBuilder feedbackString = new StringBuilder();
    String query = "SELECT rating, feedback_text, created_at FROM feedback ORDER BY created_at DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int rating = rs.getInt("rating");
            String feedbackText = rs.getString("feedback_text");
            java.sql.Timestamp createdAt = rs.getTimestamp("created_at");

            // Convert rating into stars
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < rating; i++) {
                stars.append("★");
            }
            for (int i = rating; i < 5; i++) {
                stars.append("☆");
            }

            // Format each feedback entry
            feedbackString.append("Rating: ").append(stars).append("\n");
            feedbackString.append("Feedback: ").append(feedbackText).append("\n");
            feedbackString.append("Date: ").append(createdAt.toString()).append("\n\n");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return feedbackString.toString();
}



// About Dialog
private void showAboutDialog() {
    Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
    aboutDialog.setTitle("About NinHash");
    aboutDialog.setHeaderText("About Us");
    String aboutContent = 
          "NinHash is a secure digital identity management platform.\n"
        + "Our mission is to provide easy-to-use and secure solutions for managing online credentials.";
    aboutDialog.setContentText(aboutContent);
    aboutDialog.showAndWait();
}


    
 
    private StackPane createAnimatedBackground() {
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
        for (int i = 0; i < 25; i++) {
            Rectangle square = new Rectangle(30 + Math.random() * 70, 30 + Math.random() * 70, Color.rgb(0, 0, 255, 1)); // Random size, semi-transparent blue
            square.setTranslateX(-300 + i * 60); // Horizontal spacing with variation
            square.setTranslateY(600 + Math.random() * 300); // Start at the bottom with random offset
            square.setOpacity(0.8 + Math.random() * 0.7);
            square.setEffect(blurEffect);
            root.getChildren().add(square);

            // Animate each square to move upwards, rotate, and fade out
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

        return root;
    }

    private VBox createTransparentContainer(GridPane gridPane, int width, int height) {
        VBox transparentContainer = new VBox(gridPane);
        transparentContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);"
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #B3B3B3;");
        transparentContainer.setPadding(new Insets(20));
        transparentContainer.setPrefSize(width, height);
        transparentContainer.setMaxSize(width, height);
        transparentContainer.setMinSize(width, height);
        transparentContainer.setAlignment(Pos.CENTER);
        return transparentContainer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
