package com.bank_account_management_system.Repository;

import com.bank_account_management_system.model.User;

public class UserRepository extends BaseRepository<User ,String> implements CRUD<User , String> {

    //========== Constructor for file path ========

    public UserRepository() {
        super("../Files/User.txt");
    }


    //======= Override Methods ========

    @Override
    protected String format(User object) {
        return object.getUsername() +
                separator +
               protecting.encrypt(object.getPassword()) +
                separator +
                object.getName() +
                separator +
                object.getEmail();
    }

    @Override
    protected User parse(String line) {

        String[] lineArray = line.split(separator);

        if(lineArray.length<4)
            return null;

        return new User(lineArray[0]
                ,protecting.decrypt(lineArray[1])
                ,lineArray[2]
                ,lineArray[3]);
    }

    @Override
    protected String getKey(User object) {
        return object.getUsername();
    }

}
