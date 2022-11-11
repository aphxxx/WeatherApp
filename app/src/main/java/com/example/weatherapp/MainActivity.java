package com.example.weatherapp;

import static com.example.weatherapp.NetworkUtils.generateURL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;


public class MainActivity extends AppCompatActivity {

    TextView weatherText;
    EditText cityEnterField;
    Button cityEnterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherText = findViewById(R.id.tv_temperature);
        cityEnterField = findViewById(R.id.et_city_enter_field);
        cityEnterButton = findViewById(R.id.bt_city_enter_button);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedURL = generateURL(cityEnterField.getText().toString());
                weatherText.setText(generatedURL.toString());
            }
        };

        cityEnterButton.setOnClickListener(onClickListener);
    }
}