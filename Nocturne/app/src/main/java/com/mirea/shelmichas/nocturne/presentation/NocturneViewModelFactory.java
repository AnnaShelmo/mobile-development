package com.mirea.shelmichas.nocturne.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.data.network.NetworkApi;
import com.mirea.shelmichas.nocturne.data.network.weather.WeatherApi;
import com.mirea.shelmichas.nocturne.data.repository.AuthRepositoryImpl;
import com.mirea.shelmichas.nocturne.data.repository.PlaceRepositoryImpl;
import com.mirea.shelmichas.nocturne.data.repository.WeatherRepositoryImpl;
import com.mirea.shelmichas.nocturne.data.storage.PlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.room.AppDatabase;
import com.mirea.shelmichas.nocturne.data.storage.room.RoomPlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.ClientStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.SharedPrefClientStorage;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.repository.WeatherRepository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NocturneViewModelFactory implements ViewModelProvider.Factory {

    private final Context context; // Activity/фрагмент передаёт контекст, но в ViewModel он не попадает

    public NocturneViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlacesViewModel.class)) {
            return (T) new PlacesViewModel(createPlaceRepository()); // каталог: сервис + БД
        }
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel.class)) {
            return (T) new PlaceDetailsViewModel(createPlaceRepository(),
                    createWeatherRepository(), createAuthRepository());
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(createAuthRepository());
        }
        if (modelClass.isAssignableFrom(WelcomeViewModel.class)) {
            return (T) new WelcomeViewModel(createAuthRepository());
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(createAuthRepository(), createPlaceRepository());
        }
        if (modelClass.isAssignableFrom(FavoritesViewModel.class)) {
            return (T) new FavoritesViewModel(createPlaceRepository(), createAuthRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    private PlaceRepository createPlaceRepository() {
        PlaceStorage placeStorage = new RoomPlaceStorage(
                AppDatabase.getInstance(context).placeDao(),
                AppDatabase.getInstance(context).favoriteDao());
        ClientStorage clientStorage = new SharedPrefClientStorage(context);
        return new PlaceRepositoryImpl(placeStorage, clientStorage, new NetworkApi()); // composition root
    }

    private AuthRepository createAuthRepository() {
        return new AuthRepositoryImpl();
    }

    // Внешний сервис погоды (Open-Meteo) через Retrofit.
    // baseUrl здесь формальный: реальные адреса заданы в WeatherRepositoryImpl через @Url.
    private WeatherRepository createWeatherRepository() {
        WeatherApi weatherApi = new Retrofit.Builder()
                .baseUrl("https://historical-forecast-api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi.class);
        return new WeatherRepositoryImpl(weatherApi);
    }
}
