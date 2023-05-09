package com.foxminded.university;

import java.util.List;
import java.util.Scanner;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public class Menu {

    private JdbcGroupDao jdbcGroup;
    private JdbcStudentDao jdbcStudent;

    static final String START_MENU = "a. Find all groups with less or equals student count \n"
            + "b. Find all students related to course with given name \n" 
            + "c. Add new student \n"
            + "d. Delete student by STUDENT_ID \n" 
            + "e. Add a student to the course (from a list) \n"
            + "f. Remove the student from one of his or her courses \n";

    Scanner scanner = new Scanner(System.in);

    public Menu(JdbcGroupDao jdbcGroup, JdbcStudentDao jdbcStudent) {
        this.jdbcGroup = jdbcGroup;
        this.jdbcStudent = jdbcStudent;
    }

    public void getStart(List<Student> students, List<Course> courses, List<Group> groups) {
        System.out.println(START_MENU);
        System.out.println("Please, make your choice");
        String responce = scanner.nextLine();
        if (responce.equals("a")) {
            getGroup();
        } else if (responce.equals("b")) {
            getStudentsByCourse(courses);
        } else if (responce.equals("c")) {
            addStudent(groups);
        } else if (responce.equals("d")) {
            deleteStudent(students);
        } else if (responce.equals("e")) {
            addStudentToCourse(courses, students);
        } else if (responce.equals("f")) {
            deleteStudentFromCourse(courses, students);
        }
    }

    private void getGroup() {
        System.out.println("Please, insert your number: ");
        int number = scanner.nextInt();
        System.out.println(jdbcGroup.findByStudentsAmount(number));
    }

    private void getStudentsByCourse(List<Course> courses) {
        System.out.println("Please, choose course's name from a list: ");
        courses.forEach(System.out::println);
        String courseName = scanner.nextLine();
        System.out.println(jdbcStudent.findByCourseName(courseName));
    }

    private void addStudent(List<Group> groups) {
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
    }

    private void deleteStudent(List<Student> students) {
        System.out.println(students);
        System.out.println("Please, choose id of student to delete: ");
        int id = scanner.nextInt();
        jdbcStudent.delete(id);
        System.out.println("Student deleted");
    }

    private void addStudentToCourse(List<Course> courses, List<Student> students) {
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
    }
    
    private void deleteStudentFromCourse(List<Course> courses, List<Student> students) {
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
