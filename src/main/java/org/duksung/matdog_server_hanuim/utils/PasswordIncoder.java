package org.duksung.matdog_server_hanuim.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordIncoder {
    public static String incodePw(String str){

        String sha = "";

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();
        }catch(NoSuchAlgorithmException e){
            //e.printStackTrace();
            System.out.println("Encrypt Error - NoSuchAlgorithmException");
            sha = null;
        }
        return sha;
    }
}