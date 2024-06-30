create table employees
(
    id  int auto_increment primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    department varchar(255) null,
    salary     int null
);

INSERT INTO employees (first_name, last_name, department, salary)

SELECT * FROM notes;
DROP TABLE notes;

CREATE TABLE notes (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT,
                       created_at TEXT
);