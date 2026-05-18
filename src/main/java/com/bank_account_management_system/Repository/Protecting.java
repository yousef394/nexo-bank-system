package com.bank_account_management_system.Repository;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Protecting {
    //fixed KEY
    private  final String KEY;

   public Protecting (String key){
        this.KEY =key;
    }

    private  SecretKeySpec getKey() {
        return new SecretKeySpec(KEY.getBytes(), "AES");
    }

    //  Encrypt
    public  String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            return null;
        }
    }

    //  Decrypt
    public  String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());

            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            byte[] original = cipher.doFinal(decoded);

            return new String(original);

        } catch (Exception e) {
            return null;
        }
    }
}

