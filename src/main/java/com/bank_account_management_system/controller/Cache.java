package com.bank_account_management_system.controller;

import com.bank_account_management_system.Repository.*;
import com.bank_account_management_system.model.User;

public class Cache {
    private final static UserRepository userRepo = new UserRepository();
    private static User user;
    protected static void setUser(User u){
        user = u;
    }
    protected static User getUser(){
        return user;
    }







    // Repositories needed for real data
}

