package com.mirea.shelmichas.timeservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textDate;
    private TextView textTime;
    private final String host = "time.nist.gov"; // сервер времени NIST
    private final int port = 13;                  // стандартный порт time-сервиса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запускаем AsyncTask в отдельном потоке
                new GetTimeTask().execute();
            }
        });
    }

    // AsyncTask выполняет сетевой запрос в фоновом потоке
    // и возвращает результат в UI-поток
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                // Socket - прямое TCP-соединение с сервером
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // первая строка - служебная, игнорируем
                timeResult = reader.readLine(); // вторая строка - дата и время
                Log.d(TAG, timeResult);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Разбираем строку формата: "59097 24-05-26 15:30:00 50 0 0 ..."
            if (result != null && !result.isEmpty()) {
                String[] parts = result.split(" ");
                if (parts.length >= 3) {
                    String date = parts[1]; // YY-MM-DD
                    String time = parts[2]; // HH:MM:SS
                    textDate.setText("Дата: " + date);
                    textTime.setText("Время: " + time);
                }
            }
        }
    }
}
