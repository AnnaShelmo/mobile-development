package com.mirea.shelmichas.nocturne.domain.models;

public class Weather {
    private final double temperature;
    private final String condition;
    private final double windSpeed;
    private final int humidity;

    public Weather(double temperature, String condition, double windSpeed, int humidity) {
        this.temperature = temperature;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }
}
