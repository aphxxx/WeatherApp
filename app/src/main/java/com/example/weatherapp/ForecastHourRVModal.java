package com.example.weatherapp;

public class ForecastHourRVModal {

    @Override
    public String toString() {
        return "WeatherRVModal{" +
                "time='" + time + '\'' +
                ", temperature=" + temperature +
                ", icon=" + icon +
                '}';
    }

    private String time;
    private CharSequence temperature;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CharSequence getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;


    public ForecastHourRVModal(String time, CharSequence temperature, int icon) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
    }
}
