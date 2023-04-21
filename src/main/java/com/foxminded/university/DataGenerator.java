package com.foxminded.university;

import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Random;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public class DataGenerator {

    Random random = new Random();

    public List<Student> getListOfStudent() {
        List<Student> listOfStudents = new ArrayList<>();
        List<String> listOfName = new ArrayList<>();
        List<String> listOfSurname = new ArrayList<>();

        boolean addName = Collections.addAll(listOfName, "Aurora", "Ginevra", "Alice", "Beatrice", "Emma", "Giorgia",
                "Vittoria", "Matilde", "Sofia", "Ludovica", "Leonardo", "Tommaso", "Francesco", "Andrea", "Alessandro",
                "Lorenzo", "Riccardo", "Mattia", "Edoardo", "Gabriele");
        boolean addLastName = Collections.addAll(listOfSurname, "Rossi", "Ferrari", "Bianchi", "Gallo", "Costa",
                "Fontana", "Conti", "Esposito", "Rizzo", "Moretti", "Barbieri", "Lombardi", "Longo", "Rinaldi",
                "Colombo", "Martinelli", "Gatti", "Cartelli", "Rota", "Locatelli");

        for (int i = 0; i < 200; i++) {
            int index = random.nextInt(listOfName.size());

            int value = random.nextInt(listOfSurname.size());

            listOfStudents.add(new Student(listOfName.get(index), listOfSurname.get(value)));

        }
        return listOfStudents;
    }

    public List<Group> getListOfGroups() {

        List<Group> listOfGroups = new ArrayList<>();

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String numbers = "0123456789";

        for (int i = 0; i < 10; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));

            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));

            stringBuilder.append("-");

            stringBuilder.append(numbers.charAt(random.nextInt(numbers.length())));

            stringBuilder.append(numbers.charAt(random.nextInt(numbers.length())));

            listOfGroups.add(new Group(stringBuilder.toString()));

        }
        return listOfGroups;
    }

    public List<Course> getListOfCourses() {
        List<Course> listOfCourses = new ArrayList<>();
        listOfCourses.add(new Course("Math", "Science of numbers"));
        listOfCourses.add(new Course("Biology", "Science of nature"));
        listOfCourses.add(new Course("Information technologgy", "Science of computers"));
        listOfCourses.add(new Course("Science", "Science of science"));
        listOfCourses.add(new Course("History", "Science of events"));
        listOfCourses.add(new Course("English", "Science of international language"));
        listOfCourses.add(new Course("Physical education", "Science of body's force"));
        listOfCourses.add(new Course("Art", "Science of beautifull things"));
        listOfCourses.add(new Course("Economics", "Science of money"));
        listOfCourses.add(new Course("Italian", "Language of love :)"));

        return listOfCourses;
    }

    public void assignStudentsToGroups(List<Student> students, List<Group> groups, int minStudents, int maxStudents) {

        Map<Integer, Integer> studentsGroup = new HashMap<>();

        for (Student student : students) {
            student.setGroupId(groups.get(random.nextInt(groups.size())).getGroupId());
            Integer count = studentsGroup.get(student.getGroupId());
            if (count == null) {
                studentsGroup.put(student.getGroupId(), 1);
            } else {
                studentsGroup.put(student.getGroupId(), count + 1);
            }
        }
        for (Student student : students) {

            Integer studentsInGroup = studentsGroup.get(student.getGroupId());
            if (studentsInGroup < minStudents || studentsInGroup > maxStudents) {
                student.setGroupId(0);
            }
        }

    }

    public Map<Student, List<Course>> assignCoursesToStudent(List<Student> students, List<Course> courses) {
        Map<Student, List<Course>> studentsCourses = new HashMap<>();

        for (Student student : students) {
            List<Course> coursesList = new ArrayList<>();
            int index = random.nextInt(3 - 1 + 1) + 1;
            for (int i = 1; i <= index; i++) {
                Course course = courses.get(random.nextInt(courses.size()));
                if (!coursesList.contains(course) || coursesList.isEmpty()) {
                    coursesList.add(course);
                }
            }
            studentsCourses.put(student, coursesList);
        }
        return studentsCourses;
    }
}
