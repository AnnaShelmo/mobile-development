package com.mirea.shelmichas.mireaproject.ui.music;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel {

    private final MutableLiveData<String> mText = new MutableLiveData<>();

    public MusicViewModel() {
        mText.setValue("Music Player");
    }

    public LiveData<String> getText() {
        return mText;
    }
}