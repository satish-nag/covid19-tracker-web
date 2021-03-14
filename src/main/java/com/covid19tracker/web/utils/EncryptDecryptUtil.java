package com.covid19tracker.web.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class EncryptDecryptUtil {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) throws Exception {
        MessageDigest sha = null;
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String strToEncrypt, String secret) throws Exception{
        setKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public static String decrypt(String strToDecrypt, String secret) throws Exception{
        setKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    public static void main(String[] args) throws Exception {
        if( args.length < 2 || args[0]==null || args[0].trim().isEmpty() || args[1]==null || args[1].trim().isEmpty()) {
            System.out.println("Password to encrypt is not provided");
            System.out.println("usage: java -jar <path_to_jar> <class_file> <password> <encrypt/decrypt>");
            System.exit(1);
        }else {
            switch (args[1]){
                case "encrypt":
                    System.out.println(encrypt(args[0],"secretKey"));
                    break;
                case "decrypt":
                    System.out.println(decrypt(args[0],"secretKey"));
                    break;
            }
        }
    }
}
