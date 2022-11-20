package com.example.weatherapp;

import static com.example.weatherapp.NetworkUtils.generateURL;
import static com.example.weatherapp.NetworkUtils.getResponseFromURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static TextView weatherText;
    @SuppressLint("StaticFieldLeak")
    private static TextView feelsLikeText;
    @SuppressLint("StaticFieldLeak")
    private static TextView cityText;
    private static ImageButton changeCityButton;
    private static ProgressBar loading;
    private static Context context;
    private static LinearLayout weatherInfo;
    private static ImageView weatherIcon;
    private static TextView conditionText;
    private static TextView windSpeedText;
    private static TextView dateText;
    private static LocalDate today;
    private static TextView time;
    private static ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private static WeatherRVAdapter weatherRVAdapter;
    private static RecyclerView weatherRV;

    static class WeatherQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                Toast.makeText(context, "Error! Please try again!", Toast.LENGTH_LONG).show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject responseObject = new JSONObject(response);
                JSONObject current = responseObject.getJSONObject("current");
                JSONObject condition = current.getJSONObject("condition");
                JSONArray forecastArray = responseObject.getJSONObject("forecast").getJSONArray("forecastday");

                setAllFields(responseObject, current, condition, forecastArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAllVisibility();

        }

        public void setAllFields(JSONObject responseObject, JSONObject current, JSONObject condition, JSONArray forecastArray) throws JSONException {
            String conditionString = condition.getString("text");

            String temp = Integer.toString((int) current.getDouble("temp_c"));
            char[] tempArray = temp.toCharArray();
            CharSequence weatherTextString = tempArray[0] != '-' ? "+" + temp : temp;
            weatherText.setText(weatherTextString + "°");

            String feelsLikeTemp = Integer.toString((int) (current.getDouble("feelslike_c")));
            char[] feelsLikeTempArray = feelsLikeTemp.toCharArray();
            CharSequence feelsLikeTextString
                    = feelsLikeTempArray[0] != '-' ? "+" + feelsLikeTemp : feelsLikeTemp;
            feelsLikeText.setText("Feels like "
                    + feelsLikeTextString + "°");

            switch (conditionString) {
                case "Sunny":
                    Glide.with(context).load(R.drawable.clear_day).into(weatherIcon);
                    break;
                case "Overcast":
                    Glide.with(context).load(R.drawable.cloudy).into(weatherIcon);
                    break;
                case "Clear":
                    Glide.with(context).load(R.drawable.clear_night).into(weatherIcon);
                    break;
                case "Cloudy":
                    Glide.with(context).load(R.drawable.cloudy).into(weatherIcon);
                    break;
                case "Partly Cloudy":
                    Glide.with(context).load(R.drawable.cloudy).into(weatherIcon);
                    break;
                case "Fog":
                    Glide.with(context).load(R.drawable.fog).into(weatherIcon);
                    break;
                case "Mist":
                    Glide.with(context).load(R.drawable.fog).into(weatherIcon);
                    break;
                case "Freezing fog":
                    Glide.with(context).load(R.drawable.fog).into(weatherIcon);
                    break;
                case "Rain":
                    Glide.with(context).load(R.drawable.rain).into(weatherIcon);
                    break;
                case "Heavy snow":
                    Glide.with(context).load(R.drawable.snow).into(weatherIcon);
                    break;
                case "Moderate snow":
                    Glide.with(context).load(R.drawable.snow).into(weatherIcon);
                    break;
                case "Light snow":
                    Glide.with(context).load(R.drawable.snow).into(weatherIcon);
                    break;
                default:
                    Glide.with(context).load(R.drawable.clear_day).into(weatherIcon);
                    break;
            }

            cityText.setText(responseObject.getJSONObject("location").getString("name"));

            conditionText.setText(conditionString);

            windSpeedText.setText(Integer.toString((int) current.getDouble("wind_mph"))
                    + " m/s, "
                    + current.getString("wind_dir"));

            String[] last_updated = current.getString("last_updated").split(" ");

            today = LocalDate.parse(last_updated[0]);

            dateText.setText(today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                    " " + today.getDayOfMonth());

            time.setText("Last updated: " + last_updated[1]);

            JSONArray hourArray = forecastArray.getJSONArray(4);
            for(int i = 0; i < hourArray.length(); i++){
                JSONObject localHourObject = hourArray.getJSONObject(i);
                String tempForecast = Integer.toString((int) current.getDouble("temp_c"));
                char[] tempForecastArray = temp.toCharArray();
                CharSequence weatherForecastText = tempArray[0] != '-' ? "+" + temp : temp;
            }

        }

        public void setAllVisibility() {
            loading.setVisibility(View.INVISIBLE);
            weatherInfo.setVisibility(View.VISIBLE);
            weatherIcon.setVisibility(View.VISIBLE);
            conditionText.setVisibility(View.VISIBLE);
            windSpeedText.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFields();

        ChangeCityDialogFragment.changeCity("Yakutsk");


        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new ChangeCityDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "ChangeCityDialogFragment");
            }
        });
    }

    public void setFields() {
        cityText = findViewById(R.id.tv_city);
        weatherText = findViewById(R.id.tv_temperature);
        feelsLikeText = findViewById(R.id.tv_temperature_feels_like);
        changeCityButton = findViewById(R.id.ib_change_city);
        loading = findViewById(R.id.pb_get_loading);
        context = getApplicationContext();
        weatherInfo = findViewById(R.id.ll_weather_info);
        weatherIcon = findViewById(R.id.iv_weather_icon);
        conditionText = findViewById(R.id.tv_conditionText);
        windSpeedText = findViewById(R.id.tv_wind_speed);
        dateText = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);
        weatherRV = findViewById(R.id.rv_time_forecast);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);

    }

    public static class ChangeCityDialogFragment extends DialogFragment {

        final String[] cityNamesArray = {"Yakutsk", "Moscow", "Vladivostok", "Sochi", "Kazan"};

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(cityNamesArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (cityNamesArray[which]) {
                        case "Yakutsk":
                            changeCity("Yakutsk");
                            break;
                        case "Moscow":
                            changeCity("Moscow");
                            break;
                        case "Vladivostok":
                            changeCity("Vladivostok");
                            break;
                        case "Sochi":
                            changeCity("Sochi");
                            break;
                        case "Kazan":
                            changeCity("Kazan");
                            break;
                    }
                }
            });
            return builder.create();
        }

        public static void changeCity(String city) {
            URL generatedURL = generateURL(city);
            new WeatherQueryTask().execute(generatedURL);
        }


    }
}

