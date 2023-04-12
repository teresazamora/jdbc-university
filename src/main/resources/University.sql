DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_courses CASCADE;

CREATE TABLE groups
(
group_id SERIAL PRIMARY KEY,
group_name VARCHAR (30) NOT NULL
);

CREATE TABLE students
(
student_id SERIAL UNIQUE,
group_id INTEGER REFERENCES groups(group_id) ON UPDATE CASCADE,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL
);

CREATE TABLE courses
(
course_id SERIAL UNIQUE,
course_name VARCHAR(30) NOT NULL,
course_description VARCHAR(50) NOT NULL
);

CREATE TABLE students_courses
(
student_id INTEGER,
course_id INTEGER,
UNIQUE (student_id,course_id)
);