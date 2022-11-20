package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {

        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.forecastTemperature.setText(modal.getTemperature());
        holder.forecastTime.setText(modal.getTime());
        Glide.with(context).load(modal.getIcon()).into(holder.forecastConditionIcon);

    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView forecastTime;
        private TextView forecastTemperature;
        private ImageView forecastConditionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            forecastTime = itemView.findViewById(R.id.tv_forecast_time);
            forecastTemperature = itemView.findViewById(R.id.tv_forecast_time);
            forecastConditionIcon = itemView.findViewById(R.id.iv_forecast_condition_icon);
        }
    }
}
