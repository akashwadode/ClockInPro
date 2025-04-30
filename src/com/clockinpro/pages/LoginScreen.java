package com.clockinpro.pages;

import com.clockinpro.controllers.LoginController;
import com.clockinpro.utils.AlertUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class LoginScreen {

    private BorderPane view;
    private LoginController controller;
    private Stage primaryStage;

    public LoginScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        controller = new LoginController();
        view = new BorderPane();
        initializeUI();
    }

    private void initializeUI() {
        // Logo
        ImageView logoView = null;
        try {
            InputStream imageStream = getClass().getResourceAsStream("/images/logo.png");
            if (imageStream == null) {
                System.err.println("Warning: logo.png not found in resources/images/");
            } else {
                Image logoImage = new Image(imageStream);
                logoView = new ImageView(logoImage);
                logoView.setFitWidth(150);
                logoView.setPreserveRatio(true);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }

        // Form
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");

        loginButton.setOnAction(e -> {
            if (controller.authenticate(usernameField, passwordField)) {
                AlertUtil.showInfo("Success", "Login successful!");
                // Navigate based on user role
                Scene scene;
                if (controller.getLoggedInEmployee().isAdmin()) {
                    AdminDashboardScreen adminScreen = new AdminDashboardScreen(primaryStage);
                    scene = new Scene(adminScreen.getView(), 1000, 600);
                    primaryStage.setTitle("ClockInPro - Admin Dashboard");
                } else {
                    ClockInOutScreen clockInOutScreen = new ClockInOutScreen(controller.getLoggedInEmployee(), primaryStage);
                    scene = new Scene(clockInOutScreen.getView(), 800, 600);
                    primaryStage.setTitle("ClockInPro - Clock In/Out");
                }
                scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
                primaryStage.setScene(scene);
            }
        });

        VBox formBox = new VBox(10);
        if (logoView != null) {
            formBox.getChildren().add(logoView);
        }
        formBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        view.setCenter(formBox);
    }

    public BorderPane getView() {
        return view;
    }
}
