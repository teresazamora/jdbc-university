package com.foxminded.university;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;

public class DataProviderTest  {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    protected ConnectionProvider connectionProvider;
    protected IDatabaseTester databaseTester;
    
    public DataProviderTest() throws ClassNotFoundException, SQLException, IOException {
            this.connectionProvider = new ConnectionProvider("application.properties");
            this.databaseTester = new JdbcDatabaseTester(JDBC_DRIVER,
                    connectionProvider.getConnection().getMetaData().getURL());    
        }

    
    public void beforeAll() throws Exception {
        createSchema();
        init();
    }
    
    public void createSchema() throws IOException, URISyntaxException, SQLException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("schema.sql");
        File file = new File(url.toURI());
        RunScript.execute(connectionProvider.getConnection(), new FileReader(file));
    }

     public void init() throws Exception {
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, connectionProvider.getConnection().getMetaData().getURL());
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(readDataSet());
        databaseTester.onSetup();
    }

    public IDataSet readDataSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = classLoader.getResource("testdata.xml").getFile();
        return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }

}
