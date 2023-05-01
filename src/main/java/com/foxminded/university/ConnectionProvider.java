package com.foxminded.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    final String URL_RESOURCE = "jdbc.url";
    final String USER_RESOURCE = "jdbc.user";
    final String PASSWORD_RESOURCE = "jdbc.password";
    String url;
    String user;
    String password;

    public ConnectionProvider(String fileProperties) throws SQLException {

        Reader reader = new Reader();
        Properties properties = new Properties();

        try {

            properties.load(reader.getFileFromResource(fileProperties));

            url = properties.getProperty(URL_RESOURCE);

            user = properties.getProperty(USER_RESOURCE);

            password = properties.getProperty(PASSWORD_RESOURCE);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, user, password);

    }
}
