package com.foxminded.university.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.ConnectionProvider;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;

public class JdbcStudentDao implements StudentDao {

    final String ADD_NEW_STUDENTS = "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?)";
    final String DELETE_STUDENT_FROM_COURSE = "DELETE FROM students_courses WHERE student_id = ? and course_id = ?";
    final String DELETE_STUDENT_BY_ID = "DELETE FROM students WHERE student_id = ?";
    final String FIND_STUDENTS_BY_COURSE = "SELECT students.student_id, students.group_id, students.first_name, students.last_name "
            + "FROM students INNER JOIN students_courses ON students.student_id = students_courses.student_id "
            + "INNER JOIN courses ON students_courses.course_id = courses.course_id "
            + "WHERE courses.course_name = ?";
    final String ADD_STUDENT_TO_COURSE = "INSERT INTO students_courses (student_id, course_id) VALUES(?,?)";
    private ConnectionProvider dataConnection;

    public JdbcStudentDao(ConnectionProvider dataConnection) {
        this.dataConnection = dataConnection;
    }

    @Override
    public void create(Student student) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_STUDENTS,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
            try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                generateKeys.next();
                student.setStudentId(generateKeys.getInt(1));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_STUDENTS_BY_COURSE)) {
            preparedStatement.setString(1, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    students.add(new Student(resultSet.getInt("student_id"), resultSet.getInt("group_id"),
                            resultSet.getString("first_name"), resultSet.getString("last_name")));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return students;
    }

    @Override
    public void delete(int id) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteFromCourse(int studentId, int courseId) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_FROM_COURSE)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToCourse(int studentId, int courseId) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_STUDENT_TO_COURSE)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

}
