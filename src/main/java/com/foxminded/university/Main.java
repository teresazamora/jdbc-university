package com.foxminded.university;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
       DataBaseConnection dataConnection = new DataBaseConnection();
       TableCreator.createTable();
    }

}
