package com.bank_account_management_system.Repository;


import com.bank_account_management_system.model.HomeLoan;
import java.time.LocalDateTime;


public class HomeLoanRepository extends BaseRepository<HomeLoan , Integer> {

    public HomeLoanRepository() {
        //file path
        super("src/main/resources/Files/homeLoan.txt");

    }


    //=======implements abstract methods======

    @Override
    protected HomeLoan parse(String line) {
        String[] lineArray = line.split(separator);
        //to avoid crash
        if(lineArray.length<8)
            return null;

        return new HomeLoan(Integer.parseInt(lineArray[0])
                ,protecting.decrypt(lineArray[1])
                , LocalDateTime.parse(lineArray[2])
                ,lineArray[3]
                ,Double.parseDouble(lineArray[4])
                ,Double.parseDouble(lineArray[5])
                ,Double.parseDouble(lineArray[6]),lineArray[7]);

    }

    @Override
    protected String format(HomeLoan object) {
        return commonFormat(object)
                + separator+ object.getLoanAmount()
                + separator+ object.getRemainingAmount()
                + separator+ sanitize(object.getPropertyAddress());
    }

    @Override
    protected Integer getKey(HomeLoan object) {
        return object.getAccountId();
    }


}
