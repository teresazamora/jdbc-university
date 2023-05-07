package com.foxminded.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.foxminded.university.Reader;

public class ConnectionProvider {

    private final String URL_RESOURCE = "jdbc.url";
    private final String USER_RESOURCE = "jdbc.user";
    private final String PASSWORD_RESOURCE = "jdbc.password";
    private String url;
    private String user;
    private String password;

    public ConnectionProvider(String fileProperties) throws SQLException {

        Reader reader = new Reader();
        Properties properties = new Properties();

        try {
            properties.load(reader.getFileFromResource(fileProperties));
            url = properties.getProperty(URL_RESOURCE);
            user = properties.getProperty(USER_RESOURCE);
            password = properties.getProperty(PASSWORD_RESOURCE);
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, user, password);
    }
}
