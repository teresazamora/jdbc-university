package com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.Test;

import com.foxminded.university.DataProviderTest;
import com.foxminded.university.model.Course;

public class JdbcCourseDaoTest extends DataProviderTest {

    private JdbcCourseDao courseDao;

    public JdbcCourseDaoTest() throws Exception {
        this.courseDao = new JdbcCourseDao(connectionProvider);
        super.beforeAll();
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
