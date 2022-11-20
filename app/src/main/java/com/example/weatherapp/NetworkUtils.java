package com.example.weatherapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    public static final String WEATHER_API_BASE_URL = "https://api.weatherapi.com/v1/";
    public static final String WEATHER_API_GETCURRENT_METHOD = "current.json";
    public static final String WEATHER_API_GETFORECAST_METHOD = "forecast.json";
    public static final String PARAM_DAYS = "days";
    public static final String PARAM_API_KEY = "key";
    public static final String PARAM_CITY = "q";
    public static final String API_KEY = "d3adc39ae3104934b88143506221011";
    public static final String DAYS = "7";

    public static URL generateURL(String city) {
        Uri builtUri = Uri.parse(WEATHER_API_BASE_URL + WEATHER_API_GETFORECAST_METHOD)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_CITY, city)
                .appendQueryParameter(PARAM_DAYS, DAYS)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            System.out.println(scanner);

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }


    }

}
