package com.mirea.shelmichas.nocturne.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.models.Client;

public class SharedPrefClientStorage implements ClientStorage {

    private static final String SHARED_PREFS_NAME = "client_info";
    private static final String NAME_KEY = "client_name";
    private static final String PHONE_KEY = "client_phone";

    private final SharedPreferences sharedPreferences;

    public SharedPrefClientStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Client get() {
        String name = sharedPreferences.getString(NAME_KEY, "unknown");
        String phone = sharedPreferences.getString(PHONE_KEY, "unknown");
        return new Client(name, phone);
    }

    @Override
    public boolean save(Client client) {
        sharedPreferences.edit()
                .putString(NAME_KEY, client.getName())
                .putString(PHONE_KEY, client.getPhone())
                .apply();
        return true;
    }
}