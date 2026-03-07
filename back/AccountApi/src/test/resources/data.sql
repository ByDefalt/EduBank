INSERT INTO Role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'CUSTOMER');

INSERT INTO PersonalInformation (firstname, lastname, email, address, phone_number) VALUES
    ('John', 'Doe', 'john.doe@localhost.com', '123 Main St', '0644054058');

INSERT INTO Account (id, personal_info_id, role_id, password, state) VALUES
    ('admin', 1, 1, 'admin', 'ACTIVE');