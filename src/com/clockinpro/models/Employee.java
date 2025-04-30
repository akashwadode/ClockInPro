package com.clockinpro.models;

import java.time.LocalDateTime;

public class Employee {
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;
    private double hourlyRate;

    public Employee(int id, String username, String password, boolean isAdmin, double hourlyRate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.hourlyRate = hourlyRate;
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

    public double getHourlyRate() {
        return hourlyRate;
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

    // Payroll Record inner class
    public static class PayrollRecord {
        private int id;
        private int employeeId;
        private int timeRecordId;
        private double hoursWorked;
        private double amountPaid;

        public PayrollRecord(int id, int employeeId, int timeRecordId, double hoursWorked, double amountPaid) {
            this.id = id;
            this.employeeId = employeeId;
            this.timeRecordId = timeRecordId;
            this.hoursWorked = hoursWorked;
            this.amountPaid = amountPaid;
        }

        public int getId() {
            return id;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public int getTimeRecordId() {
            return timeRecordId;
        }

        public double getHoursWorked() {
            return hoursWorked;
        }

        public double getAmountPaid() {
            return amountPaid;
        }
    }
}
