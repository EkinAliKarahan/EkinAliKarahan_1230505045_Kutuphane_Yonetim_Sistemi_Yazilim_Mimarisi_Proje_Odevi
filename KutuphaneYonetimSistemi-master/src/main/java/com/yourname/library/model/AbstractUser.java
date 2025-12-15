package com.yourname.library.model;

public abstract class AbstractUser {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String tcNo;
    protected String email;
    protected String password;
    protected String number;
    protected String phone;

    public AbstractUser(int id, String firstName, String lastName, String tcNo, String email, String password, String number, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tcNo = tcNo;
        this.email = email;
        this.password = password;
        this.number = number;
        this.phone = phone;

    }

    public AbstractUser() {}



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTcNo() { return tcNo; }
    public void setTcNo(String tcNo) { this.tcNo = tcNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public abstract String getUserType();
}