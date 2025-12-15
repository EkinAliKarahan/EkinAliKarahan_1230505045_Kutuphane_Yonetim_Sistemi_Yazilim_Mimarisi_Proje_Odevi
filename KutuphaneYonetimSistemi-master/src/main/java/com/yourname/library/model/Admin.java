package com.yourname.library.model;

public class Admin extends AbstractUser {

    public Admin(int id, String firstName, String lastName, String tcNo, String email, String password, String number, String phone) {
        super(id, firstName, lastName, tcNo, email, password, number, phone);
    }


    @Override
    public String getUserType() {
        return "Admin";
    }
}