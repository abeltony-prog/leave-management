-- Drop existing tables if they exist
DROP TABLE IF EXISTS documents CASCADE;
DROP TABLE IF EXISTS leave_requests CASCADE;
DROP TABLE IF EXISTS leave_balances CASCADE;
DROP TABLE IF EXISTS leave_accrual_rules CASCADE;
DROP TABLE IF EXISTS leave_types CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

-- Create departments table
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    department_id INTEGER REFERENCES departments(id),
    manager_id INTEGER REFERENCES users(id),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create leave_types table
CREATE TABLE leave_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    max_days INTEGER NOT NULL,
    default_days INTEGER NOT NULL,
    accrual_rate DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create leave_balances table
CREATE TABLE leave_balances (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    leave_type_id INTEGER REFERENCES leave_types(id),
    total_days INTEGER NOT NULL,
    used_days INTEGER NOT NULL,
    remaining_days INTEGER NOT NULL,
    carried_forward_days INTEGER NOT NULL,
    year INTEGER NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    UNIQUE(user_id, leave_type_id, year)
);

-- Create leave_requests table
CREATE TABLE leave_requests (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    leave_type_id INTEGER REFERENCES leave_types(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason TEXT,
    approver_id INTEGER REFERENCES users(id),
    approver_comment TEXT,
    is_half_day BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create documents table
CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    leave_request_id INTEGER REFERENCES leave_requests(id),
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(50),
    uploaded_at TIMESTAMP NOT NULL
);

-- Create leave_accrual_rules table
CREATE TABLE leave_accrual_rules (
    id SERIAL PRIMARY KEY,
    leave_type_id INTEGER REFERENCES leave_types(id),
    accrual_frequency VARCHAR(50) NOT NULL,
    accrual_rate DOUBLE PRECISION NOT NULL,
    max_carry_forward INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
); 