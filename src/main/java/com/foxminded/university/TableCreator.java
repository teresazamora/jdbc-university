package com.foxminded.university;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class TableCreator {

    public static void createTable(String file,ConnectionProvider dataConnection ) throws Exception {

        Reader reader = new Reader();

        String text = new BufferedReader(
                new InputStreamReader(reader.getFileFromResource(file), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        try (Connection connection = dataConnection.getConnection();

                PreparedStatement statement = connection.prepareStatement(text)) {

            statement.executeUpdate();
            System.out.println("Table created!");

        } catch (SQLException e) {
            System.out.println("Read more about connection or sql o IO… You don’t catch it!");
        }

    }
}
