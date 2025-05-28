CREATE TABLE addresses
(
    id         UUID PRIMARY KEY,
    street     VARCHAR(255) NOT NULL,
    complement VARCHAR(255),
    city       VARCHAR(100) NOT NULL,
    state      VARCHAR(100) NOT NULL,
    zip_code   VARCHAR(10)  NOT NULL,
    client_id  UUID      NOT NULL,
    CONSTRAINT fk_addresses_clients FOREIGN KEY (client_id)
        REFERENCES clients (id) ON DELETE CASCADE
);