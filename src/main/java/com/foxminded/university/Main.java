package com.foxminded.university;

import java.util.List;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
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

        GroupDao groupDao = new JdbcGroupDao(connectionProvider);
        StudentDao studentDao = new JdbcStudentDao(connectionProvider);
        CourseDao courseDao = new JdbcCourseDao(connectionProvider);

        for (Group group : groups) {
            groupDao.create(group);
        }

        generator.assignStudentsToGroups(students, groups, 10, 30);

        for (Student student : students) {
            studentDao.create(student);
        }

        for (Course course : courses) {
            courseDao.create(course);
        }

        generator.assignCoursesToStudent(students, courses, 3, 1);

        for (Student student : students) {
            for (Course course : student.getCourses()) {
                studentDao.addToCourse(student.getId(), course.getId());
            }
        }

        Menu menu = new Menu(groupDao, studentDao);
        menu.getStart(students, courses, groups);
    }
}
