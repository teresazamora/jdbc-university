package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Course;

public class JdbcCourseDaoTest {
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();

    ConnectionProvider connectionProvider;
    JdbcCourseDao courseDao;
    private IDatabaseTester databaseTester;

    public JdbcCourseDaoTest() throws Exception {
        this.connectionProvider = new ConnectionProvider("application.properties");
        this.courseDao = new JdbcCourseDao(connectionProvider);
    }

    @BeforeAll
    public static void createSchema() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("schema.sql");
        File file = new File(url.toURI());
        ConnectionProvider connectionProvider = new ConnectionProvider("application.properties");
        RunScript.execute(connectionProvider.getConnection(), new FileReader(file));
    }

    @BeforeEach
    void init() throws Exception {
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, connectionProvider.getConnection().getMetaData().getURL());
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(readDataSet());
        databaseTester.onSetup();
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

    private IDataSet readDataSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = classLoader.getResource("testdata.xml").getFile();
        return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }

}
