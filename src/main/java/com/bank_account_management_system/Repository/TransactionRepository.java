package com.bank_account_management_system.Repository;

import com.bank_account_management_system.model.Transaction;
import com.bank_account_management_system.model.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionRepository extends BaseRepository<Transaction , Integer>{


    public TransactionRepository() {
        super("src/main/resources/Files/transactionsFile.txt");
    }

    @Override
     protected String format(Transaction transaction){
        return transaction.getAccountId()+ separator
                +transaction.getType()+ separator
                +transaction.getAmount()+ separator
                +transaction.getBalanceBefore()+ separator
                +transaction.getBalanceAfter()+ separator
                +transaction.getDate() + separator
                +transaction.getCurrentUser();
    }

    @Override
     protected Transaction parse(String line) {

        String[] dataLine = line.split(separator);

        if(dataLine.length < 6) {
            return null;
        }
       return new Transaction(Integer.parseInt(dataLine[0])
                , TransactionType.valueOf(dataLine[1]),
                Double.parseDouble(dataLine[2]),Double.parseDouble(dataLine[3]),
                Double.parseDouble(dataLine[4]),
                LocalDateTime.parse(dataLine[5]),
                 dataLine[6]);
    }

    @Override
    protected Integer getKey(Transaction object) {
        return object.getAccountId();
    }

    public ArrayList<Transaction> getTransactionsByAccountId(int accountId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Transaction TR : getAll()) {
            if (TR.getAccountId() == accountId) {
                transactions.add(TR);
            }
        }
        return transactions;
    }


}
