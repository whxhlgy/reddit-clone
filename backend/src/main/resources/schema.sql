drop table if exists community;

CREATE TABLE community (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(100),
    description VARCHAR(255)
);