package com.foxminded.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {

    Properties properties = new Properties();
    String urlResource = "jdbc.url";
    String userResource = "jdbc.user";
    String passwordResource = "jdbc.password";
    String fileProperties = "configuration.properties";
    String url;
    String user;
    String password;

    public DataBaseConnection() throws SQLException {

        FileReader reader = new FileReader();

        try {

            properties.load(reader.getFileFromResource(fileProperties));

            url = properties.getProperty(urlResource);

            user = properties.getProperty(userResource);

            password = properties.getProperty(passwordResource);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, user, password);

    }
}

