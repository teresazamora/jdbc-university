package com.foxminded.university;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    private static final String URL_RESOURCE = "jdbc.url";
    private static final String USER_RESOURCE = "jdbc.user";
    private static final String PASSWORD_RESOURCE = "jdbc.password";

    private String url;
    private String user;
    private String password;

    public ConnectionProvider(String fileProperties) throws IOException {
        Reader reader = new Reader();
        Properties properties = new Properties();

        try (InputStream inputStream = reader.getFileFromResource(fileProperties);){            
            properties.load(inputStream);
            url = properties.getProperty(URL_RESOURCE);
            user = properties.getProperty(USER_RESOURCE);
            password = properties.getProperty(PASSWORD_RESOURCE);
        } 
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
