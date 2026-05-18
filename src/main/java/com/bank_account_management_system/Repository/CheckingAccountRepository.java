package com.bank_account_management_system.Repository;

import com.bank_account_management_system.model.CheckingAccount;
import java.time.LocalDateTime;

public  class CheckingAccountRepository extends BaseRepository<CheckingAccount , Integer> {

    public CheckingAccountRepository() {
        //file path
        super("src/main/resources/Files/checkingAccount.txt");

    }

    //=======implements abstract methods======

    @Override
     protected CheckingAccount parse(String line) {
       String[] lineArray = line.split(separator);
       //to avoid crash
       if(lineArray.length<7)
           return null;

        return new CheckingAccount(Integer.parseInt(lineArray[0])
                ,protecting.decrypt(lineArray[1])
                , LocalDateTime.parse(lineArray[2])
                ,lineArray[3]
                ,Double.parseDouble(lineArray[4])
                ,Double.parseDouble(lineArray[5])
                ,Double.parseDouble(lineArray[6]));

    }

    @Override
    protected String format(CheckingAccount object) {

          return
             super.commonFormat(object)
             + separator+ object.getOverdraftLimit()
             + separator+ object.getFee();
    }

    @Override
    protected Integer getKey(CheckingAccount object) {
        return object.getAccountId();
    }



}

