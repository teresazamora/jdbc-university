package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Student;

public interface StudentDao {
    
    void create(Student student);
    List<Student> findByCourse(String courseName);
    void delete(int id);
    void deleteFromCourse(int studentId, int courseId);
    void addToCourse(int studentId, int courseId);
    

}
