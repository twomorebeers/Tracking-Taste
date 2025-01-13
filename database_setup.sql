-- Create user roles table
CREATE TABLE user_roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INTEGER REFERENCES user_roles(role_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create food categories table
CREATE TABLE food_categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create food items table
CREATE TABLE food_items (
    food_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories DECIMAL(10,2) NOT NULL,
    protein DECIMAL(10,2) NOT NULL,
    carbs DECIMAL(10,2) NOT NULL,
    fats DECIMAL(10,2) NOT NULL,
    category_id INTEGER REFERENCES food_categories(category_id),
    created_by INTEGER REFERENCES users(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user food logs table
CREATE TABLE user_food_logs (
    log_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    food_id INTEGER REFERENCES food_items(food_id),
    portion_size DECIMAL(10,2) DEFAULT 1.0,
    consumed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO user_roles (role_name) VALUES
    ('ADMIN'),
    ('USER'),
    ('NUTRITIONIST');

-- Insert default food categories
INSERT INTO food_categories (category_name) VALUES
    ('FRUITS'),
    ('VEGETABLES'),
    ('PROTEINS'),
    ('GRAINS'),
    ('DAIRY'),
    ('OTHER');
