CREATE TABLE roles
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    role_name VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);