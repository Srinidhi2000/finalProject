package com.example.android.project.specificvenue;

public class Weather {
    private String temp;
   private String condition;

    public Weather(String temp, String condition) {
        this.temp = temp;
        this.condition = condition;
    }

    public String getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }
}
