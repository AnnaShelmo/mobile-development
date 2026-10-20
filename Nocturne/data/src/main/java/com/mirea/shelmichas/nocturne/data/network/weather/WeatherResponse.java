package com.mirea.shelmichas.nocturne.data.network.weather;

import com.google.gson.annotations.SerializedName;

// Модель ответ сервиса погоды Open-Meteo: https://api.open-meteo.com/v1/forecast
public class WeatherResponse {

    @SerializedName("current")
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public static class Current {

        @SerializedName("temperature_2m")
        private double temperature;

        @SerializedName("weather_code")
        private int weatherCode;

        @SerializedName("wind_speed_10m")
        private double windSpeed;

        @SerializedName("relative_humidity_2m")
        private int humidity;

        public double getTemperature() {
            return temperature;
        }

        public int getWeatherCode() {
            return weatherCode;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public int getHumidity() {
            return humidity;
        }
    }
}
