package com.foxminded.university.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.foxminded.university.ConnectionProvider;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;

public class JdbcCourseDao implements CourseDao {

    static final String ADD_NEW_COURSE = "INSERT INTO courses (course_name, course_description) VALUES (?,?)";
    
    private ConnectionProvider connectionProvider;

    public JdbcCourseDao(ConnectionProvider dataConnection) {
        this.connectionProvider = dataConnection;
    }

    @Override
    public void create(Course course) {
        try (Connection connection = connectionProvider.getConnection();
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
