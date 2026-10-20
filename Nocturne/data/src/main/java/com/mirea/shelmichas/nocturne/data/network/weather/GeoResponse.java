package com.mirea.shelmichas.nocturne.data.network.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Модель ответ геокодера Open-Meteo: https://geocoding-api.open-meteo.com/v1/search?name=Париж
public class GeoResponse {

    @SerializedName("results")
    private List<GeoResult> results;

    public List<GeoResult> getResults() {
        return results;
    }

    public static class GeoResult {
        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        @SerializedName("name")
        private String name;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getName() {
            return name;
        }
    }
}
