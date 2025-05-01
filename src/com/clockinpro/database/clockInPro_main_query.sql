-- Create the database
CREATE DATABASE IF NOT EXISTS clockinpro_db;
USE clockinpro_db;

-- Create employees table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Create time_records table
CREATE TABLE time_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    clock_in DATETIME,
    clock_out DATETIME,
    hours_worked DOUBLE,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Create payroll table
CREATE TABLE payroll (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    time_record_id INT,
    hours_worked DOUBLE,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (time_record_id) REFERENCES time_records(id)
);

-- Add is_admin column to employees
ALTER TABLE employees ADD is_admin BOOLEAN DEFAULT FALSE;

-- Insert sample data into employees
INSERT INTO employees (username, password) VALUES
('john_doe', '123'),
('jane_smith', '456'),
('mike_brown', '789');

-- Insert admin user
INSERT INTO employees (username, password, is_admin) VALUES
('john_doe', '123', False),
('jane_smith', '456', False),
('mike_brown', '789', False)
('admin_user', '123', TRUE);

-- Insert sample data into time_records
INSERT INTO time_records (employee_id, clock_in, clock_out, hours_worked) VALUES
(1, '2025-04-29 09:00:00', '2025-04-29 17:00:00', 8.0), -- John Doe, 8 hours
(1, '2025-04-30 08:30:00', '2025-04-30 16:30:00', 8.0), -- John Doe, 8 hours
(2, '2025-04-29 10:00:00', '2025-04-29 15:00:00', 5.0), -- Jane Smith, 5 hours
(2, '2025-04-30 09:15:00', '2025-04-30 17:45:00', 8.5), -- Jane Smith, 8.5 hours
(3, '2025-04-29 08:00:00', '2025-04-29 16:00:00', 8.0), -- Mike Brown, 8 hours
(3, '2025-04-30 07:45:00', '2025-04-30 15:45:00', 8.0); -- Mike Brown, 8 hours

-- Insert sample data into payroll
INSERT INTO payroll (employee_id, time_record_id, hours_worked) VALUES
(1, 1, 8.0), -- John Doe, time record 1
(1, 2, 8.0), -- John Doe, time record 2
(2, 3, 5.0), -- Jane Smith, time record 3
(2, 4, 8.5), -- Jane Smith, time record 4
(3, 5, 8.0), -- Mike Brown, time record 5
(3, 6, 8.0); -- Mike Brown, time record 6