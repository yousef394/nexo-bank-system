package com.bank_account_management_system.Repository;


import java.io.*;
import java.util.ArrayList;

public abstract class FileManagement <T> {

    protected final String separator = "#//#";

    private final String fileName;

    public FileManagement(String fileName) {
        this.fileName = fileName;
    }

    //========= file methods =========
    protected boolean appendLine(String Line)  {

        boolean flag=false;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,true));) {
            bw.write(Line+"\n");
            flag= true;
        }
        catch(IOException e) {
            return false;

        }


        return flag;
    }

    protected boolean resetFile() {
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(fileName,false))) {
            return  true;
        }
        catch (IOException e) {
            return false;
        }

    }

    public ArrayList<T> getAll(){

        ArrayList<T> Accounts = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            T account;

            while ((line = br.readLine()) != null)
            {
                account =  parse(line);

                if(account!=null) {
                    Accounts.add(account);
                }
            }

        }
        catch(IOException e) {
            return Accounts;

        }

        return Accounts;
    }


    //=========== abstract methods ============
    protected abstract String format(T object);
    protected abstract T  parse(String line);



}
