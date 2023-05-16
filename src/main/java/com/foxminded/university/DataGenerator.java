package com.foxminded.university;

import java.util.*;
import java.util.stream.Collectors;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public class DataGenerator {

    Random random = new Random();

    public List<Student> generateStudents(int amount) {
        List<Student> students = new ArrayList<>();
        List<String> names = Arrays.asList("Aurora", "Ginevra", "Alice", "Beatrice", "Emma", "Giorgia", "Vittoria",
                "Matilde", "Sofia", "Ludovica", "Leonardo", "Tommaso", "Francesco", "Andrea", "Alessandro", "Lorenzo",
                "Riccardo", "Mattia", "Edoardo", "Gabriele");
        List<String> surnames = Arrays.asList("Rossi", "Ferrari", "Bianchi", "Gallo", "Costa", "Fontana", "Conti",
                "Esposito", "Rizzo", "Moretti", "Barbieri", "Lombardi", "Longo", "Rinaldi", "Colombo", "Martinelli",
                "Gatti", "Cartelli", "Rota", "Locatelli");

        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(names.size());
            int value = random.nextInt(surnames.size());
            students.add(new Student(names.get(index), surnames.get(value)));
        }
        return students;
    }

    public List<Group> generateGroups(int amount) {
        List<Group> groups = new ArrayList<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

        for (int i = 0; i < amount; i++) {
            StringBuilder groupName = new StringBuilder();

            groupName.append(alphabet.charAt(random.nextInt(alphabet.length())));
            groupName.append(alphabet.charAt(random.nextInt(alphabet.length())));
            groupName.append("-");
            groupName.append(numbers.charAt(random.nextInt(numbers.length())));
            groupName.append(numbers.charAt(random.nextInt(numbers.length())));
            groups.add(new Group(groupName.toString()));

        }
        return groups;
    }

    public List<Course> generateCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Math", "Science of numbers"));
        courses.add(new Course("Biology", "Science of nature"));
        courses.add(new Course("Information technologgy", "Science of computers"));
        courses.add(new Course("Science", "Science of science"));
        courses.add(new Course("History", "Science of events"));
        courses.add(new Course("English", "Science of international language"));
        courses.add(new Course("Physical education", "Science of body's force"));
        courses.add(new Course("Art", "Science of beautifull things"));
        courses.add(new Course("Economics", "Science of money"));
        courses.add(new Course("Italian", "Language of love :)"));

        return courses;
    }

    public void assignStudentsToGroups(List<Student> students, List<Group> groups, int minStudents, int maxStudents) {

        Map<Integer, Long> studentsGroup = new HashMap<>();

        for (Student student : students) {
            student.setGroupId(groups.get(random.nextInt(groups.size())).getId());
        }
        studentsGroup = students.stream().collect(Collectors.groupingBy(Student::getGroupId, Collectors.counting()));

        for (Student student : students) {
            Long studentsInGroup = studentsGroup.get(student.getGroupId());
            if (studentsInGroup < minStudents || studentsInGroup > maxStudents) {
                studentsGroup.remove(student);
            }
        }
    }

    public void assignCoursesToStudent(List<Student> students, List<Course> courses, int maxCourses, int minCourses) {
        for (Student student : students) {
            List<Course> coursesList = new ArrayList<>();
            int index = random.nextInt(maxCourses - minCourses + 1) + minCourses;
            for (int i = minCourses; i <= index; i++) {
                Course course = courses.get(random.nextInt(courses.size()));
                if (!coursesList.contains(course) || coursesList.isEmpty()) {
                    coursesList.add(course);
                }
            }
            student.setCourses(coursesList);
        }
    }
}
