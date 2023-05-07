package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.foxminded.university.ConnectionProvider;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Student;

public class JdbcStudentDaoTest {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private ConnectionProvider connectionProvider;
    private JdbcStudentDao studentDao;
    private IDatabaseTester databaseTester;

    public JdbcStudentDaoTest() throws SQLException, ClassNotFoundException {
        this.connectionProvider = new ConnectionProvider("application.properties");
        this.studentDao = new JdbcStudentDao(connectionProvider);
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
    void giveStudent_whenDelete_thenGetAmountRowToVerificateIfStudentWasDelete() throws DataSetException, Exception {
        Student expectedStudent = new Student(3, 3, "Andrea", "Rota");
        studentDao.delete(expectedStudent.getStudentId());
        int actual = databaseTester.getConnection().createQueryTable("students", "select * from students")
                .getRowCount();
        assertEquals(2, actual);
    }

    @Test
    void giveCourse_whenEnterCourseName_thenFindStudentsByCourseName() throws SQLException {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student(1, 1, "Matilde", "Gatti"));
        expected.add(new Student(2, 2, "Riccardo", "Rossi"));
        List<Student> actual = studentDao.findByCourseName("History");
        assertEquals(expected, actual);
    }

    @Test
    void giveStudent_whenRemoveFromCourse_thenVerificateIfStudentsWasDeleteFromCourse() throws DataSetException, Exception {
        Student expectedStudent = new Student(1, 1, "Matilde", "Gatti");
        Course expectedCourse = new Course(1, "Math", "Science of numbers");

        studentDao.deleteFromCourse(expectedStudent.getStudentId(), expectedCourse.getId());
        int actual = databaseTester.getConnection().createQueryTable("students_courses",
                "select * from students_courses where (student_id = 1) and (course_id = 1)").getRowCount();
        assertEquals(0, actual);
    } 
}
