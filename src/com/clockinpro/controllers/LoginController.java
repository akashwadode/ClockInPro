package com.clockinpro.controllers;

import com.clockinpro.database.DBUtil;
import com.clockinpro.models.Employee;
import com.clockinpro.utils.AlertUtil;
import javafx.scene.control.TextField;

public class LoginController {

    private Employee loggedInEmployee;

    public boolean authenticate(TextField usernameField, TextField passwordField) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Login Error", "Username and password cannot be empty.");
            return false;
        }

        loggedInEmployee = DBUtil.validateLogin(username, password);
        if (loggedInEmployee != null) {
            return true;
        } else {
            AlertUtil.showError("Login Error", "Invalid username or password.");
            return false;
        }
    }

    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public void clearLoggedInEmployee() {
        loggedInEmployee = null;
    }
}
