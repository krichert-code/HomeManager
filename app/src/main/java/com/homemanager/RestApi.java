package com.homemanager;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
            os.writeBytes(data.toString());

            os.flush();
            os.close();

            //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            //Log.i("MSG" , conn.getResponseMessage());

            this.responseCode = conn.getResponseCode();
            //TODO: check if content says that it went correctly (not only HTTP 200)
            conn.disconnect();
        }
        catch (Exception e){
            this.responseCode = INTERNAL_ERROR;
        }
    }
}
