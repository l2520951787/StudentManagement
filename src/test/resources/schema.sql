CREATE TABLE IF NOT EXISTS students
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    ruby VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    mail_address VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    remark TEXT,
    is_deleted boolean DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course VARCHAR(50) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course_status
(
    id int AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    status VARCHAR(50) NOT NULL
);