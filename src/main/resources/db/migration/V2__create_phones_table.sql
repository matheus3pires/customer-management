CREATE TABLE phones
(
    id        UUID PRIMARY KEY,
    number    VARCHAR(20) NOT NULL UNIQUE,
    client_id UUID     NOT NULL,
    CONSTRAINT fk_phones_clients FOREIGN KEY (client_id)
        REFERENCES clients (id) ON DELETE CASCADE
);