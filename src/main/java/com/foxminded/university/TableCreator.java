package com.foxminded.university;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class TableCreator {

    public static void createTable(String file, ConnectionProvider connectionProvider) throws Exception {

        Reader reader = new Reader();

        try (Connection connection = connectionProvider.getConnection();
                InputStream inputStream = reader.getFileFromResource(file);
                PreparedStatement statement = connection
                        .prepareStatement(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                                .lines().collect(Collectors.joining("\n")));) {

            statement.executeUpdate();
            System.out.println("Table created!");
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (SQLException e) {
            System.out.println("Read more about connection or sql o IO… You don’t catch it!");
        }
    }
}
