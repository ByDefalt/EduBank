CREATE TABLE offer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    picture_path VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    state VARCHAR(10) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

