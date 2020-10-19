package com.homemanager.Alarm;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmArea {
    private int id;
    private int roomX;
    private int roomY;
    private int roomWidth;
    private int roomHeight;
    private String  name;
    private String  temperature;
    private Boolean isAlarmActive;
    private boolean lightOn;
    private boolean isLightConnected;


    AlarmArea(String name, String temp, Boolean alarmState, boolean lightConnected){
        this.name = name;
        this.temperature = temp;
        this.isAlarmActive = alarmState;
        this.isLightConnected = lightConnected;
    }

    public Bitmap drawLight(Bitmap src) {
        if (src == null) {
            return null;
        }
        // Source image size
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        //get pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int x = 0; x < pixels.length; ++x) {
            int y1 = x/width;
            int x1 = x % width;

            if (x1 > roomX && x1 < roomX+ roomWidth && y1 > roomY && y1 < roomY + roomHeight) {
                int color = pixels[x];
                float ratio = 1.0f;
                if (lightOn) ratio = ratio + 0.2f;

                int a = (color >> 24) & 0xFF;
                int r = (int) (((color >> 16) & 0xFF) * ratio);
                int g = (int) (((color >> 8) & 0xFF) * ratio);
                int b = (int) ((color & 0xFF) * ratio);

                pixels[x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        // create result bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        //set pixels
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }

    public void toggleLight(){
        if (lightOn) {
            lightOn = false;
        }
        else {
            lightOn = true;
        }
    }

    public boolean isAlarmActive(){
        return isAlarmActive;
    }


    public String getTemperature(){
        return temperature;
    }

    public boolean isLightOn()
    {
        return lightOn;
    }

    public boolean isLightConnected() { return isLightConnected; }

    public String getName(){
        return name;
    }

}
