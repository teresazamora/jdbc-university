package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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

import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Group;

public class JdbcGroupDaoTest {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();

    ConnectionProvider connectionProvider;
    JdbcGroupDao groupDao;
    private IDatabaseTester databaseTester;

    public JdbcGroupDaoTest() throws IOException, SQLException {
        this.connectionProvider = new ConnectionProvider("application.properties");
        this.groupDao = new JdbcGroupDao(connectionProvider);
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
    void givenGroup_whenDelete_thenGroupDeleted() throws DataSetException, Exception {
       Group expectGroup = new Group(2, "AS-02");
        groupDao.delete(expectGroup.getId());
        int actual = databaseTester.getConnection().createQueryTable("groups", "select * from groups;").getRowCount();

        assertEquals(3, actual);
    }

    @Test
    void givenStudentsCount1_whenGetByStudentsCount_then3Groups() throws SQLException {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "QW-01"));
        expected.add(new Group(2, "AS-02"));
        expected.add(new Group(3, "ZX-03"));

        assertEquals(expected, groupDao.findGroupByAmountStudent(1));
    }

    private IDataSet readDataSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = classLoader.getResource("testdata.xml").getFile();
        return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }


}
