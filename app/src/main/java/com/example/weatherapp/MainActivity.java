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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;


@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity {

    private static TextView weatherText;
    private static TextView feelsLikeText;
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
    private static ArrayList<ForecastHourRVModal> forecastHourRVModalArrayList;
    private static ForecastHourRVAdapter forecastHourRVAdapter;
    private static RecyclerView forecastHourRV;
    private static RecyclerView forecastDayRV;
    private static ArrayList<ForecastDayRVModal> forecastDayRVModalArrayList;
    private static ForecastDayRVAdapter forecastDayRVAdapter;

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
                JSONObject forecastHourObject = responseObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0);
                setAllFields(responseObject, current, condition, forecastHourObject, forecastArray);
                setAllVisibility();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        public int switchBitch(String otkudaBeru){
            int icon;
            switch (otkudaBeru) {
                case "Overcast":
                case "Partly Cloudy":
                case "Cloudy":
                    icon = R.drawable.cloudy;
                    break;
                case "Clear":
                    icon = R.drawable.clear_night;
                    break;
                case "Fog":
                case "Mist":
                case "Freezing fog":
                    icon = R.drawable.fog;
                    break;
                case "Rain":
                case "Light rain":
                case "Moderate rain":
                case "Heavy rain":
                    icon = R.drawable.rain;
                    break;
                case "Heavy snow":
                case "Moderate snow":
                case "Light snow":
                    icon = R.drawable.snow;
                    break;
                case "Sunny":
                default:
                    icon = R.drawable.clear_day;
                    break;
            }
            return icon;
        }

        public void setAllFields(JSONObject responseObject, JSONObject current, JSONObject condition, JSONObject forecastHourObject, JSONArray forecastArray) throws JSONException {
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


            int currentIcon = switchBitch(conditionString);
            Glide.with(context).load(currentIcon).into(weatherIcon);

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


            forecastHourRVModalArrayList.clear();
            JSONArray hourArray = forecastHourObject.getJSONArray("hour");
            for(int i = 0; i < hourArray.length(); i++){
                JSONObject localHourObject = hourArray.getJSONObject(i);
                int forecastIcon;

                String localHourTime = localHourObject.getString("time").split(" ")[1];
                if(Integer.parseInt(last_updated[1].split(":")[0]) > Integer.parseInt(localHourTime.split(":")[0])){
                    continue;
                }

                String tempForecast = Integer.toString((int) localHourObject.getDouble("temp_c"));
                char[] tempForecastArray = tempForecast.toCharArray();
                CharSequence weatherForecastTemp = tempArray[0] != '-' ? "+" + tempForecast : tempForecast;


                String forecastCondition = localHourObject.getJSONObject("condition").getString("text");

                forecastIcon = switchBitch(forecastCondition);


                forecastHourRVModalArrayList.add(new ForecastHourRVModal(localHourTime, weatherForecastTemp, forecastIcon));

            }
            forecastHourRVAdapter.notifyDataSetChanged();


            forecastDayRVModalArrayList.clear();
            for(int i = 0; i < forecastArray.length(); i++){
                JSONObject localDayObject = forecastArray.getJSONObject(i);
                int forecastIcon;
                String[] localDateArray = null;

                if(i == 0){
                    continue;
                }

                JSONObject dayObject = localDayObject.getJSONObject("day");
                String tempForecast = Integer.toString((int) dayObject.getDouble("avgtemp_c")) + "°";
                char[] tempForecastArray = tempForecast.toCharArray();
                CharSequence weatherForecastTemp = tempArray[0] != '-' ? "+" + tempForecast : tempForecast;

                String localDay = localDayObject.getString("date");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    localDateArray = String.valueOf(formatter.parse(localDay)).split(" ");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(localDate);
                String localDateString = localDateArray[1] + " " + localDateArray[2];

                String forecastCondition = dayObject.getJSONObject("condition").getString("text");

                forecastIcon = switchBitch(forecastCondition);


                forecastDayRVModalArrayList.add(new ForecastDayRVModal(localDateString, weatherForecastTemp, forecastIcon));
            }

            forecastDayRVAdapter.notifyDataSetChanged();





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
        forecastHourRV.setAdapter(forecastHourRVAdapter);
        forecastDayRV.setAdapter(forecastDayRVAdapter);
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
        forecastHourRV = findViewById(R.id.rv_hour_forecast);
        forecastHourRVModalArrayList = new ArrayList<>();
        forecastHourRVAdapter = new ForecastHourRVAdapter(this, forecastHourRVModalArrayList);
        forecastDayRV = findViewById(R.id.rv_day_forecast);
        forecastDayRVModalArrayList = new ArrayList<>();
        forecastDayRVAdapter = new ForecastDayRVAdapter(this, forecastDayRVModalArrayList);

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

