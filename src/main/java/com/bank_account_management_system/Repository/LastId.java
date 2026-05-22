package com.bank_account_management_system.Repository;

import java.io.*;

public class LastId {
    private static final String fileName = "../Files/lastId.txt";

    public static boolean addLastId(int id) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false)))
        {
            bw.write(""+id);
        }
        catch (IOException e)
        {
            return false;
        }
        return true;

    }

    public  static int getLastId(){
        int id;
        try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            id = Integer.parseInt(br.readLine());
        }
        catch (IOException e)
        {
            return 0;
        }
        return id;
    }

}
