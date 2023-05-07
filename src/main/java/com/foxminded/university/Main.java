package com.foxminded.university;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String file = "schema.sql";
        String fileProperties = "configuration.properties";
        ConnectionProvider connectionProvider = new ConnectionProvider(fileProperties);
        TableCreator.createTable(file, connectionProvider);

        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.generateStudents(200);
        List<Group> groups = generator.generateGroups(10);
        List<Course> courses = generator.generateCourses();
        Map<Student, List<Course>> studentsCourses = generator.assignCoursesToStudent(students, courses);

        JdbcGroupDao jdbcGroup = new JdbcGroupDao(connectionProvider);
        JdbcStudentDao jdbcStudent = new JdbcStudentDao(connectionProvider);
        JdbcCourseDao jdbcCourse = new JdbcCourseDao(connectionProvider);

        for (Group group : groups) {
            jdbcGroup.create(group);
        }

        generator.assignStudentsToGroups(students, groups, 10, 30);

        for (Student student : students) {
            jdbcStudent.create(student);
        }

        for (Course course : courses) {
            jdbcCourse.create(course);
        }

        for (Map.Entry<Student, List<Course>> entry : studentsCourses.entrySet()) {
            Student student = entry.getKey();
            List<Course> entry2 = entry.getValue();
            for (Course course : entry2) {
                jdbcStudent.addToCourse(student.getStudentId(), course.getId());

            }
        }

        Menu menu = new Menu(jdbcGroup, jdbcStudent);
        menu.getStart(students, courses, groups);
    }
}
