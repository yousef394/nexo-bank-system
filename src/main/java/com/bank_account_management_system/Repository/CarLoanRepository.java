package com.bank_account_management_system.Repository;

import com.bank_account_management_system.model.CarLoan;
import java.time.LocalDateTime;

public class CarLoanRepository extends BaseRepository<CarLoan , Integer> {

    public CarLoanRepository() {
        //file path
        super("src/main/resources/Files/carLoan.txt");

    }


    //=======implements abstract methods======

    protected CarLoan parse(String line) {
        String[] lineArray = line.split(separator);
        //to avoid crash
        if(lineArray.length<8)
            return null;

        return new CarLoan(Integer.parseInt(lineArray[0])
                ,protecting.decrypt(lineArray[1])
                , LocalDateTime.parse(lineArray[2])
                ,lineArray[3]
                ,Double.parseDouble(lineArray[4])
                ,Double.parseDouble(lineArray[5])
                ,Double.parseDouble(lineArray[6]),lineArray[7]);

    }

    protected String format(CarLoan object) {
        return commonFormat(object)
                + separator+ object.getLoanAmount()
                + separator+ object.getRemainingAmount()
                + separator+ sanitize(object.getCarModel());
    }

    @Override
    protected Integer getKey(CarLoan object) {
        return object.getAccountId();
    }

}
