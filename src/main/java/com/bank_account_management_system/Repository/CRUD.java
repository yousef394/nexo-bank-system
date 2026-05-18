package com.bank_account_management_system.Repository;

import java.util.ArrayList;

public interface CRUD<T ,K> {

     boolean add(T object);

     ArrayList<T> getAll();

     T find(K key);

     boolean delete(K key);

     boolean update(T key) ;

}
