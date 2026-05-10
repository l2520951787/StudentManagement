CREATE TABLE IF NOT EXISTS students
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    ruby VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    mail_address VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    gender VARCHAR(50),
    remark TEXT,
    deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course VARCHAR(50) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);
