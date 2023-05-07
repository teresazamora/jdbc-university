package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.foxminded.university.ConnectionProvider;
import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Course;

public class JdbcCourseDaoTest {
    
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private ConnectionProvider connectionProvider;
    private JdbcCourseDao courseDao;
    private IDatabaseTester databaseTester;

    public JdbcCourseDaoTest() throws Exception {
        this.connectionProvider = new ConnectionProvider("application.properties");
        this.courseDao = new JdbcCourseDao(connectionProvider);
        this.databaseTester = new JdbcDatabaseTester(JDBC_DRIVER,
                connectionProvider.getConnection().getMetaData().getURL());
    }

    @BeforeAll
    public static void beforeAll() throws Exception {
        ConnectionForTest connectionForTest = new ConnectionForTest();
        connectionForTest.createSchema();
        connectionForTest.init();
    }
   
    @Test
    void givenNewCourse_whenCreate_thenNewCourse() throws Exception {
        Course expected = new Course("Art", "Science of beautifull things");
        courseDao.create(expected);
        Course actual = mapToCourse(
                "select course_id, course_name, course_description from courses where course_name = 'Art';");
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }

    private Course mapToCourse(String sqlQuery) throws Exception {
        ITable itable = databaseTester.getConnection().createQueryTable("courses", sqlQuery);
        return new Course(Integer.valueOf(itable.getValue(0, "course_id").toString()),
                itable.getValue(0, "course_name").toString(), itable.getValue(0, "course_description").toString());
    }
}
