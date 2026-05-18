package com.bank_account_management_system.model;

public class User implements Printable {

    //======== Data Fields =======
    private final String username; // Read only
    private String password;
    private String name;
    private String email;

    //======== Constructor ==========
    public User(String username, String password, String name, String email) {

        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;

    }

    //======== Getters ==============
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    //======= Setters ==========
    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    //=========== No Logic yet ============



    //========== Override Methods =========
    @Override
    public String printDetails() {
        return "username: "+ username + " | " +
               "password: " + password + " | " +
               "name: "+name + " | " +
               "email: " + email;
    }

}
