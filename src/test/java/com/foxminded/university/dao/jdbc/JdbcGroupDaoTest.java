package com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.foxminded.university.ConnectionForTest;
import com.foxminded.university.ConnectionProvider;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Group;

public class JdbcGroupDaoTest extends ConnectionForTest {

    private ConnectionProvider connectionProvider;
    private JdbcGroupDao groupDao;
    private IDatabaseTester databaseTester;
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();

    public JdbcGroupDaoTest() throws Exception {
        this.connectionProvider = new ConnectionProvider("application.properties");
        this.groupDao = new JdbcGroupDao(connectionProvider);
        this.databaseTester = new JdbcDatabaseTester(JDBC_DRIVER,
                connectionProvider.getConnection().getMetaData().getURL());
        super.beforeAll();
    }

    @Test
    void givenStudentsCount_whenGetByStudentsCount_thenFindGroupByAmountStudent() throws SQLException {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "QW-01"));
        expected.add(new Group(2, "AS-02"));
        expected.add(new Group(3, "ZX-03"));
        assertEquals(expected, groupDao.findByStudentsAmount(1));
    }

    @Test
    void addGroup_whenCreate_thenGetAmountRowToVerificateIfGroupWasCreate()
            throws DataSetException, SQLException, Exception {
        Group expectedGroup = new Group(7, "DF-89");
        groupDao.create(expectedGroup);
        int actual = databaseTester.getConnection().createQueryTable("groups", "select * from groups").getRowCount();
        assertEquals(4, actual);
    }

    @Test
    void givenGroup_whenDelete_thenGetAmountRowToVerificateIfGroupWasDelete() throws DataSetException, Exception {
        Group expectedGroup = new Group(4, "ZX-03");
        groupDao.delete(expectedGroup.getId());
        int actual = databaseTester.getConnection().createQueryTable("groups", "select * from groups").getRowCount();
        assertEquals(3, actual);
    }
}
