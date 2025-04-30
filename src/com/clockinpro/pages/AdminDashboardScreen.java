package com.clockinpro.pages;

import com.clockinpro.database.DBUtil;
import com.clockinpro.models.Employee;
import com.clockinpro.models.Employee.TimeRecord;
import com.clockinpro.models.Employee.PayrollRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AdminDashboardScreen {

    private BorderPane view;
    private TableView<Employee> employeeTable;
    private TableView<TimeRecord> timeRecordTable;
    private TableView<PayrollRecord> payrollTable;
    private Stage primaryStage;

    public AdminDashboardScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        view = new BorderPane();
        initializeUI();
    }

    private void initializeUI() {
        // Employee Table
        employeeTable = new TableView<>();
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Employee, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Employee, Double> hourlyRateCol = new TableColumn<>("Hourly Rate");
        hourlyRateCol.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        employeeTable.getColumns().addAll(idCol, usernameCol, hourlyRateCol);

        // Load employees (excludes admins)
        employeeTable.setItems(FXCollections.observableArrayList(DBUtil.getAllEmployees()));

        // Time Record Table
        timeRecordTable = new TableView<>();
        TableColumn<TimeRecord, Integer> empIdCol = new TableColumn<>("Employee ID");
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        TableColumn<TimeRecord, LocalDateTime> clockInCol = new TableColumn<>("Clock-In");
        clockInCol.setCellValueFactory(new PropertyValueFactory<>("clockIn"));
        TableColumn<TimeRecord, LocalDateTime> clockOutCol = new TableColumn<>("Clock-Out");
        clockOutCol.setCellValueFactory(new PropertyValueFactory<>("clockOut"));
        TableColumn<TimeRecord, Double> hoursCol = new TableColumn<>("Hours Worked");
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        timeRecordTable.getColumns().addAll(empIdCol, clockInCol, clockOutCol, hoursCol);

        // Payroll Table
        payrollTable = new TableView<>();
        TableColumn<PayrollRecord, Integer> payrollIdCol = new TableColumn<>("Payroll ID");
        payrollIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<PayrollRecord, Integer> timeRecordIdCol = new TableColumn<>("Time Record ID");
        timeRecordIdCol.setCellValueFactory(new PropertyValueFactory<>("timeRecordId"));
        TableColumn<PayrollRecord, Double> payrollHoursCol = new TableColumn<>("Hours Worked");
        payrollHoursCol.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        TableColumn<PayrollRecord, Double> amountCol = new TableColumn<>("Amount Paid");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        payrollTable.getColumns().addAll(payrollIdCol, timeRecordIdCol, payrollHoursCol, amountCol);

        // Update time and payroll records when an employee is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                timeRecordTable.setItems(FXCollections.observableArrayList(DBUtil.getTimeRecords(newSelection.getId())));
                payrollTable.setItems(FXCollections.observableArrayList(DBUtil.getPayrollRecords(newSelection.getId())));
            } else {
                timeRecordTable.setItems(FXCollections.observableArrayList());
                payrollTable.setItems(FXCollections.observableArrayList());
            }
        });

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("primary-button");
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(primaryStage);
            Scene scene = new Scene(loginScreen.getView(), 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            primaryStage.setTitle("ClockInPro - Login");
            primaryStage.setScene(scene);
        });

        HBox buttonBox = new HBox(10, logoutButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // Layout
        VBox employeeBox = new VBox(10, new Label("All Employees"), employeeTable);
        employeeBox.setPadding(new Insets(20));
        VBox timeRecordBox = new VBox(10, new Label("Selected Employee Time Records"), timeRecordTable);
        timeRecordBox.setPadding(new Insets(20));
        VBox payrollBox = new VBox(10, new Label("Selected Employee Payroll Records"), payrollTable);
        payrollBox.setPadding(new Insets(20));

        view.setTop(buttonBox);
        view.setLeft(employeeBox);
        view.setCenter(timeRecordBox);
        view.setRight(payrollBox);
        view.setAlignment(view, Pos.CENTER);
    }

    public BorderPane getView() {
        return view;
    }
}
