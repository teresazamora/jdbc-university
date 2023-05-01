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
        ConnectionProvider dataConnection = new ConnectionProvider(fileProperties);
        TableCreator.createTable(file, dataConnection);
        String startMenu = "a. Find all groups with less or equals student count \n"
                + "b. Find all students related to course with given name \n" + "c. Add new student \n"
                + "d. Delete student by STUDENT_ID \n" + "e. Add a student to the course (from a list) \n"
                + "f. Remove the student from one of his or her courses \n";
        System.out.println(startMenu);

        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.generateStudents(200);
        List<Group> groups = generator.generateGroups();
        List<Course> courses = generator.generateCourses();
        Map<Student, List<Course>> studentsCourses = generator.assignCoursesToStudent(students, courses);

        JdbcGroupDao jdbcGroup = new JdbcGroupDao(dataConnection);
        JdbcStudentDao jdbcStudent = new JdbcStudentDao(dataConnection);
        JdbcCourseDao jdbcCourse = new JdbcCourseDao(dataConnection);

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

        System.out.println("Please, make your choice: ");
        Scanner scanner = new Scanner(System.in);
        String responce = scanner.nextLine();

        if (responce.equals("a")) {
            System.out.println("Please, insert your number: ");
            int number = scanner.nextInt();
            System.out.println(jdbcGroup.findGroupByAmountStudent(number));

        } else if (responce.equals("b")) {
            System.out.println("Please, choose course's name from a list: ");
            courses.forEach(System.out::println);
            String courseName = scanner.nextLine();
            System.out.println(jdbcStudent.findByCourseName(courseName));

        } else if (responce.equals("c")) {
            System.out.println("Please, choose group from a list: ");
            System.out.println(groups);
            int groupId = scanner.nextInt();
            System.out.println("Please, enter student's name: ");
            scanner.nextLine();
            String name = scanner.nextLine();
            System.out.println("Please, enter student's last name: ");
            String surname = scanner.nextLine();
            jdbcStudent.create(new Student(groupId, name, surname));
            System.out.println("Student created");

        } else if (responce.equals("d")) {
            System.out.println(students);
            System.out.println("Please, choose id of student to delete: ");
            int id = scanner.nextInt();
            jdbcStudent.delete(id);
            System.out.println("Student deleted");

        } else if (responce.equals("e")) {
            courses.forEach(System.out::println);
            System.out.println("____________________");
            System.out.println("Please, choose course id: ");
            int courseId = scanner.nextInt();
            students.forEach(System.out::println);
            System.out.println("____________________");
            System.out.println("Please, choose student id: ");
            int studentId = scanner.nextInt();
            jdbcStudent.addToCourse(studentId, courseId);
            System.out.println("Done");

        } else if (responce.equals("f")) {
            courses.forEach(System.out::println);
            System.out.println("____________________");
            System.out.println("Please, choose course id: ");
            int courseId = scanner.nextInt();
            students.forEach(System.out::println);
            System.out.println("____________________");
            System.out.println("Please, choose student id: ");
            int studentId = scanner.nextInt();
            jdbcStudent.deleteFromCourse(studentId, courseId);
            System.out.println("Done");
        }

    }

}
