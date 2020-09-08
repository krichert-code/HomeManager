package com.homemanager;


import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RestApi {
    private String jsonResult;
    private int responseCode;

    private final int INTERNAL_ERROR = 500;

    public String getJsonResponse() {
        return this.jsonResult;
    }

    public int getResponseCode(){
        return this.responseCode;
    }

    public String hex2Str(byte[] data){

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String Encode(String data){
        String password= "AABB";
        byte[] encrypted;
        Random RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] iv   = new byte[16];
        byte[] key;

        //generate salt
        RANDOM.nextBytes(salt);
        try {
            //---------------generate sha256 from the salt + password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(salt);
            outputStream.write(password.getBytes());
            md.update(outputStream.toByteArray());
            key = md.digest();

            //generate IV
            md.update(salt);
            byte[] digest = md.digest();
            for (int i = 0; i < iv.length; i++) iv[i] = digest[i];

            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));

            ByteArrayOutputStream encryptStream = new ByteArrayOutputStream();
            encryptStream.write(salt);
            encryptStream.write(cipher.doFinal(data.getBytes("UTF8")));
            encrypted = encryptStream.toByteArray();
        }
        catch (Exception e){
            encrypted = new byte[16];
        }
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

    private String Decode(String data){
        String password= "AABB";
        byte[] encrypted = Base64.decode(data, Base64.DEFAULT);
        byte[] salt = new byte[16];
        byte[] iv   = new byte[16];
        byte[] key;
        byte[] decrypted;
        String result = "";
        try {
            for (int i = 0; i < salt.length; i++) salt[i] = encrypted[i];
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(salt);
            outputStream.write(password.getBytes());
            md.update(outputStream.toByteArray());
            key = md.digest();

            md.update(salt);
            byte[] digest = md.digest();
            for (int i = 0; i < iv.length; i++) iv[i] = digest[i];

            String k  = hex2Str(key);
            String s  = hex2Str(salt);
            String i1  = hex2Str(iv);

            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
            byte[] data1 = new byte[encrypted.length - salt.length];
            for (int i=0; i<data1.length; i++) data1[i]=encrypted[i+salt.length];
            String d1  = hex2Str(data1);
            //decrypted = cipher.doFinal(encrypted, salt.length, encrypted.length - salt.length);
            decrypted = cipher.doFinal(data1);
            result = new String(decrypted, "utf-8");
        }
        catch (Exception e){
            this.responseCode = INTERNAL_ERROR;
        }

        return result;
    }

    public void readDataFromServer(String url){
        try {
            URL objUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            // user agent
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            // język
            connection.setRequestProperty("Accept-Language", "pl-PL,en;q=0.5");

            // Pobranie kodu odpowiedzi
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                this.jsonResult = response.toString();
                this.responseCode = responseCode;
            }

            connection.disconnect();
        } catch (Exception e) {
            this.responseCode = INTERNAL_ERROR;
        }
    }

    public void writeDataToServer(String url, JSONObject data){
        try {
            URL objUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) objUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes( Encode(data.toString()) );

            os.flush();
            os.close();

            //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            //Log.i("MSG" , conn.getResponseMessage());

            this.responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                this.jsonResult = Decode(response.toString());
                this.responseCode = responseCode;
            }

            conn.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
            this.responseCode = INTERNAL_ERROR;
        }
    }
}


