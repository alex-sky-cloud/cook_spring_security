CREATE USER keycloak WITH PASSWORD 'kc';

GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak;

DROP TABLE IF EXISTS Member;

CREATE TABLE Member (
                        id VARCHAR(50) NOT NULL PRIMARY KEY,
                        username VARCHAR(50) NOT NULL,
                        email VARCHAR(50) NOT NULL,
                        createdAt TIMESTAMP DEFAULT NOW()
);

DROP TABLE IF EXISTS Post;

CREATE TABLE Post (
                      id SERIAL NOT NULL PRIMARY KEY,
                      title VARCHAR(50) NOT NULL,
                      scientificName VARCHAR(50) NOT NULL,
                      family VARCHAR(32) NOT NULL,
                      rating NUMERIC(3,2) DEFAULT 0.5,
                      authorId VARCHAR(50),
                      createdAt TIMESTAMP DEFAULT NOW(),
                      CONSTRAINT fk_member FOREIGN KEY(authorId) REFERENCES Member(id)
                          ON DELETE SET NULL ON UPDATE CASCADE
);