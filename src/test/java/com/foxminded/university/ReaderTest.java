package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class ReaderTest {

    private Reader reader = new Reader();

    @Test
    void giveFileAndString_whenUseReader_thenCompareTwoStrings() {
        String expected = "jdbc.url = jdbc:postgresql://localhost:5432/University\n" + "jdbc.user = user\n"
                + "jdbc.password = password";
        String actual = new BufferedReader(
                new InputStreamReader(reader.getFileFromResource("configuration.properties"), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        assertEquals(expected, actual);
    }

    @Test
    void giveUnexistFile_whenUseReader_thenLaunchErrore() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reader.getFileFromResource("null.properties");
        });
        String expectedMessage = "file not found! null.properties";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
