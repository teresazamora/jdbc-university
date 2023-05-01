package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Student;

public class JdbcStudentDaoTest {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();

    ConnectionProvider connectionProvider;
    JdbcStudentDao studentDao;
    private IDatabaseTester databaseTester;

    public JdbcStudentDaoTest() throws SQLException {
        this.connectionProvider = new ConnectionProvider("application.properties");
        studentDao = new JdbcStudentDao(connectionProvider);
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
    void givenDelete_whenDelete_then2Students() throws DataSetException, Exception {
        Student expectedStudent = new Student(3, 3, "Andrea", "Rota");
        studentDao.delete(expectedStudent.getStudentId());
        int actual = databaseTester.getConnection().createQueryTable("students", "select * from students")
                .getRowCount();

        assertEquals(2, actual);
    }

    @Test
    void givenCourseNo3_whenGetByCourseName_then2Students() throws SQLException {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student(1, 1, "Matilde", "Gatti"));
        expected.add(new Student(2, 2, "Riccardo", "Rossi"));

        assertEquals(expected, studentDao.findByCourseName("Math"));
    }

    @Test
    void givenStudent_whenRemoveFromCourse_thenNoStudentsInCourse() throws DataSetException, Exception {
        Student expectedStudent = new Student(1, 1, "Matilde", "Gatti");
        Course expectedCourse = new Course(1, "Math", "Science of numbers");

        studentDao.deleteFromCourse(expectedStudent.getStudentId(), expectedCourse.getId());
        int actual = databaseTester.getConnection().createQueryTable("students_courses",
                "select * from students_courses where (student_id = 1) and (course_id = 1)").getRowCount();
        assertEquals(0, actual);
    }

    private IDataSet readDataSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = classLoader.getResource("testdata.xml").getFile();
        return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }
}
