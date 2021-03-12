package com.homemanager;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.app.Activity;
import android.content.Context;

public class RestApi {
    private String jsonResult;
    private int responseCode;
    private SharedPreferences prefs;

    private final int HTTP_INTERNAL_ERROR = 500;
    private final int HTTP_OK             = 200;

    public RestApi(Context context){
        prefs = context.getSharedPreferences(
                "com.homemanager", Activity.MODE_PRIVATE);
    }

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

    private String Encode(String data,String password, String id){
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
            encryptStream.write(id.getBytes());
            encryptStream.write(salt);
            encryptStream.write(cipher.doFinal(data.getBytes("UTF8")));
            encrypted = encryptStream.toByteArray();
        }
        catch (Exception e){
            encrypted = new byte[16];
        }
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

    private String Decode(String data, String password, String deviceId){
        byte[] encrypted = Base64.decode(data, Base64.DEFAULT);
        byte[] id = new byte[8];
        byte[] salt = new byte[16];
        byte[] iv   = new byte[16];
        byte[] key;
        byte[] decrypted;
        String result = "";
        try {
            //format : id+salt+message
            for (int i = 0; i < id.length; i++) id[i] = encrypted[i];
            //do not even decode if id is incorrect
            //if (deviceId != id ) throw;

            for (int i = id.length; i < id.length + salt.length; i++) salt[i-id.length] = encrypted[i];

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
            byte[] data1 = new byte[encrypted.length - salt.length - id.length];
            for (int i=0; i<data1.length; i++) data1[i]=encrypted[i+salt.length+id.length];
            String d1  = hex2Str(data1);

            decrypted = cipher.doFinal(data1);
            result = new String(decrypted, "utf-8");
        }
        catch (Exception e){
            this.responseCode = HTTP_INTERNAL_ERROR;
        }

        return result;
    }

    public void writeDataToServer(String url, JSONObject data){
        String password = prefs.getString("com.homemanager.password", "password");
        String id = prefs.getString("com.homemanager.deviceId", "");
        //00000567;
        //"!@Elizka()";

        try {
            URL objUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) objUrl.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes( Encode(data.toString(), password, id) );

            os.flush();
            os.close();

            //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            //Log.i("MSG" , conn.getResponseMessage());

            this.responseCode = conn.getResponseCode();
            if (responseCode == HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                this.jsonResult = Decode(response.toString(), password, id);
            }

            conn.disconnect();
        }
        catch (Exception e){
            //e.printStackTrace();
            this.responseCode = HTTP_INTERNAL_ERROR;
        }
    }
}


