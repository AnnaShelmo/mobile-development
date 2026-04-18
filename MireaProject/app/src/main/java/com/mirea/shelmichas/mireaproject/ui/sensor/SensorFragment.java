package com.mirea.shelmichas.mireaproject.ui.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private android.widget.TextView textPosition;
    private android.widget.TextView textDetails;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.mirea.shelmichas.mireaproject.R.layout.fragment_sensor, container, false);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textPosition = view.findViewById(com.mirea.shelmichas.mireaproject.R.id.textPosition);
        textDetails = view.findViewById(com.mirea.shelmichas.mireaproject.R.id.textDetails);

        if (accelerometerSensor == null) {
            Toast.makeText(getContext(), "Акселерометр недоступен!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            String position = determinePosition(x, y, z);
            
            if (textPosition != null) {
                textPosition.setText(position);
            }
            if (textDetails != null) {
                textDetails.setText(String.format("X: %.2f  Y: %.2f  Z: %.2f", x, y, z));
            }
        }
    }

    private String determinePosition(float x, float y, float z) {
        float threshold = 5.0f;
        
        if (Math.abs(x) > threshold) {
            if (x > 0) {
                return "На левом боку";
            } else {
                return "На правом боку";
            }
        } else if (Math.abs(y) > threshold) {
            if (y > 0) {
                return "Вертикально";
            } else {
                return "Вверх ногами";
            }
        } else if (z > threshold) {
            return "Экран вверх";
        } else if (z < -threshold) {
            return "Экран вниз";
        }
        
        return "Определение...";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}