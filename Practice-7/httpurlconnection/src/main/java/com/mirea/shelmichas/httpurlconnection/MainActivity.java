package com.mirea.shelmichas.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textIp;
    private TextView textCity;
    private TextView textRegion;
    private TextView textCountry;
    private TextView textWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textIp = findViewById(R.id.textIp);
        textCity = findViewById(R.id.textCity);
        textRegion = findViewById(R.id.textRegion);
        textCountry = findViewById(R.id.textCountry);
        textWeather = findViewById(R.id.textWeather);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Проверяем доступность интернета через ConnectivityManager
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (cm != null) {
                    networkinfo = cm.getActiveNetworkInfo();
                }
                if (networkinfo != null && networkinfo.isConnected()) {
                    // Запускаем загрузку в фоновом потоке
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // AsyncTask для загрузки данных по IP
    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Парсим JSON-ответ от ipinfo.io
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);

                // Извлекаем поля из JSON-объекта
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String country = responseJson.getString("country");
                String loc = responseJson.getString("loc"); // формат "lat,lon"

                textIp.setText("IP: " + ip);
                textCity.setText("Город: " + city);
                textRegion.setText("Регион: " + region);
                textCountry.setText("Страна: " + country);

                // Передаём координаты в сервис погоды
                String[] coords = loc.split(",");
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude="
                        + coords[0] + "&longitude=" + coords[1] + "&current_weather=true";
                new WeatherTask().execute(weatherUrl);

            } catch (Exception e) {
                e.printStackTrace();
                textWeather.setText("Ошибка: " + e.getMessage());
            }
        }
    }

    // AsyncTask для получения погоды
    private class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Парсим JSON от open-meteo
                JSONObject weatherJson = new JSONObject(result);
                JSONObject current = weatherJson.getJSONObject("current_weather");
                double temperature = current.getDouble("temperature");
                double windspeed = current.getDouble("windspeed");

                textWeather.setText("Температура: " + temperature + "°C\n"
                        + "Скорость ветра: " + windspeed + " км/ч");
            } catch (Exception e) {
                textWeather.setText("Ошибка получения погоды");
            }
        }
    }

    // HttpURLConnection - HTTP-запрос к серверу
    // Чтение данных через InputStream, запись в ByteArrayOutputStream
    private String downloadIpInfo(String address) throws Exception {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } finally {
            if (inputStream != null) inputStream.close();
        }
        return data;
    }
}
