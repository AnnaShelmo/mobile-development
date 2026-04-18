package com.mirea.shelmichas.mireaproject.ui.sensor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorViewModel extends ViewModel {

    private final MutableLiveData<String> mPosition = new MutableLiveData<String>();

    public SensorViewModel() {
        mPosition.setValue("Определение положения...");
    }

    public LiveData<String> getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition.setValue(position);
    }
}