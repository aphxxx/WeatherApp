package com.example.weatherapp;

public class ForecastDayRVModal {
    private String date;
    private CharSequence temperature;
    private int icon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CharSequence getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "ForecastDayRVModal{" +
                "date='" + date + '\'' +
                ", temperature=" + temperature +
                ", icon=" + icon +
                '}';
    }

    public void setTemperature(CharSequence temperature) {
        this.temperature = temperature;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ForecastDayRVModal(String date, CharSequence temperature, int icon) {
        this.date = date;
        this.temperature = temperature;
        this.icon = icon;
    }


}
