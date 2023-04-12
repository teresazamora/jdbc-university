package com.foxminded.university;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    static String file = "University.sql";

    public static void createTable() throws SQLException {

        DataBaseConnection dataConnection = new DataBaseConnection();
        FileReader reader = new FileReader();
        String line;

        try (Connection connection = dataConnection.getConnection();

                Statement statement = connection.createStatement();

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(reader.getFileFromResource(file), StandardCharsets.UTF_8))) {

            while ((line = bufferedReader.readLine()) != null) {

                statement.execute(line);

            }
            System.out.println("Table created!");

        } catch (Exception e) {

       System.out.println("Read more about connection or sql o IO… You don’t catch it!");
       e.getMessage();
        }
    }
}
