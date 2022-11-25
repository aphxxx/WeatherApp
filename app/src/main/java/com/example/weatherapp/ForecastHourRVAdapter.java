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

import java.util.ArrayList;

public class ForecastHourRVAdapter extends RecyclerView.Adapter<ForecastHourRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ForecastHourRVModal> forecastHourRVModalArrayList;

    public ForecastHourRVAdapter(Context context, ArrayList<ForecastHourRVModal> weatherModalArrayList) {
        this.context = context;
        this.forecastHourRVModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public ForecastHourRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_hour_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHourRVAdapter.ViewHolder holder, int position) {

        ForecastHourRVModal modal = forecastHourRVModalArrayList.get(position);
        holder.forecastTemperature.setText(modal.getTemperature());
        holder.forecastTime.setText(modal.getTime());
        Glide.with(context).load(modal.getIcon()).into(holder.forecastConditionIcon);

    }

    @Override
    public int getItemCount() {
        return forecastHourRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView forecastTime;
        private TextView forecastTemperature;
        private ImageView forecastConditionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            forecastTime = itemView.findViewById(R.id.tv_forecast_hour_time);
            forecastTemperature = itemView.findViewById(R.id.tv_forecast_hour_temperature);
            forecastConditionIcon = itemView.findViewById(R.id.iv_forecast_hour_condition_icon);
        }
    }
}
