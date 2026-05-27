package com.mirea.shelmichas.mireaproject.ui.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mirea.shelmichas.mireaproject.databinding.FragmentNetworkBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

// Фрагмент для получения даты и времени с сервера NIST через Socket
public class NetworkFragment extends Fragment {

    private FragmentNetworkBinding binding;
    private final String host = "time.nist.gov";
    private final int port = 13;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);

        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTimeTask().execute();
            }
        });

        return binding.getRoot();
    }

    // AsyncTask для сетевого запроса в фоновом потоке
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                reader.readLine();
                result = reader.readLine();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Ошибка: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.isEmpty() && !result.startsWith("Ошибка")) {
                String[] parts = result.split(" ");
                if (parts.length >= 3) {
                    binding.textResult.setText("Дата: " + parts[1] + "\nВремя: " + parts[2]);
                } else {
                    binding.textResult.setText("Ответ: " + result);
                }
            } else {
                binding.textResult.setText(result);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
