package com.clockinpro.models;

import java.time.LocalDateTime;

public class Employee {
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

    public Employee(int id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    // Time Record inner class
    public static class TimeRecord {
        private int employeeId;
        private LocalDateTime clockIn;
        private LocalDateTime clockOut;
        private double hoursWorked;

        public TimeRecord(int employeeId, LocalDateTime clockIn, LocalDateTime clockOut) {
            this.employeeId = employeeId;
            this.clockIn = clockIn;
            this.clockOut = clockOut;
            this.hoursWorked = calculateHoursWorked();
        }

        private double calculateHoursWorked() {
            if (clockIn != null && clockOut != null) {
                return java.time.Duration.between(clockIn, clockOut).toHours();
            }
            return 0.0;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public LocalDateTime getClockIn() {
            return clockIn;
        }

        public LocalDateTime getClockOut() {
            return clockOut;
        }

        public double getHoursWorked() {
            return hoursWorked;
        }
    }
}
