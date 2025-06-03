package com.example.studentmanagement;

public class Student {
    public String regNumber;
    public String fullName;
    public int catMarks;
    public int examMarks;
    public int totalMarks;
    public String grade;

    public Student() {
        // Required for Firebase
    }

    public Student(String regNumber, String fullName, int catMarks, int examMarks, int totalMarks, String grade) {
        this.regNumber = regNumber;
        this.fullName = fullName;
        this.catMarks = catMarks;
        this.examMarks = examMarks;
        this.totalMarks = totalMarks;
        this.grade = grade;
    }
}