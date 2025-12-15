package com.yourname.library.model;

public class Student extends AbstractUser {

    public Student(int id, String firstName, String lastName, String tcNo, String email, String password, String studentNumber, String phone) {
        super(id, firstName, lastName, tcNo, email, password, studentNumber, phone);
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}