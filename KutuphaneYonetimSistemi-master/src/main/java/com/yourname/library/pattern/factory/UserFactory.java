package com.yourname.library.pattern.factory;

import com.yourname.library.model.AbstractUser;
import com.yourname.library.model.Admin;
import com.yourname.library.model.Student;
import com.yourname.library.model.Staff;

public class UserFactory {

    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_STAFF = "Staff";
    public static final String TYPE_ADMIN = "Admin";

    public static AbstractUser createUser(String userType, int id, String firstName, String lastName, String tcNo, String email, String password, String number, String phone) {

        if (userType == null) return null;

        if (TYPE_STUDENT.equalsIgnoreCase(userType) || "Öğrenci".equalsIgnoreCase(userType)) {
            return new Student(id, firstName, lastName, tcNo, email, password, number, phone);

        } else if (TYPE_STAFF.equalsIgnoreCase(userType) || "Personel".equalsIgnoreCase(userType)) {
            return new Staff(id, firstName, lastName, tcNo, email, password, number, phone);

        } else if (TYPE_ADMIN.equalsIgnoreCase(userType)) {
            return new Admin(id, firstName, lastName, tcNo, email, password, number, phone);

        } else {
            throw new IllegalArgumentException("Geçersiz kullanıcı tipi: " + userType);
        }
    }
}