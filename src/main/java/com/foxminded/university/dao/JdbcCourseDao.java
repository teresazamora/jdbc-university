package com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.foxminded.university.DataBaseConnection;
import com.foxminded.university.model.Course;

public class JdbcCourseDao implements CourseDao {

    private DataBaseConnection dataConnection;

    public JdbcCourseDao(DataBaseConnection dataConnection) {
        this.dataConnection = dataConnection;
    }

    String ADD_NEW_COURSE = "INSERT INTO courses (course_name, course_description) VALUES (?,?)";

    @Override
    public void create(Course course) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_COURSE,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                course.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
