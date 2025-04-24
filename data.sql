-- Clean up existing data
TRUNCATE departments, users, leave_types, leave_balances, leave_requests, documents, leave_accrual_rules CASCADE;

-- Reset sequences
ALTER SEQUENCE departments_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE leave_types_id_seq RESTART WITH 1;
ALTER SEQUENCE leave_balances_id_seq RESTART WITH 1;
ALTER SEQUENCE leave_requests_id_seq RESTART WITH 1;
ALTER SEQUENCE documents_id_seq RESTART WITH 1;
ALTER SEQUENCE leave_accrual_rules_id_seq RESTART WITH 1;

-- Insert default departments
INSERT INTO departments (name, description, created_at, updated_at) VALUES
('Human Resources', 'HR Department', NOW(), NOW()),
('Information Technology', 'IT Department', NOW(), NOW()),
('Finance', 'Finance Department', NOW(), NOW()),
('Operations', 'Operations Department', NOW(), NOW()),
('Marketing', 'Marketing Department', NOW(), NOW()),
('Sales', 'Sales Department', NOW(), NOW());

-- Insert default leave types
INSERT INTO leave_types (name, description, max_days, default_days, accrual_rate, created_at, updated_at) VALUES
('Annual Leave', 'Regular annual leave', 21, 21, 1.75, NOW(), NOW()),
('Sick Leave', 'Leave for health reasons', 14, 14, 1.17, NOW(), NOW()),
('Maternity Leave', 'Leave for maternity', 90, 90, 0.0, NOW(), NOW()),
('Paternity Leave', 'Leave for paternity', 14, 14, 0.0, NOW(), NOW()),
('Study Leave', 'Leave for educational purposes', 10, 10, 0.83, NOW(), NOW()),
('Unpaid Leave', 'Leave without pay', 30, 0, 0.0, NOW(), NOW());

-- Insert default users (passwords are hashed 'password123')
INSERT INTO users (email, password, first_name, last_name, department_id, manager_id, role, created_at, updated_at) VALUES
('admin@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'Admin', 'User', 1, NULL, 'ADMIN', NOW(), NOW());

-- Insert HR Manager
INSERT INTO users (email, password, first_name, last_name, department_id, manager_id, role, created_at, updated_at) VALUES
('hr@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'HR', 'Manager', 1, 1, 'HR_MANAGER', NOW(), NOW());

-- Insert Department Managers
INSERT INTO users (email, password, first_name, last_name, department_id, manager_id, role, created_at, updated_at) VALUES
('it@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'IT', 'Manager', 2, 1, 'MANAGER', NOW(), NOW()),
('finance@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'Finance', 'Manager', 3, 1, 'MANAGER', NOW(), NOW());

-- Insert Employees
INSERT INTO users (email, password, first_name, last_name, department_id, manager_id, role, created_at, updated_at) VALUES
('employee1@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'John', 'Doe', 2, 3, 'EMPLOYEE', NOW(), NOW()),
('employee2@africahr.com', '$2a$10$X7J3Y5Z8.A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4', 'Jane', 'Smith', 3, 4, 'EMPLOYEE', NOW(), NOW());

-- Insert default leave balances
INSERT INTO leave_balances (user_id, leave_type_id, total_days, used_days, remaining_days, carried_forward_days, year, last_updated) VALUES
(5, 1, 21, 5, 16, 0, 2024, NOW()),
(5, 2, 14, 2, 12, 0, 2024, NOW()),
(6, 1, 21, 10, 11, 0, 2024, NOW()),
(6, 2, 14, 3, 11, 0, 2024, NOW()),
(5, 3, 90, 0, 90, 0, 2024, NOW()),
(6, 3, 90, 0, 90, 0, 2024, NOW());

-- Insert default leave requests
INSERT INTO leave_requests (user_id, leave_type_id, start_date, end_date, status, reason, approver_id, approver_comment, is_half_day, created_at, updated_at) VALUES
(5, 1, '2024-03-01', '2024-03-05', 'PENDING', 'Annual vacation', 3, NULL, false, NOW(), NOW()),
(5, 2, '2024-03-10', '2024-03-11', 'APPROVED', 'Sick leave', 3, 'Approved', false, NOW(), NOW()),
(6, 1, '2024-03-15', '2024-03-20', 'APPROVED', 'Family vacation', 4, 'Approved', false, NOW(), NOW()),
(6, 2, '2024-03-25', '2024-03-26', 'REJECTED', 'Medical appointment', 4, 'Need more details', false, NOW(), NOW()),
(5, 3, '2024-04-01', '2024-06-30', 'PENDING', 'Maternity leave', 3, NULL, false, NOW(), NOW()),
(6, 4, '2024-04-01', '2024-04-14', 'PENDING', 'Paternity leave', 4, NULL, false, NOW(), NOW());

-- Insert default documents
INSERT INTO documents (leave_request_id, file_name, file_path, file_type, uploaded_at) VALUES
(1, 'vacation_plan.pdf', '/uploads/vacation_plan.pdf', 'application/pdf', NOW()),
(2, 'medical_certificate.pdf', '/uploads/medical_certificate.pdf', 'application/pdf', NOW()),
(3, 'flight_tickets.pdf', '/uploads/flight_tickets.pdf', 'application/pdf', NOW()),
(4, 'medical_appointment.pdf', '/uploads/medical_appointment.pdf', 'application/pdf', NOW()),
(5, 'maternity_plan.pdf', '/uploads/maternity_plan.pdf', 'application/pdf', NOW()),
(6, 'paternity_plan.pdf', '/uploads/paternity_plan.pdf', 'application/pdf', NOW());

-- Insert default leave accrual rules
INSERT INTO leave_accrual_rules (leave_type_id, accrual_frequency, accrual_rate, max_carry_forward, created_at, updated_at) VALUES
(1, 'MONTHLY', 1.75, 5, NOW(), NOW()),
(2, 'MONTHLY', 1.17, 0, NOW(), NOW()),
(3, 'YEARLY', 90.0, 0, NOW(), NOW()),
(4, 'YEARLY', 14.0, 0, NOW(), NOW()),
(5, 'MONTHLY', 0.83, 0, NOW(), NOW()),
(6, 'NONE', 0.0, 0, NOW(), NOW()); 