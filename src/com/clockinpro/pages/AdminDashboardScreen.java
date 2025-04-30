package com.clockinpro.pages;

import com.clockinpro.database.DBUtil;
import com.clockinpro.models.Employee;
import com.clockinpro.models.Employee.TimeRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class AdminDashboardScreen {

    private BorderPane view;
    private TableView<Employee> employeeTable;
    private TableView<TimeRecord> timeRecordTable;

    public AdminDashboardScreen() {
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
        TableColumn<Employee, Boolean> adminCol = new TableColumn<>("Is Admin");
        adminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));
        employeeTable.getColumns().addAll(idCol, usernameCol, adminCol);

        // Load employees
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

        // Update time records when an employee is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                timeRecordTable.setItems(FXCollections.observableArrayList(DBUtil.getTimeRecords(newSelection.getId())));
            } else {
                timeRecordTable.setItems(FXCollections.observableArrayList());
            }
        });

        // Layout
        VBox employeeBox = new VBox(10, new Label("All Employees"), employeeTable);
        employeeBox.setPadding(new Insets(20));
        VBox timeRecordBox = new VBox(10, new Label("Selected Employee Time Records"), timeRecordTable);
        timeRecordBox.setPadding(new Insets(20));

        view.setLeft(employeeBox);
        view.setCenter(timeRecordBox);
        view.setAlignment(view, Pos.CENTER);
    }

    public BorderPane getView() {
        return view;
    }
}
