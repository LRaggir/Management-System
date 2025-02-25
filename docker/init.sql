CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

INSERT INTO user (email, password, role)
VALUES ('admin@gmail.com', '1234', 'ADMIN')
ON DUPLICATE KEY UPDATE email=email;
