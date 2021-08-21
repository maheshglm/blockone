CREATE DATABASE IF NOT EXISTS studentdb;

USE studentdb;

CREATE TABLE IF NOT EXISTS student (
    id bigint PRIMARY KEY,
    firstname varchar(255) NOT NULL,
    lastname varchar(255),
    class varchar(255),
    nationality varchar(255)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

COMMIT;
