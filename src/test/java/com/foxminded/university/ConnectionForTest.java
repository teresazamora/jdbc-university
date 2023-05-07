package com.foxminded.university;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;

public class ConnectionForTest {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private IDatabaseTester databaseTester;
    
    public void createSchema() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("schema.sql");
        File file = new File(url.toURI());
        ConnectionProvider connectionProvider = new ConnectionProvider("application.properties");
        RunScript.execute(connectionProvider.getConnection(), new FileReader(file));
    }

     public void init() throws Exception {
        ConnectionProvider connectionProvider2 = new ConnectionProvider("application.properties");
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, connectionProvider2.getConnection().getMetaData().getURL());
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(readDataSet());
        databaseTester.onSetup();
    }

    private IDataSet readDataSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = classLoader.getResource("testdata.xml").getFile();
        return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }

}
