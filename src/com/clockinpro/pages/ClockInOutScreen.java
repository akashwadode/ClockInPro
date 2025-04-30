package com.clockinpro.pages;

import com.clockinpro.database.DBUtil;
import com.clockinpro.models.Employee;
import com.clockinpro.models.Employee.TimeRecord;
import com.clockinpro.models.Employee.PayrollRecord;
import com.clockinpro.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClockInOutScreen {

    private BorderPane view;
    private Employee employee;
    private TableView<TimeRecord> timeTable;
    private TableView<PayrollRecord> payrollTable;

    public ClockInOutScreen(Employee employee) {
        this.employee = employee;
        view = new BorderPane();
        initializeUI();
    }

    private void initializeUI() {
        // Form for clock-in/out
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Label clockInLabel = new Label("Clock-In Time (HH:mm):");
        TextField clockInField = new TextField();
        clockInField.setPromptText("e.g., 09:00");
        Label clockOutLabel = new Label("Clock-Out Time (HH:mm):");
        TextField clockOutField = new TextField();
        clockOutField.setPromptText("e.g., 17:00");

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("primary-button");

        submitButton.setOnAction(e -> {
            try {
                // Parse inputs
                LocalDate date = datePicker.getValue();
                String clockInStr = clockInField.getText();
                String clockOutStr = clockOutField.getText();

                if (date == null || clockInStr.isEmpty() || clockOutStr.isEmpty()) {
                    AlertUtil.showError("Input Error", "Please fill all fields.");
                    return;
                }

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime clockIn = LocalDateTime.of(date, LocalTime.parse(clockInStr, timeFormatter));
                LocalDateTime clockOut = LocalDateTime.of(date, LocalTime.parse(clockOutStr, timeFormatter));

                if (clockOut.isBefore(clockIn)) {
                    AlertUtil.showError("Input Error", "Clock-out time must be after clock-in time.");
                    return;
                }

                // Save time record and payroll
                TimeRecord record = new TimeRecord(employee.getId(), clockIn, clockOut);
                DBUtil.saveTimeRecord(record, employee.getHourlyRate());
                AlertUtil.showInfo("Success", "Time record saved successfully!");
                clockInField.clear();
                clockOutField.clear();
                refreshTables();
            } catch (Exception ex) {
                AlertUtil.showError("Error", "Invalid time format or database error: " + ex.getMessage());
            }
        });

        VBox formBox = new VBox(10, dateLabel, datePicker, clockInLabel, clockInField, clockOutLabel, clockOutField, submitButton);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        // Time Record Table
        timeTable = new TableView<>();
        TableColumn<TimeRecord, LocalDateTime> clockInCol = new TableColumn<>("Clock-In");
        clockInCol.setCellValueFactory(new PropertyValueFactory<>("clockIn"));
        TableColumn<TimeRecord, LocalDateTime> clockOutCol = new TableColumn<>("Clock-Out");
        clockOutCol.setCellValueFactory(new PropertyValueFactory<>("clockOut"));
        TableColumn<TimeRecord, Double> hoursCol = new TableColumn<>("Hours Worked");
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        timeTable.getColumns().addAll(clockInCol, clockOutCol, hoursCol);

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

        // Load initial data
        refreshTables();

        VBox timeBox = new VBox(10, new Label("Your Time Records"), timeTable);
        timeBox.setPadding(new Insets(20));
        VBox payrollBox = new VBox(10, new Label("Your Payroll Records"), payrollTable);
        payrollBox.setPadding(new Insets(20));

        // Layout
        view.setTop(formBox);
        view.setLeft(timeBox);
        view.setCenter(payrollBox);
    }

    private void refreshTables() {
        List<TimeRecord> timeRecords = DBUtil.getTimeRecords(employee.getId());
        timeTable.setItems(FXCollections.observableArrayList(timeRecords));
        List<PayrollRecord> payrollRecords = DBUtil.getPayrollRecords(employee.getId());
        payrollTable.setItems(FXCollections.observableArrayList(payrollRecords));
    }

    public BorderPane getView() {
        return view;
    }
}
