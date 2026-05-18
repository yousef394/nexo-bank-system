package com.bank_account_management_system.Repository;


import com.bank_account_management_system.model.BankAccount;

import java.util.ArrayList;

  abstract class BaseRepository<T , K> extends FileManagement<T> implements CRUD<T , K> {

      //Constructor for get filePath
      public BaseRepository(String fileName) {
          super( fileName);
      }


      // for Encryption & Decryption
      Protecting protecting = new Protecting("1234567890123456");

      //Helper Method
      protected String commonFormat(BankAccount object) {
          return object.getAccountId() +
                  separator +
                  protecting.encrypt(object.getPassword()) +
                  separator +
                  object.getDateCreated() +
                  separator +
                  object.getHolderName() +
                  separator +
                  object.getBalance();
      }


      //========CRUD Methods=======
     public boolean add(T object){
          return appendLine(format(object));
      }

     public T find(K key) {

          for( T account : getAll() ) {

              if (key.equals( getKey(account) ))
                  return account;

          }

          return null;
      }

     public boolean delete(K key) {
          boolean result = false;

          ArrayList<T> accounts = getAll();

          //to reset file to can fill file again
          if(resetFile()) {
              for (T account :  accounts) {
                  //skip
                  if (getKey(account).equals(key)) {
                      result = true;
                      continue;
                  }

                  //fill file
                  appendLine(format(account));

              }
          }
          return result;
      }

     public boolean update(T object) {

          boolean result = false;

          ArrayList<T> accounts = getAll();

          //to clear file
          if(resetFile())
          {
              for(T account : accounts) {
                  //update data
                  if(getKey(account).equals(getKey(object))) {
                      account =  object;
                      result = true;

                  }
                  //fill file
                  appendLine(format(account));

              }
          }

          return result;


      }

      //abstract method
      protected abstract K getKey(T object);


  }
