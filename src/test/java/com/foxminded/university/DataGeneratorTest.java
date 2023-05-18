package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public class DataGeneratorTest {
    
    private DataGenerator dataGenerator = new DataGenerator();
    
    @Test
    void generateStudent_whenUseGenerateMethod_thenVerificateIfStudentsWasCreate() {
        
        List<Student> students = dataGenerator.generateStudents(200);
        assertEquals(200, students.size());
    }
    
    @Test
    void generateGroup_whenUseGenerateMethod_thenVerificateIfGroupsWasCreate(){
        
        List<Group> groups = dataGenerator.generateGroups(10);
        assertEquals(10, groups.size());
    }   
}
