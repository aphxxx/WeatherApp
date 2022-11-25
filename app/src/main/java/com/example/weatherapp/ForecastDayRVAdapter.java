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

public class ForecastDayRVAdapter extends RecyclerView.Adapter<ForecastDayRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ForecastDayRVModal> forecastDayRVModalArrayList;

    public ForecastDayRVAdapter(Context context, ArrayList<ForecastDayRVModal> weatherModalArrayList) {
        this.context = context;
        this.forecastDayRVModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public ForecastDayRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_day_rv_item, parent, false);
        return new ForecastDayRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastDayRVAdapter.ViewHolder holder, int position) {

        ForecastDayRVModal modal = forecastDayRVModalArrayList.get(position);
        holder.forecastTemperature.setText(modal.getTemperature());
        holder.forecastDate.setText(modal.getDate());
        Glide.with(context).load(modal.getIcon()).into(holder.forecastConditionIcon);

    }

    @Override
    public int getItemCount() {
        return forecastDayRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView forecastDate;
        private TextView forecastTemperature;
        private ImageView forecastConditionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            forecastDate = itemView.findViewById(R.id.tv_forecast_day_day);
            forecastTemperature = itemView.findViewById(R.id.tv_forecast_day_temperature);
            forecastConditionIcon = itemView.findViewById(R.id.iv_forecast_day_condition_icon);
        }
    }
}
